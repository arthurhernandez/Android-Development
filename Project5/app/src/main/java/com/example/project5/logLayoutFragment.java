package com.example.project5;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class logLayoutFragment extends Fragment {

    View view;

    public interface logFragmentInterface {
    }

    logFragmentInterface logCommunicator;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            logCommunicator = (logFragmentInterface) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Activity must implement myFragmentInterface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.loglayout_land,container,false );

        return view;
    }

}