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
import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;

public class HeatSpotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ItemType> mItems = Collections.emptyList();

    private final HeatSpotAdapterOnClickHandler mClickHandler;

    public interface HeatSpotAdapterOnClickHandler {
        void onClick();
        void onMenuItemClick();
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDay;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            textViewDay = itemView.findViewById(R.id.textViewDay);
        }
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnCreateContextMenuListener{
        TextView textViewTime;
        TextView textViewPlace;

        public ListViewHolder(View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewPlace = itemView.findViewById(R.id.textViewPlace);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {

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
                mItems.get(getAdapterPosition());
//                mCursor.moveToPosition(getAdapterPosition());
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ItemType.TYPE_HEADER: {
                View itemView = inflater.inflate(R.layout.heatspot_header, parent, false);
                return new HeaderViewHolder(itemView);
            }
            case ItemType.TYPE_LIST: {
                View itemView = inflater.inflate(R.layout.heatspot_list, parent, false);
                return new ListViewHolder(itemView);
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case ItemType.TYPE_HEADER: {
                ItemHeader header = (ItemHeader) mItems.get(position);
                HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
                // your logic here
                viewHolder.textViewDay.setText(header.getDay());
                break;
            }
            case ItemType.TYPE_LIST: {
                ItemList list = (ItemList) mItems.get(position);
                ListViewHolder viewHolder = (ListViewHolder) holder;
                // your logic here
                viewHolder.textViewTime.setText(new SimpleDateFormat("HH:mm").format(list.getModelData().getDate()));
                viewHolder.textViewPlace.setText(list.getModelData().getPlace());
                break;
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    @Override
    public int getItemCount() {
//        if (null == mCursor) return 0;
//        return mCursor.getCount();
        return mItems.size();
    }

    public void swapCursor(List<ItemType> itemTypes){
        mItems = itemTypes;
        notifyDataSetChanged();
    }
}
