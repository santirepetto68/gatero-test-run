package utils;

import framework.Sleep;
import main.GateroTestRun;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.utility.Condition;

public class WalkingUtils {

    private static WebWalkEvent createWebWalkEvent(GateroTestRun script, Area destination, Integer simpleThreshold) {
        WebWalkEvent webWalkEvent = new WebWalkEvent(destination);

        int randomNum = script.random(1, 10);

        // Set random values for the various setters
        webWalkEvent.setBreakCondition(new Condition() { //if true, break the walker.
            @Override
            public boolean evaluate() {
                // Trigger break with 20% chance and only when character distance to destination is within 10-40 tiles from target
                return (randomNum <= 1 && script.myPosition().distance(destination.getRandomPosition()) <= script.random(5, 30)); //area.contains(myPos) || pos.distance() <= threshold
            }
        }); // Replace with your desired break condition
        webWalkEvent.setEnergyThreshold(script.random(43, 100));
        webWalkEvent.setHighBreakPriority(script.random(4) == 0); // 50% chance of setting to true
        //webWalkEvent.setMinDistanceThreshold(script.random(3, 6));
        //webWalkEvent.setMiniMapDistanceThreshold(script.random(15, 25));
        //webWalkEvent.setMisclickThreshold(script.random(1, 3));

        webWalkEvent.setMoveCameraDuringWalking(script.random(8) == 0); // chance of setting to true
        //webWalkEvent.setPathPreferenceProfile(PathPreferenceProfile.DEFAULT); // Replace with your desired profile
        webWalkEvent.setScreenDistanceThreshold(script.random(3, 6));
        webWalkEvent.setSourcePosition(script.myPosition());

        // 70% chance it will use simple path due to path in falador east bank having close roads
        if (randomNum <= 8) {
            script.log("Simple Path");
            webWalkEvent.useSimplePath();
        }

        return webWalkEvent;
    }




    public static void handleWebWalk(GateroTestRun script, Area targetArea, Integer simpleThreshold) {

        WebWalkEvent webWalkEvent = createWebWalkEvent(script, targetArea, simpleThreshold);

        script.execute(webWalkEvent);
        Sleep.sleepUntil(() -> webWalkEvent.hasFinished() || webWalkEvent.hasFailed(), script.random(50000, 90000));

    }

    public static void handleWebWalk(GateroTestRun script, Position targetPosition, Integer simpleThreshold) {

        Area targetArea = targetPosition.getArea(15);

        WebWalkEvent webWalkEvent = createWebWalkEvent(script, targetArea, simpleThreshold);

        script.execute(webWalkEvent);
        Sleep.sleepUntil(() -> webWalkEvent.hasFinished() || webWalkEvent.hasFailed(), script.random(50000, 90000));

    }
}
