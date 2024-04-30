package com.scrapper.commits.controllers;

import com.scrapper.commits.dtos.RequestDTO;
import com.scrapper.commits.models.Repo;
import com.scrapper.commits.services.ScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/scrap")
public class Scrapper {

    @Autowired
    private ScrapperService scrapperService;

    @PostMapping
    public Repo scrap(@RequestBody RequestDTO requestDTO) throws IOException {
        return scrapperService.handleScrap(requestDTO.getUrl(), requestDTO.isAllCommits());
    }
}
