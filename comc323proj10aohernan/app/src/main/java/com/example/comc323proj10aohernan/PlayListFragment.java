package com.example.comc323proj10aohernan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
/**
 * Fragment for the playlist list
 */
public class PlayListFragment extends Fragment{
    //arraylist holding the music
    private ArrayList<music> mySongs = new ArrayList<>();
    /**
     *
     * @param inflater inflates the fragment into the container
     * @param container the container that is going to contain the fragment
     * @param savedInstanceState the saved state of the fragment
     * @return the completed view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //populates our music objects into mysongs list
        populateMySongs();
        //inflates the view

        View view = inflater.inflate(R.layout.fragment_playlist,container,false);
        //custom adapter for the listitem views
       Button button = view.findViewById(R.id.nowPlayingOnClick);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * checks to see if we should open the bottom sheet
             * @param v view
             */
            @Override
            public void onClick(View v)
            {
                // open the bottom sheet if the buttion is pressed
                BottomSheetFrag bottomSheet = new BottomSheetFrag();
                bottomSheet.show(getActivity().getSupportFragmentManager(), "exampleBottomSheet");
            }
        });

        ListView songList = view.findViewById(R.id.playListHolder);
        MyCustomListAdapter songsAdapter = new MyCustomListAdapter(getActivity(), mySongs);
        songList.setAdapter(songsAdapter);
        return view;
    }
    /**
     * populates object songs into the array list
     */
    private void populateMySongs() {
        mySongs.add(new music("Video1.mp4",false,4));
        mySongs.add(new music("sun.mp3",false,1));
        mySongs.add(new music("Video3.mp4",false,6));

    }
    /**
     * Custom adapter for list items
     */
    private class MyCustomListAdapter extends ArrayAdapter<music> {
        private Context context;
        private List<music> songs = null;
        /**
         *
         * @param context of the item frame
         * @param songs that will be populated into the frames
         */
        public MyCustomListAdapter(Context context,  List<music> songs) {
            super(context, 0, songs);
            this.songs = songs;
            this.context = context;
        }
        /**
         * @param position current position in the list
         * @param convertView converts the current view into whatever we set from our list
         * @param parent holds the parent ViewGroup (ListView )
         * @return
         */
        @NonNull
        @Override
        //get our custom list view and inflate it
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.music_layout, parent, false);
            //reference our views in the custom layout to the list view
            music currentContact = mySongs.get(position);

            TextView textViewSongName = itemView.findViewById(R.id.songNameTextView);
            ImageButton textViewFavorites = itemView.findViewById(R.id.musicFavoritesButton);
            textViewSongName.setText(currentContact.getSongName());

            textViewFavorites.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something

                }
            });

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            });

            return itemView;
        }
    }
}
