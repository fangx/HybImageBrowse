package cn.nn.hybimagebrowse.ui.fragment;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.nn.hybimagebrowse.R;
import cn.nn.hybimagebrowse.adapter.ImagePagerAdapter;
import cn.nn.hybimagebrowse.helper.DefaultImageLoaderHelper;
import cn.nn.hybimagebrowse.helper.ImageLoaderHelper;
import cn.nn.hybimagebrowse.utils.BaseUtil;

/**
 * **************************
 * Class:       ImageBrowseFragment
 * Author:      fangx
 * Date:        16/9/18
 * Description:
 * ***************************
 */
public class ImageBrowseFragment extends Fragment {

    private ViewPager viewpager;

    private List<String> images = new ArrayList<>();
    private boolean phototAnim;
    private int currentItem;
    //用于做图片动画
    private int photoTop = 0;
    private int photoLeft = 0;
    private int photoWidth = 0;
    private int photoHeight = 0;

    private ImageLoaderHelper imageLoaderHelper;

    //缓存目录地址
    private String cacheDir = "";

    public final static String PHOTO_PATHS = "photo_paths";
    public final static String PHOTO_CURRENT_ITEM = "photo_current_item";
    public final static String PHOTO_TOP = "photo_top";
    public final static String PHOTO_LEFT = "photo_left";
    public final static String PHOTO_WIDTH = "photo_width";
    public final static String PHOTO_HEIGHT = "photo_height";
    public final static String PHOTO_ANIM = "photo_anim";

    //动画持续时间
    public final static int DURATION = 300;

    private final ColorMatrix colorizerMatrix = new ColorMatrix();

    public static ImageBrowseFragment newInstance(List<String> images, int currentItem) {
        Bundle args = new Bundle();
        ImageBrowseFragment fragment = new ImageBrowseFragment();
        args.putStringArray(PHOTO_PATHS, images.toArray(new String[images.size()]));
        args.putInt(PHOTO_CURRENT_ITEM, currentItem);
        args.putBoolean(PHOTO_ANIM, false);
        fragment.setArguments(args);
        return fragment;
    }


    public static ImageBrowseFragment newInstance(List<String> images, int currentItem, int[] screenLocation, int thumbnailWidth, int thumbnailHeight) {
        ImageBrowseFragment f = newInstance(images, currentItem);

        f.getArguments().putInt(PHOTO_LEFT, screenLocation[0]);
        f.getArguments().putInt(PHOTO_TOP, screenLocation[1]);
        f.getArguments().putInt(PHOTO_WIDTH, thumbnailWidth);
        f.getArguments().putInt(PHOTO_HEIGHT, thumbnailHeight);
        f.getArguments().putBoolean(PHOTO_ANIM, true);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
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
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.image_browse_layout, container, false);
        cacheDir = BaseUtil.getCacheDir(getActivity());
        imageLoaderHelper = new DefaultImageLoaderHelper(getActivity(), cacheDir);
        PagerAdapter pagerAdapter = new ImagePagerAdapter(getChildFragmentManager(), images, imageLoaderHelper);
        viewpager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setCurrentItem(currentItem);
        if (savedInstanceState == null && phototAnim) {
            ViewTreeObserver observer = viewpager.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    viewpager.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] screenLocation = new int[2];
                    viewpager.getLocationOnScreen(screenLocation);
                    photoLeft = photoLeft - screenLocation[0];
                    photoTop = photoTop - screenLocation[1];

                    runEnterAnimation(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
                        }
                    });

                    return true;
                }
            });
        }


        return rootView;
    }

    /**
     * The enter animation scales the picture in from its previous thumbnail
     * size/location, colorizing it in parallel. In parallel, the background of the
     * activity is fading in. When the pictue is in place, the text description
     * drops down.
     */
    private void runEnterAnimation(final Runnable startAction) {
        final long duration = DURATION;

        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it back up
        ViewHelper.setPivotX(viewpager, 0);
        ViewHelper.setPivotY(viewpager, 0);
        ViewHelper.setScaleX(viewpager, (float) photoWidth / viewpager.getWidth());
        ViewHelper.setScaleY(viewpager, (float) photoHeight / viewpager.getHeight());
        ViewHelper.setTranslationX(viewpager, photoLeft);
        ViewHelper.setTranslationY(viewpager, photoTop);

        // Animate scale and translation to go from thumbnail to full size
        ViewPropertyAnimator.animate(viewpager)
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
        ObjectAnimator colorizer = ObjectAnimator.ofFloat(ImageBrowseFragment.this,
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

        if (!getArguments().getBoolean(PHOTO_ANIM, false) || !phototAnim) {
            endAction.run();
            return;
        }

        final long duration = DURATION;

        // Animate image back to thumbnail size/location
        ViewPropertyAnimator.animate(viewpager)
                .setDuration(duration)
                .setInterpolator(new AccelerateInterpolator())
                .scaleX((float) photoWidth / viewpager.getWidth())
                .scaleY((float) photoHeight / viewpager.getHeight())
                .translationX(photoLeft)
                .translationY(photoTop)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
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
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(viewpager.getBackground(), "alpha", 0);
        bgAnim.setDuration(duration);
        bgAnim.start();

        // Animate a color filter to take the image back to grayscale,
        // in parallel with the image scaling and moving into place.
        ObjectAnimator colorizer =
                ObjectAnimator.ofFloat(ImageBrowseFragment.this, "saturation", 1, 0);
        colorizer.setDuration(duration);
        colorizer.start();
    }

    /**
     * 设置图片加载辅助类  不设置则走默认的DefaultImageLoaderHelper
     *
     * @param imageLoaderHelper
     */
    public void setImageLoaderHelper(ImageLoaderHelper imageLoaderHelper) {
        this.imageLoaderHelper = imageLoaderHelper;
    }

    /**
     * 设置缓存目录
     *
     * @param cacheDir
     */
    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }


}
