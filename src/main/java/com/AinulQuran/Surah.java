package com.AinulQuran;

import com.AinulQuran.model.Malay_translation;
import com.AinulQuran.model.wbw;
import com.AinulQuran.repository.Malay_translationRepository;
import com.AinulQuran.repository.wbwRepository;
import org.jqurantree.analysis.AnalysisTable;
import org.jqurantree.arabic.encoding.EncodingType;
import org.jqurantree.orthography.Chapter;
import org.jqurantree.orthography.Document;
import org.jqurantree.orthography.Token;
import org.jqurantree.orthography.Verse;
import org.jqurantree.search.TokenSearch;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

public class Surah {



    int surah;
    int ayat;
    String word;
    List<Malay_translation> malay;
    wbwRepository wbwrepo;

    public wbwRepository getWbwrepo() {
        return wbwrepo;
    }

    public void setWbwrepo(wbwRepository wbwrepo) {
        this.wbwrepo = wbwrepo;
    }

    public List<Malay_translation> getMalay() {
        return malay;
    }

    public void setMalay(List<Malay_translation> malay) {
        this.malay = malay;
    }

    public Surah(int surah) {
        this.surah = surah;
    }

    public Surah(int surah,List<Malay_translation> malay) {
        this.surah = surah;
        this.malay=malay;
    }

    public Surah(int surah, int ayat, String word) {
        this.surah = surah;
        this.ayat = ayat;
        this.word = word;

    }
    public String getSurahName(){
        Chapter chap=Document.getChapter(this.surah);
        return chap.getName().toString();
    }


    public  Ayatperayat getSurah(){
        Chapter chapter = Document.getChapter(this.surah);
        Token token;
        Verse x;

        Ayatperayat db =  new Ayatperayat();
        String[] arrayoftranslation=new String[chapter.getVerseCount()];
        for(int i=0;i<arrayoftranslation.length;i++){
            Random r=new Random();
            arrayoftranslation[i]=(i+1)+" "+malay.get(i).getText()+"<br />  ";
        }
        List<wbw> wbw=wbwrepo.findBychapter(this.surah);


        int indexwbw=0;
        for (int i=1; i<=chapter.getVerseCount(); i++) {

            int chapterNum = chapter.getChapterNumber();
            x = Document.getVerse(chapterNum, i);
            WordbyWord arrr[] = new WordbyWord[x.getTokenCount()+1];
            for (int j = 1; j <= x.getTokenCount(); j++) {
                String arabtext,tr,tl_eng,tl_bahasa;
                if(!wbw.isEmpty()){
                    arabtext=wbw.get(indexwbw).getWordarabic();
                    tr=wbw.get(indexwbw).getWordtransliteration();
                    tl_eng=wbw.get(indexwbw).getWordenglish();
                    tl_bahasa=wbw.get(indexwbw).getWordmalay();
                }
                else {
                    arabtext="";
                    tl_eng=" ";
                    tl_bahasa=" ";
                    tr=" ";

                }

                arrr[j-1]=new WordbyWord(arabtext,tl_eng,tl_bahasa,tr);
             indexwbw++;
            }
            String ish=arrayoftranslation[i-1];
            //add arabic number to the end of each sentence
            arrr[arrr.length-1]=new WordbyWord(toArabicNum(i),"","","");
            db.map.put(ish,arrr);
        }

    return db;
    }


    public void searchSun(String ayat) {

        // Search for substring "$~amos" or exact token "$amosFA"
        TokenSearch search = new TokenSearch(EncodingType.Buckwalter);
        search.findToken(ayat);
        search.findSubstring(ayat);

        // Display the results.
        AnalysisTable table = search.getResults();
        System.out.println(table);

        System.out.println("Matches: " + table.getRowCount() + "\r\n");
    }

    public String toArabicNum(int n){
        String ArabicNum=n+"";
        ArabicNum=ArabicNum.replace("0","۰");
        ArabicNum=ArabicNum.replace("1","١");
        ArabicNum=ArabicNum.replace("2","٢");
        ArabicNum=ArabicNum.replace("3","٣");
        ArabicNum=ArabicNum.replace("4","٤");
        ArabicNum=ArabicNum.replace("5","٥");
        ArabicNum=ArabicNum.replace("6","٦");
        ArabicNum=ArabicNum.replace("7","٧");
        ArabicNum=ArabicNum.replace("8","٨");
        ArabicNum=ArabicNum.replace("9","٩");

        
        ArabicNum="﴾" +ArabicNum;

        ArabicNum=ArabicNum+"﴿";


        return ArabicNum;
    }
}
