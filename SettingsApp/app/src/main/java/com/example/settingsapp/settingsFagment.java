package com.example.settingsapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class settingsFagment extends Fragment{
    //initialization of button and views
    Button sound;
    Button battery;
    Button storage;
    Button display;
    Button system;
    View view;
    //create a new interface
    public interface myFragmentInterface{
        public void settingsChanged(int count);
    }
    //create a new activity communicator from the fragment interface
    myFragmentInterface activityCommunicator;
    //the on attach method makes sure that there is a non null context for the fragment to attach onto
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            activityCommunicator = (myFragmentInterface) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Activity must implement myFragmentInterface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.settings,container,false );

        sound = view.findViewById(R.id.soundButton);
        battery = view.findViewById(R.id.batteryButton);
        storage = view.findViewById(R.id.storageButton);
        display = view.findViewById(R.id.displayButton);
        system = view.findViewById(R.id.systemButton);


        //onclick listener for sound
        sound.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                activityCommunicator.settingsChanged(1);
            }});
        //onclick listener for battery
        battery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                activityCommunicator.settingsChanged(2); }});
        //onclick listener for storage
        storage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                activityCommunicator.settingsChanged(3); }});

        //onclick listener for display
        display.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                activityCommunicator.settingsChanged(4); }});
        //onclick listener for system
        system.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                activityCommunicator.settingsChanged(5); }});
        return view;
    }
}