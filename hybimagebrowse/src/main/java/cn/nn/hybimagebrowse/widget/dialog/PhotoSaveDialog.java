package cn.nn.hybimagebrowse.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.nn.hybimagebrowse.R;
import cn.nn.hybimagebrowse.adapter.PhotoDialogAdapter;

/**
 * **************************
 * Class:       PhotoSaveDialog
 * Author:      fangx
 * Date:        16/11/4
 * Description:
 * ***************************
 */
public class PhotoSaveDialog extends BaseDialog {

    private ListView lv_photo_save;

    private PhotoDialogAdapter photoDialogAdapter;

    private List<String> items = new ArrayList<>();

    private PopItemClickListener popItemClickListener;

    public PhotoSaveDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_save_dialog);
        init();
    }

    private void init() {

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        this.getWindow().setAttributes(params);

        lv_photo_save = (ListView) findViewById(R.id.lv_photo_save);

        items.add("保存到相册");
        items.add("取消");

        photoDialogAdapter = new PhotoDialogAdapter(getContext(), items);
        lv_photo_save.setAdapter(photoDialogAdapter);

        lv_photo_save.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (popItemClickListener != null) {
                    popItemClickListener.click(position);
                }

            }
        });

    }


    public void setPopItemClickListener(PopItemClickListener popItemClickListener) {
        this.popItemClickListener = popItemClickListener;
    }

    //条目选择监听
    public interface PopItemClickListener {
        void click(int position);
    }


}
