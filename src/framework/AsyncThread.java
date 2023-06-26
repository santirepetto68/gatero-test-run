package framework;

import framework.asynctask.AsyncTask;
import framework.asynctask.CameraMoveTask;
import framework.asynctask.MouseMoveTask;
import main.GateroTestRun;

public class AsyncThread extends Thread {

    public final AsyncTask[] asyncTasks;
    public boolean running = true;

    public AsyncThread(GateroTestRun miner) {
        this.asyncTasks = new AsyncTask[] {
                new CameraMoveTask(miner),
                new MouseMoveTask(miner)
        };
    }

    @Override
    public void run() {
        while(running) {
            try {
                for(AsyncTask task : asyncTasks) {
                    if(task.activate()) {
                        task.execute();
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}