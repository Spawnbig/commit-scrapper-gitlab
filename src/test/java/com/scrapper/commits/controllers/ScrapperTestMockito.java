package com.scrapper.commits.controllers;

import com.scrapper.commits.dtos.RequestDTO;
import com.scrapper.commits.models.Repo;
import com.scrapper.commits.services.ScrapperService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class ScrapperTestMockito {

    @InjectMocks
    private Scrapper scrapper = new Scrapper();

    @Mock
    private ScrapperService scrapperService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(scrapperService);
    }

    @Test
    @DisplayName("Should scrap a repo")
    void scrap() throws IOException {
        when(scrapperService.handleScrap("URL", true)).thenReturn(new Repo());
        RequestDTO request = new RequestDTO();
        request.setUrl("URL");
        request.setAllCommits(true);

        scrapper.scrap(request);
        verify(scrapperService).handleScrap(eq("URL"), eq(true));
    }


    @Test
    @DisplayName("Should throw an exception when the url is invalid")
    void scrapInvalidUrl() throws IOException {
        when(scrapperService.handleScrap(null, true)).thenThrow(new IOException());
        RequestDTO request = new RequestDTO();
        request.setUrl(null);
        request.setAllCommits(true);

        Assertions.assertThrows(IOException.class, () -> scrapper.scrap(request));
    }
}