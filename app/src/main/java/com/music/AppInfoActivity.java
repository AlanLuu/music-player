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
import com.music.util.DadJoke;
import com.music.util.JokeFactory;
import com.music.util.Util;

public class AppInfoActivity extends AppCompatActivity {
    private static JokeFactory[] jokes = {
            new DadJoke(),
            new ChuckNorrisJoke()
    };

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
        menu.add(Menu.NONE, 2, Menu.NONE, jokes[0].getTitle());
        menu.add(Menu.NONE, 3, Menu.NONE, jokes[1].getTitle());
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
                showJoke(jokes[0]);
                break;
            case 3:
                showJoke(jokes[1]);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showJoke(JokeFactory jokeFactory) {
        String joke = jokeFactory.generateJoke();
        joke = joke == null ? Util.API_ERROR : joke;
        Util.alert(this, jokeFactory.getTitle(), joke, null);
    }
}