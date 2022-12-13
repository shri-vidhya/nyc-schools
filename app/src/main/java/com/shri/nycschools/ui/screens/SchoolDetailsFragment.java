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
        binding.description.setText(highSchoolDTO.getOverview_paragraph());
        binding.phoneNumber.setText(highSchoolDTO.getPhone_number());
        // TODO: Make address clickable
        binding.address.setText(highSchoolDTO.getLocation());

        if(highSchoolDTO.getInternational() == 1) {
            binding.international.propertyText.setText(getString(R.string.international));
        }
        if(highSchoolDTO.getPtech() == 1) {
            binding.highSchool.propertyText.setText(getString(R.string.high_school));
        }
    }

    private void loadSATScores(UIResource<SATScoresDTO> listUIResource) {
        switch (listUIResource.getStatus()) {
            case SUCCESS:
                // Data fetch was successful. Updating SAT scores in school details screen
                SATScoresDTO satScoresDTO = listUIResource.getData();
                if(satScoresDTO != null) {
                    binding.numOfSatTestTakersValue.setText(satScoresDTO.getNum_of_sat_test_takers());
                    binding.satMathAvgScore.setText(satScoresDTO.getSat_math_avg_score());
                    binding.satCriticalReadingAvgScore.setText(satScoresDTO.getSat_critical_reading_avg_score());
                    binding.satWritingAvgScore.setText(satScoresDTO.getSat_writing_avg_score());

                    binding.satError.setVisibility(View.GONE);
                    binding.satTotal.setVisibility(View.VISIBLE);
                    binding.satReading.setVisibility(View.VISIBLE);
                    binding.satMath.setVisibility(View.VISIBLE);
                    binding.satWriting.setVisibility(View.VISIBLE);
                } else {
                    binding.satError.setText(R.string.no_sat_score);
                    binding.satError.setVisibility(View.VISIBLE);
                }
                break;
            case LOADING:
                //  The content is not ready yet. Showing Loading indicator
                binding.satError.setText(R.string.data_loading);
                binding.satError.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                // Fetch failed with some error. Display the error message to user.
                Log.d(TAG, listUIResource.getMessage());
                binding.satError.setText(listUIResource.getStatus().toString());
                binding.satError.setVisibility(View.VISIBLE);
                break;
        }
    }
}
