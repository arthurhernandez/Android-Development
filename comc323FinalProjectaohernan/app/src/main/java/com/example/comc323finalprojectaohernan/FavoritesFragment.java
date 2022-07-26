package com.example.comc323finalprojectaohernan;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private static final int swipe = 100;
    private static final int swipeVelocity = 100;
    private List<Review> myFavorites = new ArrayList<>();
    private List<String> favoritesIDs = new ArrayList<>();
    ListView listView;
    ImageView imageView;
    FavoritesFragment.MyCustomListAdapter favoritesAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.favoritesfragment,container,false );
        populateMyFavorites();

        listView = view.findViewById(R.id.favortiesListLayout);
        favoritesAdapter = new MyCustomListAdapter(getActivity(), myFavorites);
        listView.setAdapter(favoritesAdapter);
        return view;
    }

    private void populateMyFavorites() {
        MyDBHandler dbHandler = new MyDBHandler(getContext(), null, null, 1);

        myFavorites = new ArrayList<Review>();
        favoritesIDs = new ArrayList<String>();
        Cursor res = dbHandler.getAllFavoritesData();

        while(res.moveToNext()){
            Review favorite = new Review();
            favoritesIDs.add(res.getString(0));
            favorite.setReviewName(res.getString(3));
            favorite.setReviewLocation(res.getString(4));
            favorite.setReviewImage(res.getString(5));
            myFavorites.add(favorite);
        }
    }

    private class MyCustomListAdapter extends ArrayAdapter<Review> {
        private Context context;
        private List<Review> products = null;
        /**
         *
         * @param context of the item frame
         * @param products that will be populated into the frames
         */
        public MyCustomListAdapter(Context context,  List<Review> products) {
            super(context, 0, products);
            this.products = products;
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
                itemView = getLayoutInflater().inflate(R.layout.favoritescustomlayout, parent, false);
            //reference our views in the custom layout to the list view
            Review currentContact = myFavorites.get(position);
            TextView favoritesName = itemView.findViewById(R.id.favoriteName);
            TextView favoritesLocation = itemView.findViewById(R.id.favoriteDistance);
            imageView = itemView.findViewById(R.id.favoriteImage);
            favoritesName.setText(currentContact.getReviewName());
            favoritesLocation.setText(currentContact.getReviewLocation());
            Bitmap imageName = currentContact.getReviewImage();
            Glide.with(context).load(imageName).into(imageView);
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(gestureDetector.onTouchEvent(event)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Are you sure you want to remove from your favorites?");
                        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                MyDBHandler dbHandler = new MyDBHandler(getContext(), null, null, 1);
                                String IDofFavorite = favoritesIDs.get(position);
                                dbHandler.deleteFavoritesItemDB(IDofFavorite);
                                myFavorites.remove(position);
                                favoritesIDs.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }});
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    return true;
                }
            });
            return itemView;
        }
    }


    GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
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
                            return true;
                        }
                    }
                }
                //catch anything that doesnt fit our bounds
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }
    });
}
