package com.example.travelapplication;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapplication.ui.home.UpcomingFragment;

public class TravelDataAdapter extends RecyclerView.Adapter<TravelDataAdapter.ViewHolder> {

    private final UpcomingFragment mContext;
    private final  TripData[] tripData;

    public TravelDataAdapter(UpcomingFragment context, TripData[] tripData){
        mContext = context;
        this.tripData = tripData;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        View itemView =mLayoutInflater.inflate(R.layout.trip_list_item, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final  TripData myTrip = tripData[position];

        holder.travelName.setText(myTrip.getTripName());
        holder.travelTime.setText(myTrip.getTripTime());
        holder.travelDate.setText(myTrip.getTripDate());
//        holder.travelDate.setText(myTrip.getTripDate());
//        holder.travelTime.setText(myTrip.getTripTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.expandableCard.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(holder.mCardView, new AutoTransition());
                    holder.expandableCard.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(holder.mCardView, new AutoTransition());
                    holder.expandableCard.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return tripData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView travelTime ;
        public final TextView travelName;
        public final TextView travelDate;
        public final TextView travelWhen;
        public final CardView mCardView;
        public final LinearLayout expandableCard;

//        public final TextView nameTravel;

        public ViewHolder(View itemView) {
            super(itemView);
            travelTime = itemView.findViewById(R.id.travelTimeText);
            travelDate = itemView.findViewById(R.id.travelDateText);
            travelName = itemView.findViewById(R.id.travelNameText);
            travelWhen = itemView.findViewById(R.id.travelWhenText);
            mCardView = itemView.findViewById(R.id.cardView);
            expandableCard = itemView.findViewById(R.id.expandableView);
//            nameTravel = itemView.findViewById(R.id.edtStartLoc);
        }

    }
}
