package com.twadeclark.thejuliazone;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button noiseButton;

    SeekBar seekbar0;
    SeekBar seekbar1;
    SeekBar seekbar2;
    SeekBar seekbar3;
    SeekBar seekbar4;

    private AudioTrack audioTrack;
    Equalizer equalizer;
    private int sampleRate;
    private int bufferSize;

    private volatile boolean isRunning = false;
    private double ma = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noiseButton = findViewById(R.id.noiseButton);
        noiseButton.setOnClickListener(view -> startStop("noise"));

        seekbar0 = findViewById(R.id.seekBar0);
        seekbar1 = findViewById(R.id.seekBar1);
        seekbar2 = findViewById(R.id.seekBar2);
        seekbar3 = findViewById(R.id.seekBar3);
        seekbar4 = findViewById(R.id.seekBar4);

        sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
        equalizer = new Equalizer(0, audioTrack.getAudioSessionId());

    }

    public void startStop(String s) {
        if(isRunning) stop(); else play(s);
    }

    public void stop() {
        isRunning = false;
    }

    public void play(String noise) {
        isRunning = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                noise();
            }
        }).start();
    }

    public void noise() {
        equalizer.setEnabled(true);
        audioTrack.play();

        short zero = 0;
        short one = 1;
        short two = 2;
        short three = 3;
        short four = 4;

        int s0;
        int s1;
        int s2;
        int s3;
        int s4;

        byte[] buffer = new byte[bufferSize];
        Random random = new Random();

        while (isRunning) {
            s0 = seekbar0.getProgress() * 1500 - 1500;
            s1 = seekbar1.getProgress() * 1500 - 1500;
            s2 = seekbar2.getProgress() * 1500 - 1500;
            s3 = seekbar3.getProgress() * 1500 - 1500;
            s4 = seekbar4.getProgress() * 1500 - 1500;

            equalizer.setBandLevel(zero, (short) s0);
            equalizer.setBandLevel(one, (short) s1);
            equalizer.setBandLevel(two, (short) s2);
            equalizer.setBandLevel(three, (short) s3);
            equalizer.setBandLevel(four, (short) s4);

            random.nextBytes(buffer);
            audioTrack.write(buffer, 0, bufferSize);
        }
    }
}