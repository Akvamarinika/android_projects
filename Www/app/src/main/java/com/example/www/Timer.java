package com.example.www;

import android.os.CountDownTimer;

public class Timer  extends CountDownTimer {
    private DrawThread drawThread;

    public Timer(DrawThread drawThread, long countDownInterval) {
        super(Integer.MAX_VALUE, countDownInterval);
        this.drawThread = drawThread;
    }

    @Override
    public void onTick(long l) {
        drawThread.updateSurface();
    }

    @Override
    public void onFinish() {

    }
}
