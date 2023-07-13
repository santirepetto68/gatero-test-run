package utils;

import framework.Sleep;
import main.GateroTestRun;
import org.osbot.rs07.api.map.Area;


public class WoolFactory {

    public static final Area lumSheapArea = new Area(3193,3257,3210,3277);


    public static boolean shearSheepInLum(GateroTestRun script) throws InterruptedException {

        if(script.inventory.isFull()) {
            // Go bank in Lum bank

            BankUtils.walkAndBankClosest(script, 8, 1735);

            Sleep.sleepUntil(() -> false, script.random(500, 2500));
            return false;
        }


        if(!lumSheapArea.contains(script.myPosition())) {

            // Go to Lum sheap area

            WalkingUtils.handleWebWalk(script, lumSheapArea, 8);

            return false;

        }


        // Shear sheep

        InteractUtils.interactNpcName(script, "Sheep", "Shear");

        return true;
    }


    }
