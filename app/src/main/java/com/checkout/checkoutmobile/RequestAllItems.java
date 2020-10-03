package com.checkout.checkoutmobile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RequestAllItems extends Thread{
    private static final String PRODUCTION_SERVER_URL = "http://checkout-env.eba-icztdryu.ca-central-1.elasticbeanstalk.com/items";
    private MainActivity activity;

    RequestAllItems(MainActivity activity){
        this.activity = activity;
    }

    public void run(){
        try {
            URL url = new URL(PRODUCTION_SERVER_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setConnectTimeout(3000);
            con.setReadTimeout(3000);

            int responseCode = con.getResponseCode();
            System.out.println("GET server with code " + responseCode);

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

                this.activity.setAllItems(allItems);
                System.out.println("COMPLETED THREAD");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
