package com.example.c323midtermprojectaohernan;

/**
 * constructor for Entry object with
 *      private String location;
 *     private String name;
 *     private String bestTimeToVisit;
 *     private String priceOrMetropolitanOrTourist;
 *     private String historyOfMonument;
 *     private int image; set as int because of bitmapping
 */
public class Entry {
    private String location;
    private String name;
    private String bestTimeToVisit;
    private String priceOrMetropolitanOrTourist;
    private String historyOfMonument;
    private int image;

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getBestTimeToVisit() {
        return bestTimeToVisit;
    }
    public void setBestTimeToVisit(String bestTimeToVisit) {
        this.bestTimeToVisit = bestTimeToVisit;
    }

    public String getpriceOrMetropolitanOrTourist() {
        return priceOrMetropolitanOrTourist;
    }
    public void setpriceOrMetropolitanOrTourist(String priceOrMetropolitanOrTourist) {
        this.priceOrMetropolitanOrTourist = priceOrMetropolitanOrTourist;
    }

    public String gethistoryOfMonument() {
        return historyOfMonument;
    }
    public void sethistoryOfMonument(String historyOfMonument) {
        this.historyOfMonument = historyOfMonument;
    }

    public int getImage() {
        return 1;
    }
    public void setimage(int image) {
        this.image = image;
    }

    /**
     *
     * @param location
     * @param name
     * @param bestTimeToVisit
     * @param priceOrMetropolitanOrTourist
     * @param historyOfMonument
     * @param image
     */
    public Entry(String location, String name, String bestTimeToVisit, String priceOrMetropolitanOrTourist,String historyOfMonument, int image) {
        this.location = location;
        this.name = name;
        this.bestTimeToVisit = bestTimeToVisit;
        this.priceOrMetropolitanOrTourist = priceOrMetropolitanOrTourist;
        this.historyOfMonument = historyOfMonument;
        this.image = image;
    }

    public void remove(int position) {

    }
}
