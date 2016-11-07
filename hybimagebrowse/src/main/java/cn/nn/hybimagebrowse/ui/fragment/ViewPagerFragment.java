package cn.nn.hybimagebrowse.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.nn.hybimagebrowse.R;
import cn.nn.hybimagebrowse.helper.ImageDownLoadListener;
import cn.nn.hybimagebrowse.helper.ImageLoader;
import cn.nn.hybimagebrowse.widget.dialog.PhotoSaveDialog;
import cn.nn.hybimagebrowse.widget.scalephoto.ImageSource;
import cn.nn.hybimagebrowse.widget.scalephoto.ImageViewState;
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

    private ImageLoader imageLoader;

    private String cacheDir = "";
    //占位图
    private String placeholderImage = "";
    //失败图
    private String failImage = "";
    //图片保存文件夹
    private String save_path;

    //图片操作dialog
    private PhotoSaveDialog photoSaveDialog;

    public ViewPagerFragment(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
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


    public String getSave_path() {
        return save_path;
    }

    public void setSave_path(String save_path) {
        this.save_path = save_path;
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

            final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

            if (imagepath.startsWith("http")) {

                progressBar.setVisibility(View.VISIBLE);

                imageLoader.downloadImage(imagepath, new ImageDownLoadListener() {

                    @Override
                    public void preview(String path) {
                        imageView.setImage(ImageSource.uri(path), new ImageViewState(0.5F, new PointF(0, 0), 0));
                    }

                    @Override
                    public void preview(Bitmap bitmap) {
                        imageView.setImage(ImageSource.bitmap(bitmap), new ImageViewState(0.5F, new PointF(0, 0), 0));
                    }

                    @Override
                    public void success(String path) {
                        imageView.setImage(ImageSource.uri(path));
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void success(Bitmap bitmap) {
                        imageView.setImage(ImageSource.bitmap(bitmap));
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void fail() {
                        progressBar.setVisibility(View.GONE);
                    }
                });

            } else {
                imageView.setImage(ImageSource.uri(imagepath));
            }


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoSaveDialog = new PhotoSaveDialog(getActivity());
                    photoSaveDialog.setPopItemClickListener(new PhotoSaveDialog.PopItemClickListener() {
                        @Override
                        public void click(int position) {
                            switch (position) {
                                case 0:
                                    imageView.setDrawingCacheEnabled(true);
                                    int status = saveImageToPhone(imageView.getDrawingCache());
                                    imageView.setDrawingCacheEnabled(false);
                                    photoSaveDialog.dismiss();

                                    if (status == 1) {
                                        Toast.makeText(getActivity(), "保存成功!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "保存失败!", Toast.LENGTH_SHORT).show();
                                    }

                                    break;
                                case 1:
                                    photoSaveDialog.dismiss();
                                    break;
                            }
                        }
                    });
                    photoSaveDialog.show();
                }
            });

        }

        return rootView;
    }


    //保存图片到相册方法
    public int saveImageToPhone(Bitmap bmp) {

        if (bmp == null) {
            return 0;
        }

        File appDir = null;

        if (!TextUtils.isEmpty(save_path)) {
            appDir = new File(save_path);
        } else {
            appDir = new File(Environment.getExternalStorageDirectory(), "imagebrowse");
        }

        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

//        try {
//            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
//                    file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return 0;
//        }
//        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        return 1;
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