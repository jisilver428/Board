package com.example.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
//    SQLiteDatabase sqlliteDB;
    TextView student,teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        student=(TextView)findViewById(R.id.student);
        teacher=(TextView)findViewById(R.id.teacher);

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it=new Intent(LoginActivity.this, LoginProfessor.class);
                startActivity(it);
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(LoginActivity.this, LoginStudent.class);
                startActivity(it);
            }
        });
    }
}
