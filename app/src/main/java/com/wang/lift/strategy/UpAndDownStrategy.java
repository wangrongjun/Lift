package com.wang.lift.strategy;

import com.wang.lift.bean.Lift;
import com.wang.lift.bean.Passenger;

/**
 * by wangrongjun on 2016/11/24.
 * 上到尽头，再下到尽头策略。
 */
public class UpAndDownStrategy extends DefaultStrategy {
    /**
     * 没人时停下的楼层
     */
    private int stayFloor;

    public UpAndDownStrategy(int stayFloor) {
        this.stayFloor = stayFloor;
    }

    /**
     * 一、在上升状态时：
     * 1.如果没人乘电梯也没人等电梯，则往stayFloor方向移动（如果当前就是stayFloor，则停止）。
     * 2.否则如果上方没人等电梯 且 电梯内没人要到上方楼层，则下降。
     * 3.否则继续上升。
     * <p/>
     * 二、在下降状态时：
     * 1.如果没人乘电梯也没人等电梯，则往stayFloor方向移动（如果当前就是stayFloor，则停止）。
     * 2.否则如果下方没人等电梯 且 电梯内没人要到下方楼层，则上升。
     * 3.否则继续下降。
     * <p/>
     * 三、在停止状态时：
     * 1.如果没人等电梯 且 电梯内没人，则继续停止。
     * 2.否则如果下方有人等电梯，则下降。如果上方有人等电梯，则上升。如果电梯内有人，则根据乘客移动。
     */
    @Override
    public void moveLift(Lift lift) {
        int currentFloor = lift.getCurrentFloor();
        switch (lift.getState()) {

            case rising:
                //如果没人乘电梯也没人等电梯，则往stayFloor方向移动（如果当前就是stayFloor，则停止）
                if (!haveTakingPassenger(lift) && !haveWaitingPassenger(lift)) {
                    if (stayFloor == currentFloor) {
                        stop(lift);
                    } else if (stayFloor > currentFloor) {
                        rise(lift);
                    } else {
                        fall(lift);
                    }

                    //否则如果上方没人等电梯 且 电梯内没人要到上方楼层，则下降。
                } else if (!haveWaitingPassengerOnTop(lift) && !haveTakingPassengerToUp(lift)) {
                    fall(lift);

                    //否则继续上升
                } else {
                    rise(lift);
                }
                break;

            case falling:
                //如果没人乘电梯也没人等电梯，则往stayFloor方向移动（如果当前就是stayFloor，则停止）
                if (!haveTakingPassenger(lift) && !haveWaitingPassenger(lift)) {
                    if (stayFloor == currentFloor) {
                        stop(lift);
                    } else if (stayFloor > currentFloor) {
                        rise(lift);
                    } else {
                        fall(lift);
                    }

                    //否则如果下方没人等电梯 且 电梯内没人要到下方楼层，则上升。
                } else if (!haveWaitingPassengerOnDown(lift) && !haveTakingPassengerToDown(lift)) {
                    rise(lift);

                    //否则继续下降
                } else {
                    fall(lift);
                }
                break;

            case stop:
                //如果没人等电梯 且 电梯内没人，则继续停止。
                if (!haveWaitingPassenger(lift) && !haveTakingPassenger(lift)) {
                    stop(lift);

                    //否则如果下方有人等电梯，则下降。
                    // 如果上方有人等电梯，则上升。
                    // 如果电梯内有人，则根据乘客移动。
                } else if (haveWaitingPassengerOnDown(lift)) {
                    fall(lift);
                } else if (haveWaitingPassengerOnTop(lift)) {
                    rise(lift);
                } else {
                    Passenger passenger = lift.getTakingPassengerList().get(0);
                    if (passenger.getToFloor() < currentFloor) {
                        fall(lift);
                    } else {
                        rise(lift);
                    }
                }
                break;

        }
    }

    public void rise(Lift lift) {
        lift.setCurrentFloor(lift.getCurrentFloor() + 1);
        lift.setState(Lift.State.rising);
    }

    public void fall(Lift lift) {
        lift.setCurrentFloor(lift.getCurrentFloor() - 1);
        lift.setState(Lift.State.falling);
    }

    public void stop(Lift lift) {
        lift.setState(Lift.State.stop);
    }

}
