package com.games.sbr.groupproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.View;

public class MenuActivity extends Activity {

    static Intent intentM;
    Settings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        settings=new Settings(this);
        intentM=new Intent(this,MusicService.class);
        if(settings.hasSound())
            startService(intentM);
    }

    public void newGame(View view) {
        Intent intent=new Intent(MenuActivity.this,LevelActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoSetting(View view) {
        Intent intent=new Intent(MenuActivity.this,SettingActivity.class);
        startActivity(intent);
        finish();
    }

    public void exitGame(View view) {
        if(settings.hasSound())
            stopService(intentM);
        finish();
        Process.killProcess(Process.myPid());
    }

    @Override
    public void onBackPressed() {

    }
}
