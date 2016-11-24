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

    /**
     * 上方是否有人在等电梯
     */
    public boolean haveWaitingPassengerOnTop(Lift lift) {
        boolean have = false;
        for (int i = lift.getCurrentFloor() + 1; i < lift.getFloorNumber(); i++) {
            if (lift.getWaitingPassengerList().get(i).size() != 0) {
                have = true;
            }
        }
        return have;
    }

    /**
     * 下方是否有人在等电梯
     */
    public boolean haveWaitingPassengerOnDown(Lift lift) {
        boolean have = false;
        for (int i = 0; i < lift.getCurrentFloor(); i++) {
            if (lift.getWaitingPassengerList().get(i).size() != 0) {
                have = true;
            }
        }
        return have;
    }

    /**
     * 是否有人在等电梯
     */
    public boolean haveWaitingPassenger(Lift lift) {
        boolean have = false;
        for (int i = 0; i < lift.getFloorNumber(); i++) {
            if (lift.getWaitingPassengerList().get(i).size() != 0) {
                have = true;
            }
        }
        return have;
    }

    /**
     * 电梯内是否有乘客
     */
    public boolean haveTakingPassenger(Lift lift) {
        return lift.getTakingPassengerList().size() != 0;
    }

    /**
     * 电梯内是否有乘客要去上方楼层
     */
    public boolean haveTakingPassengerToUp(Lift lift) {
        if (!haveTakingPassenger(lift)) {
            return false;
        }
        boolean have = false;
        List<Passenger> takingPassengerList = lift.getTakingPassengerList();
        for (int i = 0; i < takingPassengerList.size(); i++) {
            Passenger passenger = takingPassengerList.get(i);
            if (passenger.getToFloor() > lift.getCurrentFloor()) {
                have = true;
            }
        }
        return have;
    }

    /**
     * 电梯内是否有乘客要去下方楼层
     */
    public boolean haveTakingPassengerToDown(Lift lift) {
        if (!haveTakingPassenger(lift)) {
            return false;
        }
        boolean have = false;
        List<Passenger> takingPassengerList = lift.getTakingPassengerList();
        for (int i = 0; i < takingPassengerList.size(); i++) {
            Passenger passenger = takingPassengerList.get(i);
            if (passenger.getToFloor() < lift.getCurrentFloor()) {
                have = true;
            }
        }
        return have;
    }

}
