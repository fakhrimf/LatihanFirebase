package com.pwpb.latihanfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TracksAct extends AppCompatActivity implements TrackAdapter.OnUserClickListener {

    //    TODO = Create TracksAct function to be attached to artist column? or table idk, Intinya lanjutin

    TextView tvRating, artistName;
    EditText edtNamaTrack;
    Button btnTambah;
    RecyclerView rv;
    SeekBar sb;
    String nama, id;
    DatabaseReference dbTracks;
    List<Track> trackList = new ArrayList<>();

    private void initUI() {
        tvRating = findViewById(R.id.tvRating);
        artistName = findViewById(R.id.tvArtistName);
        edtNamaTrack = findViewById(R.id.edtTrack);
        btnTambah = findViewById(R.id.btnAddTrack);
        sb = findViewById(R.id.seekBar);
        rv = findViewById(R.id.rvTrack);

        //Getting String extra from Intent
        Intent i = getIntent();
        nama = i.getStringExtra("nama");
        id = i.getStringExtra("id");

        //Setting Artist's name
        artistName.setText(nama);
    }
    private void initBtn() {
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrack();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);
        initUI();
        initBtn();
        dbTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvRating.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trackList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Track track = postSnapshot.getValue(Track.class);
                    trackList.add(track);
                    RecyclerView.LayoutManager lm = new LinearLayoutManager(TracksAct.this);
                    rv.setLayoutManager(lm);
                    TrackAdapter trackAdapter = new TrackAdapter(trackList, TracksAct.this);
                    rv.setAdapter(trackAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveTrack() {
        String namaTrack = edtNamaTrack.getText().toString().trim();
        int rating = sb.getProgress();
        if (namaTrack.isEmpty()) {
            edtNamaTrack.setError("Nama track harus diisi");
        } else {
            String id = dbTracks.push().getKey();
            Track track = new Track(id, namaTrack, rating);
            dbTracks.child(id).setValue(track);
            Toast.makeText(this, namaTrack + " Saved", Toast.LENGTH_SHORT).show();
            edtNamaTrack.setText("");
        }
    }

    @Override
    public void onUserClick(String id, String nama) {

    }
}
