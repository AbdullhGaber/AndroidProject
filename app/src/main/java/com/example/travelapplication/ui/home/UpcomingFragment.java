package com.example.travelapplication.ui.home;

import android.annotation.SuppressLint;
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

import com.example.travelapplication.R;
import com.example.travelapplication.TravelDataAdapter;
import com.example.travelapplication.TripData;
import com.example.travelapplication.databinding.FragmentUpcomingBinding;

public class UpcomingFragment extends Fragment {

    private FragmentUpcomingBinding binding;
    private TextView travelName;
    private TripData[] trip;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UpcomingViewModel homeViewModel =
                new ViewModelProvider(this).get(UpcomingViewModel.class);


//        binding = FragmentUpcomingBinding.inflate(inflater, container, false);

        View root = inflater.inflate(R.layout.fragment_upcoming, container,false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        View view = inflater.inflate(R.layout.activity_new_trip,container,false);
        TextView nem = view.findViewById(R.id.edtStartLoc);
        String name = nem.getText().toString();

        TripData[] trips = new TripData[]{
                new TripData(name,name,name,name,name),
                new TripData("f","df","d","x","r")

        };

        recyclerView.setAdapter(new TravelDataAdapter(this,trips));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}