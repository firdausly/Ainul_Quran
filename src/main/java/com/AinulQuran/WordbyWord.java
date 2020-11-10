package com.AinulQuran;

public class WordbyWord {
     String ayat;
     String translation_eng;
     String translation_bahasa;
     String trannsliteration;

    public WordbyWord(String ayat, String translation_eng, String translation_bahasa, String trannsliteration) {
        this.ayat = ayat;
        this.translation_eng = translation_eng;
        this.translation_bahasa = translation_bahasa;
        this.trannsliteration = trannsliteration;
    }

    public String getTranslation_eng() {
        return translation_eng;
    }

    public void setTranslation_eng(String translation_eng) {
        this.translation_eng = translation_eng;
    }

    public String getTranslation_bahasa() {
        return translation_bahasa;
    }

    public void setTranslation_bahasa(String translation_bahasa) {
        this.translation_bahasa = translation_bahasa;
    }

    public String getAyat() {
        return ayat;
    }

    public void setAyat(String ayat) {
        this.ayat = ayat;
    }


    public String getTrannsliteration() {
        return trannsliteration;
    }

    public void setTrannsliteration(String trannsliteration) {
        this.trannsliteration = trannsliteration;
    }
}

