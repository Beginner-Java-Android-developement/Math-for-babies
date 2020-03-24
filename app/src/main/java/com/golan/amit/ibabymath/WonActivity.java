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

public class WonActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnBack;
    ImageView ivWonDisplay;
    TextView tvWonDisplay;
    SharedPreferences sp;
    Bitmap currBmPtr;
    private int pic_ptr = -1;

    private int[] currPicPtr;

    Animation[] animRotate;

    MediaPlayer mp;
    AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);

        init();

        setListener();

        action();
    }

    private void action() {
        ivWonDisplay.setImageBitmap(currBmPtr);
        ivWonDisplay.startAnimation(animRotate[(int)(Math.random() * animRotate.length)]);
    }

    private void init() {
        currPicPtr = new int[]{
                R.mipmap.boots, R.mipmap.olaf_sven, R.mipmap.lion, R.mipmap.baloons, R.mipmap.pooh,
                R.mipmap.myarok, R.mipmap.maragvania, R.mipmap.cookimonster, R.mipmap.parpar,
                R.mipmap.mickeymouse, R.mipmap.baloo
        };

        ivWonDisplay = findViewById(R.id.ivWonDisplay);
        sp = getSharedPreferences("babymath", MODE_PRIVATE);
        try {
            pic_ptr = sp.getInt("pic", -1);
        } catch (Exception e) {
            pic_ptr = 0;
        }

        currBmPtr = BitmapFactory.decodeResource(getResources(), currPicPtr[pic_ptr]);

        tvWonDisplay = findViewById(R.id.tvWonDisplay);
        btnBack = findViewById(R.id.btnWonGoBackId);

        /**
         * Animation
         */
        animRotate = new Animation[]{
                AnimationUtils.loadAnimation(this, R.anim.anim_rotate_right),
                AnimationUtils.loadAnimation(this, R.anim.anim_rotate_left)
        };

        /**
         * Sound
         */

        int[] restId = new int[]{
                R.raw.welldone, R.raw.welldone2, R.raw.yofi,
                R.raw.great1, R.raw.great2, R.raw.great3
        };
        mp = MediaPlayer.create(this, restId[(int) (Math.random() * restId.length)]);
        mp.start();

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (max * 0.75), 0);
    }

    private void setListener() {
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnBack) {
            Intent i = new Intent(this, MathActivity.class);
            startActivity(i);
        }
    }
}
