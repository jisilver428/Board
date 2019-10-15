package com.example.board;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginProfessor extends AppCompatActivity {


    //Typeface fonts1;
    CheckBox remember;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pro);

        remember = (CheckBox)findViewById(R.id.remember);


        TextView t4 =(TextView)findViewById(R.id.remember);
       // t4.setTypeface(fonts1);
    }
}
