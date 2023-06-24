package framework.asynctask;


import main.GateroTestRun;

public class MouseMoveTask extends AsyncTask {

    public MouseMoveTask(GateroTestRun miner) {
        super(miner, 25, .97);
    }

    @Override
    public void execute() {
        try {
            getMiner().getMouse().moveOutsideScreen();
        } catch (Exception e) {
            getMiner().log(e);
        }
    }

}