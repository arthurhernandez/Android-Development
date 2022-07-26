package com.example.comc323proj10aohernan;


/**
 * Creates objects of music with names, whether they are favorites, and there index number
 */
public class music {
    private String songName;
    Boolean favorites;
    int songNumber;


    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public Boolean getFavorites() {
        return favorites;
    }

    public void setFavorites(Boolean favorites) {
        this.favorites = favorites;
    }

    public int getSongNumber() {
        return songNumber;
    }

    public void setSongNumber(int songNumber) {
        this.songNumber = songNumber;
    }

    /**
     * @param songName stores the song name
     * @param favorites
     * @param songNumber
     */

    public music(String songName,Boolean favorites, int songNumber) {

        this.songName = songName;
        this.favorites = favorites;
        this.songNumber = songNumber;

    }
}
