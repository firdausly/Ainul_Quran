package com.AinulQuran.model;

import javax.persistence.*;



@Entity
@Table(name = "Wbw", uniqueConstraints=
@UniqueConstraint(columnNames={"ayat", "word","chapter"}))
public class wbw {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private int chapter;
    private int ayat;
    private int word;

    @Column(name = "word_arabic")
    private String wordarabic;

    @Column(name = "word_english")
    private String wordenglish;

    @Column(name = "word_malay")
    private String wordmalay;

    private String wordtransliteration;


    public wbw() {};

    public wbw(Integer id, int chapter, int ayat, int word, String wordarabic, String wordenglish, String wordmalay, String wordtransliteration) {
        this.id = id;
        this.chapter = chapter;
        this.ayat = ayat;
        this.word = word;
        this.wordarabic = wordarabic;
        this.wordenglish = wordenglish;
        this.wordmalay = wordmalay;
        this.wordtransliteration = wordtransliteration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getAyat() {
        return ayat;
    }

    public void setAyat(int ayat) {
        this.ayat = ayat;
    }

    public int getWord() {
        return word;
    }

    public void setWord(int word) {
        this.word = word;
    }

    public String getWordarabic() {
        return wordarabic;
    }

    public void setWordarabic(String wordarabic) {
        this.wordarabic = wordarabic;
    }

    public String getWordenglish() {
        return wordenglish;
    }

    public void setWordenglish(String wordenglish) {
        this.wordenglish = wordenglish;
    }

    public String getWordmalay() {
        return wordmalay;
    }

    public void setWordmalay(String wordmalay) {
        this.wordmalay = wordmalay;
    }

    public String getWordtransliteration() {
        return wordtransliteration;
    }

    public void setWordtransliteration(String wordtransliteration) {
        this.wordtransliteration = wordtransliteration;
    }
}