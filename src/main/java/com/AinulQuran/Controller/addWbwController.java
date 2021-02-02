package com.AinulQuran.Controller;

import com.AinulQuran.dto.UserRegistrationDto;
import com.AinulQuran.model.User;
import com.AinulQuran.model.wbw;
import com.AinulQuran.repository.wbwRepository;
import com.AinulQuran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@Controller
public class addWbwController {

    @Autowired
    private wbwRepository wbwrepo;


    @GetMapping("/admin/addWbwAfter/{id}")
    public String showAdd(Model model, @PathVariable("id") int id) {

        Optional<wbw> currentid =wbwrepo.findById(id);

        //get last index
        if(currentid.isEmpty()){
            return "redirect:/admin/listSurah";
        }

        wbw newwbw=currentid.get();
        newwbw.setWord(newwbw.getWord()+1);
        newwbw.setId(null);
        model.addAttribute("newwbw",newwbw);
        return "admin/listWbw";
    }

    @PostMapping("/admin/addWbwAfter")
    public String addWbwAfter(@ModelAttribute("newwbw") wbw newwbw) {

        int chapter=newwbw.getChapter();
        int ayat=newwbw.getAyat();
        int wordtobeadded=newwbw.getWord();
        int currentwordcount=wbwrepo.countByChapterAndAyat(chapter,ayat);


        wbw lastidrecord;

        List<wbw> tobeEdited = new ArrayList<>();
        for(int i=wordtobeadded;i<=currentwordcount;i++){


            //find the next row
            wbw nextindexwbw=wbwrepo.findBychapterAndAyatAndWord(chapter,ayat,i);

            if(nextindexwbw==null){
                continue;
            }
            tobeEdited.add(nextindexwbw);
            //delete the next row first to gain a new id
            wbwrepo.deleteById(nextindexwbw.getId());
        }

        for(int i=0;i<tobeEdited.size();i++){
            //refresh last id
            lastidrecord=wbwrepo.findTopByOrderByIdDesc();
            wbw tobeEdit=tobeEdited.get(i);
            tobeEdit.setWord(tobeEdit.getWord() +1);
            tobeEdit.setId(lastidrecord.getId() +1);

            wbwrepo.save(tobeEdit);

        }

        //refresh last id
        lastidrecord=wbwrepo.findTopByOrderByIdDesc();
        newwbw.setId(lastidrecord.getId()+ 1);


        wbw currentmatching = wbwrepo.findBychapterAndAyatAndWord(newwbw.getChapter(),newwbw.getAyat(),newwbw.getWord());



        if(currentmatching==null){
            wbwrepo.save(newwbw);
        }



        return "redirect:/admin/listWbw/"+chapter;

    }

    @Transactional
    @GetMapping("/admin/removeWbw/{id}")
    public String removeWbw(@PathVariable("id") int id, Model model){

        Optional<wbw> tobeDeleted=wbwrepo.findById(id);
        int chapter;
        int ayat;
        int word;
        if(tobeDeleted.isPresent()) {
            chapter= tobeDeleted.get().getChapter();
            ayat = tobeDeleted.get().getAyat();
            word = tobeDeleted.get().getWord();
        } else {
            return "redirect:/admin/listSurah";
        }


        //delete the selected id first
        wbwrepo.deleteById(id);

        //get list of word after deleted
        List<wbw> listOfWbwAfterDeleted=wbwrepo.findBychapterAndAyat(chapter,ayat);
        Iterator x=listOfWbwAfterDeleted.iterator();

        while(x.hasNext()){
            wbw next=(wbw) x.next();

            //find any wbw after the deleted word
            if(next.getWord() > word){
                next.setWord(next.getWord() -1);
            }

            //then save to repo
            wbwrepo.save(next);

        }

        return "redirect:/admin/listWbw/"+chapter;

    }


}