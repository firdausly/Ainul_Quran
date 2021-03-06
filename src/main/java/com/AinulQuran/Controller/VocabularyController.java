package com.AinulQuran.Controller;

import com.AinulQuran.Dataforchart;
import com.AinulQuran.model.*;
import com.AinulQuran.repository.*;
import com.AinulQuran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;


@Controller
public class VocabularyController {
    @Autowired
    private UserService service;

    @Autowired
    private UserVocab uservocabrepo;

    @Autowired
    private dictionaryrepo dictionaryrepo;

    @Autowired
    private wbwRepository wbwrepo;

    @Autowired
    private surahindexesrepo surahindexrepo;


    @GetMapping("/vocab/{vocab}")
    public String savevocab(@PathVariable("vocab") String vocabulary, Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentusername=auth.getName();

        boolean checkword=wbwrepo.findByWordarabic(vocabulary).isEmpty();

        User currentUser=service.findByUsername(currentusername);
        user_vocab user_vocabulary=new user_vocab();
        user_vocab check=uservocabrepo.findByUsernameAndVocab(currentUser.getUsername(),vocabulary);
        user_vocabulary.setUsername(currentusername);
        user_vocabulary.setVocab(vocabulary);

        if(check!=null ){
            String error="The word already existed in your vocabulary!";
            model.addAttribute("errorMessage",error);
            return "paid/vocab";
        }
        else if(checkword){
            String error="The word is not a valid word in Quran!";
            model.addAttribute("errorMessage",error);
            return "paid/vocab";
        }
        else {//save if not exist yet
            uservocabrepo.save(user_vocabulary);
        }


        return "redirect:/vocab";
    }

    @Transactional
    @GetMapping("/remove/{vocab}")
    public String deletevocab(@PathVariable("vocab") String vocabulary, Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentusername=auth.getName();

        User currentUser=service.findByUsername(currentusername);
        user_vocab check=uservocabrepo.findByUsernameAndVocab(currentusername,vocabulary);



        if(check==null ){
            String error="The word does not exist in your vocabulary!";
            model.addAttribute("errorMessage",error);
            return "paid/vocab";
        }
        else {//delete
            uservocabrepo.deleteByUsernameAndVocab(currentusername,vocabulary);
            String success="The word "+vocabulary+" has been successfully deleted from your vocabulary list";
            model.addAttribute("successMessage",success);

        }


        return "paid/vocab";
    }

    @GetMapping("/vocab")
    public String vocab(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentusername=auth.getName();


        User currentUser=service.findByUsername(currentusername);
        List<user_vocab> listuservocabulary=uservocabrepo.findByUsername(currentusername);

        long count=0;
        for(int i=0;i<listuservocabulary.size();i++){
            String arabicwordvocabulary=listuservocabulary.get(i).getVocab();

            //count matching saved word frequency and add it to count var
            count+=wbwrepo.countByWordarabic(arabicwordvocabulary);
        }

        //count total word in the whole Quran
        int totalWord=Integer.parseInt(wbwrepo.count()+"");

        //total frequency of all saved word / total word count in the whole quran
        double totalPercentageLearned=(double)count/(double)totalWord*100;

        ArrayList<String> percentagePerSurah=new ArrayList<>();


        for(int i=1;i<=114;i++){
            //count total word per Surah.
            int totalWordPerSurah=Integer.parseInt(wbwrepo.countBychapter(i)+"");

            int countPerSurah=0;
            for(int j=0;j<listuservocabulary.size();j++){
                String arabicwordvocabulary=listuservocabulary.get(j).getVocab();

                //count matching saved word frequency Per ayat in Current chapter index
                // and add it to count var
                countPerSurah+=wbwrepo.countBychapterAndWordarabic(i,arabicwordvocabulary);
            }


            //total frequency of all saved word / total word count per Surah
            double totalPercentageLearnedPerSurah=(double)countPerSurah/(double)totalWordPerSurah*100;
            //format the floating point
            String totalPercentageLearnedPerSurahString=String.format("%.3f",totalPercentageLearnedPerSurah)+"";
            percentagePerSurah.add(totalPercentageLearnedPerSurahString);
        }



        String totalPercentageLearnedString=String.format("%.3f",totalPercentageLearned)+"";
        model.addAttribute("totalpercentage",totalPercentageLearnedString);
        model.addAttribute("totalpercentagepersurah",percentagePerSurah);
        model.addAttribute("wbw",listuservocabulary);




        return "paid/vocab";
    }



    @GetMapping("/vocab/viewword/{word}")
    public String viewword(@PathVariable("word") String word,Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentusername=auth.getName();


        User currentUser=service.findByUsername(currentusername);
        List<user_vocab> listuservocabulary=uservocabrepo.findByUsername(currentusername);

        model.addAttribute("wbw",listuservocabulary);
        List<wbw> wordselected=wbwrepo.findByWordarabic(word);

        if(wordselected.isEmpty()){
            return "redirect:/vocab"+"?wordnotexist";
        }

        int totalWord=Integer.parseInt(wbwrepo.count()+"");
        int selectedWordCount=wordselected.size();

        double totalPercentageLearned=(double)selectedWordCount/(double)totalWord*100;

        String totalPercentageLearnedString=String.format("%.4f",totalPercentageLearned)+"%";
        model.addAttribute("wordselected",wordselected);
        model.addAttribute("totalpercentagelearned",totalPercentageLearnedString);






        ArrayList<Dataforchart> test=new ArrayList<Dataforchart>();
        model.addAttribute("word",word);

        ArrayList<Integer> chapterlist=new ArrayList<>();
        //fill up..
        for(int i=1;i<=114;i++){
            long count=wbwrepo.countBychapterAndWordarabic(i,word);
            if(count>0){
                chapterlist.add(i);
            }
            test.add(new Dataforchart(i+"",count+""));
        }

        Map<String,String> wordOccurence=new LinkedHashMap<>();

        for(int i=0;i<chapterlist.size();i++){
            int chapter=chapterlist.get(i);
            String percentage=String.format("%.2f",calculate(chapter,word));
            wordOccurence.put(chapter+"",percentage);
        }

        model.addAttribute("dataforpercentage",wordOccurence);

        model.addAttribute("dataforchart",test);

        model.addAttribute("countsurahoccured",chapterlist.size());




        return "paid/viewsameword";
    }

    public double calculate(int chapter,String word){

        long totalword=wbwrepo.countBychapter(chapter);


        long countwordbychapter=wbwrepo.countBychapterAndWordarabic(chapter,word);


        double percentage=0;
        percentage=((double)countwordbychapter/(double)totalword)*100;


        return percentage;
    }

//    @GetMapping("/vocab/predict/{surah1}/{surah2}")
//    public String predict(Model model,@PathVariable("surah1") int surah1
//            ,@PathVariable("surah2") int surah2){
//
//        Collection<wbw> wordSurah1=wbwrepo.findBychapter(surah1);
//        Collection<wbw> wordSurah2=wbwrepo.findBychapter(surah2);
//
//        ArrayList<String> listword1=new ArrayList();
//        ArrayList<String> listword2=new ArrayList();
//
//        for (wbw wbw: wordSurah1
//             ) {
//            listword1.add(wbw.getWordarabic());
//        }
//
//        for (wbw wbw: wordSurah2
//        ) {
//            listword2.add(wbw.getWordarabic());
//        }
//
//
//        Collection listOne=listword1;
//        Collection listTwo=listword2;
//
//        int count=0;
//        System.out.println(listOne);
//        System.out.println(listTwo);
//
//        //find similar word and list out
//        listOne.retainAll(listTwo);
//
//        int countinOne=0;
//        int countinTwo=0;
//
//        for (Object wordarabic:listOne
//             ) {
//            String word=wordarabic.toString();
//            countinOne+=wbwrepo.countBychapterAndWordarabic(surah1,word);
//            countinTwo+=wbwrepo.countBychapterAndWordarabic(surah2,word);
//        }
//
//
//        System.out.println(listOne);
//        System.out.println(countinOne+" - "+countinTwo);
//
//        model.addAttribute("similar",listOne);
//
//
//        return "predict";
//    }

    @GetMapping("/vocab/highlight/{word}")
    public String highlightword(@PathVariable("word") String words, Model model){


        List<wbw> highlightList=wbwrepo.findByWordarabic(words);
        ArrayList<Integer> listOfChapter=new ArrayList<Integer>();
        ArrayList<String> listofIdForHighlight=new ArrayList<String>();
        for(int i=0;i<highlightList.size();i++){
            int chapter=highlightList.get(i).getChapter();
            int ayat=highlightList.get(i).getAyat();
            int word=highlightList.get(i).getWord();
            listofIdForHighlight.add("c"+chapter+"a"+ayat+"w"+word);
            //for first index, add first without checking.
            if(i==0){
                listOfChapter.add(chapter);
                continue;
            }
            if(listOfChapter.contains(chapter)){
                continue;
            } else{
                listOfChapter.add(chapter);
            }

        }
        Map<Integer,List<wbw>> chapterAndWbwList=new LinkedHashMap<Integer, List<wbw>>();

        for(int i=0;i<listOfChapter.size();i++){
            int currentChapter=listOfChapter.get(i);
            List<wbw> listWbwForCurrentChapter=(wbwrepo.findBychapter(currentChapter));
            chapterAndWbwList.put(currentChapter,listWbwForCurrentChapter);

        }


        model.addAttribute("chapterDetail",chapterAndWbwList);
        model.addAttribute("highlightedWord",listofIdForHighlight);



        return "paid/highlightwordword";
    }
}