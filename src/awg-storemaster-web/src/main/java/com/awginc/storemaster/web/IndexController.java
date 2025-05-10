package com.awginc.storemaster.web;

import jakarta.servlet.ServletContext;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.support.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.*;

@Controller
public class IndexController {

    @GetMapping("/")
    public String view(Model model) {
        var contextPath = ServletUriComponentsBuilder.fromCurrentContextPath().path("/").build().toUriString();
        model.addAttribute("model", new IndexModel(contextPath));
        return "index";
    }

}
