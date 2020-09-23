package com.example.covidtest.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtest.R;
import com.example.covidtest.data.RssFeedModel;

import java.util.ArrayList;

public class RssFeedAdapter extends RecyclerView.Adapter<RssFeedAdapter.FeedModelViewHolder> {

    private ArrayList<RssFeedModel> mRssFeedModels;
    Context mContext;
    private OnFeedListener mOnFeedListener;

    public RssFeedAdapter(Context context, ArrayList<RssFeedModel> rssFeedModels, OnFeedListener onFeedListener) {
        this.mContext = context;
        this.mRssFeedModels = rssFeedModels;
        this.mOnFeedListener = onFeedListener;
    }

    @NonNull
    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v, mOnFeedListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RssFeedModel rssFeedModel = mRssFeedModels.get(position);
        ((TextView) holder.rssFeedView.findViewById(R.id.feedTitle)).setText(rssFeedModel.title);
        ((TextView) holder.rssFeedView.findViewById(R.id.feedDesc)).setText(rssFeedModel.description);
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnFeedListener mOnFeedListener;
        private View rssFeedView;

        public FeedModelViewHolder(View v, OnFeedListener onFeedListener) {
            super(v);
            rssFeedView = v;
            this.mOnFeedListener = onFeedListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e("onClick: ", String.valueOf(getAdapterPosition()));
            mOnFeedListener.onFeedClick(getAdapterPosition());
        }
    }

    public interface OnFeedListener{
        void onFeedClick(int position);
    }
}