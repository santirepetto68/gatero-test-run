package utils;

import framework.Sleep;
import main.GateroTestRun;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;

public class RuneEssenceMiner {


    private boolean isFtp = true;

    private static Area varrockRuneShop = new Area(3250, 3397, 3255, 3407);


    public static boolean essenceMiner(GateroTestRun script) throws InterruptedException {

        if(script.inventory.isFull()) {

            if(script.myPosition().getX() > 5000) {
                // Exit rune essence mine through closest portal
                NPC portalNpc = script.getNpcs().closestThatContains("Portal");
                RS2Object portalObj = script.getObjects().closestThatContains("Portal");

                if(portalNpc != null) {
                    if(portalNpc.exists()){

                        if(script.myPosition().distance(portalNpc.getPosition()) > 2) {
                            script.walking.walk(portalNpc.getArea(1));
                        }
                        InteractUtils.interactNpc(script, portalNpc.getId(), "Exit", "Use");

                        return true;
                    }
                }

                if(portalObj != null) {
                    if(portalObj.exists()){

                        if(script.myPosition().distance(portalObj.getPosition()) > 2) {
                            script.walking.walk(portalObj.getArea(1));
                        }

                        InteractUtils.interactObject(script, portalObj.getName(), false, 8, "Exit", "Use");


                        return true;
                    }
                }

            }

            BankUtils.walkAndBankClosest(script, 8);

            return false;


        } else {
            // Check current player position
            if(script.myPosition().getX() > 5000) {

                RS2Object object = script.getObjects().closest("Rune Essence");
                if(script.myPosition().distance(object.getPosition()) > 6 && !object.isVisible()) {
                    script.log("Rune essence too far, getting closer");
                    script.walking.walk(object.getArea(2));

                    Sleep.sleepUntil(() -> script.myPosition().distance(object.getPosition()) <= 5, 5000);
                }

                InteractUtils.interactObject(script, "Rune Essence", false, 8, "Mine");

            } else {
                // Go to rune essence mine through varrock south-east rune shop
                WalkingUtils.handleWebWalk(script, varrockRuneShop, 8);

                InteractUtils.interactNpc(script, 2886, "Teleport");

                Sleep.sleepUntil(() -> script.myPosition().getX() > 5000, 4000);

                return true;
            }

        }

        return true;
    }
}
