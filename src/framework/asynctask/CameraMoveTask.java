package framework.asynctask;


import main.GateroTestRun;
import org.osbot.rs07.script.MethodProvider;

public class CameraMoveTask extends AsyncTask {

    public CameraMoveTask(GateroTestRun miner) {
        super(miner, 120, .95);
    }

    @Override
    public void execute() {
        try {
            if(getScript().getMouse().isOnScreen()) {
                getScript().log("Camera move");
                getScript().getCamera().movePitch(MethodProvider.random(22, 67));
                getScript().getCamera().moveYaw(MethodProvider.random(0, 360));
            } else {
                getScript().log("Tried camera move but mouse out of screen");
            }

        } catch (Exception e) {
            getScript().log(e);
        }
    }

}