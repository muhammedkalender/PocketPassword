package com.muhammedkalender.pocketpassword.Workers;

import com.muhammedkalender.pocketpassword.Abstracts.WorkerAbstract;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.MainActivity;

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
