package com.example.shadrak.expensestracker;

public class User {

    public String Name;
    public String Email;
    public String Userid;
    public int PreviousId;

    public User() {

    }

    public User(String uid, String name, String email, int previousId) {
        this.Name = name;
        this.Email = email;
        this.Userid = uid;
        this.PreviousId = previousId;
    }

    public int getPreviousId() { return PreviousId; }

}
