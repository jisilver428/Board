package com.example.board.model;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {
    public Map <String, Boolean> users=new HashMap<>(); //채팅방 유저
    public Map <String, Comment> comments=new HashMap<>();
    public static class Comment{
        public String message;
        public String uid;
//        String uid;
//        String message;
    }
}
