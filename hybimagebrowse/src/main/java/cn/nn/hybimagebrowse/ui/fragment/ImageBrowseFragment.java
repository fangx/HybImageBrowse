package cn.nn.hybimagebrowse.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.nn.hybimagebrowse.R;
import cn.nn.hybimagebrowse.adapter.ImagePagerAdapter;
import cn.nn.hybimagebrowse.helper.DefaultImageLoader;
import cn.nn.hybimagebrowse.helper.ImageLoader;
import cn.nn.hybimagebrowse.ui.activity.ImageBrowseActivity;
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
    private int currentItem;

    private ImageLoader imageLoader;

    //缓存目录地址
    private String cacheDir = "";


    //动画持续时间
    public final static int DURATION = 200;


    //默认无动画效果
    public static ImageBrowseFragment newInstance(List<String> images, int currentItem) {
        Bundle args = new Bundle();
        ImageBrowseFragment fragment = new ImageBrowseFragment();
        args.putStringArray(ImageBrowseActivity.PHOTO_PATHS, images.toArray(new String[images.size()]));
        args.putInt(ImageBrowseActivity.PHOTO_CURRENT_ITEM, currentItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String[] pathArr = bundle.getStringArray(ImageBrowseActivity.PHOTO_PATHS);
            images.clear();
            if (pathArr != null) {
                images = new ArrayList<>(Arrays.asList(pathArr));
            }
            currentItem = bundle.getInt(ImageBrowseActivity.PHOTO_CURRENT_ITEM);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.image_browse_layout, container, false);
        cacheDir = BaseUtil.getCacheDir(getActivity());
        imageLoader = new DefaultImageLoader(getActivity(), cacheDir);
        PagerAdapter pagerAdapter = new ImagePagerAdapter(getChildFragmentManager(), images, imageLoader);
        viewpager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setCurrentItem(currentItem);
        return rootView;
    }

    /**
     * 设置图片加载辅助类  不设置则走默认的DefaultImageLoaderHelper
     *
     * @param imageLoader
     */
    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    /**
     * 设置缓存目录
     *
     * @param cacheDir
     */
    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }


    //获取当前展示第几个页面
    public int getCurrentItem() {
        if (viewpager != null) {
            return viewpager.getCurrentItem();
        }
        return 0;
    }
}
