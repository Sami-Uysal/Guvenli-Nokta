package com.guvenlinokta.app;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.guvenlinokta.app.ui.BaseActivity;

public class SirenActivity extends BaseActivity {

    private MediaPlayer mediaPlayer;
    private Button stopSirenButton;
    private AudioManager audioManager;
    private int originalVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siren);

        stopSirenButton = findViewById(R.id.stopSirenButton);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (audioManager != null) {
            originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
        }

        playSiren();

        stopSirenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSiren();
                finish();
            }
        });
    }

    private void playSiren() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build());

            mediaPlayer.setDataSource(getApplicationContext(),
                    android.net.Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren));

            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            mediaPlayer.setVolume(1.0f, 1.0f);

            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);

            mediaPlayer.start();
            Toast.makeText(this, "Siren çalınıyor...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Siren sesi dosyası bulunamadı veya yüklenemedi.",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void stopSiren() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(this, "Siren durduruldu.", Toast.LENGTH_SHORT).show();
        }

        if (audioManager != null) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
        }
    }

    @Override
    protected void onDestroy() {
        stopSiren();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        stopSiren();
        super.onBackPressed();
    }
}