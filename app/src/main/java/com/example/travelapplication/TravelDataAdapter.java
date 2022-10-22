package com.example.travelapplication;
import android.content.Intent;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TravelDataAdapter extends RecyclerView.Adapter<TravelDataAdapter.ViewHolder> {

   public List<Trip> mTrips;

    public TravelDataAdapter(List<Trip> mTrip){
        this.mTrips = mTrip;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        View itemView =mLayoutInflater.inflate(R.layout.trip_list_item, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final  Trip myTrip = mTrips.get(position);

        holder.travelName.setText(myTrip.getName());
        holder.travelTime.setText(myTrip.getTime());
        holder.travelDate.setText(myTrip.getDate());

        holder.itemView.setOnClickListener(v -> {

            if (holder.expandableCard.getVisibility()==View.GONE){
                TransitionManager.beginDelayedTransition(holder.mCardView, new AutoTransition());

                holder.expandableCard.setVisibility(View.VISIBLE);

            } else {
                TransitionManager.beginDelayedTransition(holder.mCardView, new AutoTransition());

                holder.expandableCard.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public static final String TRIP = "trip";
        public final TextView travelTime ;
        public final TextView travelName;
        public final TextView travelDate;
        public final TextView travelWhen;
        public final TextView travelFrom;
        public final TextView travelTo;
        public final CardView mCardView;
        public final LinearLayout expandableCard;

        public ViewHolder(View itemView) {
            super(itemView);
            travelTime = itemView.findViewById(R.id.travel_time);
            travelDate = itemView.findViewById(R.id.travel_date);
            travelName = itemView.findViewById(R.id.travel_name);
            travelWhen = itemView.findViewById(R.id.travel_when);
            travelFrom = itemView.findViewById(R.id.travel_from);
            travelTo = itemView.findViewById(R.id.travel_to);
            mCardView = itemView.findViewById(R.id.card_view);
            expandableCard = itemView.findViewById(R.id.expandableView);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Trip trip = mTrips.get(position);

            Intent intent = new Intent(view.getContext(),TripDetailsActivity.class);

            intent.putExtra(TRIP,trip);

            view.getContext().startActivity(intent);
        }
    }
}