package cn.nn.imagebrowsesample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

/**
 * **************************
 * Class:       StatusTestActivity
 * Author:      fangx
 * Date:        16/11/1
 * Description:
 * ***************************
 */
public class StatusTestActivity extends AppCompatActivity {

    private static final int INITIAL_DELAY = 1500;
    private View mDecorView;
    private ActionBar mActionBar;
    private View mContentView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            hideSystemUI();
        }
    };
    private GestureDetector gestureDetector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDecorView = getWindow().getDecorView();
        mContentView = LayoutInflater.from(this).inflate(R.layout.activity_immersive, null);
        setContentView(mContentView);

        gestureDetector  = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if ((mDecorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0){
                    hideSystemUI();
                }else {
                    showSystemUI();
                }
                return true;
            }
        });

        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0){
                    //systemUI is visible
                    mActionBar.show();
                }else {
                    //systemUI is invisible
                    mActionBar.hide();
                }
            }
        });
        mContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        mActionBar = getSupportActionBar();
        mActionBar.setShowHideAnimationEnabled(true);
        showSystemUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            delayedHide(INITIAL_DELAY);
        }else {
            handler.removeMessages(0);
            showSystemUI();
        }
    }

    private void delayedHide(int delay){
        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0, delay);
    }

    private void hideSystemUI(){
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI(){
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
