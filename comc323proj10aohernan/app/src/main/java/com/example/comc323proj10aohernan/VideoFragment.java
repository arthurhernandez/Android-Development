package com.example.comc323proj10aohernan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
/**
 * Fragment for the Video list
 */
public class VideoFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        ListView songList = view.findViewById(R.id.musicListHolder);
        MyCustomListAdapter songsAdapter = new MyCustomListAdapter(getActivity(), mySongs);
        songList.setAdapter(songsAdapter);

        return view;

    }
    /**
     * populates object songs into the array list
     */
    private void populateMySongs() {
        mySongs.add(new music("Video1.mp4",false,4));
        mySongs.add(new music("Video2.mp4",false,5));
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
         *
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
