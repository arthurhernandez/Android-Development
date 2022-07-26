package com.example.mybmicalculator;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class calculate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
    }

    public void calculateButtonCallBack(View view) {
        //get and convert all values
        EditText weight = findViewById(R.id.weightfield);
        EditText height = findViewById(R.id.heightfield);
        TextView bmiout = findViewById(R.id.bmiout);
        TextView typeout = findViewById(R.id.typeout);
        //do the math
        double weights = Double.parseDouble(weight.getText().toString());
        double heights = Double.parseDouble(height.getText().toString());
        double bmifinal = 703 * (weights / Math.pow(heights, 2));
        //specify bmiBelow 18.5	Underweight
        //18.5 – 24.9	Healthy Weight
        //25.0 – 29.9	Overweight
        //30.0 and Above	Obesity
        if(bmifinal < 18.5){
            typeout.setText("underweight");
        }
        else if(18.5 < bmifinal &&  bmifinal < 24.9){
            typeout.setText("Healthy weight");
        }
        else if(25.0 < bmifinal &&  bmifinal < 29.9){
            typeout.setText("Over weight");
        }
        else if(bmifinal > 30){
            typeout.setText("Obesity");
        }
        bmiout.setText(String.valueOf(bmifinal));
    }
}