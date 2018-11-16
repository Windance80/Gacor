package com.kingstone.smith.gacor.Gacor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingstone.smith.gacor.R;

import java.util.ArrayList;

public class GacorAdapter extends RecyclerView.Adapter<GacorAdapter.GacorAdapterViewHolder> {

    private ArrayList<Gacor> mGacor;
    private Context mContext;
    private final GacorAdapterOnClickHandler mClickHandler;

    public interface GacorAdapterOnClickHandler {
        void onClick();
    }

    public GacorAdapter(Context context, GacorAdapterOnClickHandler clickHandler, ArrayList<Gacor> list) {
        super();
        mContext = context;
        mClickHandler = clickHandler;
        mGacor = list;
    }

    @NonNull
    @Override
    public GacorAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_gacor, parent, false);

        GacorAdapterViewHolder viewHolder = new GacorAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GacorAdapterViewHolder holder, int position) {
        holder.mTextViewPlace.setText(mGacor.get(position).getPlace());
        holder.mTextViewDistance.setText(mGacor.get(position).getDistance());
        holder.mTextViewTime.setText(mGacor.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mGacor.size();
    }

    public class GacorAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTextViewPlace;
        TextView mTextViewDistance;
        TextView mTextViewTime;

        public GacorAdapterViewHolder(View itemView) {
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
