package com.example.comc323finalprojectaohernan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DashBoardFragment extends Fragment{
    private static final int CAMERA_REQUEST_CODE = 200;
    private List<Category> myCategories = new ArrayList<>();
    ListView listView;
    Boolean picTaken = false;
    ImageView imageView;
    MyCustomListAdapter categoriesAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dashboard_fragment,container,false );

        FloatingActionButton addButton = view.findViewById(R.id.addCategory);
        addButton.setOnClickListener(
                new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                alertDialogueClicked(view);
            }
        });

        myCategories = new ArrayList<>();
        populateMyCategories();

        listView = view.findViewById(R.id.categoryList);
        categoriesAdapter = new MyCustomListAdapter(getActivity(), myCategories);
        listView.setAdapter(categoriesAdapter);
        return view;
    }


    private void populateMyCategories() {
        MyDBHandler dbHandler = new MyDBHandler(getContext(), null, null, 1);

        if(dbHandler.isCategoriesEmpty()){
            User user = dbHandler.findUserDB();
            Category movies = new Category("Movies",user.getUserImage());
            Category restaurants = new Category("Restaurants",user.getUserImage());
            Category books = new Category("Books",user.getUserImage());
            Category electronics = new Category("Electronics",user.getUserImage());

            dbHandler.addCategoryToDB(movies);
            dbHandler.addCategoryToDB(restaurants);
            dbHandler.addCategoryToDB(books);
            dbHandler.addCategoryToDB(electronics);
        }

        myCategories = new ArrayList<Category>();
        Cursor res = dbHandler.getAllCategoryData();

        while(res.moveToNext()){
            Category category = new Category();
            category.set_category_name(res.getString(1));
            category.set_category_image(res.getString(2));
            myCategories.add(category);
        }
    }

    private class MyCustomListAdapter extends ArrayAdapter<Category> {
        private Context context;
        private List<Category> products = null;
        /**
         *
         * @param context of the item frame
         * @param products that will be populated into the frames
         */
        public MyCustomListAdapter(Context context,  List<Category> products) {
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
            Category currentContact = myCategories.get(position);

            TextView textName = itemView.findViewById(R.id.categoryName);
            ImageView imageView = itemView.findViewById(R.id.categoryImage);
            textName.setText(currentContact.get_category_name());
            Bitmap imageName = currentContact.get_category_image();
            Glide.with(context).load(imageName).into(imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), ReviewListActivity.class /*from, to*/);
                    myIntent.putExtra("category",currentContact.get_category_name());
                    startActivity(myIntent);
                }
            });
            return itemView;
        }
    }

    private void alertDialogueClicked(View view) {
        // create an alert builder
        picTaken = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add New Category");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.categorycustomalertdialog, null);
        builder.setView(customLayout);

        //TODO make image caputer inside here work
        // add a button for adding image
        imageView = customLayout.findViewById(R.id.imageView);
        Button addPhoto = customLayout.findViewById(R.id.addAPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

            }
        });

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                EditText editText = customLayout.findViewById(R.id.categoryNewName);


                MyDBHandler dbHandler = new MyDBHandler(getContext(), null, null, 1);
                if(editText.getText().toString().length() != 0){

                    Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    Category newCategory = new Category(editText.getText().toString(), image);
                    dbHandler.addCategoryToDB(newCategory);

                    Category category = new Category(editText.getText().toString(),image);
                    myCategories.add(category);

                    sendDialogDataToActivity(true);

                    Toast.makeText(getContext(), "ADDED", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), editText.getText().toString() + picTaken.toString() + "", Toast.LENGTH_SHORT).show();

                }
            }
        });

        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK ) {

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);

                //selectedImage = data.getData();
                //imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                //imageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
                //picTaken = true;
        }
    }

    // do something with the data coming from the AlertDialog
    private void sendDialogDataToActivity(boolean result) {
        if(result) {
            categoriesAdapter.notifyDataSetChanged();
        }
    }

}
