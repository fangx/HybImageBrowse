package cn.nn.hybimagebrowse.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.nn.hybimagebrowse.helper.ImageLoaderHelper;
import cn.nn.hybimagebrowse.ui.fragment.ViewPagerFragment;

/**
 * **************************
 * Class:       ImagePagerAdapter
 * Author:      fangx
 * Date:        16/9/18
 * Description:
 * ***************************
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    private List<String> images = new ArrayList<>();

    private ImageLoaderHelper imageLoaderHelper;

    private String cacheDir = "";

    public ImagePagerAdapter(FragmentManager fm, List<String> images,ImageLoaderHelper imageLoaderHelper) {
        super(fm);
        this.images = images;
        this.imageLoaderHelper = imageLoaderHelper;
    }

    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    public void setImageLoaderHelper(ImageLoaderHelper imageLoaderHelper) {
        this.imageLoaderHelper = imageLoaderHelper;
    }

    @Override
    public Fragment getItem(int position) {
        ViewPagerFragment fragment = new ViewPagerFragment(imageLoaderHelper);
        fragment.setImagepath(images.get(position));
        fragment.setCacheDir(cacheDir);
        return fragment;
    }

    @Override
    public int getCount() {
        return images.size();
    }
}