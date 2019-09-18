package com.pwpb.latihanfirebase;

public class Track {
    private String id,namaTrack;
    private int rating;

    public Track(){
        //Empty Constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaTrack() {
        return namaTrack;
    }

    public void setNamaTrack(String namaTrack) {
        this.namaTrack = namaTrack;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Track(String id, String namaTrack, int rating) {
        this.id = id;
        this.namaTrack = namaTrack;
        this.rating = rating;
    }
}
