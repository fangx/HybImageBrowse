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

    void success(String path);

    void success(Bitmap bitmap);

    void fail();

}
