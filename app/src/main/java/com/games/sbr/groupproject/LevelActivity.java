package com.games.sbr.groupproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

public class LevelActivity extends Activity {
    Settings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        int[] temp={R.id.lvl1,R.id.lvl2,R.id.lvl3,R.id.lvl4,R.id.lvl5,R.id.lvl6,R.id.lvl7,R.id.lvl8,R.id.lvl9,R.id.lvl10};
        settings=new Settings(this);
        int tem=settings.getLevel();
        for (int i=0;i<tem;i++) {
            try {
                View view=(findViewById(temp[i]));
                view.setBackgroundResource(R.drawable.buttontheme);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        level1(view);
                    }
                });
            }
            catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    public void back(View view) {
        Intent intent=new Intent(LevelActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void level1(View view) {
        Intent intent=new Intent(LevelActivity.this,MainActivity.class);
        GameSurface.level=settings.getLevel();
        startActivity(intent);
        finish();
    }
}
