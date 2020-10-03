package com.checkout.checkoutmobile;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.checkout.checkoutmobile.Config.DEFAULT_TIMEOUT;
import static com.checkout.checkoutmobile.Config.FORMAT_LOCALE;
import static com.checkout.checkoutmobile.Config.PRODUCTION_SERVER_URL;

public class PostTransaction extends Thread {
    private final int id;
    private final int amount;
    private final MainActivity activity;

    PostTransaction(int id, int amount, MainActivity activity) {
        this.id = id;
        this.amount = amount;
        this.activity = activity;
    }

    public void run() {
        try {
            URL url = new URL(PRODUCTION_SERVER_URL + "/item/purchase");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setConnectTimeout(DEFAULT_TIMEOUT);
            con.setReadTimeout(DEFAULT_TIMEOUT);
            con.setDoOutput(true);

            String transactionJSON = String.format(FORMAT_LOCALE, "{\"id\": \"%d\", \"amount\": \"%d\"}", this.id, this.amount);

            OutputStream os = con.getOutputStream();
            byte[] input = transactionJSON.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            int responseCode = con.getResponseCode();

            if (responseCode != 200) {
                Log.w("<TRANSACTION ERROR>", String.format(FORMAT_LOCALE, "Server responded with code %d\n", responseCode));
                this.activity.setNetworkError();
            }

            Log.i("<NETWORK STATUS>", "Transaction post succeeded.");

        } catch (Exception e) {
            Log.w("<NETWORK ERROR>", "Post transaction failed with following stack information.\n");
            e.printStackTrace();
            this.activity.setNetworkError();
        }


    }

}
