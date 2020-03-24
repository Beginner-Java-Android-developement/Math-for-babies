package com.golan.amit.ibabymath;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LostActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivLostDisplay;
    TextView tvLostDisplay;
    SharedPreferences sp;
    Button btnLostGoBack;
    Bitmap currBm;
    int[] currPicPtr;
    Animation[] animScale;

    MediaPlayer mp;
    AudioManager am;
    private int answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        init();

        setListener();

        action();

    }

    private void action() {
        ivLostDisplay.setImageBitmap(currBm);
        ivLostDisplay.startAnimation(animScale[(int) (Math.random() * animScale.length)]);

        if (answer == -1) {
            tvLostDisplay.setText("הפסדת");
        } else {
            tvLostDisplay.setText("הפסדת, התשובה הייתה " + answer);
        }
    }

    private void init() {
        sp = getSharedPreferences("babymath", MODE_PRIVATE);
        try {
            answer = sp.getInt("answer", -1);
        } catch (Exception e) {
            answer = -1;
        }
        ivLostDisplay = findViewById(R.id.ivLostDisplay);
        tvLostDisplay = findViewById(R.id.tvLostDisplayId);
        btnLostGoBack = findViewById(R.id.btnLostGoBackId);

        currPicPtr = new int[]{
                R.mipmap.hatfani, R.mipmap.marshmallow
        };
        currBm = BitmapFactory.decodeResource(getResources(), currPicPtr[(int) (Math.random() * currPicPtr.length)]);

        /**
         * Sound
         */

        mp = MediaPlayer.create(this, R.raw.failtrombone);
        mp.start();

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (max * 0.75), 0);

        /**
         * Animation
         */
        animScale = new Animation[]{
                AnimationUtils.loadAnimation(this, R.anim.anim_scale),
                AnimationUtils.loadAnimation(this, R.anim.anim_scale_inout)
        };
    }

    private void setListener() {
        btnLostGoBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnLostGoBack) {
            Intent i = new Intent(this, MathActivity.class);
            startActivity(i);
        }
    }
}
