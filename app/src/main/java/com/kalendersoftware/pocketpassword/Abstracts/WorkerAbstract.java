package com.kalendersoftware.pocketpassword.Abstracts;

import com.kalendersoftware.pocketpassword.Constants.ErrorCodeConstants;
import com.kalendersoftware.pocketpassword.Globals.Config;
import com.kalendersoftware.pocketpassword.Globals.Helpers;

public abstract class WorkerAbstract {
    protected boolean active = true;
    protected Runnable runnable;

    public void setRunnable(Runnable runnable){
        this.runnable = runnable;
    }

    protected Thread thread;

    public void start(){
        this.thread = new Thread(() -> {
            while (active){
                try {
                    Thread.sleep(Config.DELAY_TIME_OUT * 1000);

                    this.runnable.run();
                } catch (Exception e) {
                    Helpers.logger.error(ErrorCodeConstants.WORKER_TIME_OUT_ACTION, e);
                }
            }
        });

        thread.start();
    }

    public void stop(){
        this.active = false;
    }
}
