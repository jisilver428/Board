package com.example.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.regex.Pattern;

public class LoginProfessor extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email="";
    private String password="";
    Button bt;
    TextView signInbt;
    ImageView googlebt;
    //Typeface fonts1;

    CheckBox remember;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pro);

        remember = (CheckBox)findViewById(R.id.remember);
        bt=(Button)findViewById(R.id.registerButton);
        googlebt=(ImageView)findViewById(R.id.google);
        signInbt=(TextView) findViewById(R.id.signin1);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        editTextEmail=(EditText)findViewById(R.id.email1);
        editTextPassword=(EditText)findViewById(R.id.password);



        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(LoginProfessor.this, RegisterActivity.class);
                startActivity(it);
            }
        });
        signInbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                email=editTextEmail.getText().toString();
//                password=editTextPassword.getText().toString();
//
//                if(isValidEmail()&& isValidPasswd()){
//                    loginUser(email,password);
//                }
                loginEvent();
            }
        });

        googlebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itent=new Intent(LoginProfessor.this,SignInActivity.class);
                startActivity(itent);
            }
        });
        TextView t4 =(TextView)findViewById(R.id.remember);
       // t4.setTypeface(fonts1);

        //로그인 인터페이스 리스너
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user !=null){
                    //login
                    Intent intent=new Intent(LoginProfessor.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    //logout
                }
            }
        };
    }


    void loginEvent(){
        firebaseAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(),editTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    //login fail
                    Toast.makeText(LoginProfessor.this, task.getException().getMessage(),Toast.LENGTH_SHORT);
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
