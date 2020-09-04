package com.games.sbr.groupproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Player extends Sprite implements Runnable {
    int an,v,j;
    Rect collRect;
    boolean enableflip;
    Thread thread;
    public Player(Bitmap src,int a, int b, int x, int y) {
        super(src, a, b, x, y);
        an=0;
        enableflip=true;
        j=0;
        thread=new Thread(this);
        collRect=new Rect(this.rect.left+this.rect.width()/3,this.rect.top+this.rect.height()/3,this.rect.right-this.rect.width()/3,this.rect.bottom);
        /*this.rect.top+=this.rect.height()/3;
        this.rect.left+=this.rect.width()/3;
        this.rect.right-=this.rect.width()/3;
        this.X=this.rect.left;
        this.Y=this.rect.top;
        this.wid=this.rect.right-this.rect.left;
        this.hei=this.rect.bottom-this.rect.top;*/
    }
    public void startAnimation() {
        thread.start();
    }
    public void setX(int x) {
        X = x;
        rect.set(X,Y,X+wid,Y+hei);
        collRect.set(this.rect.left+this.rect.width()/3,this.rect.top+this.rect.height()/3,this.rect.right-this.rect.width()/3,this.rect.bottom);
    }
    public void setY(int y) {
        Y = y;
        rect.set(X,Y,X+wid,Y+hei);
        collRect.set(this.rect.left+this.rect.width()/3,this.rect.top+this.rect.height()/3,this.rect.right-this.rect.width()/3,this.rect.bottom);
    }
    public void drawSprite(Canvas canvas, Paint paint) {
        canvas.drawBitmap(printBitmap,X,Y,paint);
        /*try {
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(collRect,paint);
        }
        catch(Exception es) {

        }*/
    }
    public boolean isCollideWith(Sprite sp) {
        return this.collRect.intersect(sp.rect);
    }
    public void setAnimation(int i) {
        an=i%4;
        v=0;
        if(an==1) {
            //this.setY(this.getY()-this.hei/2);
            v = 2;
        }
        j=i;
    }
    public int getAnimation() {
        return  j;
    }
    public void nextAnime() {
        switch (an) {
            case 0:
                if(v>4)
                    v=0;
                this.createImage(v++,an);
                break;
            case 1:
                if(v>4)
                    v=0;
                this.createImage(v++,an);
                if (v==1) {
                    setAnimation(GameSurface.playerRun);
                    //this.setY(this.getY()+this.hei/2);
                }
                break;
            case 2:
                if(v>5)
                    v=0;
                this.createImage(v++,an);
                break;
            case 3:
                if(v>3)
                    setAnimation(GameSurface.playerRun);
                this.createImage(v++,an);
                break;
        }
        if(enableflip)
            this.flipVertical();
    }

    @Override
    public void run() {
        while (GameSurface.activeGame) {
            nextAnime();
            try {
                Thread.sleep(200);
            }
            catch(Exception ex) {
                System.out.println(ex);
            }
        }
    }
}
