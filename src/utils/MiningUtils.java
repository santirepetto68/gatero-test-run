package utils;

import framework.BotState;
import main.GateroTestRun;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;

public class MiningUtils {

    private static final String ironOreRocks = "Iron rocks";
    private static final String coalOreRocks = "Coal rocks";
    private static final String mithOreRocks = "Mithril rocks";

    private static Boolean isPowerLevel = true;

    public static final Area miningGuildArea = new Area(3025, 9730, 3059, 9750); // Mining Guild area coordinates

    public static void mineOreInGuild(GateroTestRun script) throws InterruptedException {


        if (miningGuildArea.contains(script.myPosition())) {

            if (script.inventory.isFull()) {
                if(isPowerLevel) {
                    script.log("Dropping ores");

                    int randomValue = script.random(1,2);

                    InventoryDropper invDropper = new InventoryDropper(script);

                    invDropper.dropAllItems(1275);

                    if(randomValue == 1){

                    } else {

                        //script.inventory.dropAll();
                    }


                } else {
                    script.log("mineIron.bank");
                    BankUtils.walkAndBankFalador(script);
                }


                return;
            }

            boolean mainMine = InteractUtils.interactObject(script, ironOreRocks, true, 10, "Mine");

            if(!mainMine && !isPowerLevel) {
                InteractUtils.interactObject(script, coalOreRocks, true, 8, "Mine");
            }


        } else {
            walkToMiningGuildIron(script);
        }
    }

    private static void walkToMiningGuildIron(GateroTestRun script) {
        Area targetArea = new Area(3033, 9738, 3031,9738); // Target position inside the mining guild
        script.log("mineIron.walkToMiningGuild...");
        if (!targetArea.contains(script.myPosition())) {
            WalkingUtils.handleWebWalk(script, targetArea);
        }
    }

    public static int calculateMiningDelay(GateroTestRun script) {
        int miningLevel = script.skills.getStatic(Skill.MINING);
        double baseTime = 2.5; // Base mining time in seconds
        double miningSpeedModifier = 1.0 + (miningLevel / 100.0); // Mining speed modifier based on mining level

        // Calculate the accurate mining time based on mining level and randomize it slightly
        int miningTime = (int) (baseTime * miningSpeedModifier * script.random(30, 175));


        if (BotState.getLastFatigueTime() == 0) {
            long currentTime = System.currentTimeMillis();
            BotState.setLastFatigueTime(currentTime);
            script.log(String.format("Setting fatigue mode - Start: %d", (currentTime + BotState.getFatigueInterval())));

        // Check if it's a fatigue period
        } else if (BotState.isFatigueActive()) {
            script.log(String.format("Fatigue mode: true - End: %d", BotState.getFatigueEndTime()));
            // Check if fatigue period has ended
             long currentTime = System.currentTimeMillis();
            if (currentTime >= BotState.getFatigueEndTime()) {
                // Fatigue period has ended, reset the fatigue flag
                BotState.setFatigueActive(false);
                script.log("Fatigue mode: false");
            } else {
                // Apply fatigue delay
                int fatigueDelay = script.random(BotState.getFatigueDelayMin(), BotState.getFatigueDelayMax());
                //miningTime += fatigueDelay;

                // Randomize the mining time to add a realistic variation
                return (script.random(miningTime - 50, miningTime + 50) * 10) + fatigueDelay;
            }
        } else {
            // Check if enough time has passed since the last fatigue period
            long currentTime = System.currentTimeMillis();
            long lastFatigueTime = BotState.getLastFatigueTime();
            long timeSinceLastFatigue = currentTime - lastFatigueTime;
            boolean isFatiguePeriod = timeSinceLastFatigue >= BotState.getFatigueInterval();

            // Apply fatigue if it's a fatigue period
            if (isFatiguePeriod) {
                // Start new fatigue period
                BotState.setFatigueActive(true);
                int fatigueDuration = script.random(BotState.getFatigueDurationMin(), BotState.getFatigueDurationMax());
                long fatigueEndTime = currentTime + fatigueDuration;
                BotState.setFatigueEndTime(fatigueEndTime);
                BotState.setLastFatigueTime(currentTime); // Update the last fatigue time

                // Apply fatigue delay
                int fatigueDelay = script.random(BotState.getFatigueDelayMin(), BotState.getFatigueDelayMax());
                //miningTime += fatigueDelay;

                // Randomize the mining time to add a realistic variation
                return (script.random(miningTime - 50, miningTime + 50) * 10) + fatigueDelay;
            }
        }
        return script.random(miningTime - 50, miningTime + 50) * 10;
    }
}
