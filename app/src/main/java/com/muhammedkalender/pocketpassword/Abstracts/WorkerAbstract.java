package com.muhammedkalender.pocketpassword.Abstracts;

import com.muhammedkalender.pocketpassword.Interfaces.WorkerInterface;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class WorkerAbstract implements WorkerInterface {
    protected boolean active = false;
    protected boolean started = false;

    public abstract void action();

    private CountDownLatch countDownLatch;

    @Override
    public void timer(int milliSeconds) {
        setStarted(true);
        setActive(true);

        while (isActive()){
            countDownLatch = new CountDownLatch(1);

            action();

            try {
                countDownLatch.await(milliSeconds, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                //TODO Helpers.logger.error();
            }
        }

        setStarted(false);
    }

    public void timerWithMinute(int minute){
        this.timer(minute * 1000);
    }

    @Override
    public void stop() {
        setStarted(false);
        setActive(false);
    }

    @Override
    public void start() {
      setActive(true);

        if(!isStarted()){
            action();
        }
    }

    @Override
    public void pause() {
        if(isStarted()){
            setActive(false);
        }
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
