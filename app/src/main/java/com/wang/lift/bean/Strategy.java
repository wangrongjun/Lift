package com.wang.lift.bean;

/**
 * by wangrongjun on 2016/11/17.
 */
public interface Strategy {

    enum HowToDo {
        /**
         * 不开门，走上
         */
        goUp,
        /**
         * 不开门，走下
         */
        goDown,
        /**
         * 停下开门让电梯中以当前层为目标层的人走出来，并且让当前楼需要乘电梯的人进来（如果有）
         */
        openDoor,
    }

    HowToDo toNewFloor(Lift lift);

}
