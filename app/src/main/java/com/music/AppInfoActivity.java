package com.music;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AppInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("About this app");
        }

        TextView textView = findViewById(R.id.versionMessage);
        String result = "Music: version " + BuildConfig.VERSION_NAME;
        textView.setText(result);

        TextView textView1 = findViewById(R.id.aboutMessage);
        textView1.setMovementMethod(LinkMovementMethod.getInstance());
        textView1.setLinkTextColor(Color.BLUE);
        textView1.setText(Html.fromHtml("A simple music player created by " +
                "<a href='https://alanluu.github.io'>Alan Luu</a>."));
    }
}