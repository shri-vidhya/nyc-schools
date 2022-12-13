package com.shri.nycschools.ui.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.shri.nycschools.R;
import com.shri.nycschools.databinding.SchoolDetailsBinding;
import com.shri.nycschools.model.HighSchoolDTO;
import com.shri.nycschools.model.SATScoresDTO;
import com.shri.nycschools.network.UIResource;
import com.shri.nycschools.ui.home.HomeViewModel;
import com.shri.nycschools.ui.home.HomeViewModelFactory;

public class SchoolDetailsFragment extends Fragment {
    private SchoolDetailsBinding binding;
    public static final String SCHOOL_DETAILS = "school_details";

    private static final String TAG = "SchoolDetailsFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this,
                        new HomeViewModelFactory()).get(HomeViewModel.class);

        binding = SchoolDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        HighSchoolDTO highSchoolDTO = null;
        if(getArguments() != null) {
            highSchoolDTO = (HighSchoolDTO) getArguments().getSerializable(SCHOOL_DETAILS);
        }
        if(highSchoolDTO != null) {
            loadData(highSchoolDTO);
            homeViewModel.getSATScores(highSchoolDTO.getDbn()).observe(getViewLifecycleOwner(), this::loadSATScores);
        }
        return root;
    }

    private void loadData(HighSchoolDTO highSchoolDTO) {
        binding.schoolName.setText(highSchoolDTO.getSchool_name());
    }

    private void loadSATScores(UIResource<SATScoresDTO> listUIResource) {
        switch (listUIResource.getStatus()) {
            case SUCCESS:
                // Data fetch was successful. Updating SAT scores in school details screen
                SATScoresDTO satScoresDTO = listUIResource.getData();
                if(satScoresDTO != null)
                    binding.satScore.setText(satScoresDTO.getNum_of_sat_test_takers());
                break;
            case LOADING:
                //  The content is not ready yet. Showing Loading indicator
                binding.satScore.setText(R.string.data_loading);
                break;
            case ERROR:
                // Fetch failed with some error. Display the error message to user.
                Log.d(TAG, listUIResource.getMessage());
                binding.satScore.setText(listUIResource.getMessage());
                break;
        }
    }
}
