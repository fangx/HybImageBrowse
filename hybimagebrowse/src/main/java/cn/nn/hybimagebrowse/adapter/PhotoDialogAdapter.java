package cn.nn.hybimagebrowse.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.nn.hybimagebrowse.R;

/**
 * **************************
 * Class:       PhotoDialogAdapter
 * Author:      fangx
 * Date:        16/11/4
 * Description:
 * ***************************
 */
public class PhotoDialogAdapter extends BaseAdapter {

    private List<String> items = new ArrayList<>();

    private Context context;

    public PhotoDialogAdapter(Context context, List<String> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.photo_save_item, null);
        TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
        if (items.size() >= position) {
            if (!TextUtils.isEmpty(items.get(position))) {
                tv_text.setText(items.get(position));
            }
        }
        return view;
    }
}
