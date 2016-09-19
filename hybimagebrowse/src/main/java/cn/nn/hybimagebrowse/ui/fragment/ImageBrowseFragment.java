package cn.nn.hybimagebrowse.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
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

    private ImageLoaderHelper imageLoaderHelper;

    //缓存目录地址
    private String cacheDir = "";

    public static ImageBrowseFragment newInstance(List<String> images) {
        Bundle args = new Bundle();
        ImageBrowseFragment fragment = new ImageBrowseFragment();
        fragment.images = images;
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

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
        return rootView;
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
