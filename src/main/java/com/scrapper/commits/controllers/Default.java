package com.scrapper.commits.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Default {

        @GetMapping
        public String index() {
            return "API FUNCIONANDO";
        }
}
