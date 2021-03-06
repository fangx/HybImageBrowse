package cn.nn.imagebrowsesample;

import android.content.Context;

/**
 * **************************
 * Class:       FrescoImageLoaderHelper
 * Author:      fangx
 * Date:        16/9/18
 * Description:
 * ***************************
 */
//implements ImageLoaderHelper
public class FrescoImageLoaderHelper {

    private Context context;

    public FrescoImageLoaderHelper(Context context) {
        this.context = context;
    }

//    @Override
//    public void downloadImage(String path, String cachePath, ImageDownLoadListener imageDownLoadListener) {
//
//
//        ImageRequest imageRequest = ImageRequest.fromUri(path);
//        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
//                .getEncodedCacheKey(imageRequest);
//
//        FileCache fileCache = ImagePipelineFactory.getInstance()
//                .getMainFileCache();
//
//        if (fileCache != null && fileCache.hasKey(cacheKey)) {
//            BinaryResource resource = fileCache.getResource(cacheKey);
//            File file = ((FileBinaryResource) resource).getFile();
//            if (file != null && !TextUtils.isEmpty(file.getAbsolutePath())) {
//                //缓存中有数据
//                imageDownLoadListener.success(file.getAbsolutePath());
//            } else {
//                downLoadImage(path, cachePath, imageDownLoadListener);
//            }
//        } else {
//            downLoadImage(path, cachePath, imageDownLoadListener);
//        }
//
//    }
//
//    public void downLoadImage(String path, final String cachePath, final ImageDownLoadListener imageDownLoadListener) {
//
//        ImageRequest imageRequest = ImageRequest.fromUri(path);
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//
//        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
//        dataSource.subscribe(new BaseBitmapDataSubscriber() {
//            @Override
//            public void onNewResultImpl(Bitmap bitmap) {
//                if (bitmap == null) {
//                    imageDownLoadListener.fail();
//                }
//                final File file = new File(cachePath);
//                try {
//                    FileOutputStream fos = new FileOutputStream(file);
//                    assert bitmap != null;
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                    fos.flush();
//                    fos.close();
//                    imageDownLoadListener.success(file.getAbsolutePath());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    imageDownLoadListener.fail();
//                }
//            }
//
//            @Override
//            public void onFailureImpl(DataSource dataSource) {
//            }
//        }, CallerThreadExecutor.getInstance());
//    }

}
