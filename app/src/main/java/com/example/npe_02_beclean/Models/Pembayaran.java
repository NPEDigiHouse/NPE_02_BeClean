package com.example.npe_02_beclean.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Pembayaran implements Parcelable {
    private String category, teamName, address, members;
    private double distance, duration;
    private int costService, costTransport, costTotal;

    public Pembayaran(String category, String teamName, String address, String members,
                      double distance, double duration,
                      int costService, int costTransport, int costTotal) {
        this.category = category;
        this.teamName = teamName;
        this.address = address;
        this.members = members;
        this.distance = distance;
        this.duration = duration;
        this.costService = costService;
        this.costTransport = costTransport;
        this.costTotal = costTotal;
    }

    protected Pembayaran(Parcel in) {
        category = in.readString();
        teamName = in.readString();
        address = in.readString();
        members = in.readString();
        distance = in.readDouble();
        duration = in.readDouble();
        costService = in.readInt();
        costTransport = in.readInt();
        costTotal = in.readInt();
    }

    public static final Creator<Pembayaran> CREATOR = new Creator<Pembayaran>() {
        @Override
        public Pembayaran createFromParcel(Parcel in) {
            return new Pembayaran(in);
        }

        @Override
        public Pembayaran[] newArray(int size) {
            return new Pembayaran[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(teamName);
        dest.writeString(address);
        dest.writeString(members);
        dest.writeDouble(distance);
        dest.writeDouble(duration);
        dest.writeInt(costService);
        dest.writeInt(costTransport);
        dest.writeInt(costTotal);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public int getCostService() {
        return costService;
    }

    public void setCostService(int costService) {
        this.costService = costService;
    }

    public int getCostTransport() {
        return costTransport;
    }

    public void setCostTransport(int costTransport) {
        this.costTransport = costTransport;
    }

    public int getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(int costTotal) {
        this.costTotal = costTotal;
    }

}
