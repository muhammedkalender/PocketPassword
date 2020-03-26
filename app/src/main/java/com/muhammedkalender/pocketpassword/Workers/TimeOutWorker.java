package com.muhammedkalender.pocketpassword.Workers;

import com.muhammedkalender.pocketpassword.Abstracts.WorkerAbstract;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Interfaces.WorkerInterface;

public class TimeOutWorker extends WorkerAbstract implements WorkerInterface {
    int everyMillSeconds = 0;

    public void action(){
        try{
            Helpers.logger.info("sayaç");
        }catch (Exception e){
            //todo
        }
    }
}
