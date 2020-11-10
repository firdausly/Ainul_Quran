package com.AinulQuran.model;

import javax.persistence.*;


@Entity
@Table(name = "malay_translation")
public class Malay_translation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer index;

    private int sura;
    private int aya;

    @Column(columnDefinition = "TEXT")
    private String text;



    public Malay_translation() {}


    public Malay_translation(Integer index, int sura, int aya, String text) {
        this.index= index;
        this.sura = sura;
        this.aya = aya;
        this.text = text;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public int getSura() {
        return sura;
    }

    public void setSura(int sura) {
        this.sura = sura;
    }

    public int getAya() {
        return aya;
    }

    public void setAya(int aya) {
        this.aya = aya;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}