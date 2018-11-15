package com.kingstone.smith.gacor;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingstone.smith.gacor.HeatSpot.HeatSpotFragment;
import com.kingstone.smith.gacor.data.GacorContract;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesAdapterViewHolder> {
//    private String[] mDataset;
//    private ArrayList<Places> mPlaces;

    private Cursor mCursor;

    private Context mContext;

    private final PlacesAdapterOnClickHandler mClickHandler;

    public interface PlacesAdapterOnClickHandler {
        void onClick(double lat, double lng);
        void onMenuItemClick(long id);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class PlacesAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener{
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
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            mCursor.moveToPosition(getAdapterPosition());
            mClickHandler.onClick(mCursor.getDouble(HeatSpotFragment.INDEX_HEATSPOT_LAT), mCursor.getDouble(HeatSpotFragment.INDEX_HEATSPOT_LANG));
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//            MenuItem Edit = contextMenu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem Delete = contextMenu.add(Menu.NONE, 2, 2, "Delete");
//            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        //ADD AN ONMENUITEM LISTENER TO EXECUTE COMMANDS ONCLICK OF CONTEXT MENU TASK
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mCursor.moveToPosition(getAdapterPosition());
                switch (item.getItemId()) {
                    case 1:
                        //Do stuff
                        break;

                    case 2: // delete
                        mClickHandler.onMenuItemClick(mCursor.getLong(HeatSpotFragment.INDEX_HEATSPOT_ID));

                        break;
                }
                return true;
            }
        };
    }

    public PlacesAdapter(Context context, PlacesAdapterOnClickHandler clickHandler) {
        mContext = context;
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

        holder.mTextViewName.setText(mCursor.getString(HeatSpotFragment.INDEX_HEATSPOT_NAME));
        holder.mTextViewDetail.setText(mCursor.getString(HeatSpotFragment.INDEX_HEATSPOT_DETAIL));
        holder.mTextViewLatLong.setText(
                "LatLong: " +
                String.valueOf(mCursor.getDouble(HeatSpotFragment.INDEX_HEATSPOT_LAT)) + ", " +
                String.valueOf(mCursor.getDouble(HeatSpotFragment.INDEX_HEATSPOT_LANG)));
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
