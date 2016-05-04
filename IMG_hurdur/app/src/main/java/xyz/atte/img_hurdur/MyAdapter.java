package xyz.atte.img_hurdur;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.NetworkOnMainThreadException;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Atte on 20.4.2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ImageCardData> imageCardDataList;
    private static final String TAG = "MyAdapter";


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView vImageView;
        protected TextView vTitle;
        protected TextView vDescription;
        protected Button vCommentsButton;
        protected View foo;

        public ViewHolder(View v) {
            super(v);
            vDescription = (TextView) v.findViewById(R.id.description);
            vImageView = (ImageView) v.findViewById(R.id.imageView);
            vTitle = (TextView) v.findViewById(R.id.title);
            vCommentsButton = (Button) v.findViewById(R.id.commentsButton);
            foo = v;

            vCommentsButton.setOnClickListener(new View.OnClickListener() {
                Context c;

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "CLICK");
                    Log.d(TAG, foo.getContext().toString());
                    if ((c = foo.getContext()) instanceof RecyclerViewItemClick) {
                        ((RecyclerViewItemClick) c).onItemClick();
                        Log.d(TAG, "FOO");
                    }
                }
            });
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ImageCardData cardData = imageCardDataList.get(position);
        Bitmap bitmap = null;
        Log.d(TAG, "onBindViewHolder: cardData.imageUrl: " + cardData.imageUrl);
        try {
            bitmap = BitmapFactory.decodeStream(cardData.imageUrl.openConnection().getInputStream());
        } catch (IOException | NetworkOnMainThreadException e) {
            //e.printStackTrace();
            Log.e(TAG, "onBindViewHolder: failed to get image from url", e);
        }
        holder.vImageView.setImageBitmap(bitmap);
        holder.vTitle.setText(cardData.title);
        holder.vDescription.setText(cardData.description);


       /* holder.vCommentsButton.setOnClickListener(new View.OnClickListener() {
            Context c;
            @Override
            public void onClick(View v) {
                Log.d(TAG, v.getContext().getApplicationContext().toString());
                if((c = v.getContext()) instanceof RecyclerViewItemClick) {
                    ((RecyclerViewItemClick) c).onItemClick(holder);
                    Log.d(TAG, "FOO");
                }
                Log.d(TAG, "CLICK");
               // FragmentManager fm = getFragmentManager();
                //MainActivity.this.getFragmentManager()
            }
        });*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return imageCardDataList.size();
    }
}
