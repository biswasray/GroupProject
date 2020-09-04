package com.games.sbr.groupproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class SettingActivity extends Activity {

    ImageButton imageButton;
    Settings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        imageButton=(ImageButton)findViewById(R.id.imB);
        settings=new Settings(this);
        if(settings.hasSound()) {
            imageButton.setImageResource(android.R.drawable.checkbox_on_background);
        }
        else {
            imageButton.setImageResource(android.R.drawable.checkbox_off_background);
        }
    }
    public void soundChange(View view) {
        if(settings.hasSound()) {
            settings.setSound(false);
            stopService(MenuActivity.intentM);
            imageButton.setImageResource(android.R.drawable.checkbox_off_background);
        }
        else {
            settings.setSound(true);
            startService(MenuActivity.intentM);
            imageButton.setImageResource(android.R.drawable.checkbox_on_background);
        }
    }
    public void reset(View view) {
        settings.setLevel(1);
        Toast.makeText(this,"Levels are reseted",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
