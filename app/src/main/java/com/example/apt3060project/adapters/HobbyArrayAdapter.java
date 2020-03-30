package com.example.apt3060project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.apt3060project.database.Hobby;
import com.example.apt3060project.R;

import java.util.List;

public class HobbyArrayAdapter extends ArrayAdapter<Hobby> {

    private final Context context;
    private final List<Hobby> values;


    public HobbyArrayAdapter(@NonNull Context context, @NonNull List<Hobby> values) {
        super(context, R.layout.hobby_list_item, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh;
        Hobby hobby = values.get(position);
        if(convertView == null){
            vh = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.hobby_list_item, parent, false);
            vh.name = (TextView) convertView.findViewById(R.id.hobby_list_item_name);
            vh.button = (Button) convertView.findViewById(R.id.hobby_list_item_button);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        vh.requested = hobby;
        vh.name.setText(hobby.getName());
        vh.button.setTag(position);

        return convertView;
    }

    private static class ViewHolder {
        public Hobby requested;
        public TextView name;
        public Button button;
    }
}
