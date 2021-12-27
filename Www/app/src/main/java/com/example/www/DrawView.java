package com.example.www;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    //доп. рисующий поток:
    private DrawThread drawThread;

    public DrawView(Context context) {
        super(context);
        //доступ к поверхности рисования
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //При создании SV: (создание и запуск доп.потока)
        drawThread = new DrawThread(getContext(),getHolder());
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //При изменении размеров SV:
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.setFlagInStop();
        boolean retry = true;

        while (retry){
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException ignored) {}
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return drawThread.onTouchEvent(event);
    }
}
