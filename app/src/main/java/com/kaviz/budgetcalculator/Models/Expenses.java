package com.kaviz.budgetcalculator.Models;

public class Expenses {

    private int id;
    private String profileID;
    private String description;
    private String amount;
    private String other;

    private String withdraw;

    public Expenses() {

    }

    public Expenses(String profileID, String description, String amount, String other, String withdraw) {
        this.profileID = profileID;
        this.description = description;
        this.amount = amount;
        this.other = other;
        this.withdraw = withdraw;
    }

    public Expenses(int id, String profileID, String description, String amount, String other, String withdraw) {
        this.id = id;
        this.profileID = profileID;
        this.description = description;
        this.amount = amount;
        this.other = other;
        this.withdraw = withdraw;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(String withdraw) {
        this.withdraw = withdraw;
    }
}
