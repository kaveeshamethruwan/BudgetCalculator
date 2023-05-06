package com.kaviz.budgetcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.kaviz.budgetcalculator.Adapters.BudgetProfileAdapter;
import com.kaviz.budgetcalculator.Databases.BudgetProfileDatabase;
import com.kaviz.budgetcalculator.Interfaces.BudgetProfileClickEvents;
import com.kaviz.budgetcalculator.Models.BudgetProfile;
import com.kaviz.budgetcalculator.databinding.ActivityHomeBinding;
import com.kaviz.budgetcalculator.databinding.EnterUserBudgetBinding;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements BudgetProfileClickEvents {
    private ActivityHomeBinding binding;
    private Dialog addProfileLayout;
    private EnterUserBudgetBinding enterUserBudgetBinding;
    private String date;
    private BudgetProfileDatabase budgetProfileDatabase;
    private List<BudgetProfile>budgetProfileList;
    private BudgetProfileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addProfileBudgetWindow();
        budgetProfileDatabase = new BudgetProfileDatabase(this);
        initializeRecyclerView();

        binding.addProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addProfileLayout.show();

            }
        });

    }

    private void initializeRecyclerView() {

        budgetProfileList = new ArrayList<>();
        adapter = new BudgetProfileAdapter(this, budgetProfileList, this);

        binding.budgetProfileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.budgetProfileRecyclerView.setHasFixedSize(true);
        binding.budgetProfileRecyclerView.setAdapter(adapter);

        //get data from database
        budgetProfileList.addAll(budgetProfileDatabase.getAllBudgetProfiles(this));
        adapter.notifyDataSetChanged();

    }

    private void addProfileBudgetWindow() {

        addProfileLayout = new Dialog(this);
        addProfileLayout.setContentView(R.layout.enter_user_budget);
        addProfileLayout.setCancelable(true);
        addProfileLayout.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.window_background));

        addProfileLayout.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        enterUserBudgetBinding = EnterUserBudgetBinding.bind(addProfileLayout.findViewById(R.id.enterUserBudgetRoot));

        enterUserBudgetBinding.userBudgetText.addTextChangedListener(new TextWatcher() {

            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().equals(current)) {
                    enterUserBudgetBinding.userBudgetText.removeTextChangedListener(this);

                    String cleanString = charSequence.toString().replaceAll("[^\\d]", "");

                    if (!cleanString.equals("")) {

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
                        enterUserBudgetBinding.userBudgetText.setText(current);
                        enterUserBudgetBinding.userBudgetText.setSelection(current.length());

                        enterUserBudgetBinding.userBudgetText.addTextChangedListener(this);

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        enterUserBudgetBinding.selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectDateButtonProcess();

            }
        });

        enterUserBudgetBinding.addProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addProfileButtonProcess();

            }
        });

    }

    private void addProfileButtonProcess() {

        String profileTitle = enterUserBudgetBinding.profileTitleText.getText().toString();
        String bankSource = enterUserBudgetBinding.bankSourceText.getText().toString();
        String budget = enterUserBudgetBinding.userBudgetText.getText().toString();

        if (profileTitle.isEmpty()) {

            Toast.makeText(this, "Profile Title Required", Toast.LENGTH_SHORT).show();

        } else if (bankSource.isEmpty()) {

            Toast.makeText(this, "Bank Source Required", Toast.LENGTH_SHORT).show();

        } else if (budget.isEmpty()) {

            Toast.makeText(this, "Your Budget Required", Toast.LENGTH_SHORT).show();

        } else if (date == null) {

            Toast.makeText(this, "Date Required", Toast.LENGTH_SHORT).show();

        } else if (budget.equals("0.00")) {

            Toast.makeText(this, "Enter Valid Budget", Toast.LENGTH_SHORT).show();

        } else {

            addBudgetToDatabase(new BudgetProfile(profileTitle, bankSource, date ,budget));

        }

    }

    private void addBudgetToDatabase(BudgetProfile budgetProfile) {

        long status = budgetProfileDatabase.addBudgetProfile(budgetProfile);

        if (status != -1) {

            Snackbar.make(binding.getRoot(), "Profile Added Successfully!", Snackbar.LENGTH_SHORT).show();
            addProfileLayout.dismiss();
            enterUserBudgetBinding.profileTitleText.getText().clear();
            enterUserBudgetBinding.bankSourceText.getText().clear();
            enterUserBudgetBinding.userBudgetText.setText(getResources().getString(R.string._0_00));

            budgetProfile.setId(budgetProfileDatabase.getLastID());
            budgetProfileList.add(budgetProfile);
            adapter.notifyItemInserted(budgetProfileList.size()-1);

        } else {

            Snackbar.make(binding.getRoot(), "Something Went Wrong!", Snackbar.LENGTH_SHORT).show();

        }

    }

    private void selectDateButtonProcess() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // get selected date
                date = day+" - "+new DateFormatSymbols().getMonths()[month]+" - "+year;
                System.out.println(date);

            }
        }, year, month, day);

        datePickerDialog.show();

    }

    @Override
    public void onBudgetProfileClick(int position) {

        Intent intent = new Intent(this, BudgetProfileActivity.class);
        intent.putExtra("ID", String.valueOf(budgetProfileList.get(position).getId()));
        intent.putExtra("profileTitle", budgetProfileList.get(position).getProfileTitle());
        startActivity(intent);

    }
}