package utils;

import main.GateroTestRun;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;

public class MiningUtils {

    private static final String ironOreRocks = "Iron rocks";
    private static final String coalOreRocks = "Coal rocks";

    private static final Area miningGuildArea = new Area(3025, 9730, 3059, 9750); // Mining Guild area coordinates

    public static void mineOreInGuild(GateroTestRun script) throws InterruptedException {
        if (miningGuildArea.contains(script.myPosition())) {
            InteractUtils.interactObject(script, ironOreRocks, "Mine", true);

            if (script.inventory.isFull()) {
                script.log("mineIron.bank");
                BankUtils.walkAndBankFalador(script);
                script.sleep(script.random(500, 1500)); // Add a delay before attempting to bank again
            }
        } else {
            walkToMiningGuildIron(script);
        }
    }

    private static void walkToMiningGuildIron(GateroTestRun script) {
        Area targetArea = new Area(3033, 9738, 3031,9738); // Target position inside the mining guild
        script.log("mineIron.walkToMiningGuild...");
        if (!targetArea.contains(script.myPosition())) {
            script.getWalking().webWalk(targetArea);
        }
    }

    private static void walkToMiningGuildCoal(GateroTestRun script) {
        Area targetArea = new Area(3038, 9739, 3040,9740); // Target position inside the mining guild
        script.log("mineIron.walkToMiningGuild...");
        if (!targetArea.contains(script.myPosition())) {
            script.getWalking().webWalk(targetArea);
        }
    }

    public static int calculateMiningDelay(GateroTestRun script) {
        int miningLevel = script.skills.getStatic(Skill.MINING);
        double baseTime = 2.5; // Base mining time in seconds
        double miningSpeedModifier = 1.0 + (miningLevel / 100.0); // Mining speed modifier based on mining level

        // Calculate the accurate mining time based on mining level and randomize it slightly
        int miningTime = (int) (baseTime * miningSpeedModifier * script.random(30, 175));


        if (script.getLastFatigueTime() == 0) {
            long currentTime = System.currentTimeMillis();
            script.setLastFatigueTime(currentTime);

        // Check if it's a fatigue period
        } else if (script.isFatigueActive()) {
            script.log(String.format("Fatigue mode: true - End: %d", script.getFatigueEndTime()));
            // Check if fatigue period has ended
            long currentTime = System.currentTimeMillis();
            if (currentTime >= script.getFatigueEndTime()) {
                // Fatigue period has ended, reset the fatigue flag
                script.setFatigueActive(false);
                script.log("Fatigue mode: false");
            } else {
                // Apply fatigue delay
                int fatigueDelay = script.random(script.getFatigueDelayMin(), script.getFatigueDelayMax());
                //miningTime += fatigueDelay;

                // Randomize the mining time to add a realistic variation
                return (script.random(miningTime - 50, miningTime + 50) * 10) + fatigueDelay;
            }
        } else {
            // Check if enough time has passed since the last fatigue period
            long currentTime = System.currentTimeMillis();
            long lastFatigueTime = script.getLastFatigueTime();
            long timeSinceLastFatigue = currentTime - lastFatigueTime;
            boolean isFatiguePeriod = timeSinceLastFatigue >= script.getFatigueInterval();

            // Apply fatigue if it's a fatigue period
            if (isFatiguePeriod) {
                script.log("Setting fatigue mode");
                // Start new fatigue period
                script.setFatigueActive(true);
                int fatigueDuration = script.random(script.getFatigueDurationMin(), script.getFatigueDurationMax());
                long fatigueEndTime = currentTime + fatigueDuration;
                script.setFatigueEndTime(fatigueEndTime);
                script.setLastFatigueTime(currentTime); // Update the last fatigue time

                // Apply fatigue delay
                int fatigueDelay = script.random(script.getFatigueDelayMin(), script.getFatigueDelayMax());
                //miningTime += fatigueDelay;

                // Randomize the mining time to add a realistic variation
                return (script.random(miningTime - 50, miningTime + 50) * 10) + fatigueDelay;
            }
        }
        return script.random(miningTime - 50, miningTime + 50) * 10;
    }
}
