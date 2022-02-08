package es.saj.cuentamelaspelotas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GraphicView extends View {
    Context context;
    Paint text;
    Paint[] paintBalls;
    static ArrayList<int[]> balls;

    final int RADIO = 45 ;
    int initPosText = getHeight()+ 32,
        textSpeed = 13,
        difficulty = 0;
    int centroX, centroY, speedX, speedY;
    int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA, Color.YELLOW};
    int x = getWidth(), y = getHeight();


    public GraphicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        text = new Paint();
        text.setColor(Color.BLUE);
        text.setTextSize(100);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        centroX = w / 2;
        centroY = h / 2;
    }

    protected void onDraw(Canvas c) {
        final int screenHeight = getHeight();
        if (initPosText < screenHeight+500){
            paintText(c, screenHeight);
        } else if (difficulty != 0){
            if (balls == null){
                createBalls(difficulty);
                new CountDownTimer(10000, 1) {

                    @Override
                    public void onTick(long l) {
                        Log.v("jamaica", "son: "+l/1000);
                        if (l < 4000){
                            Paint p = new Paint();
                            p.setTextSize(200);
                            p.setColor(Color.BLACK);
                            String t = l/1000+"";
                            c.drawText(t, x+490, y+1000, p);
                        }
                        paintBalls(c);
                    }

                    @Override
                    public void onFinish() {
                        Log.v("jamaica", "SACAB0!");
                        goToGuess();

                    }
                }.start();
            }


        } else {
            String sorry = "We're sorry, something bad happened...";
            text.setTextSize(50);
            c.drawText(sorry, centroX-250, centroY, text);
        }
        postInvalidateDelayed(1);
    }

    private void goToGuess() {
        Intent intent = new Intent(context, GuessActivity.class);
        intent.putExtra("nballs", balls.size());
        intent.putExtra("difficulty", difficulty);
        for (int i = 0; i< balls.size(); i++) {
            intent.putExtra(String.valueOf(i), balls.get(i));
        }
    }


    private void paintText(Canvas c, int screenHeight) {
        initPosText += textSpeed;
        if (initPosText < screenHeight+100){
            c.drawText("Ready?", centroX-150, initPosText, text);
        }

    }

    private void paintBalls(Canvas c) {
        for (int i = 0; i < balls.size(); i++) {
            paintBall(balls.get(i), c, i);
        }
    }

    private void paintBall(int[] ball, Canvas c, int i) {
        ball[0] += ball[2];
        ball[1] += ball[3];

        if (ball[0] >= getWidth()){
            ball[0] = getWidth();
            ball[2] *= -1;
        }

        if (ball[0] <= RADIO){
            ball[0] = RADIO;
            ball[2] *= -1;
        }

        if (ball[1] >= getHeight()){
            ball[1] = getHeight();
            ball[3] *= -1;
        }

        if (ball[1] <= RADIO){
            ball[1] = RADIO;
            ball[3] *= -1;
        }
        //Log.v("jamaica");

        Paint p = new Paint();
        p.setColor(ball[4]);
        c.drawCircle(ball[0], ball[1], RADIO, p);
    }


    private void createBalls(int difficulty) {
        int nBalls;
        switch (difficulty){
            case 1:/*Easy Mode*/  //Log.v("jamaica", "izi mode");
                nBalls = ThreadLocalRandom.current().nextInt(2, 4);
                balls = inflateBalls(nBalls, difficulty);
                break;
            case 2:/*Normal Mode*/ //Log.v("jamaica", "normalelelemode");
                nBalls = ThreadLocalRandom.current().nextInt(4, 7);
                balls = inflateBalls(nBalls, difficulty);
                break;
            case 3:/*Hard Mode*/ //Log.v("jamaica", "Dificilisimode");
                nBalls = ThreadLocalRandom.current().nextInt(7, 11);
                balls = inflateBalls(nBalls, difficulty);
                break;
        }
    }

    private ArrayList<int[]> inflateBalls(int nBalls, int difficulty) {
        int randomX, randomY;
        int[] ball;
        ArrayList<int[]> balls= new ArrayList<int[]>();
        for (int i = 0; i<nBalls; i++){
            randomX = ThreadLocalRandom.current().nextInt(RADIO, getWidth());
            randomY = ThreadLocalRandom.current().nextInt(RADIO, getHeight());
            switch (difficulty){
                case 1:
                    speedX = ThreadLocalRandom.current().nextInt(5, 10);
                    speedY = ThreadLocalRandom.current().nextInt(5, 10);
                    break;
                case 2:
                    speedX = ThreadLocalRandom.current().nextInt(5, 15);
                    speedY = ThreadLocalRandom.current().nextInt(5, 15);
                    break;
                case 3:
                    speedX = ThreadLocalRandom.current().nextInt(5, 20);
                    speedY = ThreadLocalRandom.current().nextInt(5, 20);
                    break;
            }
            ball = new int[] {randomX, randomY, speedX, speedY, colors[ThreadLocalRandom.current().nextInt(0, colors.length)]};
            balls.add(ball);
        }
        return balls;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}