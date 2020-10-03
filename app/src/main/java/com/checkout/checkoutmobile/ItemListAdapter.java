package com.checkout.checkoutmobile;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.checkout.checkoutmobile.databinding.ItemListBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {
    private List<Item> items;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemListBinding binding;

        public ItemViewHolder(ItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Item item) {
            binding.setItem(item);
        }
    }

    public ItemListAdapter(List<Item> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    @NotNull
    public ItemViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(ItemListBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int i) {
        holder.bind(items.get(i));
    }

}
