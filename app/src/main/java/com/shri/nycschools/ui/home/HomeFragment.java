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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

        final TextView textView = binding.textHome;
        homeViewModel.getHighSchoolList().observe(getViewLifecycleOwner(), data -> updateView(textView, data));
        return root;
    }
    private void updateView(TextView textView, UIResource<List<HighSchoolDTO>> listUIResource) {
        switch (listUIResource.getStatus()) {
            case SUCCESS:
                // Send data to fragment
                List<HighSchoolDTO> highSchoolDTOs = listUIResource.getData();
                if(highSchoolDTOs != null)
                    textView.setText(highSchoolDTOs.toString());
                break;
            case LOADING:
                //  The content is not ready
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