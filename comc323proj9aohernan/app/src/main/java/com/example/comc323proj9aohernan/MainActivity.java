package com.example.comc323proj9aohernan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText expenseEditText, priceEditText, dateEditText;
    Spinner dropDown;
    String expense, price, category, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expenseEditText = findViewById(R.id.ExpenseEditText);
        priceEditText = findViewById(R.id.MoneySentEditText);
        dateEditText = findViewById(R.id.dateEditText);
        dropDown = findViewById(R.id.CategorySpinner);

        String[] items = new String[]{"Food", "Housing", "Entertainment", "travel"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropDown.setAdapter(adapter);
        dropDown.setOnItemSelectedListener(this);

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

    public void addExpenseOnClick(View view) {
        expense = expenseEditText.getText().toString();
        price = priceEditText.getText().toString();
        date = dateEditText.getText().toString();

        if(expense.length() != 0 && price.length() != 0 && date.length() != 0 && validateDate(date)){
            MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
            Product product = new Product(expense,price,category,date);
            dbHandler.addProductDB(product);
            expenseEditText.setText("");
            priceEditText.setText("");
            dateEditText.setText("");
            Toast.makeText(this, "Expense added correctly!", Toast.LENGTH_SHORT).show();

        }else{

            Toast.makeText(this, "Please enter correct information", Toast.LENGTH_SHORT).show();

        }
    }

    public void viewExpenseOnClick(View view) {
        Intent myIntent = new Intent(MainActivity.this, SecondActivity.class /*from, to*/);
        startActivity(myIntent);
    }

    private boolean validateDate(String date) {

        if(date.matches("") || date.length() != 10){
            return false;
        }
        int month = Integer.parseInt(date.substring(0,2));
        int day = Integer.parseInt(date.substring(3,5));
        Log.v("month",month + "");
        Log.v("day",day + "");
        if(date.charAt(2) == '/' && date.charAt(5) == '/' && month >= 1 && month <= 12 && day >= 1 && day <= 31 && date.length() == 10){
            return true;
        }
        return false;
    }

}