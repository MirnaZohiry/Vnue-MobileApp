package com.example.a100535658.project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 100535658 on 12/12/2017.
 */

public class TeamListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;

    //to store the list of countries
    private final ArrayList<String> nameArray;


    public TeamListAdapter(Activity context, ArrayList<String> nameArrayParam){

        super(context, R.layout.listview_layout, nameArrayParam);
        this.context=context;
        this.nameArray = nameArrayParam;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.teamview_layout, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.teamName);
        ImageView userView = (ImageView) rowView.findViewById(R.id.user_icon);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray.get(position));

        return rowView;

    };
}