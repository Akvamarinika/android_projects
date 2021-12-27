package com.example.www;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import java.util.ArrayList;
import java.util.List;

public class Sprite {
    private Bitmap bitmap;

    private double x;
    private double y;

    private double velocityX;
    private double velocityY;

    private List<Rect> frames;
    private int frameWidth;
    private int frameHeight;
    private int currFrame = 0;
    private double frameTime = 25;
    private double timeForCurrFrame = 0.0;

    private int padding = 20;

    public Sprite(double x, double y, double velocityX, double velocityY, Rect initialFrame, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;

        this.bitmap = bitmap;

        this.frames = new ArrayList<>();
        this.frames.add(initialFrame);

        this.bitmap = bitmap;

        this.frameWidth = initialFrame.width();
        this.frameHeight = initialFrame.height();

    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public double getVy() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void addFrame (Rect frame) {
        frames.add(frame);
    }

    public Rect getRectangle() {
        return new Rect((int)x + padding,
                (int)y + padding,
                (int)(x + frameWidth - 2 * padding),
                (int)(y + frameHeight - 2 * padding));
    }

    // проверить прямоугольники на пересечение
    public boolean intersect (Sprite sprite) {
        return getRectangle().intersect(sprite.getRectangle());
    }

    // смена кадров спрайта по времени:
    public void updateSprite (int millis) {
        x = x + velocityX * millis/1000.0;
        y = y + velocityY * millis/1000.0;

        timeForCurrFrame += millis;

        if (timeForCurrFrame >= frameTime) {
            currFrame = (currFrame + 1) % frames.size();
            timeForCurrFrame = timeForCurrFrame - frameTime;
        }
    }

    // Отрисовка спрайтов
    public void draw (Canvas canvas) {
        Paint paint = new Paint();

        Rect destination = new Rect((int)x, (int)y, (int)(x + frameWidth), (int)(y + frameHeight));
        canvas.drawBitmap(bitmap, frames.get(currFrame), destination,  paint);
    }

}
