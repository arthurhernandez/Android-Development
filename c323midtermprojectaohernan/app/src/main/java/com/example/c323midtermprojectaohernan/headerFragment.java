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
import android.widget.Button;
import android.widget.EditText;
/**
 * Fragment containing 2 buttons for the two types of lists
 */
public class headerFragment extends Fragment {
    View view;

    public interface myHeaderFragmentInterface{
        void HeaderChange(String buttonEvent);
    }
    //create a new activity communicator from the fragment interface
    headerFragment.myHeaderFragmentInterface activityCommunicator;
    //the on attach method makes sure that there is a non null context for the fragment to attach onto
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            activityCommunicator = (headerFragment.myHeaderFragmentInterface) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Activity must implement myFragmentInterface");
        }
    }

    /**
     * 
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_header,container,false );

        Button listButton = view.findViewById(R.id.listViewButton);
        Button cardButton = view.findViewById(R.id.cardViewButton);
        listButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                activityCommunicator.HeaderChange("LIST");
            }
        });
        cardButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                activityCommunicator.HeaderChange("CARD");
            }
        });
        return view;
    }
}