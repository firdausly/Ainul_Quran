package com.AinulQuran.model;

import javax.persistence.*;



@Entity
@Table(name = "Wbw", uniqueConstraints=
@UniqueConstraint(columnNames={"ayat", "word","chapter"}))
public class wbw {

    @Id
    private Integer id;

    @Column(name = "chapter")
    private int chapter;

    @Column(name = "ayat")
    private int ayat;

    @Column(name = "word")
    private int word;

    @Column(name = "word_arabic")
    private String wordarabic;

    @Column(name = "word_english")
    private String wordenglish;

    @Column(name = "word_malay")
    private String wordmalay;

    private String wordtransliteration;


    public wbw() {};

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

    @Override
    public String toString() {
        return "{" +
                "id:'" + id + '\'' +
                ", chapter:'" + chapter + '\'' +
                ", ayat:'" + ayat + '\'' +
                ", word:'" + word + '\'' +
                ", wordarabic:'" + wordarabic + '\'' +
                ", wordenglish:'" + wordenglish + '\'' +
                ", wordmalay:'" + wordmalay + '\'' +
                ", wordtransliteration:'" + wordtransliteration + '\'' +
                '}';
    }
}