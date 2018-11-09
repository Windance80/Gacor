package com.kingstone.smith.gacor.HeatSpot;

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

import com.kingstone.smith.gacor.R;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class HeatSpotAdapter extends RecyclerView.Adapter<HeatSpotAdapter.HeatSpotAdapterViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private List<ItemType> mItems = Collections.emptyList();

    private final HeatSpotAdapterOnClickHandler mClickHandler;

    public interface HeatSpotAdapterOnClickHandler {
        void onClick();
        void onMenuItemClick();
    }

    public class HeatSpotAdapterViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnCreateContextMenuListener{

        public TextView mTextViewDate;
        public TextView mTextViewTime;
        public TextView mTextViewPlace;

        public HeatSpotAdapterViewHolder(View itemView) {
            super(itemView);
            mTextViewDate = itemView.findViewById(R.id.textViewDate);
            mTextViewTime = itemView.findViewById(R.id.textViewTime);
            mTextViewPlace = itemView.findViewById(R.id.textViewPlace);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem Edit = contextMenu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem Delete = contextMenu.add(Menu.NONE, 2, 2, "Delete");
            Edit.setOnMenuItemClickListener(onEditMenu);
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
                        mClickHandler.onMenuItemClick();

                        break;
                }
                return true;
            }
        };
    }

    public HeatSpotAdapter(Context context, HeatSpotAdapterOnClickHandler clickHandler) {
        super();
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public HeatSpotAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_heatspot, parent, false);
        HeatSpotAdapterViewHolder viewHolder = new HeatSpotAdapterViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HeatSpotAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String sDate = new SimpleDateFormat("EEEE, dd MMM yyyy").format(mCursor.getLong(HeatSpotFragment.INDEX_HEATSPOT_DATE));
        String sTime = new SimpleDateFormat("HH:mm").format(mCursor.getLong(HeatSpotFragment.INDEX_HEATSPOT_DATE));

        holder.mTextViewDate.setText(sDate);
        holder.mTextViewTime.setText(sTime);
        holder.mTextViewPlace.setText(mCursor.getString(HeatSpotFragment.INDEX_HEATSPOT_NAME));
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor, List<ItemType> itemTypes){
        mCursor = newCursor;
        mItems = itemTypes;
        notifyDataSetChanged();
    }
}
