package com.games.sbr.groupproject;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    SharedPreferences sharedPreferences;
    Context context;

    public boolean hasSound() {
        sound=sharedPreferences.getBoolean("Sound",true);
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
        sharedPreferences.edit().putBoolean("Sound",sound).commit();
    }

    boolean sound;
    public int getLevel() {
        level=sharedPreferences.getInt("Level",1);
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        sharedPreferences.edit().putInt("Level",level).commit();
    }

    int level;
    public Settings (Context context){
        this.context=context;
        sharedPreferences=context.getSharedPreferences("settings",Context.MODE_PRIVATE);
    }
}
