package com.milkmoney.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PoliticianProfileController {
    @GetMapping("/politician-profile")
    public String politicianProfilePage() {

        return "politician-profile";
    }
}
