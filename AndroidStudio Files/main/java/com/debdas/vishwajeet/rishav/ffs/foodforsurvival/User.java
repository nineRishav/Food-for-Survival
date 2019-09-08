package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

public class User {
    String email;
    String id;
    public User(){

    }
    public  User(String id,String email){
        this.id=id;
        this.email=email;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}
