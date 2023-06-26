package framework.asynctask;

import main.GateroTestRun;
import org.osbot.rs07.script.MethodProvider;


public abstract class AsyncTask implements Runnable {

    private final GateroTestRun script;
    private final int secondsToWait;
    private final double threshold;
    private long lastActivated;

    public AsyncTask(GateroTestRun script, int secondsToWait, double threshold) {
        this.script = script;
        this.secondsToWait = secondsToWait;
        this.threshold = threshold;
        this.lastActivated = System.currentTimeMillis();
    }

    public final boolean activate() {
        if(Math.random() >= getThreshold()) {
            if((System.currentTimeMillis() - getLastActivated()) >= (getSecondsToWait() * 1000)) {
                setLastActivated(System.currentTimeMillis());
                return true;
            }
        }
        return false;
    }

    @Override
    public final void run() {
        execute();
    }

    public abstract void execute();

    public GateroTestRun getScript() {
        return script;
    }

    public int getSecondsToWait() {
        return MethodProvider.random(secondsToWait - 1, secondsToWait + 1);
    }

    public double getThreshold() {
        return threshold;
    }

    public long getLastActivated() {
        return lastActivated;
    }

    public void setLastActivated(long lastActivated) {
        this.lastActivated = lastActivated;
    }

}
