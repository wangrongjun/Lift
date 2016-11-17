package com.wang.lift.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2016/11/17.
 */
public class Lift {

    public enum State {
        stop, rising, falling;
    }

    private State state;

    private int currentFloor;
    private int maxFloor;
    /**
     * 在每一层的电梯门口等待的乘客
     */
    private List<List<Passenger>> waitingPassengerList;
    /**
     * 电梯中正在乘坐电梯的乘客
     */
    private List<Passenger> takingPassengerList;

    public Lift(int maxFloor) {
        this.maxFloor = maxFloor;
        state = Lift.State.stop;
        currentFloor = 0;
        takingPassengerList = new ArrayList<>();
        waitingPassengerList = new ArrayList<>();
        for (int i = 0; i < maxFloor; i++) {
            waitingPassengerList.add(new ArrayList<Passenger>());
        }
    }

    public List<List<Passenger>> getWaitingPassengerList() {
        return waitingPassengerList;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getMaxFloor() {
        return maxFloor;
    }

    public void setWaitingPassengerList(List<List<Passenger>> waitingPassengerList) {
        this.waitingPassengerList = waitingPassengerList;
    }

    public void setTakingPassengerList(List<Passenger> takingPassengerList) {
        this.takingPassengerList = takingPassengerList;
    }

    public void setMaxFloor(int maxFloor) {
        this.maxFloor = maxFloor;
    }

    public List<Passenger> getTakingPassengerList() {
        return takingPassengerList;
    }
}
