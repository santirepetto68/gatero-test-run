package utils;

import framework.Sleep;
import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryUtils {

    private Script script;
    private Inventory inventory;

    public InventoryUtils(Script script) {
        this.script = script;
        this.inventory = script.getInventory();
    }

    public void dropAllItems(boolean randomizeMovement, Integer... itemIdsToSkip) throws InterruptedException {
        List<Integer> skipItemIds = Arrays.asList(itemIdsToSkip);
        List<Integer> itemIdsToDrop = Arrays.stream(inventory.getItems())
                .map(Item::getId)
                .filter(itemId -> !skipItemIds.contains(itemId))
                .collect(Collectors.toList());
        script.getKeyboard().pressKey(KeyEvent.VK_SHIFT);

        for (int slot = 0; slot < Inventory.SIZE; slot++) {
            Item item = inventory.getItemInSlot(slot);
            if (item != null && itemIdsToDrop.contains(item.getId())) {
                int itemToDrop = slot;
                Point slotCenterPoint = new Point(563 + inventory.getSlotBoundingBox(itemToDrop).x /2, 212 + inventory.getSlotBoundingBox(itemToDrop).y / 2);
                if (randomizeMovement) {
                    simulateRandomizedMouseMovement(slotCenterPoint);
                }
                dropItem(itemToDrop);
                Sleep.sleepUntil(() -> inventory.getItemInSlot(itemToDrop) == null, script.random(200, 400));
            }
        }

        script.getKeyboard().releaseKey(KeyEvent.VK_SHIFT); // Release SHIFT key
    }

    private void dropItem(int slot) {
        Item item = inventory.getItemInSlot(slot);
        if (item != null) {
            item.interact("Drop"); // Left-click the item
        }
    }

    private void simulateRandomizedMouseMovement(Point targetPoint) throws InterruptedException {
        Point mousePoint = script.getMouse().getPosition();
        int xOffset = script.random(-15, 15);
        int yOffset = script.random(-15, 15);
        script.getMouse().move(targetPoint.x + xOffset, targetPoint.y + yOffset);
        script.getMouse().click(false);
    }

    private Point getSlotCenterPoint(int index) {
        int slotSize = 42; // Assuming a slot size of 42 pixels (adjust as necessary)
        int slotsPerRow = 4; // Assuming 4 slots per row (adjust as necessary)
        int inventoryX = 563; // X-coordinate of the top-left corner of the inventory
        int inventoryY = 212; // Y-coordinate of the top-left corner of the inventory
        int spacing = 5; // Assuming a spacing of 5 pixels between slots

        int row = index / slotsPerRow;
        int col = index % slotsPerRow;

        int centerX = inventoryX + (col * (slotSize + spacing)) + (slotSize / 2);
        int centerY = inventoryY + (row * (slotSize + spacing)) + (slotSize / 2);

        return new Point(centerX, centerY);
    }
}