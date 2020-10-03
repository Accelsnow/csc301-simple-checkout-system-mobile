package com.checkout.checkoutmobile;

import android.os.Bundle;


import com.checkout.checkoutmobile.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.*;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ItemListAdapter itemAdapter;
    private List<Item> cartItems;
    private List<Item> allItems;
    private ActivityMainBinding binding;
    private final Locale FORMAT_LOCALE = Locale.CANADA;

    private void initializeData() {
        cartItems = new ArrayList<>();
        cartItems.add(new Item(1, "cokesadfsadfdsafasdfsdfsdafads", 99, 1.00, 0.0, 1));
        cartItems.add(new Item(2, "sprite", 4, 2.453, 0.0, 2));
        cartItems.add(new Item(3, "fries", 1, 2.0, 0.5, 1));
        cartItems.add(new Item(4, "candy", 2, 100000.0, 0.1, 1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getAllItems();
        initializeData();
        setAdapter();
        setListener();
    }

    private void getAllItems(){
        RequestAllItems request = new RequestAllItems(this);
        request.start();
    }

    protected void setAllItems(List<Item> allItems){
        System.out.println("SET TRIGGER");
        this.allItems = allItems;
    }

    private void setAdapter() {
        itemAdapter = new ItemListAdapter(cartItems);

        binding.itemRecycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.itemRecycleView.setAdapter(itemAdapter);
    }

    private void setListener() {
        binding.addItemButton.setOnClickListener(this);
        binding.checkoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int targetId = v.getId();
        System.out.println(allItems.size());

        if (targetId == binding.addItemButton.getId()) {
            // TODO add item
            String targetIdentifier = ((EditText) findViewById(R.id.defineTargetItem)).getText().toString();
            int targetIndex = findItemIndex(targetIdentifier);

            if (targetIndex == -1) {
                // TODO check if that item actually exists and then add to items
                //itemAdapter.notifyItemInserted(items.size() - 1);
            } else {
                int targetQuantity = cartItems.get(targetIndex).getIntegerQuantity();
                cartItems.get(targetIndex).setIntegerQuantity(targetQuantity + 1);
                itemAdapter.notifyItemChanged(targetIndex);
            }
        } else if (targetId == binding.checkoutButton.getId()) {
            // TODO checkout

            resetAfterCheckout();
        } else if (targetId == R.id.addOne || targetId == R.id.removeOne) {
            ConstraintLayout parent = (ConstraintLayout) v.getParent();
            int targetIndex = findItemIndex(((TextView) parent.findViewById(R.id.itemName)).getText().toString());
            int quantity = cartItems.get(targetIndex).getIntegerQuantity();
            int stock = cartItems.get(targetIndex).getIntegerStock();

            int target_quantity;
            if (targetId == R.id.addOne)
                target_quantity = quantity + 1;
            else
                target_quantity = quantity - 1;

            if (target_quantity <= 0) {
                cartItems.remove(targetIndex);
                itemAdapter.notifyItemRemoved(targetIndex);
            } else if (target_quantity <= stock) {
                cartItems.get(targetIndex).setQuantity(Integer.toString(target_quantity));
                itemAdapter.notifyItemChanged(targetIndex);
            }

        }
    }



    private void resetAfterCheckout() {
        int prevSize = cartItems.size();
        for (int i = 0; i < prevSize; i++) {
            cartItems.remove(0);
            itemAdapter.notifyItemRemoved(0);
        }
    }

    private int findItemIndex(String identifier) {
        int id = -1;
        try {
            id = Integer.parseInt(identifier);
        } catch (NumberFormatException ignored) {
        }

        for (int i = 0; i < cartItems.size(); i++) {
            if ((id == -1 && cartItems.get(i).getName().equals(identifier)) ||
                    (id != -1 && cartItems.get(i).getId() == id)) {
                return i;
            }
        }

        return -1;
    }
}