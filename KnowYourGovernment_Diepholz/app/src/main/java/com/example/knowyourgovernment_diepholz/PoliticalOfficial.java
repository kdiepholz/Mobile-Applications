package com.example.knowyourgovernment_diepholz;

import java.io.Serializable;

public class PoliticalOfficial implements Serializable {
    private String position = "";
    private String name = "";
    private String party = "";
    private String address = "";
    private String phone = "";
    private String email = "";
    private String website = "";
    private String photo = null;
    private String youtube = "";
    private String google = "";
    private String twitter = "";
    private String facebook = "";

    public PoliticalOfficial (String p, String n, String pa) {
        this.position = p;
        this.name = n;
        this.party = pa;
    }

    public PoliticalOfficial (String p) {
        this.position = p;
    }

    public String getPosition() {
        return position;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    @Override
    public String toString() {
        return "Official{" +
                "position='" + position + '\'' +
                ", name='" + name + '\'' +
                ", party='" + party + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                ", youtube='" + youtube + '\'' +
                ", google='" + google + '\'' +
                ", twitter='" + twitter + '\'' +
                ", facebook='" + facebook + '\'' +
                '}';
    }

}
