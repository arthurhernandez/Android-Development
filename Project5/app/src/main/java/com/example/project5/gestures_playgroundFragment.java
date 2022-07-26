package com.example.project5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.util.Log;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

public class gestures_playgroundFragment extends Fragment {
    //initialize vars
    private static final int swipe = 100;
    private static final int swipeVelocity = 100;
    TextView backgroundText;
    View view;
    ImageView ball;

    //create our public interface
    public interface gesturesFragmentInterface {
    }
    //this is our activity communicator
    gestures_playgroundFragment.gesturesFragmentInterface playCommunicator;

    @Override
    public void onAttach(@NonNull Context context) {
        //check to see if the ui exists
        super.onAttach(context);
        try {
            playCommunicator = (gestures_playgroundFragment.gesturesFragmentInterface) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Activity must implement myFragmentInterface");
        }
    }

    @Nullable
    @Override
    //create the view
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the view we want
        view = inflater.inflate(R.layout.gestures_playground_land, container, false);
        ball = view.findViewById(R.id.ball);
        backgroundText = view.findViewById(R.id.backgroundtext);
        //Implementing gesture detection
        GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
            @SuppressLint("NewApi")
            @Override
            //PLEASE NOTE I FOUND MOST OF THIS IMPLEMENTATION ONLINE
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                try {
                    //subtract x and y start and end points
                    float diffY = motionEvent1.getY() - motionEvent.getY();
                    float diffX = motionEvent1.getX() - motionEvent.getX();
                    //if the absolute value of x is more than y we are moving horizontally
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        //if the value is going upward we are moving to the right
                        if (Math.abs(diffX) > swipe && Math.abs(v) > swipeVelocity) {
                            if (diffX > 0) {
                                Log.v("FLING","RIGHT");
                                backgroundText.setText("Right");
                                ball.setTranslationX(650);
                                ball.setTranslationY(300);
                                //else we are moving to the left
                            } else {
                                Log.v("FLING","LEFT");
                                backgroundText.setText("left");
                                ball.setTranslationX(0);
                                ball.setTranslationY(150);



                            }
                        }
                    }
                    //if our difference in y is more than that needed to create a fling the swipe dist and swipe velocity
                    else if (Math.abs(diffY) > swipe && Math.abs(v1) > swipeVelocity) {
                        //if we are moving in a positive direction away from (0,0) we are going down
                        if (diffY > 0) {
                            Log.v("FLING","DOWN");
                            backgroundText.setText("down");
                            ball.setTranslationX(250);
                            ball.setTranslationY(600);



                        } else {
                            //else we are going up!
                            Log.v("FLING","UP");
                            backgroundText.setText("up");
                            ball.setTranslationX(200);
                            ball.setTranslationY(0);

                        }
                    }
                    //catch anything that doesnt fit our bounds
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return false;
            }
        });
        //set our touch listener and return our detector to check for events
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
                public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        return view;
    }
}