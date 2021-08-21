package com.booklover.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.booklover.R;
import com.booklover.model.Book;
import com.bumptech.glide.Glide;

import java.util.List;



public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private static final String TAG = "Adapter";

    private Context mContext;
    private List<Book> mData;

    public BookAdapter(Context mContext, List<Book> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.book_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.MyViewHolder holder, int position) {

        holder.name.setText(mData.get(position).getName());
        holder.writer.setText(mData.get(position).getWriter());
        holder.category.setText(mData.get(position).getCategory());
        holder.edition.setText(mData.get(position).getEdition());
        holder.description.setText(mData.get(position).getDescription());
        holder.downloadLink = mData.get(position).getDonwload();

        //Koristimo Glide za prikaz slike
        Glide.with(mContext)
                .load(mData.get(position).getImage())
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,writer,category,edition,description;
        ImageView image,download,like;
        String downloadLink;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.book_name);
            writer = itemView.findViewById(R.id.book_writer);
            category = itemView.findViewById(R.id.book_category);
            edition = itemView.findViewById(R.id.book_edition);
            description = itemView.findViewById(R.id.book_description);
            image = itemView.findViewById(R.id.book_image);
            download = itemView.findViewById(R.id.book_download);
            like = itemView.findViewById(R.id.book_like);

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = downloadLink;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    view.getContext().startActivity(i);
                }
            });

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Drawable.ConstantState imgID = like.getDrawable().getConstantState();
                    Drawable.ConstantState imgID2 = view.getContext().getDrawable(R.drawable.ic_favorite_red).getConstantState();
                    if(imgID!=imgID2)
                    {
                        like.setImageResource(R.drawable.ic_favorite_red);
                        Toast.makeText(view.getContext(),"Svidja vam se "+name.getText(),Toast.LENGTH_SHORT).show();
                    }
                    else {
                        like.setImageResource(R.drawable.ic_favorite);
                        Toast.makeText(view.getContext(),"Ne svidja vam se "+name.getText(),Toast.LENGTH_SHORT).show();
                    }// Other Block of Code



                }
            });
        }
    }


}
