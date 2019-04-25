package com.music;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.music.util.ChuckNorrisJoke;
import com.music.util.Util;

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
        textView.setText(Util.INFO);

        TextView textView1 = findViewById(R.id.aboutMessage);
        textView1.setMovementMethod(LinkMovementMethod.getInstance());
        textView1.setLinkTextColor(Color.BLUE);
        textView1.setText(Html.fromHtml("A simple music player created by " +
                "<a href='https://alanluu.github.io'>" + Util.NAME + "</a>."));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 0, Menu.NONE, "GitHub");
        menu.add(Menu.NONE, 1, Menu.NONE, "Email " + Util.NAME);
        menu.add(Menu.NONE, 2, Menu.NONE, ChuckNorrisJoke.TITLE);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Util.REPO)));
                break;
            case 1:
                Util.sendEmail(this, Util.EMAIL, "Music app", Util.versionInfo(this));
                break;
            case 2:
                String joke = new ChuckNorrisJoke().getJoke();
                joke = joke == null ? Util.API_ERROR : joke;
                Util.alert(AppInfoActivity.this, ChuckNorrisJoke.TITLE, joke, null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}