package com.pwpb.latihanfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ArtistAdapter.OnUserClickListener{

    //Assigning value for XML Stuffs
    EditText edtNama;
    Button btnAdd;
    Spinner sGenres;
    RecyclerView rv;

    //List for storing database values
    List<Artist> artistList = new ArrayList<>();

    //Database Reference
    DatabaseReference dbArtist;

    //Initialization of UI and Button functions
    private void initUI() {
        edtNama = findViewById(R.id.edtArtist);
        btnAdd = findViewById(R.id.btnAdd);
        sGenres = findViewById(R.id.spinnerGenres);
        rv = findViewById(R.id.rv);
    }

    private void initBtn() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArtist();
            }
        });
    }

    //Add Artist Function, Adding Artist name and genre to FireBase
    private void addArtist() {
        String name = edtNama.getText().toString().trim();
        String genre = sGenres.getSelectedItem().toString().trim();

        if (name.isEmpty()) {
            edtNama.setError("Nama harus diisi"); //Setting error for the EditText
        } else {
            String id = dbArtist.push().getKey(); //Create a unique string for artist (id)
            Artist artist = new Artist(id, name, genre); //Creating new POJO for artist
            dbArtist.child(id).setValue(artist); //Inserting artist model value to dbArtist under the unique id child
            Toast.makeText(this, name + " inserted!", Toast.LENGTH_SHORT).show();
            edtNama.setText("");
        }
    }

    //Update Artist Function, Updating Artist name and genre to FireBase
    private boolean updateArtist(String id, String name, String genre) {
        //Getting the specified Artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("artists").child(id);

        //Updating Artist
        Artist artist = new Artist(id, name, genre);
        dR.setValue(artist);
        Toast.makeText(getApplicationContext(), artist.getName()+" Updated", Toast.LENGTH_SHORT).show();
        return true;
    }

    //Delete Artist Function
    private boolean deleteArtist(String id) {
        //Getting the specified Artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("artists").child(id);
        //Removing Artist
        dR.removeValue();
        //Getting the Tracks reference for the specified Artist
        DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);
        //Removing all Tracks
        drTracks.removeValue();
        Toast.makeText(getApplicationContext(), "Artist Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    //Showing Update and Delete Dialog
    private void showUpdateDeleteDialog(final String artistId, String artistName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final Spinner spinnerGenre = dialogView.findViewById(R.id.spinnerGenres);
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdateArtist);
        final Button buttonDelete = dialogView.findViewById(R.id.buttonDeleteArtist);

        dialogBuilder.setTitle(artistName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String genre = spinnerGenre.getSelectedItem().toString();
                if (!name.isEmpty()) {
                    updateArtist(artistId, name, genre);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArtist(artistId);
                b.dismiss();
            }
        });
    }

    //Overriding onStart
    @Override
    protected void onStart() {
        super.onStart();
        //Adding value event listener
        dbArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Clearing previous list
                artistList.clear();

                //Iterating (Performing repeatedly) through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Getting Artists
                    Artist artist = postSnapshot.getValue(Artist.class);
                    //Adding to the List
                    artistList.add(artist);
                }

                //Adding Layout Manager
                rv.setHasFixedSize(true);
                RecyclerView.LayoutManager lm = new LinearLayoutManager(MainActivity.this);
                rv.setLayoutManager(lm);

                //Creating RecyclerView Adapter
                ArtistAdapter artistAdapter = new ArtistAdapter(artistList,MainActivity.this);
                //Attaching Adapter
                rv.setAdapter(artistAdapter);
                artistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Empty
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Calling UI and Button Initializing function
        initUI();
        initBtn();

        //Getting Reference from the FireBaseDatabase
        dbArtist = FirebaseDatabase.getInstance().getReference("artists");
    }

    @Override
    public void onUserClick(String id, String nama) {
        Intent i = new Intent(this,TracksAct.class);
        System.out.println(id);
        i.putExtra("id",id);
        i.putExtra("nama",nama);
        startActivity(i);
    }

    @Override
    public void onUserEdit(String id, String nama) {
        showUpdateDeleteDialog(id,nama);
    }
}
