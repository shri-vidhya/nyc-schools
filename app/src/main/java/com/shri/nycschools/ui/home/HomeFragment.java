package com.shri.nycschools.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

        homeViewModel.getHighSchoolList().observe(getViewLifecycleOwner(), data -> updateView(binding, data));
        return root;
    }
    private void updateView(FragmentHomeBinding homeBinding, UIResource<List<HighSchoolDTO>> listUIResource) {
        switch (listUIResource.getStatus()) {
            case SUCCESS:
                homeBinding.textHome.setVisibility(View.GONE);
                List<HighSchoolDTO> highSchoolDTOs = listUIResource.getData();
                if(highSchoolDTOs != null)
                    homeBinding.textHome.setText(highSchoolDTOs.toString());
                break;
            case LOADING:
                //  The content is not ready
                homeBinding.textHome.setText(R.string.data_loading);
                break;
            case ERROR:
                Log.d(TAG, listUIResource.getMessage());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}