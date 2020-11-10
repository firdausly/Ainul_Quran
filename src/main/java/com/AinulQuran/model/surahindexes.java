package com.AinulQuran.model;

import javax.persistence.*;


@Entity
@Table(name = "surahindexes")
public class surahindexes {

    @Id
    @Column(name = "Sura_No")
    private int surano;

    @Column(name = "Aya_Count")
    private int ayacount;

    @Column(name = "Name_Arb")
    private String namearb;




    public surahindexes() {};

    public int getSurano() {
        return surano;
    }

    public void setSurano(int surano) {
        this.surano = surano;
    }

    public int getAyacount() {
        return ayacount;
    }

    public void setAyacount(int ayacount) {
        this.ayacount = ayacount;
    }

    public String getNamearb() {
        return namearb;
    }

    public void setNamearb(String namearb) {
        this.namearb = namearb;
    }
}