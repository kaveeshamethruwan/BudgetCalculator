package com.kaviz.budgetcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kaviz.budgetcalculator.Adapters.ToMyAccountAdapter;
import com.kaviz.budgetcalculator.Adapters.UserExpensesAdapter;
import com.kaviz.budgetcalculator.Adapters.WithdrawAdapter;
import com.kaviz.budgetcalculator.Databases.BudgetProfileDatabase;
import com.kaviz.budgetcalculator.Databases.ExpensesDatabase;
import com.kaviz.budgetcalculator.Interfaces.SelectedExpenses;
import com.kaviz.budgetcalculator.Models.BudgetProfile;
import com.kaviz.budgetcalculator.Models.Expenses;
import com.kaviz.budgetcalculator.databinding.ActivityBudgetProfileBinding;
import com.kaviz.budgetcalculator.databinding.EnterUserAmountBinding;
import com.kaviz.budgetcalculator.databinding.EnterUserBudgetBinding;

import java.util.ArrayList;
import java.util.List;

public class BudgetProfileActivity extends AppCompatActivity implements SelectedExpenses {
    private ActivityBudgetProfileBinding binding;
    private int budgetProfileID;
    private List<PieEntry>pieChartDataList;
    private PieDataSet pieDataSet;
    private PieData pieData;
    private Dialog userAmountLayout;
    private EnterUserAmountBinding userAmountBinding;
    private BudgetProfileDatabase budgetProfileDatabase;
    private BudgetProfile budgetProfile;
    private ExpensesDatabase expensesDatabase;
    private List<Expenses>expensesList;
    private UserExpensesAdapter expensesAdapter;

    private List<Expenses>selectedExpenses;
    private List<Expenses>selectedWithdrawList;
    private float totalSpendValue;
    private float totalSavingValue;
    private List<Expenses>toMyAccountList;
    private ToMyAccountAdapter toMyAccountAdapter;
    private List<Expenses>withdrawList;
    private WithdrawAdapter withdrawAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBudgetProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String profileTitle = getIntent().getStringExtra("profileTitle");
        budgetProfileID = Integer.parseInt(getIntent().getStringExtra("ID"));

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(profileTitle);

        }
        budgetProfileDatabase = new BudgetProfileDatabase(this);
        expensesDatabase = new ExpensesDatabase(this);
        userAmountWindow();

        budgetProfile = budgetProfileDatabase.budgetProfileByID(budgetProfileID);
        final String budget = "Budget RS "+budgetProfile.getBudget();
        binding.budgetText.setText(budget);
        selectedExpenses = new ArrayList<>();
        selectedWithdrawList = new ArrayList<>();

        pieChartConfig();
        expensesRecyclerViewConfig();
        updateToMyAccountRecyclerView();
        updateWithdrawRecyclerView();

        binding.selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!selectedExpenses.isEmpty()) {

                    for (Expenses expenses : selectedExpenses) {

                        int status = expensesDatabase.updateExpenseToUser(expenses.getId(), "User");
                        System.out.println(status);
                        System.out.println("ID : "+expenses.getId()+" - Expenses : " + expenses.getDescription() + " - Cost : "+expenses.getAmount());

                    }

                    binding.buttonsLayout.setVisibility(View.GONE);
                    expensesAdapter.setLongPressed(false);
                    expensesAdapter.notifyDataSetChanged();
                    binding.toMyAccountText.setVisibility(View.VISIBLE);
                    updateToMyAccountRecyclerView();
                    selectedExpenses.clear();

                }

            }
        });

        binding.deselectTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.deselectTextButton.setVisibility(View.GONE);
                expensesAdapter.setLongPressed(false);
                expensesAdapter.notifyDataSetChanged();
                selectedExpenses.clear();

            }
        });

        binding.addExpensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                pieChartDataList.add(new PieEntry(500f, "Waiter"));
//                pieDataSet.notifyDataSetChanged();
//                PieData data = new PieData(pieDataSet);
//                binding.pieChart.setData(data);
//                binding.pieChart.invalidate();
                userAmountLayout.show();

            }
        });

        binding.withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!selectedWithdrawList.isEmpty()) {

                    for (Expenses expenses : selectedExpenses) {

                        int status = expensesDatabase.updateExpenseToWithdraw(expenses.getId(), "Withdraw");
                        System.out.println(status);
                        System.out.println("ID : "+expenses.getId()+" - Expenses : " + expenses.getDescription() + " - Cost : "+expenses.getAmount());

                    }

                    binding.buttonsLayout.setVisibility(View.GONE);
                    expensesAdapter.setLongPressed(false);
                    expensesAdapter.notifyDataSetChanged();
                    binding.withdrawText.setVisibility(View.VISIBLE);
                    updateWithdrawRecyclerView();
                    selectedWithdrawList.clear();

                }

            }
        });

//        binding.userBudgetText.addTextChangedListener(new TextWatcher() {
//            private String current = "";
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if (!charSequence.toString().equals(current)) {
//                    binding.userBudgetText.removeTextChangedListener(this);
//
//                    String cleanString = charSequence.toString().replaceAll("[^\\d]", "");
//                    double parsed = Double.parseDouble(cleanString);
//                    String formatted = "";
//
//                    if (cleanString.length() <= 2) {
//                        formatted = "0." + String.format("%02d", (int) parsed);
//                        parsed = Double.parseDouble(formatted);
//                    } else {
//                        String firstPart = cleanString.substring(0, cleanString.length() - 2);
//                        String lastPart = cleanString.substring(cleanString.length() - 2);
//                        formatted = String.format("%s.%s", firstPart, lastPart);
//                        parsed = Double.parseDouble(formatted);
//                    }
//
//                    current = String.format("%.2f", parsed);
//                    binding.userBudgetText.setText(current);
//                    binding.userBudgetText.setSelection(current.length());
//
//                    binding.userBudgetText.addTextChangedListener(this);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

    }

    private void expensesRecyclerViewConfig() {

        expensesList = new ArrayList<>();
        expensesAdapter = new UserExpensesAdapter(this, expensesList, binding.buttonsLayout, this, false, binding.deselectTextButton);
        binding.expensesRecyclerView.setHasFixedSize(true);
        binding.expensesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.expensesRecyclerView.setAdapter(expensesAdapter);

        expensesList.addAll(expensesDatabase.getAllExpensesByProfileID(budgetProfile.getId()));
        System.out.println(expensesList.size());
        if (expensesList.size() > 0) {

            float totalSpendAmount = 0;

            expensesAdapter.notifyDataSetChanged();
            
            pieChartDataList.clear();
            for (Expenses expensesObject : expensesList) {

                pieChartDataList.add(new PieEntry(Float.parseFloat(expensesObject.getAmount()), expensesObject.getDescription()));
                totalSpendAmount = totalSpendAmount + Float.parseFloat(expensesObject.getAmount());

            }

            System.out.println("totalSpendAmount : "+totalSpendAmount);
            String formattedSpendValue = String.format("%.2f", totalSpendAmount);
            System.out.println("formattedSpendValue : " + formattedSpendValue);
            totalSpendValue = Float.parseFloat(formattedSpendValue);
            String totalSpend = "Total Spend RS " + formattedSpendValue;
            binding.totalSpendText.setText(totalSpend);

            String formattedSavingValue;
            float totalSavingValue = Float.parseFloat(budgetProfile.getBudget()) - totalSpendValue;
            if (totalSavingValue % 1 == 0) {
                // If totalSavingValue is a whole number, format it with ".00" without the extra ".00" added by String.format()
                formattedSavingValue = String.format("%.0f", totalSavingValue) + ".00";
            } else {
                // If totalSavingValue is not a whole number, format it normally
                formattedSavingValue = String.format("%.2f", totalSavingValue);
            }
            System.out.println("formattedSavingValue : " + formattedSavingValue);
            totalSavingValue = Float.parseFloat(budgetProfile.getBudget()) - Float.parseFloat(formattedSavingValue);
            String saving = "Savings RS " + (Float.parseFloat(budgetProfile.getBudget()) - totalSavingValue);
            binding.savingsText.setText(saving);

            pieDataSet.notifyDataSetChanged();
            PieData data = new PieData(pieDataSet);
            binding.pieChart.setData(data);
            binding.pieChart.invalidate();

        } else {

            binding.pieChart.setVisibility(View.GONE);
            String totalSpend = "Total Spend RS 0";
            binding.totalSpendText.setText(totalSpend);

            String savings = "Savings RS "+budgetProfile.getBudget();
            binding.savingsText.setText(savings);

        }

    }

    private void userAmountWindow() {

        userAmountLayout = new Dialog(this);
        userAmountLayout.setContentView(R.layout.enter_user_amount);
        userAmountLayout.setCancelable(true);
        userAmountLayout.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.window_background));
        userAmountLayout.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        userAmountBinding = EnterUserAmountBinding.bind(userAmountLayout.findViewById(R.id.rootContainer));

        userAmountBinding.userAmountText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().equals(current)) {
                    userAmountBinding.userAmountText.removeTextChangedListener(this);

                    String cleanString = charSequence.toString().replaceAll("[^\\d]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = "";

                    if (cleanString.length() <= 2) {
                        formatted = "0." + String.format("%02d", (int) parsed);
                        parsed = Double.parseDouble(formatted);
                    } else {
                        String firstPart = cleanString.substring(0, cleanString.length() - 2);
                        String lastPart = cleanString.substring(cleanString.length() - 2);
                        formatted = String.format("%s.%s", firstPart, lastPart);
                        parsed = Double.parseDouble(formatted);
                    }

                    current = String.format("%.2f", parsed);
                    userAmountBinding.userAmountText.setText(current);
                    userAmountBinding.userAmountText.setSelection(current.length());

                    userAmountBinding.userAmountText.addTextChangedListener(this);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userAmountBinding.addAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addAmountButtonProcess();

            }
        });

    }

    private void addAmountButtonProcess() {

        String userDescription = userAmountBinding.descriptionText.getText().toString();
        String userAmount = userAmountBinding.userAmountText.getText().toString();

        if (userDescription.isEmpty()) {

            Toast.makeText(this, "Description Required", Toast.LENGTH_SHORT).show();

        } else if (userAmount.isEmpty()) {

            Toast.makeText(this, "User Amount Required", Toast.LENGTH_SHORT).show();

        } else if (userAmount.equals("0.00")) {

            Toast.makeText(this, "Enter Valid Amount", Toast.LENGTH_SHORT).show();

        } else {

            addAmountToDatabase(userAmount, userDescription);

        }

    }

    private void addAmountToDatabase(String userAmount, String userDescription) {

        Expenses expenses = new Expenses(String.valueOf(budgetProfile.getId()), userDescription, userAmount, "No", "No");
        long status = expensesDatabase.addExpenses(expenses);

        if (status != -1) {

            binding.pieChart.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Amount Added Successfully!", Toast.LENGTH_SHORT).show();
            userAmountLayout.cancel();
            expenses.setId(expensesDatabase.getLastRowId(budgetProfile.getId()));
            System.out.println(expensesDatabase.getLastRowId(budgetProfile.getId()));
            expensesList.add(expenses);
            expensesAdapter.notifyItemInserted(expensesList.size() - 1);
            binding.expensesRecyclerView.scrollToPosition(expensesList.size() -1);

            pieChartDataList.clear();
            for (Expenses expensesObject : expensesList) {

                pieChartDataList.add(new PieEntry(Float.parseFloat(expensesObject.getAmount()), expensesObject.getDescription()));

            }

            pieDataSet.notifyDataSetChanged();
            PieData data = new PieData(pieDataSet);
            binding.pieChart.setData(data);
            binding.pieChart.invalidate();

        } else {

            Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

        }

    }

    private void pieChartConfig() {

        pieChartDataList = new ArrayList<>();
        pieDataSet = new PieDataSet(pieChartDataList, getResources().getString(R.string.expenses));
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(ContextCompat.getColor(this, R.color.white));
        pieDataSet.setLabel(getResources().getString(R.string.expenses));
        pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(12f);
        binding.pieChart.setData(pieData);
        binding.pieChart.invalidate();

    }

    private void updateToMyAccountRecyclerView() {

        toMyAccountList = new ArrayList<>();
        binding.toMyBankRecyclerView.setHasFixedSize(true);
        binding.toMyBankRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        toMyAccountAdapter = new ToMyAccountAdapter(this, toMyAccountList);
        binding.toMyBankRecyclerView.setAdapter(toMyAccountAdapter);

        toMyAccountList.addAll(expensesDatabase.getExpensesByUser(String.valueOf(budgetProfile.getId())));

        if (!toMyAccountList.isEmpty()) {

            binding.toMyAccountText.setVisibility(View.VISIBLE);
            toMyAccountAdapter.notifyDataSetChanged();

            float totalAmount = 0;
            for (Expenses expenses : toMyAccountList) {

                totalAmount = totalAmount + Float.parseFloat(expenses.getAmount());

            }

            String total = "Total : RS "+totalAmount+"/=";
            binding.totalMyAccountAmountText.setText(total);
            binding.totalMyAccountAmountText.setVisibility(View.VISIBLE);

        }

    }

    private void updateWithdrawRecyclerView() {

        withdrawList = new ArrayList<>();
        binding.withdrawRecyclerView.setHasFixedSize(true);
        binding.withdrawRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        withdrawAdapter = new WithdrawAdapter(this, withdrawList);
        binding.withdrawRecyclerView.setAdapter(withdrawAdapter);

        withdrawList.addAll(expensesDatabase.getExpensesByWithdraw(String.valueOf(budgetProfile.getId())));

        if (!withdrawList.isEmpty()) {

            binding.withdrawText.setVisibility(View.VISIBLE);
            withdrawAdapter.notifyDataSetChanged();

            float totalAmount = 0;
            for (Expenses expenses : withdrawList) {

                totalAmount = totalAmount + Float.parseFloat(expenses.getAmount());

            }

            String total = "Total : RS "+totalAmount+"/=";
            binding.totalWithdrawAmountText.setText(total);
            binding.totalWithdrawAmountText.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onAccountSelected(int position, String amount) {

        if (amount == null) {

            selectedExpenses.add(expensesList.get(position));

        } else {

            List<Expenses>tempExpenses = new ArrayList<>();
            tempExpenses.addAll(selectedExpenses);
            selectedExpenses.clear();

            for (Expenses expenses : tempExpenses) {

                if (!expenses.getAmount().equals(amount)) {

                    selectedExpenses.add(expenses);

                }

            }

        }

    }

    @Override
    public void onWithdrawSelected(int position, String amount) {

        if (amount == null) {

            selectedWithdrawList.add(expensesList.get(position));

        } else {

            List<Expenses>tempExpenses = new ArrayList<>();
            tempExpenses.addAll(selectedWithdrawList);
            selectedExpenses.clear();

            for (Expenses expenses : tempExpenses) {

                if (!expenses.getAmount().equals(amount)) {

                    selectedWithdrawList.add(expenses);

                }

            }

        }

    }
}