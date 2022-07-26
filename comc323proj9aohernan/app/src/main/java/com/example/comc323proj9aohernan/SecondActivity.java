package com.example.comc323proj9aohernan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private List<Product> myProducts;
    ListView listView;
    Spinner dropDown;
    String category;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        dropDown = findViewById(R.id.spinnerSecondActivity);
        searchEditText = findViewById(R.id.searchTextBox);

        String[] items = new String[]{"Food", "Housing", "Entertainment", "travel"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropDown.setAdapter(adapter);
        dropDown.setOnItemSelectedListener(this);

        populateMyProducts();
        populateList();
    }

    public void searchButtonOnclick(View view) {
        if(searchEditText.getText().toString().length() != 0){
            myProducts = new ArrayList<Product>();
            MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
            Cursor res = dbHandler.getAllData();

            while(res.moveToNext()){
                if(category.equals(res.getString(2)) && (res.getString(0).equals(searchEditText.getText().toString()) || res.getString(1).equals(searchEditText.getText().toString()) || res.getString(3).equals(searchEditText.getText().toString()))) {
                    myProducts.add(new Product(res.getString(0), res.getString(1), res.getString(2), res.getString(3)));
                }
            }
            populateList();
        }else{
            //only seach by category
            myProducts = new ArrayList<Product>();
            MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
            Cursor res = dbHandler.getAllData();

            while(res.moveToNext()){
                if(category.equals(res.getString(2))) {
                    myProducts.add(new Product(res.getString(0), res.getString(1), res.getString(2), res.getString(3)));
                }
            }
            populateList();
        }
    }

    private void populateMyProducts() {
        myProducts = new ArrayList<Product>();
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Cursor res = dbHandler.getAllData();

        while(res.moveToNext()){
            myProducts.add(new Product(res.getString(0),res.getString(1),res.getString(2),res.getString(3)));
        }
    }

    private void populateList() {
        //create a listview adapter
        ArrayAdapter<Product> myAdapter = new MyCustomListAdapter();
        listView = findViewById(R.id.listView);
        listView.setAdapter(myAdapter);
    }

    private class MyCustomListAdapter extends ArrayAdapter<Product> {
        //create a customlist adapter that finds each value in our custom layout
        public MyCustomListAdapter() {
            super(SecondActivity.this, R.layout.custom_list_layout , myProducts);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.custom_list_layout, parent, false);
            //find all views in custom layout and do something with them

            Product currentProduct = myProducts.get(position);
            TextView textViewExpense = itemView.findViewById(R.id.expenseCustom);
            TextView textViewPrice = itemView.findViewById(R.id.priceCustom);
            TextView textViewCategory = itemView.findViewById(R.id.categoryCustom);
            TextView textViewDate = itemView.findViewById(R.id.dateCustom);
            ImageView image = itemView.findViewById(R.id.iconImage);

            ImageButton deleteButton = itemView.findViewById(R.id.deleteButton);
            ImageButton editButton = itemView.findViewById(R.id.editButton);

            textViewExpense.setText(currentProduct.get_productPrice());
            textViewPrice.setText(currentProduct.get_productPrice());
            textViewCategory.setText(currentProduct.get_productCategory());
            textViewDate.setText(currentProduct.get_productDate());

            if(currentProduct.get_productCategory().equals("food")){
                image.setImageDrawable(getDrawable(R.drawable.food));
            }else if(currentProduct.get_productCategory().equals("housing")){
                image.setImageDrawable(getDrawable(R.drawable.house));
            }else if(currentProduct.get_productCategory().equals("entertainment")){
                image.setImageDrawable(getDrawable(R.drawable.entertain));
            }else{
                image.setImageDrawable(getDrawable(R.drawable.travel));            }

            deleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    MyDBHandler dbHandler = new MyDBHandler(getContext(), null, null, 1);
                    dbHandler.deleteProductDB(currentProduct.get_productName());

                    //do something
                    currentProduct.remove(position); //or some other task
                    notifyDataSetChanged();
                }
            });

            editButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something

                    notifyDataSetChanged();
                }
            });

            return itemView;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                category = "food";
                break;
            case 1:
                category = "housing";
                break;
            case 2:
                category = "entertainment";
                break;
            case 3:
                category = "travel";
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //something will always automatically be selected
    }

}