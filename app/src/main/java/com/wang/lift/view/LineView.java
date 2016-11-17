package com.wang.lift.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Android 中通过Canvas 与线程结合实现动画效果
 * http://blog.csdn.net/adrian24/article/details/51623171
 */
public class LineView extends View {

    private int a = 0;
    private int b = -600;
    private int c = 600;
    private boolean isRun;

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        paint1.setStrokeWidth((float) 3.0);
        canvas.drawLine(50, 0, 50, 1300, paint1);
        canvas.drawLine(600, 0, 600, 1300, paint1);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(303, b, 333, b + 200, paint);
        canvas.drawRect(303, b + 300, 333, b + 500, paint);
        canvas.drawRect(303, a, 333, a + 200, paint);
        canvas.drawRect(303, a + 300, 333, a + 500, paint);
        canvas.drawRect(303, c, 333, c + 200, paint);
        canvas.drawRect(303, c + 300, 333, c + 500, paint);
    }

    public void moveView() {
        if (isRun == false) {
            isRun = true;
        } else if (isRun == true) {
            isRun = false;
        }
        new Thread(moveThread).start();
    }

    private final Runnable moveThread = new Runnable() {

        @Override
        public void run() {
            a = 0;
            b = -600;
            c = 600;
            while (isRun = true) {
                a += 5;
                b += 5;
                c += 5;
                if (c >= 1200) {
                    c = -600;
                }
                if (b >= 1200) {
                    b = -600;
                }
                if (a >= 1200) {
                    a = -600;
                }
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block  
                    e.printStackTrace();
                }
                postInvalidate();
            }
        }
    };

}
