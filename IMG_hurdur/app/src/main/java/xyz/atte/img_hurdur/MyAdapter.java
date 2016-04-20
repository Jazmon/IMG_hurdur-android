package xyz.atte.img_hurdur;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Atte on 20.4.2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ImageCardData> imageCardDataList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView vImageView;
        protected TextView vTitle;
        protected TextView vDescription;

        public ViewHolder(View v) {
            super(v);
            vDescription = (TextView) v.findViewById(R.id.description);
            vImageView = (ImageView) v.findViewById(R.id.imageView);
            vTitle = (TextView) v.findViewById(R.id.title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<ImageCardData> imageCardDataList) {
        this.imageCardDataList = imageCardDataList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.image_card, parent, false);

        // TODO: set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ImageCardData cardData = imageCardDataList.get(position);
        holder.vImageView.setImageResource(cardData.imageResourceId);
        holder.vTitle.setText(cardData.title);
        holder.vDescription.setText(cardData.description);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return imageCardDataList.size();
    }
}
