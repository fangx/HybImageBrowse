package cn.nn.hybimagebrowse.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.nn.hybimagebrowse.R;
import cn.nn.hybimagebrowse.helper.ImageDownLoadListener;
import cn.nn.hybimagebrowse.helper.ImageLoaderHelper;
import cn.nn.hybimagebrowse.utils.BaseUtil;
import cn.nn.hybimagebrowse.widget.scalephoto.ImageSource;
import cn.nn.hybimagebrowse.widget.scalephoto.SubsamplingScaleImageView;

/**
 * **************************
 * Class:       ViewPagerFragment
 * Author:      fangx
 * Date:        16/9/18
 * Description:
 * ***************************
 */
public class ViewPagerFragment extends Fragment {

    private static final String BUNDLE_IMAGEPATH = "imagepath";

    private String imagepath;

    private ImageLoaderHelper imageLoaderHelper;

    private String cacheDir = "";
    //占位图
    private String placeholderImage = "";
    //失败图
    private String failImage = "";

    public ViewPagerFragment(ImageLoaderHelper imageLoaderHelper) {
        this.imageLoaderHelper = imageLoaderHelper;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    public void setPlaceholderImage(String placeholderImage) {
        this.placeholderImage = placeholderImage;
    }

    public void setFailImage(String failImage) {
        this.failImage = failImage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_pager_page, container, false);

        if (savedInstanceState != null) {
            if (imagepath == null && savedInstanceState.containsKey(BUNDLE_IMAGEPATH)) {
                imagepath = savedInstanceState.getString(BUNDLE_IMAGEPATH);
            }
        }
        if (imagepath != null) {
            final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) rootView.findViewById(R.id.imageView);

            if (imagepath.startsWith("http")) {

                imageLoaderHelper.downloadImage(imagepath, new ImageDownLoadListener() {
                    @Override
                    public void success(String path) {
                        imageView.setImage(ImageSource.uri(path));
                    }

                    @Override
                    public void success(Bitmap bitmap) {
                        imageView.setImage(ImageSource.bitmap(bitmap));
                    }

                    @Override
                    public void fail() {

                    }
                });

            } else {
                imageView.setImage(ImageSource.uri(imagepath));
            }

        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        View rootView = getView();
        if (rootView != null) {
            outState.putString(BUNDLE_IMAGEPATH, imagepath);
        }
    }

}