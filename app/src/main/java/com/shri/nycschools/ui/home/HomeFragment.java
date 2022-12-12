package com.shri.nycschools.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shri.nycschools.R;
import com.shri.nycschools.databinding.FragmentHomeBinding;
import com.shri.nycschools.model.HighSchoolDTO;
import com.shri.nycschools.network.UIResource;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private static final String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this, new HomeViewModelFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getHighSchoolList().observe(getViewLifecycleOwner(), this::loadData);
        return root;
    }
    private void loadData(UIResource<List<HighSchoolDTO>> listUIResource) {
        switch (listUIResource.getStatus()) {
            case SUCCESS:
                // Data fetch was successful. Updating Home screen with list of schools
                binding.textHome.setVisibility(View.GONE);
                List<HighSchoolDTO> highSchoolDTOs = listUIResource.getData();
                int size = highSchoolDTOs == null? 0: highSchoolDTOs.size();
                binding.totalResultsText.setText(getString(R.string.total_results, size));
                if(size != 0) {
                    displaySchoolList(highSchoolDTOs);
                }
                break;
            case LOADING:
                //  The content is not ready yet. Showing Loading indicator
                // TODO: Update loading indicator with better UI
                binding.textHome.setText(R.string.data_loading);
                break;
            case ERROR:
                // Fetch failed with some error. Display the error message to user.
                // TODO: Store data so that it can be fetched from DB for offline mode
                Log.d(TAG, listUIResource.getMessage());
                binding.textHome.setText(listUIResource.getMessage());
                break;
        }
    }

    private void displaySchoolList(List<HighSchoolDTO> highSchoolDTOs) {
        RecyclerView recyclerView =  binding.schoolListRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SchoolListAdapter(highSchoolDTOs));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}