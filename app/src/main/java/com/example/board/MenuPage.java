package com.example.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPage extends AppCompatActivity{
    TextView chat, bo;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        chat=(TextView)findViewById(R.id.chat);
        bo=(TextView)findViewById(R.id.bo);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(MenuPage.this,SignInActivity.class);
            }
        });


    }
}
