package cn.nn.hybimagebrowse.helper;

/**
 * **************************
 * Class:       PageChangeListener
 * Author:      fangx
 * Date:        16/11/3
 * Description:
 * ***************************
 */
public interface PageChangeListener{

    void onPageSelected(int position);

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageScrollStateChanged(int state);

}
