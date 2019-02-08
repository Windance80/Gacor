package com.kingstone.smith.gacor.Gacor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingstone.smith.gacor.ItemType;
import com.kingstone.smith.gacor.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GacorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemType> mItems = Collections.emptyList();
    private Context mContext;
    private final GacorAdapterOnClickHandler mClickHandler;

    public interface GacorAdapterOnClickHandler {
        void onClick();
    }

    public GacorAdapter(Context context, GacorAdapterOnClickHandler clickHandler, List<ItemType> list) {
        super();
        mContext = context;
        mClickHandler = clickHandler;
        mItems = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case 0: {// header
                View view = inflater.inflate(R.layout.gacor_header, parent, false);
                return new HeaderViewHolder(view);
            }
            case 1: {// list
                View view = inflater.inflate(R.layout.gacor_list, parent, false);
                return new ListViewHolder(view);
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
                GacorHeader header = (GacorHeader) mItems.get(position);
                HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
                viewHolder.textViewHeader.setText(header.getHeader());
                break;
            }
            case ItemType.TYPE_LIST: {
                GacorList list = (GacorList) mItems.get(position);
                ListViewHolder viewHolder = (ListViewHolder) holder;
                viewHolder.mTextViewPlace.setText(list.getGacor().getPlace());
                viewHolder.mTextViewDistance.setText(list.getGacor().getDistance());
                viewHolder.mTextViewTime.setText(list.getGacor().getTime());
                break;
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHeader;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            textViewHeader = itemView.findViewById(R.id.textViewHeader);
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTextViewPlace;
        TextView mTextViewDistance;
        TextView mTextViewTime;

        public ListViewHolder(View itemView) {
            super(itemView);
            mTextViewPlace = itemView.findViewById(R.id.textViewPlace);
            mTextViewDistance = itemView.findViewById(R.id.textViewDistance);
            mTextViewTime = itemView.findViewById(R.id.textViewTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick();
        }
    }
}
