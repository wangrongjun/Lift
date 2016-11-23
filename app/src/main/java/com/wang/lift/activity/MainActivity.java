package com.wang.lift.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wang.android_lib.util.DialogUtil;
import com.wang.android_lib.util.M;
import com.wang.lift.R;
import com.wang.lift.bean.Lift;
import com.wang.lift.bean.Passenger;
import com.wang.lift.strategy.SillyStrategy;
import com.wang.lift.view.LiftView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.lift_view)
    LiftView liftView;
    @Bind(R.id.ll_btn_add)
    LinearLayout llBtnAdd;
    @Bind(R.id.btn_run)
    Button btnRun;

    private Lift lift;
    private static int floorNumber = 8;//只在改变楼层数后需要重启时用到

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        lift = new Lift(floorNumber);
        updateAddButton();
        showLiftDelay();
    }

    private int fromFloor = -1;

    private void updateAddButton() {
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

    @OnClick({R.id.btn_run, R.id.btn_restore, R.id.btn_floor_number})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_run:
                btnRun.setClickable(false);
                startRun();
                break;
            case R.id.btn_restore:
                restore();
                break;
            case R.id.btn_floor_number:
                changeFloorNumber();
                break;
        }
    }

    private void restore() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void changeFloorNumber() {
        String s = lift.getFloorNumber() + "";
        DialogUtil.showInputDialog(this, "输入楼层数", s, new DialogUtil.OnInputFinishListener() {
            @Override
            public void onInputFinish(String text) {
                try {
                    int i = Integer.parseInt(text);
                    if (i > 0) {
                        floorNumber = i;
                        restore();
                        return;
                    }
                } catch (NumberFormatException ignored) {
                }
                M.t(MainActivity.this, "请输入正确的数字");
            }
        });
    }

    public void startRun() {
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SillyStrategy strategy = new SillyStrategy();
                if (msg.what == 0) {
                    strategy.updateCurrentFloorPassenger(lift);
                } else {
                    strategy.moveLift(lift);
                }
                liftView.setLift(lift);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();


    }

}
