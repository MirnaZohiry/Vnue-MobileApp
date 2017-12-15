package com.example.a100535658.project;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 100535658 on 12/14/2017.
 */

public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context context;
    private ArrayList<String> data;

    public CustomSpinnerAdapter(Context context,ArrayList<String> data) {
        this.data = data;
        this.context = context;
    }

    public int getCount()
    {
        return data.size();
    }

    public Object getItem(int position)
    {
        return data.get(position);
    }

    public long getItemId(int position)
    {
        return (long)position;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(data.get(position));
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

    public View getView(int viewPosition, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(context);
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
        txt.setText(data.get(viewPosition));
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

}

