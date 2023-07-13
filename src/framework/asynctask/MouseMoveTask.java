package framework.asynctask;


import framework.Sleep;
import main.GateroTestRun;
import org.osbot.rs07.api.Mouse;

import java.awt.*;

public class MouseMoveTask extends AsyncTask {

    public MouseMoveTask(GateroTestRun miner) {
        super(miner, 60, .97);
    }

    @Override
    public void execute() {
        try {
            if(getScript().getMouse().isOnScreen()){
                Point lastPos = getScript().getMouse().getPosition();

                Sleep.sleepUntil(() -> false, 500);

                Point newPos = getScript().getMouse().getPosition();

                if(lastPos.equals(newPos)){
                    getScript().getMouse().moveOutsideScreen();
                    getScript().log("Mouse move out");
                } else {
                    getScript().log("Mouse is moving, skipping mouse out");
                }


            }
        } catch (Exception e) {
            getScript().log(e);
        }
    }

}