package com.checkout.checkoutmobile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;


import com.checkout.checkoutmobile.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ItemListAdapter itemAdapter;
    private List<Item> cartItems;
    private List<Item> allItems;
    private ActivityMainBinding binding;
    private static final int ITEM_IN_CART = 1, ITEM_IN_ALL = 2;

    public static class ItemInvalidAlertFrag extends DialogFragment {
        
        public static ItemInvalidAlertFrag newInstance(String title) {
            ItemInvalidAlertFrag frag = new ItemInvalidAlertFrag();
            Bundle args = new Bundle();
            args.putString("title", title);
            frag.setArguments(args);
            return frag;
        }

        @NotNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.item_dne)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            return builder.create();
        }
    }

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

    private void getAllItems() {
        RequestAllItems request = new RequestAllItems(this);
        request.start();
    }

    void setAllItems(List<Item> allItems) {
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

        if (targetId == binding.addItemButton.getId()) {
            EditText targetTextField = findViewById(R.id.defineTargetItem);
            String targetIdentifier = targetTextField.getText().toString();
            int[] targetIndexData = findItemIndex(targetIdentifier);
            int targetIndex = targetIndexData[1];

            switch (targetIndexData[0]) {
                case ITEM_IN_CART:
                    int targetQuantity = cartItems.get(targetIndex).getIntegerQuantity();
                    cartItems.get(targetIndex).setIntegerQuantity(targetQuantity + 1);
                    itemAdapter.notifyItemChanged(targetIndex);
                    break;

                case ITEM_IN_ALL:
                    Item newCartItem = allItems.get(targetIndex);
                    newCartItem.setIntegerQuantity(1);
                    cartItems.add(newCartItem);
                    itemAdapter.notifyItemInserted(cartItems.size() - 1);
                    break;

                default:
                    FragmentManager fm = getSupportFragmentManager();
                    ItemInvalidAlertFrag alertDialog = ItemInvalidAlertFrag.newInstance("error");
                    alertDialog.show(fm, "alert");
                    targetTextField.setError("This item does not exist");
                    break;
            }
        } else if (targetId == binding.checkoutButton.getId()) {
            // TODO checkout

            resetAfterCheckout();
        } else if (targetId == R.id.addOne || targetId == R.id.removeOne || targetId == R.id.removeItem) {
            ConstraintLayout parent = (ConstraintLayout) v.getParent();
            int[] targetIndexData = findItemIndex(((TextView) parent.findViewById(R.id.itemName)).getText().toString());

            if (targetIndexData[0] != ITEM_IN_CART) {
                Log.e("<ITEM ERROR>", "Item not in cart can not possibly have a +1 or -1 button.");
                return;
            }
            int targetIndex = targetIndexData[1];

            int quantity = cartItems.get(targetIndex).getIntegerQuantity();
            int stock = cartItems.get(targetIndex).getIntegerStock();

            int target_quantity;
            if (targetId == R.id.addOne)
                target_quantity = quantity + 1;
            else if (targetId == R.id.removeOne)
                target_quantity = quantity - 1;
            else
                target_quantity = 0;

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

    private int[] findItemIndex(String identifier) {
        int id = -1;
        try {
            id = Integer.parseInt(identifier);
        } catch (NumberFormatException ignored) {
        }

        for (int i = 0; i < cartItems.size(); i++) {
            if ((id == -1 && cartItems.get(i).getName().equals(identifier)) ||
                    (id != -1 && cartItems.get(i).getId() == id)) {
                return new int[]{ITEM_IN_CART, i};
            }
        }

        for (int i = 0; i < allItems.size(); i++) {
            if ((id == -1 && allItems.get(i).getName().equals(identifier)) ||
                    (id != -1 && allItems.get(i).getId() == id)) {
                return new int[]{ITEM_IN_ALL, i};
            }
        }

        return new int[]{-1, -1};
    }
}
