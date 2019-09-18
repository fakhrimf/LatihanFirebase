package com.pwpb.latihanfirebase;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder>{
    private List<Artist> artistList;

    public ArtistAdapter(List<Artist> artistList) {
        this.artistList = artistList;
    }

    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rv_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ArtistAdapter.ViewHolder holder, int position) {
        Artist a = artistList.get(position);
        holder.tvNama.setText(a.getName());
        holder.tvGenre.setText(a.getGenre());
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNama, tvGenre;

        public ViewHolder(View v) {
            super(v);
            tvNama = v.findViewById(R.id.tvNama);
            tvGenre = v.findViewById(R.id.tvGenre);
        }
    }
}
