package framework;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import utils.WoodcuttingUtils;

public  class BotState {
    private static  int triedCamera;
    private static  Position nextPos;
    private static  CustomMouse customMouse;
    private static  long lastFatigueTime = 0;
    private static  int fatigueInterval;
    private static  int fatigueDurationMin;
    private static  int fatigueDurationMax;
    private static  int fatigueDelayMin;
    private static  int fatigueDelayMax;
    private static  boolean isFatigueActive;
    private static  long fatigueEndTime;
    private static  WoodcuttingUtils woodcuttingUtils;
    private static  boolean isIdleWoodBot;
    private static  Position firstPlayerPosition;
    private static  Area firstIdleWoodArea;
    private static  Area closestBankArea;

    public static  void setNextPos(Position nextPosIn) {
        nextPos = nextPosIn;
    }

    public static  Position getNextPos() {
        return nextPos;
    }

    public static  void setTriedCamera(int triedCameraIn) {
        triedCamera = triedCameraIn;
    }

    public static  int getTriedCamera() {
        return triedCamera;
    }

    public static long getLastFatigueTime() {
        return lastFatigueTime;
    }

    public static void setLastFatigueTime(long lastFatigueTimeW) {
        lastFatigueTime = lastFatigueTimeW;
    }

    public static  int getFatigueInterval() {
        return fatigueInterval;
    }

    public static  int getFatigueDurationMin() {
        return fatigueDurationMin;
    }

    public static  int getFatigueDurationMax() {
        return fatigueDurationMax;
    }

    public static  int getFatigueDelayMin() {
        return fatigueDelayMin;
    }

    public static  int getFatigueDelayMax() {
        return fatigueDelayMax;
    }

    public static  boolean isFatigueActive() {
        return isFatigueActive;
    }

    public static  void setFatigueActive(boolean fatigueActive) {
        isFatigueActive = fatigueActive;
    }

    public static  long getFatigueEndTime() {
        return fatigueEndTime;
    }

    public static  void setFatigueEndTime(long fatigueEndTimeD) {
        fatigueEndTime = fatigueEndTimeD;
    }

    public static  void applyFatigue() {
        lastFatigueTime = System.currentTimeMillis();
    }

    public static  boolean isIdleWoodBot() {
        return isIdleWoodBot;
    }

    public static  void setIdleWoodBot(boolean idleWoodBot) {
        isIdleWoodBot = idleWoodBot;
    }

    public static Position getFirstPlayerPosition() {
        return firstPlayerPosition;
    }

    public static void setFirstPlayerPosition(Position firstPlayerPositionL) {
        firstPlayerPosition = firstPlayerPositionL;
    }

    public static  Area getFirstIdleWoodArea() {
        return firstIdleWoodArea;
    }

    public static  void setFirstIdleWoodArea(Area firstIdleWoodAreaL) {
        firstIdleWoodArea = firstIdleWoodAreaL;
    }

    public static  Area getClosestBankArea() {
        return closestBankArea;
    }

    public static  void setClosestBankArea(Area closestBankAreaL) {
        closestBankArea = closestBankAreaL;
    }

    // Other methods related to state management...
}
