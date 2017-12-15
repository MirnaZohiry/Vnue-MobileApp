package com.example.a100535658.project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

// This is done whenever we want to create Spinner/ListView/GridView
public class LabelAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> data;

    public LabelAdapter(Context context, ArrayList<String> data) {
        //super(context, R.layout.listview_layout, data);
        this.data = data;
        this.context = context;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;


        if (convertView == null) {
            // there is no object to reuse, create one
            textView = new TextView(context);
            textView.setText(data.get(position));
        } else {
            // we're reusing an old object
            textView = (TextView)convertView;
        }

        return textView;
    }
}