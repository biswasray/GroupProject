package com.games.sbr.groupproject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.Handler;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class GameSurface extends SurfaceView implements SurfaceHolder.Callback,Runnable{
    int ScreenWidth,ScreenHeight;
    static AlertDialog alertDialog,alertDialog0;
    Bitmap skyBitmap,skyImage,player,pow,blast,tankB;
    Bitmap joy,joyStick,sprint,coinB,bomb,medExp,shootB;
    Context myContext;
    Player sp;
    View view,view0;
    Sprite joyS;
    Canvas canvas;
    static Thread thread,thread0;
    Paint paint;
    int tk,bs,ki,distance,xd,fd;
    int fa,fg,sy,score,target;
    int ki1=1;
    static int speed,playerRun,level;
    Rect rt,lt,tp,bm,md,ground,powRect;
    Sprite tnk;
    static boolean touched,activeGame;
    public static Resources resources;
    public ArrayList<Sprite> coins=new ArrayList<>();
    public ArrayList<Sprite> bombs=new ArrayList<>();
    public ArrayList<Sprite> pBlast=new ArrayList<>();
    public ArrayList<Sprite> shoots=new ArrayList<>();
    Vibrator v;
    AlertDialog.Builder ab;
    public GameSurface(Context context) {
        super(context);
        activeGame=true;
        myContext=context;
        tk=bs=ki=0;
        fa=sy=playerRun=0;
        speed=50;
        score=0;
        tnk=null;
        this.ScreenWidth=((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        this.ScreenHeight=((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
        fd=(ScreenWidth*4)/5;
        xd=fd;
        distance=0;
        this.setBackgroundColor(Color.WHITE);
        resources=getResources();
        skyImage= BitmapFactory.decodeResource(resources,R.drawable.background);
        skyBitmap=Bitmap.createScaledBitmap(skyImage,(int)(skyImage.getWidth()*((float)ScreenHeight/skyImage.getHeight())),ScreenHeight,true);
        player=fitToScreenBitmap(BitmapFactory.decodeResource(resources,R.drawable.player));
        joy=fitToScreenBitmap(BitmapFactory.decodeResource(resources,R.drawable.onscreen_control_base));
        joyStick=fitToScreenBitmap(BitmapFactory.decodeResource(resources,R.drawable.onscreen_control_knob));
        sprint=fitToScreenBitmap(BitmapFactory.decodeResource(resources,R.drawable.sprint));
        pow=fitToScreenBitmap(BitmapFactory.decodeResource(resources,R.drawable.power));
        blast=fitToScreenBitmap(BitmapFactory.decodeResource(resources,R.drawable.powerblast));
        coinB=fitToScreenBitmap(BitmapFactory.decodeResource(resources,R.drawable.coin));
        bomb=fitToScreenBitmap(BitmapFactory.decodeResource(resources,R.drawable.bomb));
        shootB=fitToScreenBitmap(BitmapFactory.decodeResource(resources,R.drawable.shoot));
        tankB=fitToScreenBitmap(BitmapFactory.decodeResource(resources,R.drawable.tank));
        medExp=fitToScreenBitmap(BitmapFactory.decodeResource(resources,R.drawable.med_explo));
        sp=new Player(player,6,4,50,50);
        joyS=new Sprite(joyStick,1,1,((ScreenWidth*3)/4)+joy.getWidth()/4,((ScreenHeight*2)/3)+joy.getHeight()/4);
        rt=new Rect(((ScreenWidth*3)/4)+joy.getWidth()-10,((ScreenHeight*2)/3)-10,((ScreenWidth*3)/4)+joy.getWidth()+10,((ScreenHeight*2)/3)+joy.getHeight()+10);
        bm=new Rect(((ScreenWidth*3)/4)-10,((ScreenHeight*2)/3)+joy.getHeight()-10,((ScreenWidth*3)/4)+joy.getWidth()+10,((ScreenHeight*2)/3)+joy.getHeight()+10);
        tp=new Rect(((ScreenWidth*3)/4)-10,((ScreenHeight*2)/3)-10,((ScreenWidth*3)/4)+joy.getWidth()+10,((ScreenHeight*2)/3)+10);
        lt=new Rect(((ScreenWidth*3)/4)-10,((ScreenHeight*2)/3)-10,((ScreenWidth*3)/4)+10,((ScreenHeight*2)/3)+joy.getHeight()+10);
        md=new Rect((ScreenWidth*3)/4+joy.getWidth()/2,sp.hei/8,sprint.getWidth()+(ScreenWidth*3)/4+joy.getWidth()/2,sprint.getHeight()+sp.hei/8);
        powRect=new Rect((ScreenWidth*4)/5+joy.getWidth()/2,(ScreenHeight/2)-(pow.getHeight()/2),pow.getWidth()+(ScreenWidth*4)/5+joy.getWidth()/2,(ScreenHeight/2)+(pow.getHeight()/2));
        sp.setY(((skyBitmap.getHeight()*320)/400)-sp.hei);
        //sp.startAnimation();
        fg=sp.hei/3;
        paint=new Paint();
        ground=new Rect(0,((skyBitmap.getHeight()*320)/400),ScreenWidth,ScreenHeight);
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSPARENT);
        this.getHolder().addCallback(this);
        coins.add(new Sprite(coinB,1,1,xd,(new Random()).nextInt((ground.top-coinB.getHeight()))));
        ab=new AlertDialog.Builder(myContext);

        target=(5*level);
        v=(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    public Bitmap fitToScreenBitmap(Bitmap bitmap) {
        float ratio=(float)ScreenHeight/skyImage.getHeight();
        return Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*ratio),(int)(bitmap.getHeight()*ratio),true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //System.out.println("handler");
                thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (activeGame) {
                            theku();
                            if(score>=target) {
                                ((Activity)myContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        nextLevel();
                                    }
                                });
                            }
                            if(level>1) {
                                if(tnk==null&&(score%4)==0&&score!=0) {
                                    tnk=new Sprite(tankB,1,1,ScreenWidth,((skyBitmap.getHeight()*320)/400)-tankB.getHeight());
                                }
                                else if(tnk!=null) {
                                    tnk.setX(tnk.getX()-15);
                                    if(tnk.getX()<(-tnk.wid)) {
                                        tnk=null;
                                    }
                                    if(sp.isCollideWith(tnk)) {
                                        ((Activity)myContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                gameOver();
                                            }
                                        });
                                    }
                                    for(int i=0;i<pBlast.size();i++) {
                                        if(tnk.isCollideWith(pBlast.get(i))) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                v.vibrate(VibrationEffect.createOneShot(100,VibrationEffect.DEFAULT_AMPLITUDE));
                                            }
                                            else {
                                                v.vibrate(100);
                                            }
                                            tnk.life--;
                                            pBlast.remove(i);
                                            if(tnk.life<=0) {
                                                canvas=GameSurface.this.getHolder().lockCanvas();
                                                canvas.save();
                                                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.ADD);
                                                canvas.drawBitmap(medExp,tnk.getX(),tnk.getY(),paint);
                                                canvas.restore();
                                                getHolder().unlockCanvasAndPost(canvas);
                                                tnk=null;
                                                score+=2;
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    v.vibrate(VibrationEffect.createOneShot(300,VibrationEffect.DEFAULT_AMPLITUDE));
                                                }
                                                else {
                                                    v.vibrate(300);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if(level>2) {
                                if(tnk!=null) {
                                    if(tnk.blow<1) {
                                        shoots.add(new Sprite(shootB, 1, 1, tnk.getX() - shootB.getWidth(), tnk.getY() + (tnk.hei * 80) / 222));
                                        tnk.blow=15;

                                    }
                                    tnk.blow--;
                                }
                                for(int i=0;i<shoots.size();i++) {
                                    shoots.get(i).setX(shoots.get(i).getX()-40);
                                    if(sp.isCollideWith(shoots.get(i))) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            v.vibrate(VibrationEffect.createOneShot(100,VibrationEffect.DEFAULT_AMPLITUDE));
                                        }
                                        else {
                                            v.vibrate(100);
                                        }
                                        sp.life--;
                                        shoots.remove(i);
                                        if(sp.life<=0) {
                                            ((Activity)myContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    gameOver();
                                                }
                                            });
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                v.vibrate(VibrationEffect.createOneShot(300,VibrationEffect.DEFAULT_AMPLITUDE));
                                            }
                                            else {
                                                v.vibrate(300);
                                            }
                                        }
                                    }
                                    else if(shoots.get(i).getX()<(-shootB.getWidth()))
                                        shoots.remove(i);
                                }
                            }
                            for(int i=0;i<pBlast.size();i++) {
                                pBlast.get(i).setX(pBlast.get(i).getX()+30);
                                if(pBlast.get(i).getX()>ScreenWidth)
                                    pBlast.remove(i);
                            }
                            for(int i=0;i<coins.size();i++) {
                                if(coins.get(i).getX()<(-coinB.getWidth())) {
                                    coins.remove(i);
                                }
                                else if(sp.isCollideWith(coins.get(i))) {
                                    coins.remove(i);
                                    score++;
                                }
                            }
                            for(int i=0;i<bombs.size();i++) {
                                if(bombs.get(i).getX()<(-bomb.getWidth())) {
                                    bombs.remove(i);
                                }
                                else if(sp.isCollideWith(bombs.get(i))) {
                                    bombs.remove(i);
                                    //((Activity)this.getContext()).onBackPressed();
                                    //System.out.println("coll");
                                    ((Activity)myContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            gameOver();
                                        }
                                    });
                                }
                            }
                            try {
                                Thread.sleep(150);
                            }
                            catch (Exception ex) {
                                System.out.println("catchrun");
                            }
                        }
                    }
                });
                thread.start();

            }
        },200);
    }

    private void nextLevel() {
        activeGame=false;
        playerRun=0;
        sp.setAnimation(0);
        touched=false;
        ViewGroup viewGroup=findViewById(android.R.id.content);
        view0=LayoutInflater.from(myContext).inflate(R.layout.my_layout0,viewGroup);
        ab.setView(view0);
        alertDialog0=ab.create();
        alertDialog0.show();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap temp=skyBitmap;
            if (bs > temp.getWidth()) {
                bs = bs - temp.getWidth();
                Bitmap temp2 = Bitmap.createBitmap(temp, bs, 0, ScreenWidth, ScreenHeight);
                canvas.drawBitmap(temp2, 0, 0, null);
            } else if (bs > (temp.getWidth() - ScreenWidth)) {
                Bitmap temp2 = Bitmap.createBitmap(temp, bs, 0, temp.getWidth() - bs, ScreenHeight);
                canvas.drawBitmap(temp2, 0, 0, null);
                temp2 = Bitmap.createBitmap(temp, 0, 0, ScreenWidth + bs - temp.getWidth(), ScreenHeight);
                canvas.drawBitmap(temp2, temp.getWidth() - bs, 0, null);
            } else if(bs<0) {
                Bitmap temp2 = Bitmap.createBitmap(temp, temp.getWidth() + bs, 0, -bs, ScreenHeight);
                canvas.drawBitmap(temp2, 0, 0, null);
                temp2 = Bitmap.createBitmap(temp, 0, 0, ScreenWidth + bs, ScreenHeight);
                canvas.drawBitmap(temp2, - bs, 0, null);
            }
             else {
                Bitmap temp2 = Bitmap.createBitmap(temp, bs, 0, ScreenWidth, ScreenHeight);
                canvas.drawBitmap(temp2, 0, 0, null);
            }
        /*else if(bs<0) {
            Bitmap temp2 = Bitmap.createBitmap(temp, temp.getWidth() + bs, 0, -bs, ScreenHeight);
            canvas.drawBitmap(temp2, 0, 0, null);
            temp2 = Bitmap.createBitmap(temp, 0, 0, ScreenWidth + bs, ScreenHeight);
            canvas.drawBitmap(temp2, - bs, 0, null);
        }*/
        //System.out.println(bs);
        //canvas.drawBitmap(temp,0,0,null);
        super.onDraw(canvas);
    }
    public void theku() {
        canvas=this.getHolder().lockCanvas();
        canvas.save();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        sy=sy+fg+fa;
        fa=0;
        sp.setY(sp.getY()+sy);
        if(sp.rect.intersect(ground)) {
            sp.setY(ground.top-sp.hei);
            sy=0;
        }
        sp.nextAnime();
        this.draw(canvas);
        paint.setColor(Color.BLUE);
        sp.drawSprite(canvas,paint);
        canvas.drawBitmap(joy,(ScreenWidth*3)/4,(ScreenHeight*2)/3,null);
        joyS.drawSprite(canvas,null);
        if(playerRun==2) {
            paint.setColor(Color.YELLOW);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            canvas.drawCircle((sprint.getWidth()/2)+(ScreenWidth*3)/4+joy.getWidth()/2,(sprint.getWidth()/2)+sp.hei/8,(sprint.getWidth()/2),paint);
        }
        for(Sprite sprite:coins) {
            sprite.drawSprite(canvas,null);
        }
        for(Sprite sprite:bombs) {
            sprite.drawSprite(canvas,null);
        }
        for(Sprite sprite:pBlast) {
            sprite.drawSprite(canvas,null);
        }
        for(Sprite sprite:shoots) {
            sprite.drawSprite(canvas,null);
        }
        if(tnk!=null) {
            tnk.drawSprite(canvas,null);
        }
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(255);
        paint.setTextSize(40);
        canvas.drawText("Score:"+Integer.toString(score),50,50,paint);
        paint.setColor(Color.RED);
        canvas.drawText("Target:"+Integer.toString(target),ScreenWidth/2,50,paint);
        paint.setAlpha(100);
        canvas.drawBitmap(sprint,(ScreenWidth*3)/4+joy.getWidth()/2,sp.hei/8,paint);
        if(level>1)
            canvas.drawBitmap(pow,(ScreenWidth*4)/5+joy.getWidth()/2,(ScreenHeight/2)-(pow.getHeight()/2),paint);
        canvas.restore();
        getHolder().unlockCanvasAndPost(canvas);
        if(sp.getAnimation()==0) {
            touched=false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            ki1=0;
            if(md.contains((int)event.getX(),(int)event.getY())) {
                if(playerRun==0) {
                    playerRun=2;
                    sp.setAnimation(2);
                    thread0 = new Thread(this);
                    touched = true;
                    thread0.start();
                }
                else {
                    playerRun=0;
                    sp.setAnimation(0);
                    touched=false;
                }
            }
            else if(level>1&&powRect.contains((int)event.getX(),(int)event.getY())) {
                pBlast.add(new Sprite(blast,1,1,sp.collRect.right,sp.collRect.top));
                sp.setAnimation(3);
            }
        }
        else if(event.getAction()==MotionEvent.ACTION_MOVE) {
            if(joyS.rect.intersect((int) event.getX(),(int)event.getY(),(int)event.getX()+2,(int)event.getY()+2)) {
                joyS.setX((int)event.getX()-(joyStick.getWidth()/2));
                joyS.setY((int)event.getY()-(joyStick.getHeight()/2));
                if(joyS.rect.intersect(rt)&&joyS.rect.intersect(tp)) {
                    if(ki==0) {
                        thread0 = new Thread(this);
                        touched = true;
                        speed=50;
                        fa=-sp.hei;
                        sp.setAnimation(1);
                        thread0.start();
                    }
                    joyS.setX(((ScreenWidth*3)/4)+joy.getWidth()/2);
                    joyS.setY(((ScreenHeight*2)/3));
                }
                else if(joyS.rect.intersect(lt)&&joyS.rect.intersect(tp)) {
                    if(ki==0) {
                        thread0 = new Thread(this);
                        touched = true;
                        speed=-50;
                        fa=-sp.hei;
                        sp.enableflip=false;
                        sp.setAnimation(1);
                        thread0.start();
                    }
                    joyS.setX(((ScreenWidth*3)/4));
                    joyS.setY(((ScreenHeight*2)/3));
                }
                else if(joyS.rect.intersect(rt)) {
                    //touched=false;
                    playerRun=0;
                    if(ki==0) {
                        thread0 = new Thread(this);
                        touched = true;
                        speed=50;
                        sp.enableflip=true;
                        sp.setAnimation(2);
                        thread0.start();
                    }
                    joyS.setX(((ScreenWidth*3)/4)+joy.getWidth()/2);
                }
                else if(joyS.rect.intersect(bm)) {
                    joyS.setY(((ScreenHeight*2)/3)+joy.getHeight()/2);
                }
                else if(joyS.rect.intersect(lt)) {
                    //touched=false;
                    playerRun=0;
                    if(ki==0) {
                        thread0 = new Thread(this);
                        touched = true;
                        speed=-50;
                        sp.enableflip=false;
                        sp.setAnimation(2);
                        thread0.start();
                    }
                    joyS.setX(((ScreenWidth*3)/4));
                    //sp.setAnimation(0);
                }
                else if(joyS.rect.intersect(tp)) {
                    if(ki1==0) {
                        fa=-sp.hei;
                        sp.setAnimation(1);
                        ki1=1;
                    }
                    joyS.setY(((ScreenHeight*2)/3));
                }
            }
        }
        else if(event.getAction()==MotionEvent.ACTION_UP) {
            //System.out.println("spX"+sp.getX()+"spY"+sp.getY());
            joyS.setX(((ScreenWidth*3)/4)+joy.getWidth()/4);
            joyS.setY(((ScreenHeight*2)/3)+joy.getHeight()/4);
            if(sp.getAnimation()!=3&&sp.getAnimation()!=1&&playerRun==0) {
                touched=false;
                sp.setAnimation(0);
            }
        }
        return true;
    }

    public void gameOver() {
        canvas=this.getHolder().lockCanvas();
        canvas.save();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.ADD);
        canvas.drawBitmap(medExp,sp.getX(),sp.getY(),paint);
        canvas.restore();
        getHolder().unlockCanvasAndPost(canvas);
        ViewGroup viewGroup=findViewById(android.R.id.content);
        view= LayoutInflater.from(myContext).inflate(R.layout.my_layout,viewGroup);
        ab.setView(view);
        alertDialog=ab.create();
        ((TextView)view.findViewById(R.id.scoreTxt)).append(""+score);
        alertDialog.show();
        touched=false;
        activeGame=false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else {
            v.vibrate(500);
        }
    }

    @Override
    public void run() {
        ki=1;
        while (touched) {
            bs+=speed;
            xd-=speed;
            for(Sprite sprite:coins) {
                sprite.setX(sprite.getX()-speed);
            }
            for(Sprite sprite:bombs) {
                sprite.setX(sprite.getX()-speed);
            }
            if(xd<=fd) {
                xd=(ScreenWidth*6)/5;
                if((new Random()).nextFloat()<0.7f)
                    coins.add(new Sprite(coinB,1,1,xd,(new Random()).nextInt((ground.top-coinB.getHeight()))));
                else
                    bombs.add(new Sprite(bomb,1,1,xd,(ground.top-bomb.getHeight())));
            }
            //thila
            try {
                Thread.sleep(150);
            }
            catch (Exception ex) {
                System.out.println("errrrr");
            }
        }
        ki=0;
    }

}
