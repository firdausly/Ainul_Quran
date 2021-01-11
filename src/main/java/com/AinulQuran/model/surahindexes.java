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

    @Column(name = "Name_Eng")
    private String nameeng;

    @Column(name = "Name_Roman")
    private String nameroman;


    @Column(name = "Surah_Type")
    private String surahtype;


    public String getNameeng() {
        return nameeng;
    }

    public void setNameeng(String nameeng) {
        this.nameeng = nameeng;
    }

    public String getNameroman() {
        return nameroman;
    }

    public void setNameroman(String nameroman) {
        this.nameroman = nameroman;
    }

    public String getSurahtype() {
        return surahtype;
    }



    public void setSurahtype(String surahtype) {
        this.surahtype = surahtype;
    }

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

    @Override
    public String toString() {
        return "{" +
                "surano:'" + surano + '\'' +
                ", ayacount:'" + ayacount + '\'' +
                ", namearb='" + namearb + '\'' +
                ", nameeng='" + nameeng + '\'' +
                ", nameroman='" + nameroman + '\'' +
                ", surahtype='" + surahtype + '\'' +
                '}';
    }
}