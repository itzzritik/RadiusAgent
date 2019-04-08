package com.radiusagent.radius;
import android.content.Intent;
        import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
        import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private List<Users> users;
    private MainActivity activity;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,email,age;
        RelativeLayout item;
        ImageView dp;
        ProgressBar glidepro;
        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            name.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/exo2.ttf"));
            email = view.findViewById(R.id.email);
            email.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/exo2.ttf"));
            age = view.findViewById(R.id.age);
            age.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/exo2.ttf"));
            dp = view.findViewById(R.id.dp);
            item = view.findViewById(R.id.item);
            glidepro = view.findViewById(R.id.glidepro);
        }
    }
    UserAdapter(MainActivity activity,List<Users> users) {
        this.users = users;
        this.activity = activity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,int position) {
        final Users user = users.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.age.setText(user.getAge());

        Glide.with(activity).load(user.getDp())
                .apply(new RequestOptions()
                        .centerCrop()
                        .override(Target.SIZE_ORIGINAL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.glidepro.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.dp);
    }
    @Override
    public int getItemCount() {
        return users.size();
    }
}