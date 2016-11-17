package com.wang.lift.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wang.lift.R;
import com.wang.lift.bean.Lift;
import com.wang.lift.bean.Passenger;
import com.wang.lift.view.LiftView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.line_view)
    LiftView lineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_start)
    public void onClick() {
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

        lineView.setLift(lift);
    }
}
