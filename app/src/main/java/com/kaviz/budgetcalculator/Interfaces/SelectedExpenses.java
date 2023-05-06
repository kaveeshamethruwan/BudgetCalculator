package com.kaviz.budgetcalculator.Interfaces;

public interface SelectedExpenses {

    void onAccountSelected(int position, String amount);
    void onWithdrawSelected(int position, String amount);

}
