package com.wang.lift.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wang.android_lib.util.DialogUtil;
import com.wang.android_lib.util.M;
import com.wang.java_util.MathUtil;
import com.wang.lift.R;
import com.wang.lift.bean.Lift;
import com.wang.lift.bean.Passenger;
import com.wang.lift.strategy.UpAndDownStrategy;
import com.wang.lift.view.LiftView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Bind(R.id.lift_view)
    LiftView liftView;
    @Bind(R.id.ll_btn_add)
    LinearLayout llBtnAdd;
    @Bind(R.id.btn_run_or_pause)
    Button btnRun;
    @Bind(R.id.btn_speed)
    Button btnSpeed;
    @Bind(R.id.btn_random)
    Button btnRandom;

    private Lift lift;
    /**
     * 子线程结束的判断条件
     */
    private boolean canRun = false;
    /**
     * 电梯运行速度，即子线程沉睡时间。1为1秒，2为0.5秒，4为0.25秒，以此类推，sleepTime=1000/speed
     */
    private int speed = 1;
    /**
     * 是否需要生成随机乘客
     */
    private boolean canCreateRandomPassenger = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        lift = new Lift(8);
        initSpeed();
        updateAddButton();
        showLiftDelay();
    }

    private void initSpeed() {
        speed = 1;
        btnSpeed.setText("慢");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRun();
    }

    private int fromFloor = -1;

    private void updateAddButton() {

        for (int i = 0; i < llBtnAdd.getChildCount(); i++) {//先清空已存在的按钮
            llBtnAdd.removeView(llBtnAdd.getChildAt(i));
            i--;
        }

        for (int i = 0; i < lift.getFloorNumber(); i++) {
            final ImageView imageView = new ImageView(this);
            imageView.setImageResource(android.R.drawable.ic_input_add);
            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (fromFloor == -1) {//若当前状态为选择从第几层楼
                        for (int i = 0; i < lift.getFloorNumber(); i++) {
                            ImageView iv = (ImageView) llBtnAdd.getChildAt(i);
                            iv.setImageResource(android.R.drawable.ic_menu_add);
                        }
                        fromFloor = finalI;
                    } else {//若当前状态为选择到第几层楼
                        addWaitingPassenger(fromFloor, finalI);
                        for (int i = 0; i < lift.getFloorNumber(); i++) {
                            ImageView iv = (ImageView) llBtnAdd.getChildAt(i);
                            iv.setImageResource(android.R.drawable.ic_input_add);
                        }
                        fromFloor = -1;
                    }

                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    0
            );
            params.weight = 1;
            llBtnAdd.addView(imageView, params);
        }
    }

    private void showLiftDelay() {
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                liftView.setLift(lift);
                return true;
            }
        }).sendEmptyMessageDelayed(0, 300);
    }

    private void addWaitingPassenger(int fromFloor, int toFloor) {
        if (fromFloor == toFloor) {
            M.t(this, "不能相同");
        } else {
            Passenger passenger = new Passenger(fromFloor, toFloor, 0);
            lift.getWaitingPassengerList().get(fromFloor).add(passenger);
            liftView.setLift(lift);
        }
    }
/*
    private void showLift() {
        Lift lift = new Lift(8);

        lift.setCurrentFloor(4);

        List<Passenger> takingPassengerList = lift.getTakingPassengerList();
        for (int i = 1; i <= 5; i++) {
            takingPassengerList.add(new Passenger(0, i, 0));
        }

        List<List<Passenger>> waitingPassengerList = lift.getWaitingPassengerList();
        for (int i = 0; i < waitingPassengerList.size(); i++) {
            List<Passenger> passengerList = waitingPassengerList.get(i);
            for (int j = 1; j <= 5; j++) {
                passengerList.add(new Passenger(0, j, 0));
            }
        }

        liftView.setLift(lift);
    }*/

    @OnClick({R.id.btn_run_or_pause, R.id.btn_restore, R.id.btn_floor_number, R.id.btn_speed, R.id.btn_strategy, R.id.btn_random})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_run_or_pause:
                startRun();
                break;
            case R.id.btn_restore:
                stopRun();
                lift = new Lift(lift.getFloorNumber());
                liftView.setLift(lift);
                break;
            case R.id.btn_floor_number:
                changeFloorNumber();
                break;
            case R.id.btn_speed:
                changeSpeed();
                break;
            case R.id.btn_strategy:
                break;
            case R.id.btn_random:
                toggleRandom();
                break;
        }
    }

    private void toggleRandom() {
        if (canCreateRandomPassenger) {
            canCreateRandomPassenger = false;
            btnRandom.setText("生成乘客");
        } else {
            canCreateRandomPassenger = true;
            btnRandom.setText("不生成");
        }
    }

    private void changeSpeed() {
        switch (speed) {
            case 1:
                speed = 2;
                btnSpeed.setText("中");
                break;
            case 2:
                speed = 3;
                btnSpeed.setText("快");
                break;
            case 3:
                speed = 1;
                btnSpeed.setText("慢");
                break;
        }
    }

    private void changeFloorNumber() {
        String s = lift.getFloorNumber() + "";
        DialogUtil.showInputDialog(this, "输入楼层数", s, new DialogUtil.OnInputFinishListener() {
            @Override
            public void onInputFinish(String text) {
                try {
                    int i = Integer.parseInt(text);
                    if (i > 0) {
                        stopRun();
                        lift = new Lift(i);
                        liftView.setLift(lift);
                        updateAddButton();
                        return;
                    }
                } catch (NumberFormatException ignored) {
                }
                M.t(MainActivity.this, "请输入正确的数字");
            }
        });
    }

    private void startRun() {
        canRun = true;
        runLift();
        btnRun.setText("暂停");
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRun();
            }
        });
    }

    private void stopRun() {
        canRun = false;
        btnRun.setText("运行");
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRun();
            }
        });
    }

    public void runLift() {
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
//                SillyStrategy strategy = new SillyStrategy();
                UpAndDownStrategy strategy = new UpAndDownStrategy(lift.getFloorNumber() / 2);
                if (msg.what == 0) {
                    strategy.updateCurrentFloorPassenger(lift);
                } else if (msg.what == 1) {
                    strategy.moveLift(lift);
                } else {
                    int from;
                    int to;
                    do {
                        from = MathUtil.random(0, lift.getFloorNumber() - 1);
                    } while (from == lift.getCurrentFloor());
                    do {
                        to = MathUtil.random(0, lift.getFloorNumber() - 1);
                    } while (to == from);
                    Passenger passenger = new Passenger(from, to, 0);
                    lift.getWaitingPassengerList().get(from).add(passenger);
                }
                liftView.setLift(lift);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (canRun) {
                    if (canCreateRandomPassenger) {
                        handler.sendEmptyMessage(2);
                    }
                    try {
                        Thread.sleep((long) (1000 / speed));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!canRun) {
                        return;
                    }
                    handler.sendEmptyMessage(0);
                    try {
                        Thread.sleep((long) (1000 / speed));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!canRun) {
                        return;
                    }
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();

    }

}
