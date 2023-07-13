package utils;

import framework.Sleep;
import main.GateroTestRun;
import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.MethodProvider;

import java.util.concurrent.atomic.AtomicReference;

public class CookingUtils  extends MethodProvider  {


    public GateroTestRun script;

    public Inventory inventory;

    public CookingUtils(GateroTestRun script){
        this.script = script;
        this.inventory = script.inventory;
    }

    public boolean cookAllInv(int... skipIds) {
        int infiniteLoopCount = 0;
        int lastItemId = 0;

        while((skipIds != null ? !inventory.isEmptyExcept(skipIds) : !inventory.isEmpty()) && infiniteLoopCount < 50) {
            infiniteLoopCount++;
            Item itemToCook = inventory.getItem(item -> !item.idContains(skipIds) && item.getName().contains("Raw "));

            if(itemToCook == null) {

                // No more items to cook
                script.log("Cooking ended.");
                break;

            }

            if(itemToCook.getId() != lastItemId) {
                int lastId = lastItemId;
                Item invContainLast = inventory.getItem(item -> item.idContains(lastId));

                if(invContainLast != null) {
                    script.log("Inv still contains items in inv");
                    Sleep.sleepUntil(()-> false, script.random(1132, 2487));
                    continue;
                }

            }

            if(script.myPlayer().isAnimating() || script.myPlayer().isMoving() || itemToCook.getId() == lastItemId) {
                Sleep.sleepUntil(()-> false, script.random(1132, 2487));
                script.log("Still Cooking...");
                continue;
            }

            boolean cookSuccess = cookItem(itemToCook);

            if(!cookSuccess) {
                continue;
            }

            lastItemId = itemToCook.getId();
            Sleep.sleepUntil(()-> (!script.myPlayer().isAnimating()), random(25000, 85000));
        }

        return true;
    }

    public boolean cookItem(Item item) {

        item.interact("Use");

        InteractUtils.interactObject(script, "Fire", false, 0, "Use");

        Sleep.sleepUntil(()-> script.objects.closest("Fire").getPosition().distance(script.myPosition()) <= 1, script.random(2000,3000));

        AtomicReference<RS2Widget> cookWid = new AtomicReference<>();

        Sleep.sleepUntil(() -> {
            RS2Widget widget = script.widgets.get(270, 14);
            cookWid.set(widget);
            return widget != null && widget.isVisible();
        }, random(2500, 5000));

        RS2Widget widget = cookWid.get();
        if (widget != null) {
            widget.interact("Cook");
            InteractUtils.endMouseMove(script);
            script.log("Cooking...");
            return true;
        } else {
            script.log("Not cooking yet");
            return false;
        }

    }






}
