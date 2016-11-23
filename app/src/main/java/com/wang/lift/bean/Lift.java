package com.wang.lift.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2016/11/17.
 */
public class Lift {

    public enum State {
        stop,//因为没有乘客，电梯不运行，一般停在最低层，但不强制要求
        rising,//处于上升状态
        falling//处于下降状态
    }

    private State state;
    /**
     * 从0到floorNumber-1
     */
    private int currentFloor;
    private int floorNumber;
    /**
     * 在每一层的电梯门口等待的乘客
     */
    private List<List<Passenger>> waitingPassengerList;
    /**
     * 电梯中正在乘坐电梯的乘客
     */
    private List<Passenger> takingPassengerList;

    public Lift(int floorNumber) {
        this.floorNumber = floorNumber;
        state = Lift.State.stop;
        currentFloor = 0;
        takingPassengerList = new ArrayList<>();
        waitingPassengerList = new ArrayList<>();
        for (int i = 0; i < floorNumber; i++) {
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

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setWaitingPassengerList(List<List<Passenger>> waitingPassengerList) {
        this.waitingPassengerList = waitingPassengerList;
    }

    public void setTakingPassengerList(List<Passenger> takingPassengerList) {
        this.takingPassengerList = takingPassengerList;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public List<Passenger> getTakingPassengerList() {
        return takingPassengerList;
    }
}
