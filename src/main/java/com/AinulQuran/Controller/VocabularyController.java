package com.AinulQuran.Controller;

import com.AinulQuran.Dataforchart;
import com.AinulQuran.model.User;
import com.AinulQuran.model.dictionary;
import com.AinulQuran.model.user_vocab;
import com.AinulQuran.model.wbw;
import com.AinulQuran.repository.*;
import com.AinulQuran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;



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


    @GetMapping("/vocab/{vocab}")
    public String savevocab(@PathVariable("vocab") String vocabulary, Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentusername=auth.getName();

        User currentUser=service.findByUsername(currentusername);
        user_vocab user_vocabulary=new user_vocab();
        user_vocab check=uservocabrepo.findByUsernameAndVocab(currentUser.getUsername(),vocabulary);
        user_vocabulary.setUsername(currentusername);
        user_vocabulary.setVocab(vocabulary);

        if(check!=null ){
            String error="The word already existed in your vocabulary!";
            model.addAttribute("errorMessage",error);
            return "vocab";
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
            return "vocab";
        }
        else {//delete
            uservocabrepo.deleteByUsernameAndVocab(currentusername,vocabulary);
            String success="The word "+vocabulary+" has been successfully deleted from your vocabulary list";
            model.addAttribute("successMessage",success);

        }


        return "vocab";
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
            dictionary wordp=dictionaryrepo.findBywordp(arabicwordvocabulary);

            //count matching saved word frequency and add it to count var
            count+=wbwrepo.countByWordarabic(arabicwordvocabulary);
        }

        //count total word in the whole Quran
        int totalWord=Integer.parseInt(wbwrepo.count()+"");

        //total frequency of all saved word / total word count in the whole quran
        double totalPercentageLearned=(double)count/(double)totalWord*100;

        String totalPercentageLearnedString=String.format("%.4f",totalPercentageLearned)+"";
        model.addAttribute("totalpercentage",totalPercentageLearnedString);
        model.addAttribute("wbw",listuservocabulary);




        return "vocab";
    }



    @GetMapping("/vocab/viewword/{word}")
    public String viewword(@PathVariable("word") String word,Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentusername=auth.getName();


        User currentUser=service.findByUsername(currentusername);
        List<user_vocab> listuservocabulary=uservocabrepo.findByUsername(currentusername);

        model.addAttribute("wbw",listuservocabulary);
        List<wbw> wordselected=wbwrepo.findByWordarabic(word);
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

        ArrayList<String> stringofanalysis=new ArrayList<>();

        for(int i=0;i<chapterlist.size();i++){
            int chapter=chapterlist.get(i);
            String percentage=String.format("%.4f",calculate(chapter,word))+"%";
            stringofanalysis.add("Chapter: "+chapter+" Percentage: "+percentage+"%");
        }

        model.addAttribute("dataforpercentage",stringofanalysis);

        model.addAttribute("dataforchart",test);

        model.addAttribute("countsurahoccured",chapterlist.size());




        return "viewsameword";
    }

    public double calculate(int chapter,String word){

        long totalword=wbwrepo.countBychapter(chapter);


        long countwordbychapter=wbwrepo.countBychapterAndWordarabic(chapter,word);


        double percentage=0;
        percentage=((double)countwordbychapter/(double)totalword)*100;


        return percentage;
    }

}