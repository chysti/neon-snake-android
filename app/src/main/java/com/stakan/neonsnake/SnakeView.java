package com.stakan.neonsnake;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class SnakeView extends View {
    private static final int COLS=18,ROWS=26;
    private final Paint p=new Paint(3); private final Random random=new Random();
    private final ToneGenerator tones=new ToneGenerator(AudioManager.STREAM_MUSIC,45);
    private final Deque<Point> snake=new ArrayDeque<>();
    private final SharedPreferences prefs;
    private Point food; private int dx=1,dy=0,nextDx=1,nextDy=0,score,best;
    private boolean gameOver,paused; private long lastStep; private float downX,downY;

    public SnakeView(Context c){super(c);setLayerType(View.LAYER_TYPE_SOFTWARE,null);prefs=c.getSharedPreferences("snake",0);best=prefs.getInt("best",0);reset();}
    private void reset(){snake.clear();snake.addFirst(new Point(7,13));snake.addLast(new Point(6,13));snake.addLast(new Point(5,13));dx=nextDx=1;dy=nextDy=0;score=0;gameOver=paused=false;spawnFood();lastStep=SystemClock.uptimeMillis();}
    private boolean contains(int x,int y){for(Point q:snake)if(q.x==x&&q.y==y)return true;return false;}
    private void spawnFood(){do{food=new Point(random.nextInt(COLS),random.nextInt(ROWS));}while(contains(food.x,food.y));}
    private long delay(){return Math.max(65,180-score*3L);}
    private void step(){dx=nextDx;dy=nextDy;Point head=snake.peekFirst();int nx=head.x+dx,ny=head.y+dy;if(nx<0||nx>=COLS||ny<0||ny>=ROWS||contains(nx,ny)){gameOver=true;if(score>best){best=score;prefs.edit().putInt("best",best).apply();}return;}snake.addFirst(new Point(nx,ny));if(nx==food.x&&ny==food.y){score++;tones.startTone(ToneGenerator.TONE_PROP_BEEP2,75);spawnFood();}else snake.removeLast();}
    private void direction(int x,int y){if(x==-dx&&y==-dy)return;nextDx=x;nextDy=y;}
    @Override protected void onDraw(Canvas c){super.onDraw(c);long now=SystemClock.uptimeMillis();if(!gameOver&&!paused&&now-lastStep>=delay()){step();lastStep=now;}drawScene(c);postInvalidateDelayed(16);}
    private void txt(Canvas c,String s,float size,float x,float y,Paint.Align align){p.setTypeface(Typeface.create("sans",Typeface.BOLD));p.setTextSize(size);p.setTextAlign(align);p.setColor(0xffe9ffff);p.setShadowLayer(12,0,0,0xff00f5ff);c.drawText(s,x,y,p);p.clearShadowLayer();}
    private void drawScene(Canvas c){float top=getHeight()*.13f,available=getHeight()*.70f,cell=Math.min(getWidth()*.88f/COLS,available/ROWS),left=(getWidth()-cell*COLS)/2;
        txt(c,"NEON SNAKE",getWidth()*.075f,getWidth()/2f,getHeight()*.075f,Paint.Align.CENTER);txt(c,"SCORE  "+score,getWidth()*.035f,left,top-18,Paint.Align.LEFT);txt(c,"BEST  "+best,getWidth()*.035f,left+cell*COLS,top-18,Paint.Align.RIGHT);
        p.setStyle(Paint.Style.FILL);p.setColor(0xaa070b20);p.setShadowLayer(20,0,0,0xff0077ff);c.drawRoundRect(left-7,top-7,left+cell*COLS+7,top+cell*ROWS+7,18,18,p);p.clearShadowLayer();
        p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(1);p.setColor(0x2228b8ff);for(int y=0;y<=ROWS;y++)c.drawLine(left,top+y*cell,left+COLS*cell,top+y*cell,p);for(int x=0;x<=COLS;x++)c.drawLine(left+x*cell,top,left+x*cell,top+ROWS*cell,p);
        p.setStyle(Paint.Style.FILL);p.setColor(0xffff3d81);p.setShadowLayer(cell*.65f,0,0,0xffff006e);c.drawCircle(left+(food.x+.5f)*cell,top+(food.y+.5f)*cell,cell*.33f,p);p.clearShadowLayer();
        int i=0;for(Point q:snake){int color=i++==0?0xffe9ffff:0xff39ff88;p.setColor(color);p.setShadowLayer(cell*.45f,0,0,color);c.drawRoundRect(left+q.x*cell+2,top+q.y*cell+2,left+(q.x+1)*cell-2,top+(q.y+1)*cell-2,cell*.25f,cell*.25f,p);p.clearShadowLayer();}
        txt(c,"SWIPE TO TURN   •   TAP TO PAUSE",getWidth()*.027f,getWidth()/2f,top+cell*ROWS+48,Paint.Align.CENTER);
        if(paused&&!gameOver)overlay(c,left,top,cell,"PAUSED","TAP TO CONTINUE");if(gameOver)overlay(c,left,top,cell,"GAME OVER","TAP TO RESTART");}
    private void overlay(Canvas c,float left,float top,float cell,String title,String sub){p.setStyle(Paint.Style.FILL);p.setColor(0xe6040713);c.drawRoundRect(left+16,top+cell*9,left+cell*COLS-16,top+cell*17,26,26,p);txt(c,title,getWidth()*.07f,getWidth()/2f,top+cell*12.2f,Paint.Align.CENTER);txt(c,sub,getWidth()*.033f,getWidth()/2f,top+cell*14.6f,Paint.Align.CENTER);}
    @Override public boolean onTouchEvent(MotionEvent e){if(e.getAction()==MotionEvent.ACTION_DOWN){downX=e.getX();downY=e.getY();return true;}if(e.getAction()==MotionEvent.ACTION_UP){if(gameOver){reset();invalidate();return true;}float sx=e.getX()-downX,sy=e.getY()-downY;if(Math.abs(sx)<40&&Math.abs(sy)<40){paused=!paused;lastStep=SystemClock.uptimeMillis();}else if(Math.abs(sx)>Math.abs(sy))direction(sx>0?1:-1,0);else direction(0,sy>0?1:-1);invalidate();return true;}return true;}
    @Override protected void onDetachedFromWindow(){tones.release();super.onDetachedFromWindow();}
}
