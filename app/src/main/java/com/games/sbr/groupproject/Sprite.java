package com.games.sbr.groupproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sprite {
    Bitmap mainBitmap,printBitmap;
    Rect rect;
    int divX;
    int divY;
    int wid;
    int hei;
    int life;
    int blow;

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
        rect.set(X,Y,X+wid,Y+hei);
    }

    int X;

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
        rect.set(X,Y,X+wid,Y+hei);
    }

    int Y;
    public Sprite(Bitmap src,int a,int b,int x,int y) {
        mainBitmap=src;
        wid=src.getWidth()/a;
        hei=src.getHeight()/b;
        divX=a;
        divY=b;
        X=x;
        Y=y;
        rect=new Rect(x,y,x+wid,y+hei);
        printBitmap=Bitmap.createBitmap(mainBitmap,0,0,wid,hei);
        life=3;
        blow=15;
    }
    public void createImage(int row,int col) {
        printBitmap=Bitmap.createBitmap(mainBitmap,row*wid,col*hei,wid,hei);
    }
    public void flipVertical() {
        Matrix mt=new Matrix();
        int cx=wid/2;
        int cy=hei/2;
        mt.postScale(-1,1,cx,cy);
        printBitmap=Bitmap.createBitmap(printBitmap,0,0,wid,hei,mt,true);
    }
    public void drawSprite(Canvas canvas, Paint paint) {
        canvas.drawBitmap(printBitmap,X,Y,paint);
    }
    public boolean isCollideWith(Sprite sp) {
        return this.rect.intersect(sp.rect);
    }
}
