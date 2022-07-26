package com.example.comc323proj7aohernan;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

/**PLEASE NOTE
 * i followed along with reference from
 * https://www.youtube.com/watch?v=3uAqwnxZMwI&t=0s
 * for this activity
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private Context myContext;
    private List<MovieClass> mydata;
    private onMovieClickListener onMovieClickListener1;

    public MovieAdapter(Context myContext, List<MovieClass> mydata, onMovieClickListener onMovieClickListener) {
        this.myContext = myContext;
        this.mydata = mydata;
        this.onMovieClickListener1 = onMovieClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(myContext);
        v = inflater.inflate(R.layout.moviecustomlayout,parent,false);
        return new MyViewHolder(v, onMovieClickListener1);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(mydata.get(position).getName());

        Glide.with(myContext)
                .load("https://image.tmdb.org/t/p/w500"+mydata.get(position).getImage())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mydata.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        ImageView image;
        onMovieClickListener onMovieClickListener;
        /**
         *
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
    public interface onMovieClickListener{
        void onMovieClick(int position);
    }
}
