package utils;

import framework.Sleep;
import main.GateroTestRun;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;

public class BarbarianFisher {

    private static int[] flyIds = {309,314};

    public static boolean idlePowerFisher(GateroTestRun script) throws InterruptedException {

        if(script.inventory.isFull()){
            int timeOut = script.random(500, 4000);
            int customDrop = script.random(1,10);
            Sleep.sleepUntil(() -> false, timeOut);
            if(true){
                script.inventory.dropAllExcept(flyIds);

            } else {
                script.log("Custom dropper");

                InventoryUtils inventoryUtils = new InventoryUtils(script);

                inventoryUtils.dropAllItems(true, 309,314);

            }
            Sleep.sleepUntil(() -> script.inventory.isEmptyExcept(309,314), script.random(500, 22000));
            return true;
        }

        // Check if nearest object is x tiles far

        NPC closeNpc = script.getNpcs().closest(1527);
        if(script.myPosition().distance(closeNpc.getPosition()) > 6 && !closeNpc.isVisible()) {
            script.log("Fishing spot too far or out of view");
            WalkingUtils.handleWebWalk(script, closeNpc.getArea(2));
        }

        InteractUtils.interactNpc(script, 1527, "Lure");
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


