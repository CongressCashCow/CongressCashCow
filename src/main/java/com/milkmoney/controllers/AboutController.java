package com.milkmoney.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class AboutController {

        @GetMapping("/about")
        public String aboutPage() {

            return "about";
        }

        @GetMapping("/meet-the-team")
        public String meetTheTeamPage() {

            return "meet-the-team";
        }
}
