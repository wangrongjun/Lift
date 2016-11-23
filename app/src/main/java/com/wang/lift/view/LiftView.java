package com.wang.lift.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wang.lift.bean.Lift;
import com.wang.lift.bean.Passenger;

import java.util.List;

/**
 * by wangrongjun on 2016/11/17.
 */
public class LiftView extends SurfaceView {

    private Lift lift;

    public LiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(callback);
        setZOrderOnTop(true);//设置画布  背景透明
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    private void drawLift(Canvas canvas) {

        if (lift == null) {
            return;
        }

        Paint paint = new Paint();
        paint.setTextSize(16);
        paint.setColor(Color.BLACK);

        //画楼层编号和间隔线，楼层的高为1/maxFloor
        int floorNumber = lift.getFloorNumber();
        for (int i = 0; i < floorNumber; i++) {
            double scale = i * 1.0 / floorNumber;
            canvas.drawLine(x(0), y(scale), x(1), y(scale), paint);
            canvas.drawText(i + "楼", x(0.03), y(scale + 0.05), paint);
        }

        //画电梯，电梯的y轴高1/maxFloor，x轴从0.1到0.6
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3f);
        double liftTop = lift.getCurrentFloor() * 1.0 / floorNumber;
        double liftBottom = (lift.getCurrentFloor() + 1) * 1.0 / floorNumber;
        double liftLeft = 0.1;
        double liftRight = 0.6;
        canvas.drawRect(x(liftLeft), y(liftTop), x(liftRight), y(liftBottom), paint);

        //画电梯中的人
        List<Passenger> takingPassengerList = lift.getTakingPassengerList();
        int radius = 15;
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.GREEN);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAntiAlias(true);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(18);
        int textWidth = (int) textPaint.measureText("0");
        for (int i = 0; i < takingPassengerList.size(); i++) {
            String toFloor = takingPassengerList.get(i).getToFloor() + "";
            canvas.drawCircle(x(liftLeft) + (2 * i + 1) * radius, y(liftTop) + radius, radius, bgPaint);
            int textCenterX = x(liftLeft) + (2 * i + 1) * radius - textWidth / 2;
            int textCenterY = y(liftTop) + radius + textWidth / 2;
            canvas.drawText(toFloor, textCenterX, textCenterY, textPaint);
        }

        //画每个楼层等待电梯的人
        List<List<Passenger>> waitingPassengerList = lift.getWaitingPassengerList();
        for (int i = 0; i < waitingPassengerList.size(); i++) {

            List<Passenger> passengerList = waitingPassengerList.get(i);//某一层正在等待乘客列表
            double doorLeft = liftRight;
            double doorTop = i * 1.0 / floorNumber;

            for (int j = 0; j < passengerList.size(); j++) {
                String toFloor = passengerList.get(j).getToFloor() + "";

                int circleCenterX = x(doorLeft) + (2 * j + 1) * radius;
                int circleCenterY = y(doorTop) + radius;
                canvas.drawCircle(circleCenterX, circleCenterY, radius, bgPaint);

                int textCenterX = x(doorLeft) + (2 * j + 1) * radius - textWidth / 2;
                int textCenterY = y(doorTop) + radius + textWidth / 2;

                canvas.drawText(toFloor, textCenterX, textCenterY, textPaint);
            }
        }

    }

    public void setLift(Lift lift) {
        this.lift = lift;

        clearCanvas();

        Canvas canvas = getHolder().lockCanvas();
        drawLift(canvas);
        getHolder().unlockCanvasAndPost(canvas);
    }

    private void clearCanvas() {
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.WHITE);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
        getHolder().unlockCanvasAndPost(canvas);

        canvas = getHolder().lockCanvas();
        getHolder().unlockCanvasAndPost(canvas);
    }

    private int x(double scale) {
        return (int) (getMeasuredWidth() * scale);
    }

    private int y(double scale) {
        return (int) (getMeasuredHeight() * scale);
    }

}
