package utils;

import framework.BotState;
import framework.Sleep;
import locations.WoodcuttingLocations;
import main.GateroTestRun;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;

import java.util.Arrays;

public class WoodcuttingUtils {

    private static WoodcuttingLocations[] locations = WoodcuttingLocations.values();


    private static Area findClosestWoodcuttingArea(Position playerPosition, String tree) {
        WoodcuttingLocations closestLocation = null;
        double closestDistance = Double.MAX_VALUE;

        for (WoodcuttingLocations location : locations) {
            if (Arrays.asList(location.getTreeNames()).contains(tree)) { // Check if the location has the desired tree
                double distance = playerPosition.distance(location.getArea().getRandomPosition());
                if (distance < closestDistance) {
                    closestLocation = location;
                    closestDistance = distance;
                }
            }
        }

        if (closestLocation != null) {
            return closestLocation.getArea();
        }

        return null;
    }

    // Other methods for woodcutting actions...

    public static void idleWoodCutter(GateroTestRun script) throws InterruptedException {

        Area closestArea;

        if(BotState.getFirstIdleWoodArea() == null) {

            script.log("Saving closest area...");
            closestArea = findClosestWoodcuttingArea(BotState.getFirstPlayerPosition(), "YEW");
            script.log("Saved closest area...");
            BotState.setFirstIdleWoodArea(closestArea);
        } else {
            closestArea = BotState.getFirstIdleWoodArea();
        }

        if(script.inventory.isFull()) {
            BankUtils.walkAndBankClosest(script);

            return;
        }

        if(script.myPlayer().isAnimating() || script.myPlayer().isMoving()) {
            Sleep.sleepUntil(() -> !script.myPlayer().isAnimating() && !script.myPlayer().isMoving(), script.random(30000,90000));
            return;
        }

        if(closestArea.contains(script.myPosition())) {
            // Bot action
            script.log("Idle wood action");
            InteractUtils.interactObject(script, "Yew", false, 8, "Chop down");

            return;
        }

        walkToWoodcuttingArea(script);

    }

    private static void walkToWoodcuttingArea(GateroTestRun script) {

        script.log("walkToWoodcuttingArea...");
        if (!BotState.getFirstIdleWoodArea().contains(script.myPosition())) {
            script.getWalking().webWalk(BotState.getFirstIdleWoodArea());
        }
    }


}
