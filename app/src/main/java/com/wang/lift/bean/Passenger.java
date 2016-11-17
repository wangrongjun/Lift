package com.wang.lift.bean;

/**
 * by wangrongjun on 2016/11/17.
 */
public class Passenger {
    /**
     * 从哪层楼来乘电梯
     */
    private int fromFloor;
    /**
     * 要到哪层楼去
     */
    private int toFloor;
    /**
     * 开始等待的时刻
     */
    private int startWaitingTime;

    public Passenger(int fromFloor, int toFloor, int startWaitingTime) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.startWaitingTime = startWaitingTime;
    }

    public int getFromFloor() {
        return fromFloor;
    }


    public int getToFloor() {
        return toFloor;
    }


    public int getStartWaitingTime() {
        return startWaitingTime;
    }

}
