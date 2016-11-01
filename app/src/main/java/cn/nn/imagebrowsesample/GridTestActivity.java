package cn.nn.imagebrowsesample;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.nn.hybimagebrowse.ui.fragment.ImageBrowseFragment;

/**
 * **************************
 * Class:       GridTestActivity
 * Author:      fangx
 * Date:        16/10/31
 * Description:
 * ***************************
 */
public class GridTestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<String> list = new ArrayList<>();

    private Context context;

    private ImageBrowseFragment imageBrowseFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        context = this;
        init();
    }


    private void init() {
        list.add("http://img.haoyunbang.cn/app/0c6cd032-98dd-357d-acec-b60ed20155bf/8f6c0736-ca1e-4bce-a05c-b7b0d334c604.jpg");
        list.add("http://fxblog.oss-cn-beijing.aliyuncs.com/0034A2F7-B067-458A-9DE1-CF18B4C97941.png");
        list.add("http://img.haoyunbang.cn/app/0c6cd032-98dd-357d-acec-b60ed20155bf/8f6c0736-ca1e-4bce-a05c-b7b0d334c604.jpg");
        QuickAdapter quickAdapter = new QuickAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(quickAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    public class QuickAdapter extends BaseQuickAdapter<String> {
        public QuickAdapter() {
            super(R.layout.image_item, list);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final String url) {
            Glide
                    .with(context)
                    .load(url)
                    .into((ImageView) helper.getView(R.id.imagview));

            helper.getView(R.id.imagview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] screenLocation = new int[2];
                    v.getLocationOnScreen(screenLocation);
                    imageBrowseFragment =
                            ImageBrowseFragment.newInstance(list, helper.getAdapterPosition(), screenLocation, v.getWidth(),
                                    v.getHeight());
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction
                            .replace(android.R.id.content, imageBrowseFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

        }
    }


    /**
     * Overriding this method allows us to run our exit animation first, then exiting
     * the activity when it complete.
     */
    @Override
    public void onBackPressed() {
        if (imageBrowseFragment != null && imageBrowseFragment.isVisible()) {
            imageBrowseFragment.runExitAnimation(new Runnable() {
                public void run() {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    }
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
            });
        } else {
            super.onBackPressed();
        }
    }


}
