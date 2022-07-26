package com.example.c323midtermprojectaohernan;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
/**
 * Fragment for adding monument information
 */
public class addMonumentFragment extends Fragment {
    View view;
    /**
     * Interface that passes the data received back to the parent activity
     */
    public interface myMonumentFragmentInterface{
        void MonumentChange(String monumentName, String monumentHistory, String monumentBestTimeToVisit, String monumentPrice);
    }
    //create a new activity communicator from the fragment interface
    myMonumentFragmentInterface activityCommunicator;
    //the on attach method makes sure that there is a non null context for the fragment to attach onto
    /**
     * uses the activity communicator to speak to the parent and
     * @param context to get info from the fragment
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            activityCommunicator = (myMonumentFragmentInterface) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Activity must implement myFragmentInterface");
        }
    }
    /**
     * Inflates the fragment making it visible
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the inflated fragment view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_monument,container,false );
        EditText monumentName = view.findViewById(R.id.monumentNameEditText);
        EditText monumentHistory = view.findViewById(R.id.monumentHistoryEditText);
        EditText monumentBestTimeToVisit = view.findViewById(R.id.bestTimeToVisitEditText);
        EditText monumentPrice = view.findViewById(R.id.ticketPriceEditText);
/**
 * Check to see if any of the edittexts have changed
 * and reports these changes back to the activity through the interface method
 */
        monumentName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                activityCommunicator.MonumentChange(monumentName.getText().toString(),monumentHistory.getText().toString(),
                        monumentBestTimeToVisit.getText().toString(),monumentPrice.getText().toString());
            }
        });
        monumentHistory.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                activityCommunicator.MonumentChange(monumentName.getText().toString(),monumentHistory.getText().toString(),
                        monumentBestTimeToVisit.getText().toString(),monumentPrice.getText().toString());
            }
        });
        monumentBestTimeToVisit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                activityCommunicator.MonumentChange(monumentName.getText().toString(),monumentHistory.getText().toString(),
                        monumentBestTimeToVisit.getText().toString(),monumentPrice.getText().toString());
            }
        });
        monumentPrice.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                activityCommunicator.MonumentChange(monumentName.getText().toString(),monumentHistory.getText().toString(),
                        monumentBestTimeToVisit.getText().toString(),monumentPrice.getText().toString());
            }
        });
        return view;
    }
}