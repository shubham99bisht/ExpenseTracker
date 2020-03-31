package com.example.shadrak.expensestracker;

public class User {

    public String Name;
    public String Email;
    public String Userid;
    public String Prev_id;

    public User() {

    }

    public User(String uid, String name, String email, String previousId) {
        this.Name = name;
        this.Email = email;
        this.Userid = uid;
        this.Prev_id = previousId;
    }

    public String getPreviousId() { return Prev_id; }

}
