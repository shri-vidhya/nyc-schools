package com.shri.nycschools.ui.screens;

import static com.shri.nycschools.ui.screens.SchoolDetailsFragment.SCHOOL_DETAILS;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shri.nycschools.R;
import com.shri.nycschools.databinding.FragmentHomeBinding;
import com.shri.nycschools.model.HighSchoolDTO;
import com.shri.nycschools.network.UIResource;
import com.shri.nycschools.ui.home.HomeViewModel;
import com.shri.nycschools.ui.home.HomeViewModelFactory;
import com.shri.nycschools.ui.home.SchoolListAdapter;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HomeFragment extends Fragment implements SchoolListAdapter.ListItemClickListener {

    private FragmentHomeBinding binding;

    private static final String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this,
                        new HomeViewModelFactory()).get(HomeViewModel.class);

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
                // TODO: Provide option to refresh/retry
                Log.d(TAG, listUIResource.getMessage());
                binding.textHome.setText(listUIResource.getMessage());
                break;
        }
    }

    private void displaySchoolList(List<HighSchoolDTO> highSchoolDTOs) {
        RecyclerView recyclerView =  binding.schoolListRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SchoolListAdapter(highSchoolDTOs, this));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onListItemClick(int position, @Nullable HighSchoolDTO highSchoolDTO) {
        // Go to new fragment
        Fragment parent = getParentFragment();
        if(highSchoolDTO != null &&  parent != null) {
            NavController navController = Navigation.findNavController(requireActivity(), parent.getId());
            Bundle bundle = new Bundle();
            bundle.putSerializable(SCHOOL_DETAILS, highSchoolDTO);
            navController.navigate(R.id.navigation_school_details, bundle);
        }
    }
}