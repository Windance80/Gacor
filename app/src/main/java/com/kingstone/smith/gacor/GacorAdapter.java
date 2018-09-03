package com.kingstone.smith.gacor;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        holder.mTextViewPlace.setText(mGacor.get(position).getmPlace());
        holder.mTextViewDistance.setText(mGacor.get(position).getmDistance());
    }

    @Override
    public int getItemCount() {
        return mGacor.size();
    }

    public class GacorAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTextViewPlace;
        TextView mTextViewDistance;

        public GacorAdapterViewHolder(View itemView) {
            super(itemView);
            mTextViewPlace = itemView.findViewById(R.id.textViewPlace);
            mTextViewDistance = itemView.findViewById(R.id.textViewDistance);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick();
        }
    }
}
