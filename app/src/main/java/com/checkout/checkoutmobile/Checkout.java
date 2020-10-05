package com.checkout.checkoutmobile;

import static com.checkout.checkoutmobile.Config.*;

public class Checkout {
    private int id;
    private double discount;
    private double taxRate;

    Checkout(int id, double discount, double tax_rate) {
        this.discount = roundTwoDigit(discount);
        this.taxRate = roundTwoDigit(tax_rate);
        this.id = id;
    }

    public String getDiscount() {
        return String.format(FORMAT_LOCALE, "Discount: %d%% off", (int) (discount * 100));
    }

    public String getTaxRate() {
        return String.format(FORMAT_LOCALE, "Tax %d%%", (int) (taxRate * 100));
    }

    public int getId() {
        return id;
    }

    private static double roundTwoDigit(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    double getFinalPrice(double itemTotal){
        return roundTwoDigit(itemTotal * (1.0 - this.discount) * (1.0 + this.taxRate));
    }
}
