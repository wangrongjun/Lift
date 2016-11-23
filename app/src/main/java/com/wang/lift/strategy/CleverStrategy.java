package com.wang.lift.strategy;

import com.wang.lift.bean.Lift;

/**
 * by wangrongjun on 2016/11/17.
 * 上到尽头，再下到尽头
 */
public class CleverStrategy extends DefaultStrategy {

    /**
     * 2.如果在最底层，且上方没人等电梯，就不动（没有乘客，就停在最底层）。
     * 3.上升时，如果上方有人等电梯，往上走；否则往下走（不过下方有没有人）。
     * 4.下降时，如果下方有人等电梯，往下走；否则：如果上方有人等电梯，往上走，否则往下走。
     */
    @Override
    public void moveLift(Lift lift) {
        int fromFloor;
        int toFloor; //fromFloor < toFloor
        switch (lift.getState()) {
            case stop:
                fromFloor = 0;
                toFloor = lift.getFloorNumber() - 1;
            case rising:
                fromFloor = lift.getCurrentFloor();
                toFloor = lift.getFloorNumber() - 1;
                break;
            case falling:
                fromFloor = 0;
                toFloor = lift.getCurrentFloor();
                break;
        }
    }

}