package com.kingstone.smith.gacor;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesAdapterViewHolder> {
//    private String[] mDataset;
//    private ArrayList<Places> mPlaces;

    private Cursor mCursor;

    private Context mContext;

    private final PlacesAdapterOnClickHandler mClickHandler;

    public interface PlacesAdapterOnClickHandler {
        void onClick(double lat, double lng);
    };

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class PlacesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView mTextViewName;
        public TextView mTextViewDetail;
        public TextView mTextViewLatLong;

        public PlacesAdapterViewHolder(View v) {
            super(v);
            mTextViewName = v.findViewById(R.id.textViewName);
            mTextViewDetail = v.findViewById(R.id.textViewDetail);
            mTextViewLatLong = v.findViewById(R.id.textViewLatLong);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCursor.moveToPosition(getAdapterPosition());
            mClickHandler.onClick(mCursor.getDouble(PlacesFragment.INDEX_PLACE_LAT), mCursor.getDouble(PlacesFragment.INDEX_PLACE_LANG));
        }
    }

    public PlacesAdapter(Context context, PlacesAdapterOnClickHandler clickHandler) {
        mContext = context;
//        mPlaces = places;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public PlacesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.list_places, parent, false);

        PlacesAdapterViewHolder vh = new PlacesAdapterViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesAdapterViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        holder.mTextViewName.setText(mCursor.getString(PlacesFragment.INDEX_PLACE_NAME));
        holder.mTextViewDetail.setText(mCursor.getString(PlacesFragment.INDEX_PLACE_DETAIL));
        holder.mTextViewLatLong.setText(
                "LatLong: " +
                String.valueOf(mCursor.getDouble(PlacesFragment.INDEX_PLACE_LAT)) + ", " +
                String.valueOf(mCursor.getDouble(PlacesFragment.INDEX_PLACE_LANG)));

//        holder.mTextViewName.setText(mPlaces.get(position).getName());
//        holder.mTextViewDetail.setText(mPlaces.get(position).getDetail());
//        holder.mTextViewLatLong.setText(
//                "LatLong: " +
//                String.valueOf(mPlaces.get(position).getLattitude()) + ", " +
//                String.valueOf(mPlaces.get(position).getLongitude()));
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
//        return mPlaces.size();
    }

    void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
