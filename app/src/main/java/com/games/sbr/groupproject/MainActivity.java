package com.games.sbr.groupproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class MainActivity extends Activity {
    Settings settings;
    boolean b1,b2;
    GameSurface gameSurface;
    boolean paused;
    Thread temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameSurface=new GameSurface(this);
        setContentView(gameSurface);
        settings=new Settings(this);
        paused=b1=b2=false;
    }

    @Override
    public void onBackPressed() {
        /*if(paused) {
            try {
                GameSurface.touched=b2;
                GameSurface.thread0=temp;
                if(GameSurface.thread0!=null)
                    GameSurface.thread0.start();
                paused=false;
            } catch (Exception e) {
               System.out.println(e);
            }
        }
        else {
            try {
                b2=GameSurface.touched;
                GameSurface.touched=false;
                temp=GameSurface.thread0;
                paused=true;
            } catch (Exception e) {
                System.out.println(e);
            }
        }*/
    }

    public void gotoMenu(View view) {
        GameSurface.alertDialog.cancel();
        Intent intent=new Intent(this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
    public void gotoLevel(View view) {
        settings.setLevel(settings.getLevel()+1);
        //System.out.println(settings.getLevel());
        GameSurface.alertDialog0.cancel();
        Intent intent=new Intent(this,LevelActivity.class);
        startActivity(intent);
        finish();
    }
}
