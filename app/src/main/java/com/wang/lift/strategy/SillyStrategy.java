package com.wang.lift.strategy;

import com.wang.lift.bean.Lift;

/**
 * by wangrongjun on 2016/11/24.
 * 上到尽头，再下到尽头
 */
public class SillyStrategy extends DefaultStrategy {

    @Override
    public void moveLift(Lift lift) {
        int floorNumber = lift.getFloorNumber();
        int currentFloor = lift.getCurrentFloor();
        switch (lift.getState()) {
            case stop:
                if (haveWaitingPassenger(lift, 0, floorNumber - 1)) {
                    lift.setCurrentFloor(currentFloor + 1);
                    lift.setState(Lift.State.rising);
                }
                break;
            case rising:
                if (currentFloor == floorNumber - 1) {
                    lift.setCurrentFloor(currentFloor - 1);
                    lift.setState(Lift.State.falling);
                } else {
                    lift.setCurrentFloor(currentFloor + 1);
                }
                break;
            case falling:
                if (currentFloor == 0) {
                    if (haveWaitingPassenger(lift, 0, floorNumber - 1)) {
                        lift.setCurrentFloor(currentFloor + 1);
                        lift.setState(Lift.State.rising);
                    } else {
                        lift.setState(Lift.State.stop);
                    }
                } else {
                    lift.setCurrentFloor(currentFloor - 1);
                }
                break;
        }
    }
}
