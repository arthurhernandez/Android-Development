package com.example.c323proj11aohernan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * This class is our category adapter but is really used for the categories and the recipies.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context myContext;
    private List<CategoryClass> mydata;
    private onMovieClickListener onMovieClickListener1;

    /**
     *
     * @param myContext passes back the context
     * @param mydata holds our values for later
     * @param onMovieClickListener is the on click listener for any event in a list
     */
    public CategoryAdapter(Context myContext, List<CategoryClass> mydata, onMovieClickListener onMovieClickListener) {
        this.myContext = myContext;
        this.mydata = mydata;
        this.onMovieClickListener1 = onMovieClickListener;
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     * Our overall view holder and inflater for our first 2 activities
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(myContext);
        v = inflater.inflate(R.layout.categorycustomlayout,parent,false);
        return new MyViewHolder(v, onMovieClickListener1);
    }

    /**
     * Prepares to bind the name and the image of the current position to the view it belongs to
     * It also uses glide for the image
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(mydata.get(position).getName());

        Glide.with(myContext)
                .load(mydata.get(position).getImage())
                .into(holder.image);
    }

    /**
     * counts the data size
     * @return
     */
    @Override
    public int getItemCount() {
        return mydata.size();
    }

    /**
     * This is what we actually use to set each view with the current resource
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        ImageView image;
        onMovieClickListener onMovieClickListener;
        /**
         *sets our instantiations
         * @param itemView
         * @param onMovieClickListener
         */
        public MyViewHolder(@NonNull View itemView, onMovieClickListener onMovieClickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.movieTitleTextView);
            image = itemView.findViewById(R.id.movieImage);
            this.onMovieClickListener = onMovieClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMovieClickListener.onMovieClick(getAdapterPosition());

        }
    }

    /**
     * interface between items being clicked
     */
    public interface onMovieClickListener{
        void onMovieClick(int position);

    }
}

