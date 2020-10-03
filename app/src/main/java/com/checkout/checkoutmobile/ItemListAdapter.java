package com.checkout.checkoutmobile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.checkout.checkoutmobile.databinding.ItemListBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {
    private List<Item> cartItems;

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

    public ItemListAdapter(List<Item> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    @Override
    @NotNull
    public ItemViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(ItemListBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int i) {
        holder.bind(cartItems.get(i));
    }

}
