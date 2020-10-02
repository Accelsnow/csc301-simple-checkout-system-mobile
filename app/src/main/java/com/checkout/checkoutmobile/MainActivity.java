package com.checkout.checkoutmobile;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class Item {
    private String name;
    private int stock;
    private double originalPrice;
    private double itemDiscount;
    private double discountedPrice;
    private int quantity;
    private double totalPrice;
    private static final Locale FORMAT_LOCALE = Locale.CANADA;

    Item(String name, int stock, double originalPrice, double itemDiscount, int quantity) {
        this.name = name;
        this.stock = stock;
        this.originalPrice = roundTwoDigit(originalPrice);
        this.itemDiscount = roundTwoDigit(itemDiscount);
        this.discountedPrice = roundTwoDigit(originalPrice * (1.00 - itemDiscount));
        this.quantity = quantity;
        this.totalPrice = roundTwoDigit(discountedPrice * quantity);
    }

    private double roundTwoDigit(double val){
        return Math.round(val * 100.0) / 100.0;
    }

    public String getDiscountedPriceText() {
        return String.format(FORMAT_LOCALE, "$%.2f/p", discountedPrice);
    }

    public String getItemDiscountText() {
        int percentage_off = (int) (itemDiscount * 100);
        return String.format(FORMAT_LOCALE, "%d%% off", percentage_off);
    }

    public String getOriginalPriceText() {
        return String.format(FORMAT_LOCALE, "$%.2f/p", originalPrice);
    }

    public String getTotalPriceText() {
        return String.format(FORMAT_LOCALE, "$%.2f", totalPrice);
    }

    public String getQuantityText() {
        return String.format(FORMAT_LOCALE, "%d", quantity);
    }

    public String getStockText() {
        if (stock > 1000) {
            return "Stock 999+";
        } else {
            return String.format(FORMAT_LOCALE, "Stock %d", stock);
        }
    }

    public String getNameText() {
        return String.format(FORMAT_LOCALE, "%s", name);
    }
}


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private List<Item> items;

    private void initializeData() {
        items = new ArrayList<>();
        items.add(new Item("cokesadfsadfdsafasdfsdfsdafads", 99, 1.00, 0.0, 1));
        items.add(new Item("sprite", 4, 2.453, 0.0, 2));
        items.add(new Item("fries", 1, 2.0, 0.5, 1));
        items.add(new Item("candy", 2, 100000.0, 0.1, 1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeData();

        recyclerView = findViewById(R.id.itemRecycleView);
        recyclerView.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);
        rvAdapter = new RvAdapter(new ArrayList<>(items));
        recyclerView.setAdapter(rvAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}