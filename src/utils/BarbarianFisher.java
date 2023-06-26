package utils;

import framework.Sleep;
import main.GateroTestRun;
import org.osbot.rs07.api.model.Item;

public class BarbarianFisher {

    private static int[] flyIds = {309,314};

    public static boolean idlePowerFisher(GateroTestRun script) throws InterruptedException {

        if(script.inventory.isFull()){
            int timeOut = script.random(500, 4000);
            int customDrop = script.random(1,10);
            Sleep.sleepUntil(() -> false, timeOut);
            if(customDrop > script.random(1,2)){
                script.inventory.dropAllExcept(flyIds);

            } else {
                script.log("Custom dropper");




                while(!script.inventory.isEmptyExcept(309,314)){
                    Item[] invItems = script.inventory.getItems();

                    for (Item item: invItems) {
                        if(item != null){
                            if(!item.idContains(flyIds)){
                                dropItem(item, script);
                            }
                        }

                    }
                }


            }
            Sleep.sleepUntil(() -> script.inventory.isEmptyExcept(309,314), script.random(30000, 35000));
            return true;
        }

        InteractUtils.interactNpc(script, 1526, "Lure");
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


