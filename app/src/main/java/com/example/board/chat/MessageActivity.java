package com.example.board.chat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.board.R;
import com.example.board.model.ChatModel;
import com.example.board.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private String destinationUid;
    private Button button;
    private EditText editText;

    private String uid;
    private  String chatRoomUid;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid(); //채팅 요구하는 아이디(로그인된아이디)
        destinationUid=getIntent().getStringExtra("destinationUid"); //채팅 당하는 아이디
        button=(Button)findViewById(R.id.messageActi_button);
        editText=(EditText)findViewById(R.id.messageActi_editText);

        recyclerView=(RecyclerView)findViewById(R.id.messageActi_reciclerview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(uid, true);
                chatModel.users.put(destinationUid, true);

                if (chatRoomUid == null) {
                    button.setEnabled(true);
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }
                    });

                }else{
                    ChatModel.Comment comment=new ChatModel.Comment();
                    comment.uid=uid;
                    comment.message=editText.getText().toString(); //메세지 보내는 부분
                    //firebase 메시지 보내는부분(통신)
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            editText.setText("");
                        }
                    });

                }
            }
        });

        checkChatRoom();
    }

    void checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    ChatModel chatModel=item.getValue(ChatModel.class);
                        if(chatModel.users.containsKey(destinationUid)){
                            chatRoomUid=item.getKey();
                            button.setEnabled(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                            recyclerView.setAdapter(new RecycleViewAdapter());
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class RecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<ChatModel.Comment> comments;
        UserModel userModel;
        public RecycleViewAdapter(){
            comments=new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userModel=dataSnapshot.getValue(UserModel.class);

                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        //메시지 받는부분(통신)
        void getMessageList(){
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear();

                    for(DataSnapshot item: dataSnapshot.getChildren()){
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }
                    //메세지가 갱신
                    notifyDataSetChanged();

                    recyclerView.scrollToPosition(comments.size()-1); //맨 마지막 말풍선으로 이동
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent,false);


            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            MessageViewHolder messageViewHolder=((MessageViewHolder)holder);
            //내가 보낸 메세지
            if(comments.get(position).uid.equals(uid)){
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.textView_message.setTextSize(25);
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
            //상대방이 보낸 메세지
            }else{

                Glide.with(holder.itemView.getContext())
                        .load(userModel.profileImageUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.imageView_profile);

                messageViewHolder.textView_name.setText(userModel.userName);
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setTextSize(25);
                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);

            }

        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView textView_message;
            public TextView textView_name;
            public ImageView imageView_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;

            public MessageViewHolder(View view) {
                super(view);
                textView_message=(TextView)view.findViewById(R.id.messageItem_textView_message);
                textView_name=(TextView)view.findViewById(R.id.messageItem_textView_name);
                imageView_profile=(ImageView)view.findViewById(R.id.messageItem_imageview_profile);
                linearLayout_destination=(LinearLayout)view.findViewById(R.id.messageItem_linearlayout_destination);
                linearLayout_main=(LinearLayout)view.findViewById(R.id.messageItem_linearlayout_main);

            }
        }
    }

//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//        finish();
//        overridePendingTransition(R.anim.fromleft, R.anim.toright);
//    }
}
