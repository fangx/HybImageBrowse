package cn.nn.imagebrowsesample;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nn.hybimagebrowse.ui.activity.ImageBrowseActivity;
import cn.nn.hybimagebrowse.ui.fragment.ImageBrowseFragment;

/**
 * **************************
 * Class:       DefaultDemoActivity
 * Author:      fangx
 * Date:        16/11/4
 * Description:
 * ***************************
 */
public class DefaultDemoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<String> list = new ArrayList<>();

    private Map<Integer, ImageView> imageViewMap = new HashMap<>();

    private Context context;

    private int column = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        context = this;
        initStatusBar(R.color.colorPrimary);
        init();
    }


    private void init() {

        list.add("http://img.haoyunbang.cn/app/0c6cd032-98dd-357d-acec-b60ed20155bf/8f6c0736-ca1e-4bce-a05c-b7b0d334c604.jpg");
        list.add("http://fxblog.oss-cn-beijing.aliyuncs.com/0034A2F7-B067-458A-9DE1-CF18B4C97941.png");
        list.add("http://img.haoyunbang.cn/app/0c6cd032-98dd-357d-acec-b60ed20155bf/8f6c0736-ca1e-4bce-a05c-b7b0d334c604.jpg");

        DefaultDemoActivity.QuickAdapter quickAdapter = new DefaultDemoActivity.QuickAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this, column);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(quickAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    public class QuickAdapter extends BaseQuickAdapter<String> {
        public QuickAdapter() {
            super(R.layout.image_item, list);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final String url) {

            ImageView imageView = (ImageView) helper.getView(R.id.imagview);

            if (imageViewMap.get(helper.getAdapterPosition()) == null) {
                imageViewMap.put(helper.getAdapterPosition(), imageView);
            }
            Glide
                    .with(context)
                    .load(url)
                    .into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int[] screenLocation = new int[2];
                    v.getLocationOnScreen(screenLocation);

                    Intent intent = new Intent(DefaultDemoActivity.this, ImageBrowseActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putStringArray(ImageBrowseActivity.PHOTO_PATHS, list.toArray(new String[list.size()]));
                    bundle.putInt(ImageBrowseActivity.PHOTO_CURRENT_ITEM, helper.getAdapterPosition());
                    bundle.putInt(ImageBrowseActivity.PHOTO_LEFT, screenLocation[0]);
                    bundle.putInt(ImageBrowseActivity.PHOTO_TOP, screenLocation[1]);
                    bundle.putInt(ImageBrowseActivity.PHOTO_WIDTH, v.getWidth());
                    bundle.putInt(ImageBrowseActivity.PHOTO_HEIGHT, v.getHeight());
                    bundle.putBoolean(ImageBrowseActivity.PHOTO_ANIM, true);
                    bundle.putInt(ImageBrowseActivity.PHOTO_COLUMN, column);
                    bundle.putInt(ImageBrowseActivity.PHOTO_COLUMN, v.getHeight());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });

        }
    }

    /**
     * 状态栏处理：解决全屏切换非全屏页面被压缩问题
     */
    public void initStatusBar(int barColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            // 获取状态栏高度
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            View rectView = new View(this);
            // 绘制一个和状态栏一样高的矩形，并添加到视图中
            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            rectView.setLayoutParams(params);
            //设置状态栏颜色
            rectView.setBackgroundColor(getResources().getColor(barColor));
            // 添加矩形View到布局中
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            decorView.addView(rectView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }


}