package com.AinulQuran.model;

import javax.persistence.*;


@Entity
@Table(name = "malay_translation")
public class Malay_translation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private int sura;
    private int aya;

    @Column(columnDefinition = "TEXT")
    private String text;


    @Override
    public String toString() {
        return "Malay_translation{" +
                "index=" + id +
                ", sura=" + sura +
                ", aya=" + aya +
                ", text='" + text + '\'' +
                '}';
    }

    public Malay_translation() {}


    public Malay_translation(Integer index, int sura, int aya, String text) {
        this.id= index;
        this.sura = sura;
        this.aya = aya;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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