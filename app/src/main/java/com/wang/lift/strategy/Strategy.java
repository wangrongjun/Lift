package com.wang.lift.strategy;

import com.wang.lift.bean.Lift;

/**
 * by wangrongjun on 2016/11/17.
 * 这里所有接口的调用前提是：
 * 1.电梯内想要出去到当前楼层的人已全部出去。
 * 2.当前楼层想要进来乘电梯的人已全部进电梯。
 * 所以在实现这些接口方法时不用考虑当前楼层的情况。
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
     * <p/>
     * 本方法只允许更改currentFloor和state。
     */
    void moveLift(Lift lift);

}
