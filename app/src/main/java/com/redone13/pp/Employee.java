package com.redone13.pp;

public class Employee {
    private int mId;
    private String mLastName;
    private String mFirstName;
    private String mEmail;
    private String mPassword;

    public Employee() {

    }

    public Employee(String lastName, String firstName, String email, String password) {
        mLastName = lastName;
        mFirstName = firstName;
        mEmail = email;
        mPassword = password;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
