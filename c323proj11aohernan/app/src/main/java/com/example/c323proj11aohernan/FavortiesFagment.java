package com.example.c323proj11aohernan;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment is used for the fravorites list
 * grabbing items from our sql database
 */
public class FavortiesFagment extends Fragment {
    private List<Product> myProducts = new ArrayList<>();
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //populates our music objects into mysongs list
        //inflates the view
        View view = inflater.inflate(R.layout.favoritesfragment, container, false);
        populateMyProducts();
        //creating a custom adapter for the listview
        listView = view.findViewById(R.id.listLayout);
        MyCustomListAdapter songsAdapter = new MyCustomListAdapter(getActivity(), myProducts);
        listView.setAdapter(songsAdapter);
        return view;
    }

    /**
     * This will use our db handler and get all the data that we stored in the db
     * it will also add the Products created with the object constructor and add them to our products list to be populated
     */
    private void populateMyProducts() {
        MyDBHandler dbHandler = new MyDBHandler(getContext(), null, null, 1);
        Cursor res = dbHandler.getAllData();

        while(res.moveToNext()){
            myProducts.add(new Product(res.getString(0),res.getString(1),res.getString(2),res.getString(3)));
        }
    }

    private class MyCustomListAdapter extends ArrayAdapter<Product> {
        private Context context;
        private List<Product> products = null;
        /**
         *
         * @param context of the item frame
         * @param products that will be populated into the frames
         */
        public MyCustomListAdapter(Context context,  List<Product> products) {
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
                itemView = getLayoutInflater().inflate(R.layout.categorycustomlayout, parent, false);
            //reference our views in the custom layout to the list view

            Product currentContact = myProducts.get(position);

            TextView textName = itemView.findViewById(R.id.movieTitleTextView);
            ImageView imageView = itemView.findViewById(R.id.movieImage);
            textName.setText(currentContact.get_productName());
            String imageName = currentContact.get_productDate();
            Glide.with(context).load(imageName).into(imageView);


            return itemView;
        }
    }

}
