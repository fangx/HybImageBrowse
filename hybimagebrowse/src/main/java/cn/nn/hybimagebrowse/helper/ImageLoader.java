package cn.nn.hybimagebrowse.helper;

import android.app.Activity;

/**
 * **************************
 * Class:       ImageLoaderHelper
 * Author:      fangx
 * Date:        16/9/18
 * Description:
 * ***************************
 */
public interface ImageLoader {

    void downloadImage(String path,ImageDownLoadListener imageDownLoadListener);

}
