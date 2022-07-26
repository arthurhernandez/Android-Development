package com.example.settingsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class headerFragment extends Fragment {

    TextView name;
    TextView place;
    Button edit;
    Button save;
    View view;

    public interface headerFragmentInterface{
        public void headerChanged();

    }
    headerFragmentInterface activityCommunicator;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            activityCommunicator = (headerFragmentInterface) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Activity must implement myFragmentInterface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.header,container,false );

        name = view.findViewById(R.id.edittNameText);
        place = view.findViewById(R.id.editPlaceText);
        edit = view.findViewById(R.id.editButton);
        save = view.findViewById(R.id.saveButton);
        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                edit.setEnabled(false);
                save.setEnabled(true);
                name.setEnabled(true);
                place.setEnabled(true);
            }});

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                edit.setEnabled(true);
                save.setEnabled(false);
                name.setEnabled(false);
                place.setEnabled(false);
            }});

        return view;
    }
}
