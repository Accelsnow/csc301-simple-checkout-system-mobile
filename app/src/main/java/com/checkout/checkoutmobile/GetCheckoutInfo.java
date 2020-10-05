package com.checkout.checkoutmobile;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.checkout.checkoutmobile.Config.*;


public class GetCheckoutInfo extends Thread {
    private MainActivity activity;

    GetCheckoutInfo(MainActivity activity) {
        this.activity = activity;
    }

    public void run() {
        try {
            URL url = new URL(PRODUCTION_SERVER_URL + "/checkout/1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setConnectTimeout(DEFAULT_TIMEOUT);
            con.setReadTimeout(DEFAULT_TIMEOUT);

            int responseCode = con.getResponseCode();

            if (responseCode != 200) {
                Log.e("<NETWORK ERROR>", "Response code " + responseCode);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine.trim());
            }
            in.close();

            if (response.length() > 0) {
                JSONObject responseObj = new JSONObject(response.toString());
                JSONObject checkoutObj = responseObj.getJSONObject("checkout");
                double discount = checkoutObj.getDouble("discount");
                double taxRate = checkoutObj.getDouble("tax_rate");
                int id = checkoutObj.getInt("id");
                Checkout checkout = new Checkout(id, discount, taxRate);

                this.activity.setCheckout(checkout);
                Log.i("<NETWORK STATUS>", "Network request getting checkout info completed.");
            }
        } catch (Exception e) {
            Log.e("<NETWORK ERROR>", "Request checkout info failed with following stack information.\n");
            e.printStackTrace();
        }


    }

}
