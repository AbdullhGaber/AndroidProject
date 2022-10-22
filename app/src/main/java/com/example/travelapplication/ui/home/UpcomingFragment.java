package com.example.travelapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapplication.Note;
import com.example.travelapplication.R;
import com.example.travelapplication.TravelDataAdapter;
import com.example.travelapplication.Trip;
import com.example.travelapplication.databinding.FragmentUpcomingBinding;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class UpcomingFragment extends Fragment {

    private FragmentUpcomingBinding binding;
    private TextView travelName;
    private List<Trip> mTrips;
    private List<Note> mNotes;
    RecyclerView recyclerView ;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UpcomingViewModel homeViewModel =
                new ViewModelProvider(this).get(UpcomingViewModel.class);


        binding = FragmentUpcomingBinding.inflate(inflater, container, false);

        View root = inflater.inflate(R.layout.fragment_upcoming, container,false);

        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        View view = inflater.inflate(R.layout.activity_new_trip,container,false);
        TextView nem = view.findViewById(R.id.edtStartLoc);
        String name = nem.getText().toString();
        mNotes = new ArrayList<>();
        mTrips = new ArrayList<>();

        mNotes.add( new Note("title 1" , "body 1 "));
        mNotes.add( new Note("title 2" , "body 2 "));

        mTrips.add
        (
          new Trip
          (
                    new GeoPoint(51.33014, 2.00177) ,
                    new GeoPoint(22.33014, 11.00177) ,
                          "myFisrtTrip" , "2/3/2023" , "2:00" ,
                          "one direction", "2/3/2023"
                        ,"5:00" , mNotes
          )
        );

        recyclerView.setAdapter(new TravelDataAdapter(mTrips));
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}