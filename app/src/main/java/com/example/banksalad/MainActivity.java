package com.example.banksalad;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.banksalad.databinding.CalendarListBinding;
import com.example.banksalad.ui.adapter.CalendarAdapter;
import com.example.banksalad.ui.viewmodel.CalendarListViewModel;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Menu menu;

    private CalendarListBinding binding;
    private CalendarAdapter calendarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setVariable(BR.model, new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(CalendarListViewModel.class));
        binding.setLifecycleOwner(this);
        binding.getModel().initCalendarList();

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        calendarAdapter = new CalendarAdapter();
        binding.calendar.setLayoutManager(manager);
        binding.calendar.setAdapter(calendarAdapter);
        observe();
    }

    private void observe() {
        binding.getModel().mCalendarList.observe(this, new Observer<ArrayList<Object>>() {
            @Override
            public void onChanged(ArrayList<Object> objects) {
                calendarAdapter.submitList(objects);
                if (binding.getModel().mCenterPosition > 0) {
                    binding.calendar.scrollToPosition(binding.getModel().mCenterPosition);
                }
            }
        });
    }
}

