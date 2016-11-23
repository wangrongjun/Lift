package com.wang.lift.strategy;

import com.wang.lift.bean.Lift;

/**
 * by wangrongjun on 2016/11/17.
 */
public interface Strategy {

    /**
     * 在当前楼层，删除电梯中要出来的人，移动门口的人到电梯中。
     * <p/>
     * 本方法只允许更改当前楼层的乘客和电梯内的乘客。
     */
    void updateCurrentFloorPassenger(Lift lift);

    /**
     * 对电梯进行移动：上移，下移，不动。对应的currentFloor++/--/不动
     * 前提条件：电梯已经没有人要出来，楼层也没有人要进去。
     * <p/>
     * 本方法只允许更改currentFloor和state。
     */
    void moveLift(Lift lift);

}
