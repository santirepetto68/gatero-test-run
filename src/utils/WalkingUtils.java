package utils;

import main.GateroTestRun;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.event.WebWalkEvent;

public class WalkingUtils {

    public static WebWalkEvent createWebWalkEvent(GateroTestRun script, Position destination) {
        WebWalkEvent webWalkEvent = new WebWalkEvent(destination);

        int randomNum = script.random(0, 9);

        // Set random values for the various setters
        //webWalkEvent.setBreakCondition(() -> randomNum < 3); // Replace with your desired break condition
        webWalkEvent.setEnergyThreshold(script.random(43, 100));
        webWalkEvent.setHighBreakPriority(script.random(2) == 0); // 50% chance of setting to true
        webWalkEvent.setMinDistanceThreshold(script.random(3, 6));
        webWalkEvent.setMiniMapDistanceThreshold(script.random(10, 20));
        webWalkEvent.setMisclickThreshold(script.random(1, 3));
        webWalkEvent.setMoveCameraDuringWalking(script.random(2) == 0); // 50% chance of setting to true
        //webWalkEvent.setPathPreferenceProfile(PathPreferenceProfile.DEFAULT); // Replace with your desired profile
        webWalkEvent.setScreenDistanceThreshold(script.random(10, 20));
        webWalkEvent.setSourcePosition(script.myPosition());

        if (randomNum < 5) {
            webWalkEvent.useSimplePath();
        }

        return webWalkEvent;
    }
}
