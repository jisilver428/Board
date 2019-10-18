package com.example.board;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginProfessor extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    private FirebaseAuth firebaseAuth;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email="";
    private String password="";
    Button bt;

    //Typeface fonts1;

    CheckBox remember;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pro);

        remember = (CheckBox)findViewById(R.id.remember);
        bt=(Button)findViewById(R.id.registerButton);
        firebaseAuth=FirebaseAuth.getInstance();
        editTextEmail=findViewById(R.id.email1);
        editTextPassword=findViewById(R.id.password);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(LoginProfessor.this, RegisterActivity.class);
                startActivity(it);
            }
        });


        TextView t4 =(TextView)findViewById(R.id.remember);
       // t4.setTypeface(fonts1);
    }

    public void signIn(View view){
        email=editTextEmail.getText().toString();
        password=editTextPassword.getText().toString();

        if(isValidEmail()&& isValidPasswd()){
            loginUser(email,password);
        }
    }

    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }
    
    private void loginUser(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginProfessor.this,R.string.success_login , Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginProfessor.this,R.string.failed_login,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
