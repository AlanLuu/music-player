package com.music;

public final class Song implements Comparable<Song> {
    private long id;
    private String title;
    private String artist;
    private String album;

    public Song(long id, String title, String artist, String album) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
    }

    public Song(String title, String artist, String album) {
        this(0, title, artist, album);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Song)) return false;
        Song other = (Song) obj;
        return compareTo(other) == 0 && artist.equals(other.artist) && album.equals(other.album);
    }

    @Override
    public int compareTo(Song song) {
        return title.compareToIgnoreCase(song.title);
    }

    @Override
    public String toString() {
        return title + " " + artist + " " + album;
    }
}