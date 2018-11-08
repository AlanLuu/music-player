package com.music;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MusicActivity extends AppCompatActivity {
    private Button playButton;
    private SeekBar positionBar, volumeBar;
    private TextView elapsed, remaining;
    private int totalTime;
    private long songId;
    private MediaPlayer player;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            int position = message.what;
            positionBar.setProgress(position);

            String elapsedTime = createTimeLabel(position);
            elapsed.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - position);
            remaining.setText("- " + remainingTime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getIntent().getStringExtra("Song"));
        }

        playButton = findViewById(R.id.playButton);
        playButton.setBackgroundResource(R.drawable.stop);
        playButton.setOnClickListener(view -> {
            if (!player.isPlaying()) {
                player.start();
                playButton.setBackgroundResource(R.drawable.stop);
            } else {
                player.pause();
                playButton.setBackgroundResource(R.drawable.play);
            }
        });

        elapsed = findViewById(R.id.elapsed);
        remaining = findViewById(R.id.remaining);

        positionBar = findViewById(R.id.positionBar);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(progress);
                    positionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        volumeBar = findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumeNum = progress / 100f;
                player.setVolume(volumeNum, volumeNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        songId = Long.parseLong(getIntent().getStringExtra("Id"));

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(getApplicationContext(), ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId));
            player.prepare();
        } catch (IOException e) {}

        totalTime = player.getDuration();
        positionBar.setMax(totalTime);
        player.start();

        new Thread(() -> {
            while (player != null) {
                try {
                    Message message = new Message();
                    message.what = player.getCurrentPosition();
                    handler.sendMessage(message);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        player.reset();
        super.onDestroy();
    }

    private String createTimeLabel(int time) {
        StringBuilder builder = new StringBuilder();
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        builder.append(min).append(":");
        if (sec < 10) builder.append("0");
        builder.append(sec);

        return builder.toString();
    }
}