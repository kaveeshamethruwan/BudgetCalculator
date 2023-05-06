package com.kaviz.budgetcalculator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaviz.budgetcalculator.Interfaces.SelectedExpenses;
import com.kaviz.budgetcalculator.Models.Expenses;
import com.kaviz.budgetcalculator.R;
import com.kaviz.budgetcalculator.databinding.AmountContainerBinding;

import java.util.ArrayList;
import java.util.List;

public class UserExpensesAdapter extends RecyclerView.Adapter<UserExpensesAdapter.UserExpensesViewHolder> {

    private Context context;
    private List<Expenses>expensesList;
    private LinearLayout buttonsLayout;
    private SelectedExpenses selectedExpenses;
    private boolean isLongPressed;
    private TextView deselectTextButton;

    public void setLongPressed(boolean longPressed) {
        isLongPressed = longPressed;
    }

    public UserExpensesAdapter(Context context, List<Expenses> expensesList, LinearLayout buttonsLayout, SelectedExpenses selectedExpenses, boolean isLongPressed, TextView deselectTextButton) {
        this.context = context;
        this.expensesList = expensesList;
        this.buttonsLayout = buttonsLayout;
        this.selectedExpenses = selectedExpenses;
        this.isLongPressed = isLongPressed;
        this.deselectTextButton = deselectTextButton;
    }

    @NonNull
    @Override
    public UserExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.amount_container, parent, false);
        return new UserExpensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserExpensesViewHolder holder, int position) {

        Expenses expenses = expensesList.get(position);

        String amount = "RS "+expenses.getAmount();
        holder.binding.amountText.setText(amount);
        holder.binding.descriptionText.setText(expenses.getDescription());

        if (isLongPressed) {

            holder.binding.checkBox.setVisibility(View.VISIBLE);
            buttonsLayout.setVisibility(View.VISIBLE);

        } else {

            holder.binding.checkBox.setVisibility(View.GONE);
            buttonsLayout.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public class UserExpensesViewHolder extends RecyclerView.ViewHolder {

        public AmountContainerBinding binding;

        public UserExpensesViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = AmountContainerBinding.bind(itemView);

            binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b) {

                        selectedExpenses.onAccountSelected(getAdapterPosition(), null);
                        selectedExpenses.onWithdrawSelected(getAdapterPosition(), null);

                    } else {

                        selectedExpenses.onAccountSelected(getAdapterPosition(), expensesList.get(getAdapterPosition()).getAmount());
                        selectedExpenses.onWithdrawSelected(getAdapterPosition(), expensesList.get(getAdapterPosition()).getAmount());

                    }

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    isLongPressed = true;
                    notifyDataSetChanged();
                    deselectTextButton.setVisibility(View.VISIBLE);
                    return true;
                }
            });

        }
    }

}
