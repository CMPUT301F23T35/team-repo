package com.example.team_repo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddFragment extends Fragment{
    private EditText ItemName;
    private EditText Description;
    private EditText DatePurchase;
    private EditText ItemMake;

    private EditText ItemModel;
    private EditText ItemSerial;
    private EditText EstimatedValue;
    private Button BtnConfirm;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        ItemName = view.findViewById(R.id.ItemName);
        Description = view.findViewById(R.id.Description);
        DatePurchase = view.findViewById(R.id.DatePurchase);
        ItemMake = view.findViewById(R.id.ItemMake);

        ItemModel = view.findViewById(R.id.ItemModel);
        ItemSerial = view.findViewById(R.id.ItemSerial);
        EstimatedValue = view.findViewById(R.id.EstimatedValue);
        BtnConfirm = view.findViewById(R.id.btn_confirm);


        BtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ItemName.getText().toString();
                String date = DatePurchase.getText().toString();

                String item_description = Description.getText().toString();
                String make = ItemMake.getText().toString();
                String model = ItemModel.getText().toString();
                String serial = ItemSerial.getText().toString();
                float value;
                if (EstimatedValue.getText().toString().isEmpty()){
                    value = (float)0;
                }
                else {
                    value = Float.parseFloat(EstimatedValue.getText().toString());
                }

//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
//            Date date = null;
//            try {
//                date = dateFormat.parse(dateString);
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }

                if (!name.isEmpty()) {
                    // Create an item with the received name and other default values or set appropriate values.
//                Calendar cal = Calendar.getInstance();
//                Date date = cal.getTime();
                    Item newItem = new Item(name, date, value, item_description, make, model, serial, "");
                    ItemList add_item_list = ((MainActivity) getActivity()).getAdd_item_list();
                    add_item_list.add(newItem);
                    ((MainActivity) getActivity()).setAdd_item_list(add_item_list);

                    cleanEt();

                    // give a message to show that the item is added successfully
                    Toast.makeText(getActivity(), "Item added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    // give a message to show that the item is not added successfully
                    Toast.makeText(getActivity(), "Item should have a name", Toast.LENGTH_SHORT).show();
                }

            }

        });

        return view;
    }

    private void cleanEt(){
        ItemName.setText("");
        DatePurchase.setText("");
        Description.setText("");
        ItemMake.setText("");
        ItemModel.setText("");
        ItemSerial.setText("");
        EstimatedValue.setText("");
    }


}