package com.checkout.checkoutmobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.checkout.checkoutmobile.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.checkout.checkoutmobile.Config.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ItemListAdapter itemAdapter;
    private List<Item> cartItems;
    private List<Item> allItems;
    private Checkout checkout;
    private boolean networkError;
    private ActivityMainBinding cartBinding;
    private static final int ITEM_IN_CART = 1, ITEM_IN_ALL = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        cartItems = new ArrayList<>();
        networkError = false;
        setAdapter();
        setListener();
        initializeData();
    }

    private void initializeData() {
        allItems = null;
        checkout = null;
        GetAllItems requestItems = new GetAllItems(this);
        requestItems.start();
        GetCheckoutInfo requestCheckout = new GetCheckoutInfo(this);
        requestCheckout.start();

        try {
            requestItems.join(DEFAULT_TIMEOUT * 2);
            requestCheckout.join(DEFAULT_TIMEOUT * 2);
        } catch (InterruptedException e) {
            Log.e("<Interrupted Err>", "Get info threads interrupted!");
            e.printStackTrace();
        }

        itemAdapter.notifyDataSetChanged();
    }

    void setCheckout(Checkout checkout) {
        this.checkout = checkout;
    }

    void setNetworkError() {
        this.networkError = true;
    }

    void setAllItems(List<Item> allItems) {
        this.allItems = allItems;

        for (int i = 0; i < cartItems.size(); i++) {
            Item cartItem = cartItems.get(i);
            boolean synced = false;

            for (int j = 0; j < this.allItems.size(); j++) {
                Item allItem = this.allItems.get(j);

                if (allItem.getName().equals(cartItem.getName())) {
                    int currStock = allItem.getIntegerStock();
                    int prevQuantity = cartItem.getIntegerQuantity();

                    allItem.setIntegerQuantity(Math.min(prevQuantity, currStock));

                    cartItems.set(i, allItem);
                    synced = true;
                    break;
                }
            }

            if (!synced) {
                cartItems.remove(i);
                i--;
            }
        }
    }

    private void setAdapter() {
        itemAdapter = new ItemListAdapter(cartItems);

        cartBinding.itemRecycleView.setLayoutManager(new LinearLayoutManager(this));
        cartBinding.itemRecycleView.setAdapter(itemAdapter);
    }

    private void setListener() {
        cartBinding.addItemButton.setOnClickListener(this);
        cartBinding.checkoutButton.setOnClickListener(this);
        cartBinding.refreshButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();
        int targetId = v.getId();

        if (targetId == R.id.refreshButton) {
            initializeData();
            return;
        }

        if (checkout == null || allItems == null) {
            makeAlert(R.string.data_not_initialized, context, "Alert");
            return;
        }

        if (targetId == cartBinding.addItemButton.getId()) {
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
                    makeAlert(R.string.item_dne, context, "Alert");
                    targetTextField.setError("This item does not exist");
                    break;
            }
        } else if (targetId == cartBinding.checkoutButton.getId()) {
            if (cartItems.size() == 0) {
                makeAlert(R.string.cart_empty, context, "Alert");
                return;
            }

            boolean cartValid = cartIsValid();
            if (!cartValid) {
                setAllItems(this.allItems);
                itemAdapter.notifyDataSetChanged();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(buildCheckoutSummary()).setTitle("Checkout");

            builder.setPositiveButton(R.string.pay, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    boolean fullSuccess = true;

                    for (int i = 0; i < cartItems.size(); i++) {
                        networkError = false;
                        Item item = cartItems.get(i);

                        PostTransaction postTransaction = new PostTransaction(item.getId(), item.getIntegerQuantity(), MainActivity.this);
                        postTransaction.start();

                        try {
                            postTransaction.join(DEFAULT_TIMEOUT * 2);
                        } catch (InterruptedException e) {
                            Log.e("<Interrupted Err>", "Transaction thread interrupted!");
                            e.printStackTrace();
                            fullSuccess = false;
                            continue;
                        }

                        if (networkError) {
                            fullSuccess = false;
                            continue;
                        }

                        cartItems.remove(i);
                        i--;
                    }

                    if (fullSuccess) {
                        makeAlert(R.string.transaction_success, context, "Success");
                    } else {
                        makeAlert(R.string.transaction_with_error, context, "Warning");
                    }
                    initializeData();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            if (!cartValid)
                makeAlert(R.string.cart_exceed_stock_fix, context, "Warning");

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
                target_quantity = Math.min(stock, quantity + 1);
            else if (targetId == R.id.removeOne)
                target_quantity = Math.max(1, quantity - 1);
            else {
                cartItems.remove(targetIndex);
                itemAdapter.notifyItemRemoved(targetIndex);
                return;
            }

            cartItems.get(targetIndex).setQuantity(Integer.toString(target_quantity));
            itemAdapter.notifyItemChanged(targetIndex);
        }

    }


    void makeAlert(int stringId, Context c, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(stringId);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean cartIsValid(){
        for (Item item : cartItems)
            if (item.getIntegerQuantity() > item.getIntegerStock())
                return false;
        return true;
    }

    private String buildCheckoutSummary() {
        StringBuilder message = new StringBuilder();
        double total = 0.0;

        for (Item item : cartItems) {
            message.append(String.format(FORMAT_LOCALE, "%s  %s         %s\n", item.getQuantity(), item.getName(), item.getTotalPrice()));
            total += item.getDoubleTotalPrice();
        }
        message.append("\n\n").append(checkout.getDiscount()).append("\n\n");
        message.append(checkout.getTaxRate()).append("\n\n");
        message.append(String.format(FORMAT_LOCALE, "SUM TOTAL: $%.2f", checkout.getFinalPrice(total)));
        return message.toString();
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
