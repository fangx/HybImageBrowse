package cn.nn.hybimagebrowse.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.nn.hybimagebrowse.utils.BaseUtil;

/**
 * **************************
 * Class:       DefaultImageLoaderHelper
 * Author:      fangx
 * Date:        16/9/18
 * Description:
 * ***************************
 */
public class DefaultImageLoaderHelper implements ImageLoaderHelper {

    private static LruCache<String, Bitmap> mCache;
    private static Handler mHandler;
    private static ExecutorService mThreadPool;
    private static Map<String, Future<?>> mTaskTags = new LinkedHashMap<String, Future<?>>();
    private Context mContext;
    private String cachePath;

    public DefaultImageLoaderHelper(Context context, String cachePath) {
        this.mContext = context;
        this.cachePath = cachePath;
        if (!TextUtils.isEmpty(cachePath)) {
            File file = new File(cachePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        if (mCache == null) {
            // 最大使用的内存空间
            int maxSize = (int) (Runtime.getRuntime().freeMemory() / 4);
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }
            };
        }

        if (mHandler == null) {
            mHandler = new Handler();
        }

        if (mThreadPool == null) {
            // 最多同时允许的线程数为3个
            mThreadPool = Executors.newFixedThreadPool(3);
        }
    }


    private void loadBitmapFromNet(String url, ImageDownLoadListener imageDownLoadListener) {
        // 开线程去网络获取
        // 使用线程池管理

        // 判断是否有线程在为 imageView加载数据
        Future<?> futrue = mTaskTags.get(url);
        if (futrue != null && !futrue.isCancelled() && !futrue.isDone()) {
            // 线程正在执行
            futrue.cancel(true);
            futrue = null;
        }

        futrue = mThreadPool.submit(new ImageLoadTask(url, cachePath, imageDownLoadListener));
        // Future 和 callback/Runable
        // 返回值，持有正在执行的线程
        // 保存
        mTaskTags.put(url, futrue);
    }

    class ImageLoadTask implements Runnable {

        private String mUrl;

        private String cachePath;

        private ImageDownLoadListener imageDownLoadListener;

        public ImageLoadTask(String url, String cachePath, ImageDownLoadListener imageDownLoadListener) {
            this.mUrl = url;
            this.cachePath = cachePath;
            this.imageDownLoadListener = imageDownLoadListener;
        }

        @Override
        public void run() {
            try {
                // 获取连接
                HttpURLConnection conn = (HttpURLConnection) new URL(mUrl).openConnection();

                conn.setConnectTimeout(30 * 1000);// 设置连接服务器超时时间
                conn.setReadTimeout(30 * 1000);// 设置读取响应超时时间

                // 连接网络
                conn.connect();

                // 获取响应码
                int code = conn.getResponseCode();

                if (200 == code) {
                    InputStream is = conn.getInputStream();

                    // 将流转换为bitmap
                    final Bitmap bitmap = BitmapFactory.decodeStream(is);

                    // 存储到本地
                    write2Local(mUrl, bitmap);

                    // 存储到内存
                    mCache.put(mUrl, bitmap);

                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {

                            if (imageDownLoadListener != null) {
                                imageDownLoadListener.success(bitmap);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 本地种去去图片
     *
     * @param url
     */
    private Bitmap loadBitmapFromLocal(String url) {
        // 去找文件，将文件转换为bitmap
        String name;
        try {
            name = BaseUtil.makeMD5(url);

            File file = new File(cachePath, name);
            if (file.exists()) {

                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                // 存储到内存
                mCache.put(url, bitmap);
                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void write2Local(String url, Bitmap bitmap) {
        String name;
        FileOutputStream fos = null;
        try {
            name = BaseUtil.makeMD5(url);
            File file = new File(cachePath, name);
            fos = new FileOutputStream(file);

            // 将图像写到流中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void downloadImage(String path, final ImageDownLoadListener imageDownLoadListener) {

        // 1.去内存中取
        Bitmap bitmap = mCache.get(path);

        if (bitmap != null) {

//            imageDownLoadListener.preview(bitmap);

            final Bitmap aa = bitmap;

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 直接显示
                    imageDownLoadListener.success(aa);
                }
            }, 2000);

            return;
        }
        // 2.去硬盘上取
        bitmap = loadBitmapFromLocal(path);
        if (bitmap != null) {
            // 直接显示
            imageDownLoadListener.success(bitmap);
            return;
        }

        loadBitmapFromNet(path, imageDownLoadListener);


    }

}
