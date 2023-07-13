package ui;

import framework.BotState;
import framework.Sleep;
import main.GateroTestRun;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;

import java.awt.*;
import java.util.List;

public class Paint {

    private long startTime;
    private String currentTask;
    private int itemsCollected;
    //private BufferedImage logo;
    private Font monospaced_16;

    private GateroTestRun script;

    private Skill currentSkill;

    public Paint( GateroTestRun script) {
        this.script = script;
        startTime = System.currentTimeMillis();
        loadResources();
    }

    private void loadResources() {
        /*try {
            logo = ImageIO.read(new URL("https://i.imgur.com/JXpuvgW.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        monospaced_16 = new Font("Monospaced", Font.BOLD, 16);
    }

    public void draw(Graphics2D g) {
        if(BotState.getFirstIdleWoodArea() != null) {
            drawMiningArea(g);
        }

        drawInfo(g);
        drawCursor(g);
    }

    private void drawMiningArea(Graphics2D g) {

        List<Position> positions = BotState.getFirstIdleWoodArea().getPositions();

        g.setColor(new Color(1f, 1f, 1f, 0.1f));
        g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, new float[] {10.0f}, 0.0f));
        for(Position pos : positions) {
            Polygon poly = pos.getPolygon(script.getBot());
            g.drawPolygon(poly);
        }
    }

    private void drawInfo(Graphics2D g) {
        //g.drawImage(logo, 0, 25, logo.getWidth() / 2, logo.getHeight() / 2, null);
        drawStrings(g);
    }

    private void drawStrings(Graphics2D g) {
        g.setFont(monospaced_16);
        g.setColor(Color.WHITE);

        String timeRunning = Sleep.msToString(System.currentTimeMillis() - startTime);

        double itemsPerHour = 0;
        itemsPerHour = (double) itemsCollected / ((System.currentTimeMillis() - startTime) / 3600000.0);

        g.drawString("Runtime: " + timeRunning, 10, 90);
        g.drawString("Task: " + currentTask, 10, 110);

        g.drawString("Collected: " + itemsCollected + "(" + (int) itemsPerHour + "/hr)", 10, 130);
        if(currentSkill != null) {
            g.drawString("Skill Level: " + script.getSkills().getVirtualLevel(currentSkill) + " (+" + script.getExperienceTracker().getGainedLevels(currentSkill) + ")", 10, 150);
            g.drawString("Exp Gained: " + script.getExperienceTracker().getGainedXP(currentSkill) + " (" + script.getExperienceTracker().getGainedXPPerHour(currentSkill) + "/hr)", 10, 170);
        }
    }

    private void drawCursor(Graphics2D g) {
        Point mousePoint = script.getMouse().getPosition();
        g.setStroke(new BasicStroke());
        g.setColor(Color.PINK);
        g.drawLine(mousePoint.x - 5, mousePoint.y + 5, mousePoint.x + 5, mousePoint.y - 5);
        g.setColor(Color.CYAN);
        g.drawLine(mousePoint.x + 5, mousePoint.y + 5, mousePoint.x - 5, mousePoint.y - 5);
    }

    public void setCurrentTask(String currentTask) {
        this.currentTask = currentTask;
    }

    public void setCurrentSkill(Skill currentSkill) {
        this.currentSkill = currentSkill;
        script.getExperienceTracker().start(currentSkill);
    }

    public Skill getCurrentSkill() {
        return currentSkill;
    }

    public void incrementOresMined() {
        itemsCollected++;
    }

    public void incrementOresMined(int ores) {
        itemsCollected = itemsCollected + ores;
    }
}