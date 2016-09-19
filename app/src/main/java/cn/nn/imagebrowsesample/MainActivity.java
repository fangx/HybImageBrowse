package cn.nn.imagebrowsesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.nn.hybimagebrowse.ui.fragment.ImageBrowseFragment;

public class MainActivity extends AppCompatActivity {

    private static final String IB_FRAGMENT_TAG = "ib_fragment";

    private ImageBrowseFragment imageBrowseFragment;

    private List<String> images = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }


    private void init() {

        images.add("ness.jpg");
        images.add("squirrel.jpg");
        imageBrowseFragment = (ImageBrowseFragment) getSupportFragmentManager().findFragmentByTag(IB_FRAGMENT_TAG);

        if (imageBrowseFragment == null) {
            imageBrowseFragment = ImageBrowseFragment
                    .newInstance(images);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, imageBrowseFragment, IB_FRAGMENT_TAG)
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }


    }


}
