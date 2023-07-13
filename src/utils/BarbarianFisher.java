package utils;

import framework.Sleep;
import main.GateroTestRun;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;

public class BarbarianFisher {

    private static int[] flyIds = {309,314};

    private static Area barbVillageSpot = new Area(3100, 3420, 3109, 3434);

    private static boolean isCookerOn = true;

    public static boolean idlePowerFisherBarbVill(GateroTestRun script) throws InterruptedException {

        if(!isCookerOn && script.inventory.isFull()){
            int timeOut = script.random(500, 4000);
            int customDrop = script.random(1,10);
            Sleep.sleepUntil(() -> false, timeOut);
            if(customDrop <= 3){
                script.inventory.dropAllExcept(flyIds);

            } else {
                script.log("Custom dropper");

                InventoryDropper inventoryDropper = new InventoryDropper(script);

                inventoryDropper.dropAllItems(309,314);
            }
            Sleep.sleepUntil(() -> script.inventory.isEmptyExcept(309,314), script.random(500, 22000));


            return true;
        }

        if(isCookerOn && script.inventory.isFull()){

            CookingUtils cookingUtils = new CookingUtils(script);

            cookingUtils.cookAllInv(flyIds);

            BankUtils.walkAndBankClosest(script, 1, flyIds);

            return true;
        }
        // Check if nearest object is x tiles far

        if(!barbVillageSpot.contains(script.myPosition())){
            WalkingUtils.handleWebWalk(script, barbVillageSpot, 8);
        }


        NPC closeNpc = script.getNpcs().closest(1527,1526);
        if(script.myPosition().distance(closeNpc.getPosition()) > 6 && !closeNpc.isVisible()) {
            script.log("Fishing spot too far or out of view");
            WalkingUtils.handleWebWalk(script, closeNpc.getArea(2), 8);
        }

        InteractUtils.interactNpc(script, 1526, "Lure");
        Sleep.sleepUntil(() -> false, script.random(3000, 10000));
        return true;
    }

    private static void dropItem(Item item, GateroTestRun script) throws InterruptedException {
        //script.log("Custom dropper " + item.getModelIds().toString());
        item.hover();
        script.sleep(script.random(76, 126));
        boolean dropResult = script.inventory.drop(item.getId());
        Sleep.sleepUntil(() -> dropResult, script.random(450, 700));
    }
}


