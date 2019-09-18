package com.pwpb.latihanfirebase;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    OnUserClickListener listener;
    private List<Track> trackList;

    public TrackAdapter(List<Track> artistList, OnUserClickListener listener) {
        this.trackList = artistList;
        this.listener = listener;
    }

    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rv_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TrackAdapter.ViewHolder holder, int position) {
        final Track a = trackList.get(position);
        holder.tvTrack.setText(a.getNamaTrack());
        holder.tvRating.setText(a.getRating()+"");
        holder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserClick(a.getId(),a.getRating()+"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public interface OnUserClickListener {
        void onUserClick(String id, String nama);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTrack, tvRating;
        ConstraintLayout bg;

        public ViewHolder(View v) {
            super(v);
            tvTrack = v.findViewById(R.id.tvNama);
            tvRating = v.findViewById(R.id.tvGenre);
            bg = v.findViewById(R.id.bg);
        }
    }
}
