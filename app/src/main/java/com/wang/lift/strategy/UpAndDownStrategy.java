package com.wang.lift.strategy;

import com.wang.lift.bean.Lift;

/**
 * by wangrongjun on 2016/11/24.
 * 上到尽头，再下到尽头策略。
 */
public class UpAndDownStrategy extends DefaultStrategy {
    /**
     * 一、在上升状态时：
     * 1.上方没人等电梯 且 电梯内没人要到上方楼层，则下降。
     * 2.否则继续上升。
     * <p/>
     * 二、在下降状态时：
     * 1.下方没人等电梯 且 电梯内没人要到下方楼层，则上升。
     * 2.否则继续下降。
     * <p/>
     * 1.下方没人等电梯 且 电梯内没人要到下方楼层 且 电梯内有人要到上方楼层 且 没到底层，则上升。
     * 2.否则如果到了底层，则停止
     * 3.否则继续下降。
     * <p/>
     * 三、在停止状态时（在最底层）：
     * 1.上方有人等电梯，则上升。
     * 2.否则继续停止。
     */
    @Override
    public void moveLift(Lift lift) {
        int floorNumber = lift.getFloorNumber();
        int currentFloor = lift.getCurrentFloor();
        switch (lift.getState()) {

            case rising:
                //上方没人等电梯
                boolean b1 = !haveWaitingPassenger(lift, currentFloor, floorNumber - 1);
                //电梯内没人要到上方楼层
                boolean b2 = !haveTakingPassengerToUp(lift);
                if (b1 && b2) {
                    fall(lift);
                } else {
                    rise(lift);
                }
                break;

            case falling:
                //下方没人等电梯
                b1 = !haveWaitingPassenger(lift, 0, currentFloor);
                //电梯内没人要到下方楼层
                b2 = !haveTakingPassengerToDown(lift);
                //电梯内有人要到上方楼层
                boolean b3 = haveTakingPassengerToUp(lift);
                if (b1 && b2 && b3 && !inBottom(lift)) {
                    rise(lift);
                } else if (inBottom(lift)) {
                    stop(lift);
                } else {
                    fall(lift);
                }
                break;

            case stop:
                //上方有人等电梯
                b1 = haveWaitingPassenger(lift, currentFloor, floorNumber - 1);
                if (b1) {
                    rise(lift);
                } else {
                    stop(lift);
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
