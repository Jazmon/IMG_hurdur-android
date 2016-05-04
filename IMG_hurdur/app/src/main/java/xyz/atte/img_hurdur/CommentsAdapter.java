package xyz.atte.img_hurdur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MikkoEPIC on 4.5.2016.
 */
public class CommentsAdapter extends ArrayAdapter<CommentData> {
    public CommentsAdapter(Context context, ArrayList<CommentData> data) {
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CommentData commentData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment, parent, false);
        }
        // Lookup view for data population
        TextView userName = (TextView) convertView.findViewById(R.id.userName);
        TextView commentText = (TextView) convertView.findViewById(R.id.commentText);
        // Populate the data into the template view using the data object
        userName.setText(commentData.userName);
        commentText.setText(commentData.comment);
        // Return the completed view to render on screen
        return convertView;
    }

}
