package utils;

import library.Sleep;
import main.GateroTestRun;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.input.mouse.EntityDestination;


public class InteractUtils {

    public static boolean interactObject(GateroTestRun script, String name, String action, boolean mining) {


        RS2Object object = script.objects.closest(name);

        if (object == null) {
            return false;
        }

        if (script.myPlayer().isMoving()) {
            script.log("interactObject.isMoving");
            Sleep.sleepUntil(() -> false, script.random(500, 1500));
            if(object.exists()) {
                if (script.getMap().getDestination().distance(object.getPosition()) == 0) {
                    script.log("interactObject.Waiting");
                    // interacted with the object successfully, so wait
                    return true;
                }
            }
            return false;
        }
        if (object.isVisible()) {
            EntityDestination ent = new EntityDestination(script.getBot(), object);
            if (ent.isVisible()) {
                Position oldPos = object.getPosition();
                script.log("interactObject.isVisible.interact");
                if (object.interact(action)) {
                    // Define waiting time
                    int timeOut = mining ? MiningUtils.calculateMiningDelay(script) : script.random(2521,3478);


                    script.log(String.format("Mining delay: %d", timeOut));
                    int randomWait = script.random(1, 10);
                    if(script.isFatigueActive() || randomWait <= 2) {
                        Sleep.sleepUntil(() -> false, timeOut);
                    } else {
                        Sleep.sleepUntil(() -> script.myPlayer().isAnimating() || !object.exists(), timeOut);
                    }
                    if (script.myPlayer().isAnimating()) {
                        if (!script.getInventory().isFull()) {
                            RS2Object next = script.getObjects().closest(name);
                            if (next != null) {
                                script.setNextPos(next.getPosition());
                                next.hover();
                            }
                        }
                        Sleep.sleepUntil(() -> !object.exists() || script.getInventory().isFull(), 50000);
                    }
                    return true;
                }
            }
            // TODO maybe walk to nearest adjacent tile?
            // will have to resort to that^ if method doesn't work
            return script.getCamera().toEntity(object);
        } else {
            //int angleTo = angleToTile(object.getPosition(), script);
            if (distance(object, script) > 7) {
                int triedCameraIn = script.getTriedCamera();
                if (triedCameraIn == 0) {
                    if (object.getPosition().getTileHeight(script.getBot()) == script.myPosition()
                            .getTileHeight(script.getBot())) {
                        triedCameraIn++;
                        script.setTriedCamera(triedCameraIn);
                        return script.getCamera().toBottom();
                    } else {
                        script.getWalking().webWalk(object.getPosition());
                    }
                } else {
                    script.setTriedCamera(0);
                }
            } else {
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





    private boolean nextPos(RS2Object o, GateroTestRun script) {
        if (script.getNextPos() != null) {
            return o.getPosition().equals(script.getNextPos());
        }
        return true;
    }

    public int distance(Position p, GateroTestRun script) {
        return script.myPosition().distance(p);
    }

}
