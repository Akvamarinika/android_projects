package com.example.www;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {
    //поверхность для рисования:
    private final SurfaceHolder surfaceHolder;

    private volatile boolean isRun = true;
    private Context context;

    private final Sprite player;
    private final Sprite enemy;

    private final Bitmap bitmapBG;

    private int surfaceWidth;
    private int surfaceHeight;

    private Timer timer;

    private final int intervalForUpdateSurface = 30;

    private int score = 0;

    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        this.context = context;
        this.surfaceHolder = surfaceHolder;

        //фон игры:
        bitmapBG = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);

        //создание спрайта игрока и доб. в него кадров:
        Bitmap playerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        int playerWidth = playerBitmap.getWidth()/5;
        int playerHeight = playerBitmap.getHeight()/3;
        Rect firstFrame = new Rect(0, 0, playerWidth, playerHeight);
        player = new Sprite(10, 0, 0, 400, firstFrame, playerBitmap);
        addFrameInPlayerSprite(playerWidth, playerHeight);

        //создание спрайта противника и доб. в него кадров:
        Bitmap enemyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        int enemyWidth = enemyBitmap.getWidth() / 5;
        int enemyHeight = enemyBitmap.getHeight() / 3;
        firstFrame = new Rect(4 * enemyWidth, 0, 5 * enemyWidth, enemyHeight);
        enemy = new Sprite(2000, 250, -400, 0, firstFrame, enemyBitmap);
        addFrameInEnemySprite(enemyWidth, enemyHeight);

        //запуск таймера для новой отрисовки surface через интервал:
        timer = new Timer(this, intervalForUpdateSurface);
        timer.start();
    }

    public void setFlagInStop() {
        isRun = false;
    }

    @Override
    public void run() {
        Canvas canvas;
        Paint bgPaint = new Paint();

        // кисть для подписи очков:
        Paint fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setTextSize(100);
        fontPaint.setStyle(Paint.Style.FILL);

        while (isRun) {
            //блокировка canvas перед перерисовкой
            canvas = surfaceHolder.lockCanvas();
            surfaceWidth = canvas.getWidth();
            surfaceHeight = canvas.getHeight();

            try {
                //рисование на Canvas. Прорисовка фона и созданных объектов:
                canvas.drawBitmap(bitmapBG, 0, 0, bgPaint); // фон
                player.draw(canvas);
                enemy.draw(canvas);
                canvas.drawText("Score: " + score, surfaceWidth - 500, 80, fontPaint);

            } finally {
                //разблокировка canvas после перерисовки
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    protected void updateSurface() {
        player.updateSprite(intervalForUpdateSurface);
        enemy.updateSprite(intervalForUpdateSurface);

        //касание границы экрана, развернуть игрока в противоположное направление (-5 очков):
        if (player.getY() + player.getFrameHeight() > surfaceHeight) {
            player.setY(surfaceHeight - player.getFrameHeight());
            player.setVelocityY(-player.getVy());
            score -= 5;
        } else if (player.getY() < 0) { // для верхней
            player.setY(0);
            player.setVelocityY(-player.getVy());
            score -= 5;
        }

        //при облете игроком противника начисляем очки +20
        if (enemy.getX() < - enemy.getFrameWidth()) {
            moveSpriteEnemy(enemy);
            score += 20;
        }

        // при пересечении игрока и противника снимаем очки -20, перемещение противника
        if (enemy.intersect(player)) {
            moveSpriteEnemy(enemy);
            score -= 20;
        }
    }

    //обработка касаний пользов-ля:
    public boolean onTouchEvent(MotionEvent event){
        int eventAction = event.getAction(); // coord

        if (eventAction == MotionEvent.ACTION_DOWN)  {
            //если прикосновения выше и ниже положения спрайта игрока, скорость изменяется:
             if (event.getY() < player.getRectangle().top) {
                player.setVelocityY(-400);
            } else if (event.getY() > (player.getRectangle().bottom)) {
                player.setVelocityY(400);
            }
        }
        return true;
    }

    //перемещение спрайта противника
    private void moveSpriteEnemy(Sprite sprite) {
        sprite.setX(surfaceWidth + Math.random() * 300);
        sprite.setY(Math.random() * (surfaceHeight - sprite.getFrameHeight()));
    }

    //нарезка спрайта игрока на кадры
    private void addFrameInPlayerSprite(int width, int height){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {

                if (i == 0 && j == 0) {
                    continue;
                }

                if (i == 2 && j == 3) {
                    continue;
                }

                player.addFrame(new Rect(j * width, i * height, j * width + width, i * width + width));
            }
        }
    }

    //нарезка спрайта противника на кадры
    private void addFrameInEnemySprite(int width, int height){
        for (int i = 0; i < 3; i++) {
            for (int j = 4; j >= 0; j--) {

                if (i ==0 && j == 4) {
                    continue;
                }

                if (i ==2 && j == 0) {
                    continue;
                }

                enemy.addFrame(new Rect(j * width, i * height, j * width + width, i * width + width));
            }
        }
    }

}

