package xyz.atte.img_hurdur;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Atte on 20.4.2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
   // private String[] mDataset;
    private List<ContactInfo> contactList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //public TextView mTextView;
        protected TextView vName;
        protected TextView vSurname;
        protected TextView vEmail;
        protected TextView vTitle;

        public ViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            vSurname = (TextView)  v.findViewById(R.id.txtSurname);
            vEmail = (TextView)  v.findViewById(R.id.txtEmail);
            vTitle = (TextView) v.findViewById(R.id.title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<ContactInfo> contactList) {
       this.contactList = contactList;
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
        //holder.mTextView.setText(mDataset[position]);
        ContactInfo ci = contactList.get(position);
        holder.vName.setText(ci.name);
        holder.vSurname.setText(ci.surname);
        holder.vEmail.setText(ci.email);
        holder.vTitle.setText(ci.name + " " + ci.surname);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
