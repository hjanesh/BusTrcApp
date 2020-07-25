package com.bustrc101.ammu.model;

public class Data {
    private Double Dlan;
    private Double Dlat;
    private String Dtime;

    public Data(Double dlan, Double dlat, String dtime) {
        Dlan = dlan;
        Dlat = dlat;
        Dtime = dtime;
    }

    public Data() {
    }

    public Double getDlan() {
        return Dlan;
    }

    public Double getDlat() {
        return Dlat;
    }

    public String getDtime() {
        return Dtime;
    }
}
