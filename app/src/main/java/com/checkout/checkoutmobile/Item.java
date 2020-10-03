package com.checkout.checkoutmobile;

import android.util.Log;

import java.util.Locale;

public class Item {
    private String name;
    private int id;
    private int stock;
    private double originalPrice;
    private double itemDiscount;
    private double discountedPrice;
    private int quantity;
    private double totalPrice;
    private final Locale FORMAT_LOCALE = Locale.CANADA;

    Item(int id, String name, int stock, double originalPrice, double itemDiscount, int quantity) {
        this.id = id;
        this.name = String.format(FORMAT_LOCALE, "%s", name);
        this.originalPrice = originalPrice;
        this.itemDiscount = roundTwoDigit(itemDiscount);
        this.quantity = quantity;
        this.stock = stock;
        this.discountedPrice = roundTwoDigit(originalPrice * (1.00 - itemDiscount));
        updateTotalPrice();
    }

    private static double roundTwoDigit(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    private void updateTotalPrice() {
        this.totalPrice = this.discountedPrice * quantity;
    }

    public void setIntegerQuantity(int quantity) {
        this.quantity = quantity;
        updateTotalPrice();
    }

    public void setQuantity(String quantity) {
        try {
            setIntegerQuantity(Integer.parseInt(quantity));
        } catch (NumberFormatException e) {
            Log.e("Cast Error", "Quantity should be an integer!");
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStock() {
        if (stock > 1000) {
            return "Stock 999+";
        } else {
            return String.format(FORMAT_LOCALE, "Stock %d", stock);
        }
    }

    public int getIntegerStock() {
        return this.stock;
    }

    public String getQuantity() {
        return String.format(FORMAT_LOCALE, "%d", quantity);
    }

    public int getIntegerQuantity() {
        return this.quantity;
    }

    public String getItemDiscount() {
        return String.format(FORMAT_LOCALE, "%d%% off", (int) (itemDiscount * 100));
    }

    public String getOriginalPrice() {
        return String.format(FORMAT_LOCALE, "$%.2f/p", originalPrice);
    }

    public String getDiscountedPrice() {
        return String.format(FORMAT_LOCALE, "$%.2f/p", discountedPrice);
    }

    public String getTotalPrice() {
        return String.format(FORMAT_LOCALE, "$%.2f", totalPrice);
    }

}

