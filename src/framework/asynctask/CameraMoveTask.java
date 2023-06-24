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
            getMiner().getCamera().movePitch(MethodProvider.random(22, 67));
            getMiner().getCamera().moveYaw(MethodProvider.random(0, 360));
        } catch (Exception e) {
            getMiner().log(e);
        }
    }

}