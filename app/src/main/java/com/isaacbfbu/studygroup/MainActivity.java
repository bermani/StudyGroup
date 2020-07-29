package com.isaacbfbu.studygroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.isaacbfbu.studygroup.databinding.ActivityMainBinding;
import com.isaacbfbu.studygroup.fragments.AddFragment;
import com.isaacbfbu.studygroup.fragments.CalendarFragment;
import com.isaacbfbu.studygroup.fragments.HomeFragment;
import com.isaacbfbu.studygroup.fragments.ProfileFragment;
import com.isaacbfbu.studygroup.fragments.SearchFragment;
import com.isaacbfbu.studygroup.utils.OnSwipeTouchListener;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FragmentManager fragmentManager;

    HomeFragment homeFragment;
    SearchFragment searchFragment;
    AddFragment addFragment;
    CalendarFragment calendarFragment;
    ProfileFragment profileFragment;

    private MenuItem miProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        // define your fragments here
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        addFragment = new AddFragment();
        calendarFragment = new CalendarFragment();
        profileFragment = new ProfileFragment();


        // handle navigation selection
        binding.bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                fragment = searchFragment;
                                break;
                            case R.id.action_add:
                                fragment = addFragment;
                                break;
                            case R.id.action_calendar:
                                fragment = calendarFragment;
                                break;
                            case R.id.action_profile:
                                fragment = profileFragment;
                                break;
                            default:
                                fragment = homeFragment;
                                break;
                        }
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.flContainer, fragment);
                        transaction.commit();
                        return true;
                    }
                });
        // Set default selection
        binding.layout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                goBack();
            }
        });
        goHome();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        miProgressBar = menu.findItem(R.id.miActionProgress);
        return super.onCreateOptionsMenu(menu);
    }


    public void goBack() {
        fragmentManager.popBackStackImmediate();
    }

    public void goForward(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.flContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void goHome() {
        binding.bottomNavigation.setSelectedItemId(R.id.action_home);
    }

    public void logout() {
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
        Toast.makeText(this, "You have logged out successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // This is the up button
            case android.R.id.home:
                goBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setMyProgressBarVisibility(boolean b) {
        if (miProgressBar != null) {
            miProgressBar.setVisible(b);
        }
    }
}
