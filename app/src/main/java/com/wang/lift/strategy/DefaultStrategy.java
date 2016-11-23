package com.wang.lift.strategy;

import com.wang.lift.bean.Lift;
import com.wang.lift.bean.Passenger;

import java.util.List;

/**
 * by wangrongjun on 2016/11/24.
 */
public abstract class DefaultStrategy implements Strategy {

    @Override
    public void moveLift(Lift lift) {
    }

    @Override
    public void updateCurrentFloorPassenger(Lift lift) {
        List<Passenger> takingPassengerList = lift.getTakingPassengerList();
        for (int i = 0; i < takingPassengerList.size(); i++) {//删除要出电梯的人
            Passenger passenger = takingPassengerList.get(i);
            if (passenger.getToFloor() == lift.getCurrentFloor()) {
                takingPassengerList.remove(i);
                i--;
            }
        }

        List<Passenger> passengerList = lift.getWaitingPassengerList().get(lift.getCurrentFloor());
        for (int i = 0; i < passengerList.size(); i++) {//移动本层所有要乘电梯的人
            takingPassengerList.add(passengerList.get(i));
            passengerList.remove(i);
            i--;
        }
    }

    protected boolean haveWaitingPassenger(Lift lift, int fromFloor, int toFloor) {
        boolean have = false;
        for (int i = fromFloor; i <= toFloor; i++) {
            if (lift.getWaitingPassengerList().get(i).size() != 0) {
                have = true;
            }
        }
        return have;
    }

}
