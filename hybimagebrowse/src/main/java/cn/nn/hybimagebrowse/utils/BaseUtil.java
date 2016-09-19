package cn.nn.hybimagebrowse.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * **************************
 * Class:       BaseUtil
 * Author:      fangx
 * Date:        16/9/18
 * Description:
 * ***************************
 */
public class BaseUtil {


    public static String getCacheDir(Context mContext) {
        String state = Environment.getExternalStorageState();
        File dir = null;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // 有sd卡
            dir = new File(Environment.getExternalStorageDirectory(), "/Android/data/" + mContext.getPackageName()
                    + "/imagecache");
        } else {
            // 没有sd卡
            dir = new File(mContext.getCacheDir(), "/imagecache");

        }

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir.getAbsolutePath();
    }


    /**
     * md5加密
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String makeMD5(String password) {
        MessageDigest md;
        try {
            // 生成一个MD5加密计算摘要
            md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(password.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String pwd = new BigInteger(1, md.digest()).toString(16);
            return pwd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }


}
