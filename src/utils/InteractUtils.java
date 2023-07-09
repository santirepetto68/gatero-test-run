package utils;

import framework.BotState;
import framework.Sleep;
import main.GateroTestRun;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.input.mouse.EntityDestination;


public class InteractUtils {

    public static boolean interactObject(GateroTestRun script, String name, boolean recall, Integer recallThreshold, String... action) {

        if (script.myPlayer().isAnimating()) {
            Sleep.sleepUntil(() -> false, script.random(500, 800));
            script.log("Still actioning...");

            return true;
        }

        RS2Object object;

        if (BotState.getNextPos() != null && !script.myPlayer().isAnimating() && recall) {
            object = script.getObjects().closest(obj ->
                    obj.getName().equals(name) && obj.getPosition().distance(BotState.getNextPos()) == 0
            );
            script.log("Recall");
            BotState.setNextPos(null);

        } else {
            if(recall) {
                object = script.objects.closest(targetObject -> targetObject.getName().equals(name) && MiningUtils.miningGuildArea.contains(targetObject.getPosition()));


            } else {
                object = script.objects.closest(name);
            }

        }

        if (object == null) {
            if(BotState.getNextPos() != null) {

                script.log("Forgetting");
                BotState.setNextPos(null);
            } else {
                script.log("Object no longer exists");
            }
            Sleep.sleepUntil(() -> false, script.random(135, 450));
            return false;
        }
        //script.log("Target Pos X:" + object.getPosition().getX() + " Y: " + object.getPosition().getY());
        if (script.myPlayer().isMoving() && !object.getName().contains("Bank")) {
            script.log("interactObject.isMoving");
            Sleep.sleepUntil(() -> false, script.random(500, 1500));
            if(object.exists() && script.myPlayer().isMoving()) {
                if (script.getMap().getDestination().distance(object.getPosition()) == 0) {
                    script.log("interactObject.Waiting");
                    // interacted with the object successfully, so wait
                }
            }
            return true;
        }
        if (object.isVisible()) {
            EntityDestination ent = new EntityDestination(script.getBot(), object);

            if (ent.isVisible() || object.isVisible()) {
                Position oldPos = object.getPosition();
                script.log("interactObject.isVisible.interact");
                object.hover();
                if (object.interact(action)) {
                    // Define waiting time
                    int timeOut = recall ? MiningUtils.calculateMiningDelay(script) : script.random(2521,3478);


                    script.log(String.format("Action delay: %d", timeOut));
                    int randomWait = script.random(1, 10);
                    if(BotState.isFatigueActive() || randomWait <= 1) {
                        if(randomWait <= 1) script.log("Random delay...");
                        Sleep.sleepUntil(() -> false, timeOut);
                    } else {
                        Sleep.sleepUntil(() ->  !object.exists() || script.myPlayer().isAnimating(), timeOut);
                    }
                    // Recall
                    if (script.myPlayer().isAnimating() && recall && randomWait <= recallThreshold) {
                        if (!script.getInventory().isFull()) {
                            // Find the next closest actionObj position
                            RS2Object nextActionObject = script.getObjects().closest(obj ->

                                    obj.getName().equals(name) && obj.getPosition().distance(oldPos) > 0 && MiningUtils.miningGuildArea.contains(obj.getPosition())
                            );

                            if (nextActionObject != null) {
                                BotState.setNextPos(nextActionObject.getPosition());
                                nextActionObject.hover();

                                if(randomWait <= 2) {
                                    int currentX = script.getMouse().getPosition().x;

                                    int currentY = script.getMouse().getPosition().y;
                                    script.getMouse().move(script.random(currentX - 20, currentX + 20), script.random(currentY - 20, currentY + 20));
                                }
                            }
                        }

                    }
                    Sleep.sleepUntil(() -> !object.exists() || script.getInventory().isFull(), 50000);
                    return true;
                }
            }
            // TODO maybe walk to nearest adjacent tile?
            // will have to resort to that^ if method doesn't work
            script.log("interactObject.isVisible.cannotSee");
            return script.getCamera().toEntity(object);
        } else {
            //int angleTo = angleToTile(object.getPosition(), script);
            script.log("Changing camera angle relative to target");
            Sleep.sleepUntil(() ->  false, 2000);
            if (distance(object, script) > 7) {
                script.log("1");
                int triedCameraIn = BotState.getTriedCamera();
                if (triedCameraIn == 0) {
                    if (object.getPosition().getTileHeight(script.getBot()) == script.myPosition()
                            .getTileHeight(script.getBot())) {
                        triedCameraIn++;

                        script.log("2");
                        BotState.setTriedCamera(triedCameraIn);

                        return true;
                    } else {
                        script.log("3");
                        script.getWalking().webWalk(object.getPosition());
                    }
                } else {
                    script.log("4");
                    BotState.setTriedCamera(0);
                    return script.getCamera().toEntity(object);
                }
            } else {
                script.log("5");
                return script.getCamera().toEntity(object);
            }
        }

        return false;
    }

    public static int angleToTile(final Position t, GateroTestRun script) {
        final Position me = script.myPosition();
        final int angle = (int) Math.toDegrees(Math.atan2(t.getY() - me.getY(),
                t.getX() - me.getX()));
        return angle >= 0 ? angle : 360 + angle;
    }

    public static int distance(RS2Object o, GateroTestRun script) {
        return script.myPosition().distance(o.getPosition());
    }

    public static boolean interactNpc(GateroTestRun script, int id, String... action) {

        if (script.myPlayer().isAnimating() || script.myPlayer().isMoving()) {
            Sleep.sleepUntil(() -> false, script.random(500, 3000));
            script.log("Still actioning...");
            return false;
        }
        Entity npc = script.getNpcs().closest(script.myPosition().getArea(150), id);

        if (npc == null) {
            script.log("Object no longer exists");
            Sleep.sleepUntil(() -> false, script.random(300, 800));
            return false;
        }

        if (script.myPlayer().isMoving()) {
            script.log("interactNpc.isMoving");
            Sleep.sleepUntil(() -> false, script.random(500, 1500));
            if(npc.exists() && script.myPlayer().isMoving()) {
                if (script.getMap().getDestination().distance(npc.getPosition()) == 0) {
                    script.log("interactObject.Waiting");
                    // interacted with the object successfully, so wait
                    return true;
                }
            }
            return false;
        }
        if (npc.isVisible()) {
            EntityDestination ent = new EntityDestination(script.getBot(), npc);
            if (ent.isVisible()  || npc.isVisible()) {
                Position oldPos = npc.getPosition();

                script.log("interactNpc.isVisible.interact");
                //npc.hover();
                if (npc.interact(action)) {
                    // Define waiting time
                    int timeOut = script.random(2521,3478);
                    if(script.getInventory().getSelectedItemId() != -1){
                        script.log("Item selected");
                        script.mouse.click(false);
                    }

                    script.log(String.format("Action delay: %d", timeOut));
                    int randomWait = script.random(1, 10);
                    if(BotState.isFatigueActive() || randomWait <= 2) {
                        if(randomWait <= 2) script.log("Random delay...");
                        Sleep.sleepUntil(() -> false, timeOut);
                    } else {
                        Sleep.sleepUntil(() -> script.myPlayer().isAnimating() || !npc.exists(), timeOut);
                    }
                    if (script.myPlayer().isAnimating()) {
                        if (!script.getInventory().isFull()) {
                            // Find the next closest actionObj position script.getNpcs().closest(script.myPosition().getArea(150), 1526);
                            Entity nextActionObject = script.getNpcs().closest(entity -> entity.getId() == id && entity.getPosition().distance(oldPos) > 0);


                            if (nextActionObject != null && randomWait < script.random(2, 5)) {
                                BotState.setNextPos(nextActionObject.getPosition());
                                nextActionObject.hover();
                            } else if(randomWait >= script.random(1,5)) {
                                int currentX = script.getMouse().getPosition().x;

                                int currentY = script.getMouse().getPosition().y;
                                script.getMouse().move(script.random(currentX - 70, currentX + 70), script.random(currentY - 70, currentY + 70));
                            }
                        }
                        Sleep.sleepUntil(() -> !npc.exists() || script.getInventory().isFull() || !script.myPlayer().isAnimating(), 50000);
                    }
                    return true;
                }
            }
            // TODO maybe walk to nearest adjacent tile?
            // will have to resort to that^ if method doesn't work
            return script.getCamera().toEntity(npc);
        } else {
            //int angleTo = angleToTile(object.getPosition(), script);
            script.log("Changing camera angle relative to target");
            Sleep.sleepUntil(() ->  false, 2000);
            if (distance(npc.getPosition(), script) > 7) {
                script.log("1");
                int triedCameraIn = BotState.getTriedCamera();
                if (triedCameraIn == 0) {
                    if (npc.getPosition().getTileHeight(script.getBot()) == script.myPosition()
                            .getTileHeight(script.getBot())) {
                        triedCameraIn++;

                        script.log("2");
                        BotState.setTriedCamera(triedCameraIn);


                        return true;
                    } else {
                        script.log("3");
                        script.getWalking().webWalk(npc.getPosition());
                    }
                } else {
                    script.log("4");
                    BotState.setTriedCamera(0);
                    return script.getCamera().toEntity(npc);
                }
            } else {
                script.log("5");
                return script.getCamera().toEntity(npc);
            }
        }

        return false;
    }

    public static boolean interactNpcName(GateroTestRun script, String name, String... action) {

        if (script.myPlayer().isAnimating()) {
            Sleep.sleepUntil(() -> false, script.random(500, 3000));
            script.log("Still actioning...");
            return false;
        }
        Entity npc = script.getNpcs().closest(script.myPosition().getArea(100), name);

        if (npc == null) {
            script.log("Object no longer exists");
            Sleep.sleepUntil(() -> false, script.random(300, 800));
            return false;
        }

        if (script.myPlayer().isMoving()) {
            script.log("interactNpc.isMoving");
            Sleep.sleepUntil(() -> false, script.random(500, 1500));
            if(npc.exists() && script.myPlayer().isMoving()) {
                if (script.getMap().getDestination().distance(npc.getPosition()) == 0) {
                    script.log("interactObject.Waiting");
                    // interacted with the object successfully, so wait
                    return true;
                }
            }
            return false;
        }

        if(!npc.getActions().equals(action) || npc.getId() < 2000) {
            script.log("Selected npc doesnt contain action");

            npc = script.getNpcs().closest(npc1 -> npc1.getName().equals(name) && script.myPosition().distance(npc1.getPosition()) > 2);

        }

        Entity targetNPC = npc;

        if (targetNPC.isVisible()) {
            EntityDestination ent = new EntityDestination(script.getBot(), targetNPC);
            if (ent.isVisible()  || targetNPC.isVisible()) {
                Position oldPos = targetNPC.getPosition();

                script.log("interactNpc.isVisible.interact");
                //npc.hover();
                if (targetNPC.interact(action)) {
                    // Define waiting time
                    int timeOut = script.random(2521,3478);
                    if(script.getInventory().getSelectedItemId() != -1){
                        script.log("Item selected");
                        script.mouse.click(false);
                    }

                    script.log(String.format("Action delay: %d", timeOut));
                    int randomWait = script.random(1, 10);
                    if(BotState.isFatigueActive() || randomWait <= 1) {
                        if(randomWait <= 2) script.log("Random delay...");
                        Sleep.sleepUntil(() -> false, timeOut);
                    } else {
                        script.log("Waiting...");
                        Sleep.sleepUntil(() -> script.myPlayer().isAnimating() || !targetNPC.exists(), timeOut);
                    }
                    script.log("Continue...");
                    if (script.myPlayer().isAnimating()) {
                        if (!script.getInventory().isFull()) {
                            // Find the next closest actionObj position script.getNpcs().closest(script.myPosition().getArea(150), 1526);
                            Entity nextActionObject = script.getNpcs().closest(entity -> entity.getName() == name && entity.getPosition().distance(oldPos) > 0);


                            if (nextActionObject != null && randomWait < 1) {
                                BotState.setNextPos(nextActionObject.getPosition());
                                nextActionObject.hover();
                            } else if(randomWait >= script.random(1,1)) {
                                int currentX = script.getMouse().getPosition().x;

                                int currentY = script.getMouse().getPosition().y;
                                script.getMouse().move(script.random(currentX - 70, currentX + 70), script.random(currentY - 70, currentY + 70));
                            }
                        }
                        Sleep.sleepUntil(() -> !targetNPC.exists() || script.getInventory().isFull() || !script.myPlayer().isAnimating(), 50000);
                    }
                    return true;
                }
            }
            // TODO maybe walk to nearest adjacent tile?
            // will have to resort to that^ if method doesn't work
            return script.getCamera().toEntity(npc);
        } else {
            //int angleTo = angleToTile(object.getPosition(), script);
            script.log("Changing camera angle relative to target");
            Sleep.sleepUntil(() ->  false, 2000);
            if (distance(npc.getPosition(), script) > 7) {
                script.log("1");
                int triedCameraIn = BotState.getTriedCamera();
                if (triedCameraIn == 0) {
                    if (npc.getPosition().getTileHeight(script.getBot()) == script.myPosition()
                            .getTileHeight(script.getBot())) {
                        triedCameraIn++;

                        script.log("2");
                        BotState.setTriedCamera(triedCameraIn);


                        return true;
                    } else {
                        script.log("3");
                        script.getWalking().webWalk(npc.getPosition());
                    }
                } else {
                    script.log("4");
                    BotState.setTriedCamera(0);
                    return script.getCamera().toEntity(npc);
                }
            } else {
                script.log("5");
                return script.getCamera().toEntity(npc);
            }
        }

        return false;
    }



    private boolean nextPos(RS2Object o, GateroTestRun script) {
        if (BotState.getNextPos() != null) {
            return o.getPosition().equals(BotState.getNextPos());
        }
        return true;
    }

    public static int distance(Position p, GateroTestRun script) {
        return script.myPosition().distance(p);
    }

}
