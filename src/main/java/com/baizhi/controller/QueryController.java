package com.baizhi.controller;

import com.baizhi.service.PoertyService;
import com.baizhi.view.MessageCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/query")
public class QueryController {
    @Autowired
    private PoertyService poertyService;
    @RequestMapping("/show")
    public String query(String message, Integer page, Integer size, Model model){
        if(page==null||page<1){page=1;size=5;}
        Map<String,String> query = null;
        MessageCount messageCount =null;
        try {
            System.out.println(poertyService);
             messageCount = poertyService.query(message, page, size);
            //System.out.println(messageCount.getCount()+"----------------------");

             if(page>messageCount.getCount()){page=messageCount.getCount();}

             model.addAttribute("map",messageCount.getMap());
             model.addAttribute("page",page);
             model.addAttribute("message",message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "show";
    }
}
