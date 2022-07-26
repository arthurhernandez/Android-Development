package com.example.c323midtermprojectaohernan;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Fragment for adding camping and trekking information
 */
public class addCampFragment extends Fragment {
    View view;

    /**
     * Interface that passes the data received back to the parent activity
     */
    public interface myCampingFragmentInterface{
        void CampingChange(String campName, String campingBestTimeToVisit, String campingArea);
    }
    //create a new activity communicator from the fragment interface
    addCampFragment.myCampingFragmentInterface activityCommunicator;
    //the on attach method makes sure that there is a non null context for the fragment to attach onto

    /**
     * uses the activity communicator to speak to the parent and
     * @param context to get info from the fragment
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            activityCommunicator = (addCampFragment.myCampingFragmentInterface) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Activity must implement myFragmentInterface");
        }
    }

    /**
     * Inflates the fragment making it visible
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_camp,container,false );
        EditText campingName = view.findViewById(R.id.campNameEditText);
        EditText campingBestTimeToVisit = view.findViewById(R.id.campTimeEditText);
        EditText campingSpot = view.findViewById(R.id.campAreaEditText);

        /**
         * Check to see if any of the edittexts have changed
         * and reports these changes back to the activity through the interface method
         */
        campingName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                activityCommunicator.CampingChange(campingName.getText().toString(),campingBestTimeToVisit.getText().toString(),
                        campingSpot.getText().toString());
            }
        });
        campingBestTimeToVisit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                activityCommunicator.CampingChange(campingName.getText().toString(),campingBestTimeToVisit.getText().toString(),
                        campingSpot.getText().toString());
            }
        });
        campingSpot.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                activityCommunicator.CampingChange(campingName.getText().toString(),campingBestTimeToVisit.getText().toString(),
                        campingSpot.getText().toString());
            }
        });
        return view;
    }}