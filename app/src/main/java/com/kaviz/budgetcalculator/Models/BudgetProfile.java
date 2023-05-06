package com.kaviz.budgetcalculator.Models;

public class BudgetProfile {

    private int id;
    private String profileTitle;
    private String bankSource;

    private String date;
    private String budget;

    public BudgetProfile() {

    }

    public BudgetProfile(String profileTitle, String bankSource, String date, String budget) {
        this.profileTitle = profileTitle;
        this.bankSource = bankSource;
        this.date = date;
        this.budget = budget;
    }

    public BudgetProfile(int id, String profileTitle, String bankSource, String date, String budget) {
        this.id = id;
        this.profileTitle = profileTitle;
        this.bankSource = bankSource;
        this.date = date;
        this.budget = budget;
    }

    public int getId() {
        return id;
    }

    public String getProfileTitle() {
        return profileTitle;
    }

    public String getBankSource() {
        return bankSource;
    }

    public String getDate() {
        return date;
    }

    public String getBudget() {
        return budget;
    }

    public void setId(int id) {
        this.id = id;
    }
}
