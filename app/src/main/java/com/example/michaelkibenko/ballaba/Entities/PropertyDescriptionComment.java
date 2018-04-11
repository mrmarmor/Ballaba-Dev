package com.example.michaelkibenko.ballaba.Entities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 11/04/2018.
 */

public class PropertyDescriptionComment {
    public String created_at, display;
    public ArrayList<HashMap<String, String>> positive, negative;
    public User user;
    public ArrayList<Reply> reply;

    public PropertyDescriptionComment(){}
    public PropertyDescriptionComment(String created_at, String display, ArrayList<HashMap<String, String>> positive, ArrayList<HashMap<String, String>> negative, User user, ArrayList<Reply> reply) {
        this.created_at = created_at;
        this.display = display;
        this.positive = positive;
        this.negative = negative;
        this.user = user;
        this.reply = reply;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setPositive(ArrayList<HashMap<String, String>> positive) {
        this.positive = positive;
    }

    public void setNegative(ArrayList<HashMap<String, String>> negative) {
        this.negative = negative;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setReply(ArrayList<Reply> reply) {
        this.reply = reply;
    }

    public class User {
        public String id, first_name, last_name, profile_image;

        public User(String id, String first_name, String last_name, String profile_image) {
            this.id = id;
            this.first_name = first_name;
            this.last_name = last_name;
            this.profile_image = profile_image;
        }
    }

    public class Reply {
        public String created_at, content;
        public User user;

        //public Reply newInstance(){ return new Reply(); }
        /*public Reply(String created_at, String content, User user) {
            this.created_at = created_at;
            this.content = content;
            this.user = user;
        }*/

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}
