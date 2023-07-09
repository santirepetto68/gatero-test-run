package utils;
import framework.Sleep;
import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.Script;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.ThreadLocalRandom;

public class InventoryDropper {

    private Script script;
    private Inventory inventory;

    public InventoryDropper(Script script) {
        this.script = script;
        this.inventory = script.getInventory();
    }

    public void dropAllItems(int... skipIds) throws InterruptedException {

        script.getKeyboard().pressKey(KeyEvent.VK_SHIFT);

        Item[] itemList = inventory.getItems();

        script.log("ItemList: " + itemList.toString());

        int firstItemToDrop = script.inventory.getSlot(item -> !item.idContains(skipIds));

        for(int i = firstItemToDrop; i < Inventory.SIZE; i++){
            dropItem(i);
        }

        script.getKeyboard().releaseKey(KeyEvent.VK_SHIFT);
    }

    private void dropItem(int slot) throws InterruptedException {
        script.log("Dropping item: " + slot);

        moveMouseSmoothly(getRandomizedPointWithinItemBounds(slot));

        //script.getInventory().interact(slot);

        script.getMouse().click(false);

    }

    private Point getRandomizedPointWithinItemBounds(int slot) {
        Rectangle bounds = inventory.getSlotBoundingBox(slot);
        int xOffset = randomOffset(bounds.width);
        int yOffset = randomOffset(bounds.height);
        return new Point(bounds.x + xOffset, bounds.y + yOffset);
    }

    private void moveMouseSmoothly(Point targetPoint) throws InterruptedException {
        Point currentPoint = script.getMouse().getPosition();
        int steps = 1; // Increase the number of steps for smoother movement

        int deltaX = (targetPoint.x - currentPoint.x) / steps;
        int deltaY = (targetPoint.y - currentPoint.y) / steps;

        for (int i = 0; i < steps; i++) {
            int randomDeltaX = deltaX + randomOffset(-2, 2); // Randomize the deltaX
            int randomDeltaY = deltaY + randomOffset(-2, 2); // Randomize the deltaY

            currentPoint.translate(randomDeltaX, randomDeltaY);
            script.getMouse().move(currentPoint.x, currentPoint.y);
        }
    }

    private int randomOffset(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }


    private int randomOffset(int size) {
        return ThreadLocalRandom.current().nextInt(5, size - 5);
    }

    private int randomSteps(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private int randomDelay(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public boolean useItemOnItem(String itemA, String itemB)
            throws IllegalArgumentException, RuntimeException {

        /* Variables */
        boolean successful = false;
        String selectedItemName;

        /* Validation */
        if (itemA == null || itemA.isEmpty()) {
            throw new IllegalArgumentException("Parameter \"itemA\" is invalid!");
        } else if (itemB == null || itemB.isEmpty()) {
            throw new IllegalArgumentException("Parameter \"itemB\" is invalid!");
        } else if (itemA == itemB) {
            throw new IllegalArgumentException("Parameter \"itemA\" and \"itemB\" must be different!");
        } else if (!inventory.contains(itemA)) {
            throw new RuntimeException("Inventory doesn't contain: " + itemA);
        } else if (!inventory.contains(itemB)) {
            throw new RuntimeException("Inventory doesn't contain: " + itemB);
        }

        /* Process */
        selectedItemName = inventory.getSelectedItemName();
        if (selectedItemName != null) {
            if (selectedItemName == itemA) {
                /* Now click on item B */
                successful = inventory.getItem(itemB).interact();
            } else if (selectedItemName == itemB) {
                /* Now click on item A */
                successful = inventory.getItem(itemA).interact();
            } else {
                /* Deselect and then re-try (safe recursion) */
                if (inventory.deselectItem()) {
                    useItemOnItem(itemA, itemB);
                } else {
                    throw new RuntimeException("Failed to deselect: " + selectedItemName);
                }
            }
        } else {
            /* Select item and re-try (safe recursion) */
            if (inventory.interact("Use", itemA)) {
                successful = useItemOnItem(itemA, itemB);
            } else {
                throw new RuntimeException("Failed to select: " + itemA);
            }
        }

        return successful;
    }
}
