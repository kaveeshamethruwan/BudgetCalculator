package com.kaviz.budgetcalculator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaviz.budgetcalculator.Models.Expenses;
import com.kaviz.budgetcalculator.R;
import com.kaviz.budgetcalculator.databinding.AmountContainerBinding;

import java.util.List;

public class ToMyAccountAdapter extends RecyclerView.Adapter<ToMyAccountAdapter.ToMyAccountViewHolder> {

    private Context context;
    private List<Expenses>expensesList;

    public ToMyAccountAdapter(Context context, List<Expenses> expensesList) {
        this.context = context;
        this.expensesList = expensesList;
    }

    @NonNull
    @Override
    public ToMyAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ToMyAccountViewHolder(LayoutInflater.from(context).inflate(R.layout.amount_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ToMyAccountViewHolder holder, int position) {

        Expenses expenses = expensesList.get(position);

        holder.binding.descriptionText.setText(expenses.getDescription());
        String amount = "RS "+expenses.getAmount();
        holder.binding.amountText.setText(amount);

    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public class ToMyAccountViewHolder extends RecyclerView.ViewHolder {

        public AmountContainerBinding binding;

        public ToMyAccountViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = AmountContainerBinding.bind(itemView);

        }
    }

}
