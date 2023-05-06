package com.kaviz.budgetcalculator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaviz.budgetcalculator.Interfaces.BudgetProfileClickEvents;
import com.kaviz.budgetcalculator.Models.BudgetProfile;
import com.kaviz.budgetcalculator.R;
import com.kaviz.budgetcalculator.databinding.BudgetProfileContainerBinding;

import java.util.List;

public class BudgetProfileAdapter extends RecyclerView.Adapter<BudgetProfileAdapter.BudgetProfileViewHolder> {

    private Context context;
    private List<BudgetProfile>budgetProfileList;
    private BudgetProfileClickEvents clickEvents;

    public BudgetProfileAdapter(Context context, List<BudgetProfile> budgetProfileList, BudgetProfileClickEvents clickEvents) {
        this.context = context;
        this.budgetProfileList = budgetProfileList;
        this.clickEvents = clickEvents;
    }

    @NonNull
    @Override
    public BudgetProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.budget_profile_container, parent, false);
        return new BudgetProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetProfileViewHolder holder, int position) {

        BudgetProfile budgetProfile = budgetProfileList.get(position);

        holder.binding.profileNameText.setText(budgetProfile.getProfileTitle());
        holder.binding.bankNameText.setText(budgetProfile.getBankSource());

        String date = "Date : "+budgetProfile.getDate();
        holder.binding.dateText.setText(date);

        String budget = "Budget : RS "+budgetProfile.getBudget();
        holder.binding.budgetText.setText(budget);

    }

    @Override
    public int getItemCount() {
        return budgetProfileList.size();
    }

    public class BudgetProfileViewHolder extends RecyclerView.ViewHolder {

        public BudgetProfileContainerBinding binding;

        public BudgetProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = BudgetProfileContainerBinding.bind(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickEvents.onBudgetProfileClick(getAdapterPosition());
                }
            });
        }
    }

}
