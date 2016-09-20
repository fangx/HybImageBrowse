package cn.nn.hybimagebrowse.helper;

import android.graphics.Bitmap;

/**
 * **************************
 * Class:       ImageDownLoadListener
 * Author:      fangx
 * Date:        16/9/18
 * Description:
 * ***************************
 */
public interface ImageDownLoadListener {

    //预览的小图
    void preview(String path);

    void preview(Bitmap bitmap);

    //下载或者缓存读取成功
    void success(String path);

    void success(Bitmap bitmap);

    //下载或者缓存读取失败
    void fail();

}
