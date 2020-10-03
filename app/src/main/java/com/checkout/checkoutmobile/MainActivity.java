package com.checkout.checkoutmobile;

import android.os.Bundle;


import com.checkout.checkoutmobile.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ItemListAdapter itemAdapter;
    private List<Item> items;
    private ActivityMainBinding binding;
    private final Locale FORMAT_LOCALE = Locale.CANADA;

    private void initializeData() {
        items = new ArrayList<>();
        items.add(new Item(1, "cokesadfsadfdsafasdfsdfsdafads", 99, 1.00, 0.0, 1));
        items.add(new Item(2, "sprite", 4, 2.453, 0.0, 2));
        items.add(new Item(3, "fries", 1, 2.0, 0.5, 1));
        items.add(new Item(4, "candy", 2, 100000.0, 0.1, 1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initializeData();
        setAdapter();
        setListener();
    }

    private void setAdapter() {
        itemAdapter = new ItemListAdapter(items);

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

        if (targetId == binding.addItemButton.getId()) {
            // TODO add item
            String targetIdentifier = ((EditText) findViewById(R.id.defineTargetItem)).getText().toString();
            int targetIndex = findItemIndex(targetIdentifier);

            if (targetIndex == -1) {
                // TODO check if that item actually exists and then add to items
                //itemAdapter.notifyItemInserted(items.size() - 1);
            } else {
                int targetQuantity = items.get(targetIndex).getIntegerQuantity();
                items.get(targetIndex).setIntegerQuantity(targetQuantity + 1);
                itemAdapter.notifyItemChanged(targetIndex);
            }
        } else if (targetId == binding.checkoutButton.getId()) {
            // TODO checkout

            resetAfterCheckout();
        } else if (targetId == R.id.addOne || targetId == R.id.removeOne) {
            ConstraintLayout parent = (ConstraintLayout) v.getParent();
            int targetIndex = findItemIndex(((TextView) parent.findViewById(R.id.itemName)).getText().toString());
            int quantity = items.get(targetIndex).getIntegerQuantity();
            int stock = items.get(targetIndex).getIntegerStock();

            int target_quantity;
            if (targetId == R.id.addOne)
                target_quantity = quantity + 1;
            else
                target_quantity = quantity - 1;

            if (target_quantity <= 0) {
                items.remove(targetIndex);
                itemAdapter.notifyItemRemoved(targetIndex);
            } else if (target_quantity <= stock) {
                items.get(targetIndex).setQuantity(Integer.toString(target_quantity));
                itemAdapter.notifyItemChanged(targetIndex);
            }

        }
    }

//    private void getAllItems(String targetURL, String urlParameters) {
//        try {
//            URL url = new URL("http://checkout-env.eba-icztdryu.ca-central-1.elasticbeanstalk.com/");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//            con.setRequestProperty("Accept", "application/json");
//            con.setDoOutput(true);
//
//            DataOutputStream out = new DataOutputStream(con.getOutputStream());
//            out.writeBytes(ParameterStringBuilder);
//        } catch (Exception e) {
//
//        }
//    }

    private void resetAfterCheckout() {
        int prevSize = items.size();
        for (int i = 0; i < prevSize; i++) {
            items.remove(0);
            itemAdapter.notifyItemRemoved(0);
        }
    }

    private int findItemIndex(String identifier) {
        int id = -1;
        try {
            id = Integer.parseInt(identifier);
        } catch (NumberFormatException ignored) {
        }

        for (int i = 0; i < items.size(); i++) {
            if ((id == -1 && items.get(i).getName().equals(identifier)) ||
                    (id != -1 && items.get(i).getId() == id)) {
                return i;
            }
        }

        return -1;
    }
}