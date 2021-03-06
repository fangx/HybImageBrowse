package cn.nn.hybimagebrowse.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import cn.nn.hybimagebrowse.helper.PageChangeListener;
import cn.nn.hybimagebrowse.ui.fragment.ImageBrowseFragment;
import cn.nn.hybimagebrowse.utils.BaseUtil;

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
    private LinearLayout page_index_layout;
    private ImageBrowseFragment imageBrowseFragment;
    private List<String> images = new ArrayList<>();
    private List<View> dots = new ArrayList<>();
    private boolean phototAnim;
    private int currentItem;

    public final static String PHOTO_PATHS = "photo_paths";
    public final static String PHOTO_CURRENT_ITEM = "photo_current_item";
    public final static String PHOTO_TOP = "photo_top";
    public final static String PHOTO_LEFT = "photo_left";
    public final static String PHOTO_WIDTH = "photo_width";
    public final static String PHOTO_HEIGHT = "photo_height";
    public final static String PHOTO_COLUMN = "photo_column";
    public final static String PHOTO_ANIM = "photo_anim";

    //用于做图片动画
    private int photoTop = 0;
    private int photoLeft = 0;
    private int photoWidth = 0;
    private int photoHeight = 0;
    private int photoColumn = 0;


    //动画持续时间
    public final static int ENTER_DURATION = 200;
    public final static int EXIT_DURATION = 100;

    private int dot_size = 6;

    private int dot_padding = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browse_activity);
        init(savedInstanceState);
    }


    private void init(Bundle savedInstanceState) {

        fragment_layout = (LinearLayout) findViewById(R.id.fragment);

        page_index_layout = (LinearLayout) findViewById(R.id.page_index_layout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String[] pathArr = bundle.getStringArray(PHOTO_PATHS);
            images.clear();
            if (pathArr != null) {
                images = new ArrayList<>(Arrays.asList(pathArr));
            }
            phototAnim = bundle.getBoolean(PHOTO_ANIM);
            currentItem = bundle.getInt(PHOTO_CURRENT_ITEM);
            photoTop = bundle.getInt(PHOTO_TOP);
            photoLeft = bundle.getInt(PHOTO_LEFT);
            photoWidth = bundle.getInt(PHOTO_WIDTH);
            photoHeight = bundle.getInt(PHOTO_HEIGHT);
            photoColumn = bundle.getInt(PHOTO_COLUMN);
        }

        imageBrowseFragment =
                ImageBrowseFragment.newInstance(images, currentItem);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .replace(R.id.fragment, imageBrowseFragment)
                .addToBackStack(null)
                .commit();


        if (images.size() > 1) {
            dots.clear();
            int dot_wh = BaseUtil.dp2px(this, dot_size);
            int dot_p = BaseUtil.dp2px(this, dot_padding);
            for (int i = 0; i < images.size(); i++) {
                View v = new View(this);
                v.setBackgroundResource(R.drawable.dot_bg);
                LinearLayout.LayoutParams parme = new LinearLayout.LayoutParams(dot_wh, dot_wh);
                parme.setMargins(dot_p, dot_p, dot_p, dot_p);
                page_index_layout.addView(v, parme);
                dots.add(v);
            }
        }

        initDots(currentItem);

        imageBrowseFragment.setPageChangeListener(new PageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                initDots(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


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

                        }
                    });

                    return true;
                }
            });
        }

    }

    //重置dot的状态
    private void initDots(int position) {
        for (int i = 0; i < dots.size(); i++) {
            if (i == position) {
                dots.get(i).setSelected(true);
            } else {
                dots.get(i).setSelected(false);
            }
        }
    }

    private void runEnterAnimation(final Runnable startAction) {
        final long duration = ENTER_DURATION;

        ViewHelper.setPivotX(fragment_layout, 0);
        ViewHelper.setPivotY(fragment_layout, 0);
        ViewHelper.setScaleX(fragment_layout, (float) photoWidth / fragment_layout.getWidth());
        ViewHelper.setScaleY(fragment_layout, (float) photoHeight / fragment_layout.getHeight());
        ViewHelper.setTranslationX(fragment_layout, photoLeft);
        ViewHelper.setTranslationY(fragment_layout, photoTop);

        ViewPropertyAnimator.animate(fragment_layout)
                .setDuration(duration)
                .scaleX(1)
                .scaleY(1)
                .translationX(0)
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                page_index_layout.setVisibility(View.GONE);
                fragment_layout.setAlpha(1);
                startAction.run();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                page_index_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }


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
                        page_index_layout.setVisibility(View.GONE);
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

    }


    @Override
    public void onBackPressed() {

        runExitAnimation(new Runnable() {
            public void run() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                }
                ImageBrowseActivity.this.finish();
                overridePendingTransition(0, 0);

            }
        });
    }


}
