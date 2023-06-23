package main;


import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import utils.*;
import library.*;

import java.io.IOException;

@ScriptManifest(author = "Gatero", name = "Gatero Test", version = 1.0, info = "", logo = "")
public class GateroTestRun extends Script {

    // Setters to keep track on current states
    private int triedCamera;
    private Position nextPos;

    private CustomMouse customMouse;
    public void setNextPos(Position nextPosIn) {
        nextPos = nextPosIn;
    }

    public Position getNextPos() {
        return nextPos;
    }

    public void setTriedCamera(int triedCameraIn) {
        triedCamera = triedCameraIn;
    }

    public int getTriedCamera() {
        return triedCamera;
    }

    private long lastFatigueTime = 0; // Last time when fatigue was applied
    private int fatigueInterval = random(477645, 1215182); // Interval between fatigue periods in milliseconds
    private int fatigueDurationMin = 58471; // Minimum duration of fatigue in milliseconds
    private int fatigueDurationMax = 238528; // Maximum duration of fatigue in milliseconds

    private int fatigueDelayMin = 53; // Minimum delay to add during fatigue in milliseconds
    private int fatigueDelayMax = 754; // Maximum delay to add during fatigue in milliseconds

    private boolean isFatigueActive; // Flag to indicate if fatigue is currently active
    private long fatigueEndTime; // Time when the current fatigue period ends

    public int getFatigueDelayMin() {
        return fatigueDelayMin;
    }

    public int getFatigueDelayMax() {
        return fatigueDelayMax;
    }
    public long getLastFatigueTime() {
        return lastFatigueTime;
    }

    public void setLastFatigueTime(long lastFatigueTime) {
        this.lastFatigueTime = lastFatigueTime;
    }

    public int getFatigueInterval() {
        return fatigueInterval;
    }

    public int getFatigueDurationMin() {
        return fatigueDurationMin;
    }

    public int getFatigueDurationMax() {
        return fatigueDurationMax;
    }

    public boolean isFatigueActive() {
        return isFatigueActive;
    }

    public void setFatigueActive(boolean fatigueActive) {
        isFatigueActive = fatigueActive;
    }

    public long getFatigueEndTime() {
        return fatigueEndTime;
    }

    public void setFatigueEndTime(long fatigueEndTime) {
        this.fatigueEndTime = fatigueEndTime;
    }

    // Update the lastFatigueTime variable when applying fatigue
    private void applyFatigue() {
        lastFatigueTime = System.currentTimeMillis();
    }

    @Override
    public int onLoop() throws InterruptedException {
        Position currentPosition = myPosition();
        int currentX = currentPosition.getX();
        int currentY = currentPosition.getY();
        int currentZ = currentPosition.getZ();

        //log(String.format("Bot started! " + currentX + "-" + currentY + "-" + currentZ));

        if (inventory.isFull()) {
            log("Inventory Full");
            BankUtils.walkAndBankFalador(this);
        } else {

            MiningUtils.mineIronInGuild(this);
        }
        //sleep(calculateMiningDelay()); // Add a random delay before the next action
        return 0;
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
        // Initialization tasks
        log("Bot started!");

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
        // Additional setup and configuration
    }

    @Override
    public void onExit() {
        // Cleanup tasks
        log("Bot exiting...");
        // Additional cleanup and final actions
    }
}
