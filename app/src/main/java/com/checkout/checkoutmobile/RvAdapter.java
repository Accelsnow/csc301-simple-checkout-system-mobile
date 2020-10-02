package com.checkout.checkoutmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvViewHolder> {
    private List<Item> items;

    public static class RvViewHolder extends RecyclerView.ViewHolder {
        CardView itemCardView;
        TextView itemName;
        TextView stock;
        TextView orgUnitPrice;
        TextView itemDiscount;
        TextView discountedUnitPrice;
        TextView itemTotalPrice;
        EditText quantity;

        public RvViewHolder(View v) {
            super(v);
            itemCardView = itemView.findViewById(R.id.itemCardView);
            itemName = itemView.findViewById(R.id.itemName);
            stock = itemView.findViewById(R.id.stock);
            orgUnitPrice = itemView.findViewById(R.id.orgUnitPrice);
            itemDiscount = itemView.findViewById(R.id.itemDiscount);
            discountedUnitPrice = itemView.findViewById(R.id.discountedUnitPrice);
            itemTotalPrice = itemView.findViewById(R.id.itemTotalPrice);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }

    public RvAdapter(List<Item> items) {
        this.items = items;
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    @Override
    @NotNull
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_layout, parent, false);
        RvViewHolder vh = new RvViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int i) {
        holder.itemName.setText(items.get(i).getNameText());
        holder.itemTotalPrice.setText(items.get(i).getTotalPriceText());
        holder.quantity.setText(items.get(i).getQuantityText());
        holder.discountedUnitPrice.setText(items.get(i).getDiscountedPriceText());
        holder.orgUnitPrice.setText(items.get(i).getOriginalPriceText());
        holder.stock.setText(items.get(i).getStockText());
        holder.itemDiscount.setText(items.get(i).getItemDiscountText());
    }

    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
