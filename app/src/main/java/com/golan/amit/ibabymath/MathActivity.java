package com.golan.amit.ibabymath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MathActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView[] ivLineUp, ivLineDown;
    ImageView ivSignDisplay;
    EditText etAnswer;
    ImageButton ibAnswer;
    BabyMathHelper bmh;
    SharedPreferences sp;

    Bitmap currProductPic, currOperationPic;
    int[] currProductPicPtr, currOperationPicPtr;

    int operation = -1;
    int pic = -1;

    SoundPool soundpool;
    private int soundInt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        init();

        play();
    }

    private void init() {
        bmh = new BabyMathHelper();

        /**
         * First line
         */
        ivLineUp = new ImageView[]{
                findViewById(R.id.ivB00), findViewById(R.id.ivB01), findViewById(R.id.ivB02),
                findViewById(R.id.ivB03), findViewById(R.id.ivB04)
        };

        ivLineDown = new ImageView[]{
                findViewById(R.id.ivB10), findViewById(R.id.ivB11), findViewById(R.id.ivB12),
                findViewById(R.id.ivB13), findViewById(R.id.ivB14)
        };

        /**
         * Sign view
         */
        ivSignDisplay = findViewById(R.id.ivSignId);

        /**
         * Answer edit text
         */
        etAnswer = findViewById(R.id.etAnswerId);
        /**
         * Answer Button (Image)
         */
        ibAnswer = findViewById(R.id.ibSendAnswer);
        ibAnswer.setOnClickListener(this);

        currProductPicPtr = new int[] {
                R.mipmap.banana, R.mipmap.carrot, R.mipmap.strawberry, R.mipmap.baloon, R.mipmap.honey,
                R.mipmap.melafefon, R.mipmap.tomato, R.mipmap.cake, R.mipmap.flower, R.mipmap.cheese,
                R.mipmap.grapes
        };

        currOperationPicPtr = new int[] {
                R.mipmap.plussign, R.mipmap.multiplication, R.mipmap.minus, R.mipmap.division
        };

        sp = getSharedPreferences("babymath", MODE_PRIVATE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME). build();
            soundpool = new SoundPool.Builder()
                    .setMaxStreams(10).setAudioAttributes(aa).build();
        } else {
            soundpool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        soundInt = soundpool.load(this, R.raw.laser, 1);

    }

    /**
     * Logic
     */
    private void play() {
        bmh.setOperation_ptr(BabyMathHelper.ADD);
        bmh.generate_random_operands();
        currOperationPic = BitmapFactory.decodeResource(getResources(), currOperationPicPtr[bmh.getOperation_ptr()]);
        if(MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, "left: " + bmh.getLeft() + ", right: " + bmh.getRight() + ", answer: " + bmh.result());
        }
        bmh.generate_randor_pic_pointer();
        currProductPic = BitmapFactory.decodeResource(getResources(), currProductPicPtr[bmh.getPic_ptr()]);

        wipeAllUpperImages();

        drawUpperImages();

        drawOperationSign();

        wipeAllLowerImages();

        drawLowerImages();
    }

    private void wipeAllUpperImages() {
        for(int i = 0; i < ivLineUp.length; i++) {
            ivLineUp[i].setVisibility(View.INVISIBLE);
        }
    }

    private void drawUpperImages() {

        for (int i = 0; i < bmh.getLeft(); i++) {
            ivLineUp[i].setVisibility(View.VISIBLE);
//            ivLineUp[i].setImageBitmap(fruitsBm[bmh.getPic_ptr()]);
            ivLineUp[i].setImageBitmap(currProductPic);
        }
    }

    private void drawOperationSign() {
//        ivSignDisplay.setImageBitmap(operationBm[bmh.getOperation_ptr()]);
        ivSignDisplay.setImageBitmap(currOperationPic);
    }

    private void wipeAllLowerImages() {
        for(int i = 0; i < ivLineDown.length; i++) {
            ivLineDown[i].setVisibility(View.INVISIBLE);
        }
    }

    private void drawLowerImages() {
        for (int i = 0; i < bmh.getRight(); i++) {
            ivLineDown[i].setVisibility(View.VISIBLE);
//            ivLineDown[i].setImageBitmap(fruitsBm[bmh.getPic_ptr()]);
            ivLineDown[i].setImageBitmap(currProductPic);
        }
    }

    /**
     * Events
     * @param v
     */

    @Override
    public void onClick(View v) {
        if(v == ibAnswer) {
            if(etAnswer == null || etAnswer.getText() == null) {
                if(MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "null edit text");
                }
                return;
            }
            String tmpStr = etAnswer.getText().toString();
            if(tmpStr.equalsIgnoreCase("")) {
                if(MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "empty edit text");
                }
                Toast.makeText(this, "Empty value", Toast.LENGTH_SHORT).show();
                return;
            }
            int tmpIntVal = -1;
            try {
                tmpIntVal = Integer.parseInt(tmpStr);
            } catch (Exception e) {
                Log.e(MainActivity.DEBUGTAG, "parse to int exception");
                etAnswer.setText("");
                return;
            }
            if(tmpIntVal == -1) {
                Log.e(MainActivity.DEBUGTAG, "parse to int failed, val is -1");
                return;
            }
            //  evaluation begin:
            if(tmpIntVal == bmh.result()) {
                //  won
                bmh.reset_fail_counter();
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("pic", bmh.getPic_ptr());
                editor.commit();

                Intent i = new Intent(this, WonActivity.class);
                startActivity(i);
            } else {
                //  Lost
                bmh.increase_fail_counter();
                if (bmh.getFail_counter() == 3) {
                    bmh.reset_fail_counter();

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("answer", bmh.result());
                    editor.commit();

                    Intent i = new Intent(this, LostActivity.class);
                    startActivity(i);
                } else {
                    soundpool.play(soundInt, 1, 1, 0, 0, 1);
//                    v.setAlpha((float)1);
                    etAnswer.setText("");
                }
            }
        }
    }
}
