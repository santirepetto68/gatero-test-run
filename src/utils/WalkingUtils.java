package utils;

import main.GateroTestRun;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.utility.Condition;

public class WalkingUtils {

    public static WebWalkEvent createWebWalkEvent(GateroTestRun script, Position destination) {
        WebWalkEvent webWalkEvent = new WebWalkEvent(destination);

        int randomNum = script.random(1, 10);

        // Set random values for the various setters
        webWalkEvent.setBreakCondition(new Condition() { //if true, break the walker.
            @Override
            public boolean evaluate() {
                // Trigger break with 20% chance and only when character distance to destination is within 10-40 tiles from target
                return (randomNum <= 1 && script.myPosition().distance(destination) <= script.random(10, 40)); //area.contains(myPos) || pos.distance() <= threshold
            }
        }); // Replace with your desired break condition
        webWalkEvent.setEnergyThreshold(script.random(43, 100));
        webWalkEvent.setHighBreakPriority(script.random(2) == 0); // 50% chance of setting to true
        webWalkEvent.setMinDistanceThreshold(script.random(3, 6));
        webWalkEvent.setMiniMapDistanceThreshold(script.random(10, 20));
        webWalkEvent.setMisclickThreshold(script.random(1, 3));
        webWalkEvent.setMoveCameraDuringWalking(script.random(2) == 0); // 50% chance of setting to true
        //webWalkEvent.setPathPreferenceProfile(PathPreferenceProfile.DEFAULT); // Replace with your desired profile
        webWalkEvent.setScreenDistanceThreshold(script.random(10, 20));
        webWalkEvent.setSourcePosition(script.myPosition());

        // 50% chance it will use simple path
        if (randomNum <= 5) {
            webWalkEvent.useSimplePath();
        }

        return webWalkEvent;
    }
}
