package com.AinulQuran;

import java.util.ArrayList;

public class Dataforhighlight {
    private int surano;
    private ArrayList<String> word=new ArrayList<>();

    public Dataforhighlight(){}

    public Dataforhighlight(int surano, ArrayList<String> word) {
        this.surano = surano;
        this.word = word;
    }

    public int getSurano() {
        return surano;
    }

    public void setSurano(int surano) {
        this.surano = surano;
    }

    public ArrayList<String> getWord() {
        return word;
    }

    public void setWord(ArrayList<String> word) {
        this.word = word;
    }
}
