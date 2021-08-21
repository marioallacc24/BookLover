package com.booklover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.booklover.ui.FavoritesFragment;
import com.booklover.ui.HomeFragment;
import com.booklover.ui.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {
                Fragment selFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_home:
                        selFragment = new HomeFragment();
                        break;
                    case R.id.nav_favorites:
                        selFragment = new FavoritesFragment();
                        break;
                    case R.id.nav_profile:
                        selFragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selFragment).commit();
                return true;
            };
}