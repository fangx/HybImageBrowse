package cn.nn.hybimagebrowse.dao;

/**
 * **************************
 * Class:       ImageInfo
 * Author:      fangx
 * Date:        16/11/1
 * Description: 图片信息包括位置信息
 * ***************************
 */
public class ImageInfo {

    private String path;

    private int left;

    private int top;

    private int height;

    private int width;

    private int position;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
