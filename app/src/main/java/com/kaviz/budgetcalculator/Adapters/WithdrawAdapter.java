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

public class WithdrawAdapter extends RecyclerView.Adapter<WithdrawAdapter.WithdrawViewHolder> {

    private Context context;
    private List<Expenses>withdrawList;

    public WithdrawAdapter(Context context, List<Expenses> withdrawList) {
        this.context = context;
        this.withdrawList = withdrawList;
    }

    @NonNull
    @Override
    public WithdrawViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WithdrawViewHolder(LayoutInflater.from(context).inflate(R.layout.amount_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawViewHolder holder, int position) {

        Expenses expenses = withdrawList.get(position);
        holder.binding.descriptionText.setText(expenses.getDescription());
        String amount = "RS "+expenses.getAmount();
        holder.binding.amountText.setText(amount);

    }

    @Override
    public int getItemCount() {
        return withdrawList.size();
    }

    public class WithdrawViewHolder extends RecyclerView.ViewHolder {
        public AmountContainerBinding binding;

        public WithdrawViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = AmountContainerBinding.bind(itemView);

        }
    }

}
