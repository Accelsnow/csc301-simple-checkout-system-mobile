package com.checkout.checkoutmobile;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.checkout.checkoutmobile.Config.*;

public class GetAllItems extends Thread {
    private MainActivity activity;

    GetAllItems(MainActivity activity) {
        this.activity = activity;
    }

    public void run() {
        try {
            URL url = new URL(PRODUCTION_SERVER_URL + "/items");
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
                JSONArray itemArray = responseObj.getJSONArray("items");
                List<Item> allItems = new ArrayList<>();

                for (int i = 0; i < itemArray.length(); i++) {
                    JSONObject itemObj = itemArray.getJSONObject(i);

                    Item item = new Item(itemObj.getInt("id"), itemObj.getString("name"), itemObj.getInt("stock"), itemObj.getDouble("price"), itemObj.getDouble("discount"), 0);
                    allItems.add(item);
                }

                this.activity.setAllItems(Collections.unmodifiableList(allItems));
                Log.i("<NETWORK STATUS>", "Network request getting all items completed.");
            }
        } catch (Exception e) {
            Log.e("<NETWORK ERROR>", "Request all items failed with following stack information.\n");
            e.printStackTrace();
        }


    }

}
