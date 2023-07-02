package main;


import org.osbot.B;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.api.ui.Message;

import ui.Paint;
import utils.*;
import framework.*;

import java.awt.*;
import java.io.IOException;

@ScriptManifest(author = "Gatero", name = "Gatero Test", version = 1.0, info = "", logo = "")
public class GateroTestRun extends Script {

    public final AsyncThread asyncThread = new AsyncThread(this);

    // Setters to keep track on current states
    private int triedCamera;
    private Position nextPos;

    private CustomMouse customMouse;

    private long afkModeTime = System.currentTimeMillis();

    private boolean isIdleFisher = false;
    private boolean isIdleWoodBot = false;

    private Paint paint;

    public void setPaint(){
        this.paint = paint;
    }

    public Paint getPaint() {
        return paint;
    }

    @Override
    public void onPaint(Graphics2D g) {
        paint.draw(g);
    }

    @Override
    public void onMessage(Message m) {
        if(m.getMessage().contains("You manage to mine some") || (m.getMessage().contains("You get some") && m.getMessage().contains("logs"))) {
            paint.incrementOresMined();
        }
    }


    @Override
    public int onLoop() throws InterruptedException {

        // Call the getGraphicsObjects() method to obtain the Graphics object
        // Sleep exceptions to prevent high consumption loops
        try {
            if(afkModeTime - System.currentTimeMillis() >= random(720000, 900000) ) {
                performAFK();
                afkModeTime = System.currentTimeMillis();
            }

            // Idle fisher
            if (isIdleFisher) {

                BarbarianFisher.idlePowerFisher(this);
                return 0;
            }

            // Idle woodcutter
            if (isIdleWoodBot){
                Sleep.sleepUntil(() -> false, random(100, 5000));
                WoodcuttingUtils.idleWoodCutter(this);

                return 0;
            }


            if (inventory.isFull()) {
                log("Inventory Full");
                BankUtils.walkAndBankFalador(this);
            } else {

                MiningUtils.mineOreInGuild(this);
            }
            //sleep(calculateMiningDelay()); // Add a random delay before the next action
            return 0;
        } catch (Exception e) {
            log("Big Error");
            sleep(1000);
            throw e;
        }

    }

    private void performAFK() throws InterruptedException {
        int afkDuration = random(1, 5) * 60 * 1000; // Random duration between 1 and 5 minutes
        //int mouseMovementInterval = random(20, 40) * 1000; // Random interval between 20 and 40 seconds for mouse movement

        log("AFK period: " + afkDuration / 1000 + " seconds");

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < afkDuration) {
            // Move the mouse to a random position on the screen every mouseMovementInterval

            sleep(random(1000, 2000)); // Sleep for a short period between actions during AFK
        }
    }

    @Override
    public void onStart() throws InterruptedException {

        // Get the player coordinates as soon as the bot starts
        Position currentPosition = myPosition();
        int currentX = currentPosition.getX();
        int currentY = currentPosition.getY();
        int currentZ = currentPosition.getZ();

        log(String.format("Player position: %d-%d-%d", currentX, currentY, currentZ));


        customMouse = new CustomMouse();
        customMouse.exchangeContext(getBot());

        try {
            customMouse.getCustomMouse(getDirectoryData());
        } catch (IOException e) {
            log(e);
        }

        asyncThread.start();
        // Additional setup and configuration


        paint = new Paint(this);

        if(isIdleWoodBot) {
            paint.setCurrentTask("Woodcutting");
            paint.setCurrentSkill(Skill.WOODCUTTING);
            BotState.setFirstPlayerPosition(currentPosition);

            log("Saving closest bank...");
            BotState.setClosestBankArea(Bank.closestTo(currentPosition, this));
            log("Saving closest finished...");
        } else if (isIdleFisher){
            paint.setCurrentTask("Fishing");
            paint.setCurrentSkill(Skill.FISHING);
        } else {
            paint.setCurrentTask("Mining");
            paint.setCurrentSkill(Skill.MINING);
            BotState.setFirstIdleWoodArea(MiningUtils.miningGuildArea);
        }


        // Initialization tasks
        log("Bot started!");

    }

    @Override
    public void onExit() {
        // Cleanup tasks

        log("Bot exiting...");
        asyncThread.running = false;
        try {
            asyncThread.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Additional cleanup and final actions
    }
}
