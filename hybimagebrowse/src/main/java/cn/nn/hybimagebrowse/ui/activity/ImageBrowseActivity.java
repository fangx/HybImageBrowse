package cn.nn.hybimagebrowse.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.nn.hybimagebrowse.R;
import cn.nn.hybimagebrowse.ui.fragment.ImageBrowseFragment;

/**
 * **************************
 * Class:       ImageBrowseActivity
 * Author:      fangx
 * Date:        16/11/2
 * Description:
 * ***************************
 */
public class ImageBrowseActivity extends AppCompatActivity {


    private LinearLayout fragment_layout;

    private ImageBrowseFragment imageBrowseFragment;
    private List<String> images = new ArrayList<>();
    private boolean phototAnim;
    private int currentItem;
    //用于做图片动画
    private int photoTop = 0;
    private int photoLeft = 0;
    private int photoWidth = 0;
    private int photoHeight = 0;
    private int photoRowHeight = 0;
    private int photoColumn = 0;


    //动画持续时间
    public final static int ENTER_DURATION = 200;
    public final static int EXIT_DURATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browse_activity);

        init(savedInstanceState);
    }


    private void init(Bundle savedInstanceState) {

        fragment_layout = (LinearLayout)findViewById(R.id.fragment);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String[] pathArr = bundle.getStringArray(ImageBrowseFragment.PHOTO_PATHS);
            images.clear();
            if (pathArr != null) {
                images = new ArrayList<>(Arrays.asList(pathArr));
            }
            phototAnim = bundle.getBoolean(ImageBrowseFragment.PHOTO_ANIM);
            currentItem = bundle.getInt(ImageBrowseFragment.PHOTO_CURRENT_ITEM);
            photoTop = bundle.getInt(ImageBrowseFragment.PHOTO_TOP);
            photoLeft = bundle.getInt(ImageBrowseFragment.PHOTO_LEFT);
            photoWidth = bundle.getInt(ImageBrowseFragment.PHOTO_WIDTH);
            photoHeight = bundle.getInt(ImageBrowseFragment.PHOTO_HEIGHT);
            photoRowHeight = bundle.getInt(ImageBrowseFragment.PHOTO_ROWHEIGHT);
            photoColumn = bundle.getInt(ImageBrowseFragment.PHOTO_COLUMN);
        }

        imageBrowseFragment =
                ImageBrowseFragment.newInstance(images, currentItem, new int[]{photoTop, photoLeft}, photoWidth,
                        photoHeight, photoColumn, photoHeight);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .replace(R.id.fragment, imageBrowseFragment)
                .addToBackStack(null)
                .commit();


        if (savedInstanceState == null && phototAnim) {
            ViewTreeObserver observer = fragment_layout.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    fragment_layout.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] screenLocation = new int[2];
                    fragment_layout.getLocationOnScreen(screenLocation);
                    photoLeft = photoLeft - screenLocation[0];
                    photoTop = photoTop - screenLocation[1];

                    runEnterAnimation(new Runnable() {
                        @Override
                        public void run() {
//                            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
                        }
                    });

                    return true;
                }
            });
        }


    }



    /**
     * The enter animation scales the picture in from its previous thumbnail
     * size/location, colorizing it in parallel. In parallel, the background of the
     * activity is fading in. When the pictue is in place, the text description
     * drops down.
     */
    private void runEnterAnimation(final Runnable startAction) {
        final long duration = ENTER_DURATION;


        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it back up
        ViewHelper.setPivotX(fragment_layout, 0);
        ViewHelper.setPivotY(fragment_layout, 0);
        ViewHelper.setScaleX(fragment_layout, (float) photoWidth / fragment_layout.getWidth());
        ViewHelper.setScaleY(fragment_layout, (float) photoHeight / fragment_layout.getHeight());
        ViewHelper.setTranslationX(fragment_layout, photoLeft);
        ViewHelper.setTranslationY(fragment_layout, photoTop);

        // Animate scale and translation to go from thumbnail to full size
        ViewPropertyAnimator.animate(fragment_layout)
                .setDuration(duration)
                .scaleX(1)
                .scaleY(1)
                .translationX(0)
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                startAction.run();
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

//        // Fade in the black background
//        ObjectAnimator bgAnim = ObjectAnimator.ofInt(viewpager.getBackground(), "alpha", 0, 255);
//        bgAnim.setDuration(duration);
//        bgAnim.start();

        // Animate a color filter to take the image from grayscale to full color.
        // This happens in parallel with the image scaling and moving into place.
        ObjectAnimator colorizer = ObjectAnimator.ofFloat(ImageBrowseActivity.this,
                "saturation", 0, 1);
        colorizer.setDuration(duration);
        colorizer.start();

    }


    /**
     * The exit animation is basically a reverse of the enter animation, except that if
     * the orientation has changed we simply scale the picture back into the center of
     * the screen.
     *
     * @param endAction This action gets run after the animation completes (this is
     *                  when we actually switch activities)
     */
    public void runExitAnimation(final Runnable endAction) {

        if (!phototAnim) {
            endAction.run();
            return;
        }

        final long duration = EXIT_DURATION;


        //当viewpager页面发生切换时重新计算图片位置以使缩小动画执行到当前图片位置
        int currentIndex = imageBrowseFragment.getCurrentItem();

        //当前图片非进入的图片
        if (currentIndex != currentItem) {
            if (photoColumn != 0) {
                int beforeColumn = currentItem % photoColumn;
                int beforeRow = currentItem / photoColumn;

                int nowColumn = currentIndex % photoColumn;
                int nowRow = currentIndex / photoColumn;

                photoLeft = photoLeft + (nowColumn - beforeColumn) * photoWidth;
                photoTop = photoTop + (nowRow - beforeRow) * photoHeight;
            }
        }


        // Animate image back to thumbnail size/location
        ViewPropertyAnimator.animate(fragment_layout)
                .setDuration(duration)
                .setInterpolator(new DecelerateInterpolator())
                .scaleX((float) photoWidth / fragment_layout.getWidth())
                .scaleY((float) photoHeight / fragment_layout.getHeight())
                .translationX(photoLeft)
                .translationY(photoTop)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fragment_layout.setAlpha(0);
                        endAction.run();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });

        // Fade out background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(fragment_layout.getBackground(), "alpha", 0);
        bgAnim.setDuration(duration);
        bgAnim.start();

        // Animate a color filter to take the image back to grayscale,
        // in parallel with the image scaling and moving into place.
        ObjectAnimator colorizer =
                ObjectAnimator.ofFloat(ImageBrowseActivity.this, "saturation", 1, 0);
        colorizer.setDuration(duration);
        colorizer.start();
    }






    @Override
    public void onBackPressed() {

        runExitAnimation(new Runnable() {
            public void run() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                }
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

                ImageBrowseActivity.this.finish();
                overridePendingTransition(0, 0);

            }
        });
    }


}
