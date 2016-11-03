package cn.nn.hybimagebrowse.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.nn.hybimagebrowse.helper.ImageLoader;
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

    private ImageLoader imageLoader;

    private String cacheDir = "";

    public ImagePagerAdapter(FragmentManager fm, List<String> images,ImageLoader imageLoader) {
        super(fm);
        this.images = images;
        this.imageLoader = imageLoader;
    }

    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    public Fragment getItem(int position) {
        ViewPagerFragment fragment = new ViewPagerFragment(imageLoader);
        fragment.setImagepath(images.get(position));
        fragment.setCacheDir(cacheDir);
        return fragment;
    }

    @Override
    public int getCount() {
        return images.size();
    }
}