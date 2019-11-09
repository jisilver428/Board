

package com.example.board;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.board.fragment.ChatFragment;
import com.example.board.fragment.PeopleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

//    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.mainactivity_bottomnavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new ChatFragment()).commit();
                        return true;
                    case R.id.people:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new PeopleFragment()).commit();
                        return true;
                }
                return false;
            }
        });


    }

}
