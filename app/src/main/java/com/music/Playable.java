package com.music;

import android.app.Activity;
import android.content.Context;

public interface Playable {
    long getId();
    void play(Context context, Activity activity);
}