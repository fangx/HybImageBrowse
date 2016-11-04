package cn.nn.imagebrowsesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.nn.hybimagebrowse.ui.fragment.ImageBrowseFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String IB_FRAGMENT_TAG = "ib_fragment";

    private Button bt_defult;
    private Button bt_fresco;
    private Button bt_glide;
    private Button bt_picasso;
    private Button bt_imageloader;

    private ImageBrowseFragment imageBrowseFragment;

    private List<String> images = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }


    private void init() {

        bt_defult = (Button)findViewById(R.id.bt_defult);
        bt_fresco = (Button)findViewById(R.id.bt_fresco);
        bt_glide = (Button)findViewById(R.id.bt_glide);
        bt_picasso = (Button)findViewById(R.id.bt_picasso);
        bt_imageloader = (Button)findViewById(R.id.bt_imageloader);


        images.add("http://img.haoyunbang.cn/app/0c6cd032-98dd-357d-acec-b60ed20155bf/8f6c0736-ca1e-4bce-a05c-b7b0d334c604.jpg");
        images.add("http://fxblog.oss-cn-beijing.aliyuncs.com/0034A2F7-B067-458A-9DE1-CF18B4C97941.png");
        imageBrowseFragment = (ImageBrowseFragment) getSupportFragmentManager().findFragmentByTag(IB_FRAGMENT_TAG);

        if (imageBrowseFragment == null) {
            imageBrowseFragment = ImageBrowseFragment
                    .newInstance(images,0);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, imageBrowseFragment, IB_FRAGMENT_TAG)
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_defult:

                break;
            case R.id.bt_fresco:

                break;
            case R.id.bt_glide:

                break;
            case R.id.bt_picasso:

                break;
            case R.id.bt_imageloader:

                break;
        }

    }
}
