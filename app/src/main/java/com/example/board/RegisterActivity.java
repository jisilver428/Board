package com.example.board;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.board.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    // 이메일과 비밀번호
    private EditText email;
    private EditText password;
    private EditText name;
    private Button signup;
    private ImageView profile;
    private Uri imageUri;
    private static final int PICK_FROM_ALBUM=10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        profile=(ImageView)findViewById(R.id.signupActivity_imageview_profile) ;
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });


        email=(EditText)findViewById(R.id.et_eamil);
        password=(EditText)findViewById(R.id.et_password);
        name=(EditText)findViewById(R.id.et_name);
        signup=(Button)findViewById(R.id.btn_signUp);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString()==null || name.getText().toString()==null || password.getText().toString()==null){
                    return;
                }
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final String uid=task.getResult().getUser().getUid();
//                        FirebaseStorage.getInstance().getReference().child("userImages")
//                                .child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                                @SuppressWarnings("VisibleForTests")
//                                String imageUri=task.getResult().getUploadSessionUri().toString();
//
//                                UserModel userModel=new UserModel();
//                                userModel.userName=name.getText().toString();
//                                userModel.profileImageUrl=imageUri;
//                                userModel.uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        RegisterActivity.this.finish();
//                                    }
//                                });
//                            }
//                        });
                        FirebaseStorage.getInstance().getReference().child("userImages")
                                .child(uid).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                FirebaseStorage.getInstance().getReference().child("userImages").child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUri=uri.toString();
                                        UserModel userModel=new UserModel();
                                        userModel.userName=name.getText().toString();
                                        userModel.profileImageUrl=imageUri;
                                        userModel.uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);
                                        RegisterActivity.this.finish();

                                    }
                                });
                            }
                        });

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_FROM_ALBUM && resultCode==RESULT_OK){
            profile.setImageURI(data.getData()); //가운데 뷰를 바꿈
            imageUri=data.getData(); //이미지 경로 원본
        }
    }
}
