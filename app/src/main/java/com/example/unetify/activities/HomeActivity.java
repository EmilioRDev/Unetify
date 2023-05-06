package com.example.unetify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.unetify.R;
import com.example.unetify.fragments.ChatsFragment;
import com.example.unetify.fragments.FiltersFragment;
import com.example.unetify.fragments.HomeFragment;
import com.example.unetify.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        openFragment(new HomeFragment());
        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemHome:
                        openFragment(new HomeFragment());
                        return true;
                    case R.id.itemChats:
                        openFragment(new ChatsFragment());
                        return true;
                    case R.id.itemProfile:
                        openFragment(new ProfileFragment());
                        return true;
                    case R.id.itemFilters:
                        openFragment(new FiltersFragment());
                        return true;
                }
                return false;
            }
        });
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}