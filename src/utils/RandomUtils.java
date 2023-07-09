package utils;

import framework.Sleep;
import main.GateroTestRun;
import org.osbot.rs07.api.model.Item;

public class RandomUtils {


    public static boolean powerLvlDropper(GateroTestRun script, int[] itemExcept) throws InterruptedException {
        if(script.inventory.isFull()){
            int timeOut = script.random(500, 4000);
            int customDrop = script.random(1,10);
            Sleep.sleepUntil(() -> false, timeOut);
            if(customDrop > script.random(1,2)){
                script.inventory.dropAllExcept(itemExcept);

            } else {
                script.log("Custom dropper");




                while(!script.inventory.isEmptyExcept(309,314)){
                    Item[] invItems = script.inventory.getItems();

                    for (Item item: invItems) {
                        if(item != null){
                            if(!item.idContains(itemExcept)){
                                dropItem(item, script);
                            }
                        }

                    }
                }


            }
            Sleep.sleepUntil(() -> script.inventory.isEmptyExcept(itemExcept), script.random(30000, 35000));
            return true;
        }
        return false;
    }

    private static void dropItem(Item item, GateroTestRun script) throws InterruptedException {
        //script.log("Custom dropper " + item.getModelIds().toString());
        item.hover();
        script.sleep(script.random(76, 126));
        boolean dropResult = script.inventory.drop(item.getId());
        Sleep.sleepUntil(() -> dropResult, script.random(450, 700));
    }
}
