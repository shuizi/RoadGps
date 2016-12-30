package com.rca_gps.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rca_gps.app.R;
import com.rca_gps.app.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wafer on 2016/10/25.
 */

public class CaseAdapter extends BaseAdapter {
    private Context context;
    private List<Message> lists = new ArrayList<>();

    public CaseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_userinfo_list_row, null);
            holder = new ViewHolder();
            holder.tv_case_num = (TextView) convertView.findViewById(R.id.tv_case_num);
            holder.tv_case_time = (TextView) convertView.findViewById(R.id.tv_case_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_case_num.setText(lists.get(position).getCaseNumber());
        holder.tv_case_time.setText(lists.get(position).getDateTime());
        return convertView;
    }

    public List<Message> getLists() {
        return lists;
    }

    public void setLists(List<Message> lists) {
        this.lists = lists;
    }

    class ViewHolder {
        private TextView tv_case_num;
        private TextView tv_case_time;
    }
}
