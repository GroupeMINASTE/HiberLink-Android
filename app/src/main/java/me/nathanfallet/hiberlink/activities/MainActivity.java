package me.nathanfallet.hiberlink.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import me.nathanfallet.hiberlink.R;
import me.nathanfallet.hiberlink.fragments.HistoryFragment;
import me.nathanfallet.hiberlink.fragments.UploadFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActionBar toolbar;
    private UploadFragment uploadFragment = new UploadFragment();
    private HistoryFragment historyFragment = new HistoryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        toolbar.setTitle(R.string.upload_title);
        loadFragment(uploadFragment);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_upload:
                toolbar.setTitle(R.string.upload_title);
                loadFragment(uploadFragment);
                return true;
            case R.id.navigation_history:
                toolbar.setTitle(R.string.history_title);
                loadFragment(historyFragment);
                return true;
        }
        return false;
    }

}
