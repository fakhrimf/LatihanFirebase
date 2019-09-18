package com.pwpb.latihanfirebase;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    OnUserClickListener listener;
    private List<Artist> artistList;

    public ArtistAdapter(List<Artist> artistList, OnUserClickListener listener) {
        this.artistList = artistList;
        this.listener = listener;
    }

    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rv_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ArtistAdapter.ViewHolder holder, int position) {
        final Artist a = artistList.get(position);
        holder.tvNama.setText(a.getName());
        holder.tvGenre.setText(a.getGenre());
        holder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserClick(a.getId(),a.getName());
            }
        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserEdit(a.getId(),a.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public interface OnUserClickListener {
        void onUserClick(String id, String nama);
        void onUserEdit(String id, String nama);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNama, tvGenre;
        public ImageView ivEdit;
        ConstraintLayout bg;

        public ViewHolder(View v) {
            super(v);
            tvNama = v.findViewById(R.id.tvNama);
            tvGenre = v.findViewById(R.id.tvGenre);
            bg = v.findViewById(R.id.bg);
            ivEdit = v.findViewById(R.id.ivEdit);
        }
    }
}
