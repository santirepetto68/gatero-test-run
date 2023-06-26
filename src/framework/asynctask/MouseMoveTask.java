package framework.asynctask;


import main.GateroTestRun;

public class MouseMoveTask extends AsyncTask {

    public MouseMoveTask(GateroTestRun miner) {
        super(miner, 60, .97);
    }

    @Override
    public void execute() {
        try {
            if(getScript().getMouse().isOnScreen()){
                getScript().getMouse().moveOutsideScreen();
                getScript().log("Mouse move out");
            }
        } catch (Exception e) {
            getScript().log(e);
        }
    }

}