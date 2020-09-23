package com.example.covidtest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtest.OnboardingItem;
import com.example.covidtest.R;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>{

    private List<OnboardingItem> list;

    public OnboardingAdapter (List<OnboardingItem> onboardingItemList) {
        this.list = onboardingItemList;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_onboarding, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnboardingData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mTitle;
        private TextView mDescription;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.imageOnboarding);
            mTitle = itemView.findViewById(R.id.titleOnboarding);
            mDescription = itemView.findViewById(R.id.descriptionOnboarding);
        }

        void setOnboardingData (OnboardingItem onboardingItem) {
            mTitle.setText(onboardingItem.getTitle());
            mDescription.setText(onboardingItem.getDescription());
            mImage.setImageResource(onboardingItem.getImage());
        }
    }

}
