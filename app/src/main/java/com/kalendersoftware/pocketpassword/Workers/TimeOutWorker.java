package com.kalendersoftware.pocketpassword.Workers;

import com.kalendersoftware.pocketpassword.Abstracts.WorkerAbstract;
import com.kalendersoftware.pocketpassword.Global;
import com.kalendersoftware.pocketpassword.Globals.Config;
import com.kalendersoftware.pocketpassword.Globals.Helpers;
import com.kalendersoftware.pocketpassword.MainActivity;

public class TimeOutWorker extends WorkerAbstract {

    public TimeOutWorker() {
        Global.LAST_OPERATION_TIME = System.currentTimeMillis();

        Runnable runnable = () -> {
            if(System.currentTimeMillis() - Global.LAST_OPERATION_TIME > Config.TIMEOUT_SECOND * 1000){
                if(Helpers.loading.isShowing){
                }else{
                    ((MainActivity)Global.CONTEXT).logout();

                    super.stop();
                }
            }else{
            }
        };

        super.setRunnable(runnable);
    }
}
