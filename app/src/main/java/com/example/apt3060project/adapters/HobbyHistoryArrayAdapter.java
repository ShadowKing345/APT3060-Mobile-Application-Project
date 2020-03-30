package com.example.apt3060project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.apt3060project.database.HobbyHistory;
import com.example.apt3060project.R;

import java.util.List;

public class HobbyHistoryArrayAdapter extends ArrayAdapter<HobbyHistory> {
    private final Context context;
    private final List<HobbyHistory> values;
    public HobbyHistoryArrayAdapter(@NonNull Context context, @NonNull List<HobbyHistory> values) {
        super(context, R.layout.hobby_history_list_item, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh;
        HobbyHistory hobbyHistory = values.get(position);
        if(convertView == null){
            vh = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.hobby_history_list_item, parent, false);
            vh.dateStamp = (TextView) convertView.findViewById(R.id.hobby_history_list_item_date_stamp);
            vh.duration = (TextView) convertView.findViewById(R.id.hobby_history_list_item_duration);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        vh.requested = hobbyHistory;
        vh.dateStamp.setText(hobbyHistory.getDateStamp());
        vh.duration.setText(String.valueOf(hobbyHistory.getDuration()));

        return convertView;
    }

    private static class ViewHolder {
        public HobbyHistory requested;
        public TextView dateStamp;
        public TextView duration;
    }
}
