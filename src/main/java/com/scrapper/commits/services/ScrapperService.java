package com.scrapper.commits.services;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.scrapper.commits.models.Commits;
import com.scrapper.commits.models.Repo;
import com.scrapper.commits.repositories.RepoRepository;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScrapperService {

    private final RepoRepository repoRepository;

    public Repo handleScrap(String url, boolean loadAll) throws IOException {
        Repo repo = getCommits(url, loadAll);
        repoRepository.save(repo);
        return repo;
    }


    private Repo getCommits(String url, boolean loadAll) throws IOException {
        Repo repo = new Repo();
        repo.setName(getRepoNameFromURL(url));
        List<Commits> commitsList = new ArrayList<>();

        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            configureWebClient(webClient);
            HtmlPage page = loadWebPage(url, webClient);
            Elements commitElements = loadCommitElements(page, loadAll, webClient, url);

            for (Element commitElement : commitElements) {
                Commits commit = createCommitFromElement(commitElement);
                commitsList.add(commit);
            }
        }

        repo.setCommits(commitsList);
        return repo;
    }

    private void configureWebClient(WebClient webClient) {
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);
    }

    private HtmlPage loadWebPage(String url, WebClient webClient) throws IOException {
        HtmlPage page = webClient.getPage(url);
        webClient.waitForBackgroundJavaScript(20000);
        return page;
    }

    private Elements loadCommitElements(HtmlPage page, boolean loadAll, WebClient webClient, String url) throws IOException {
        Elements commitElements = new Elements();

        if (loadAll) {
            boolean moreCommitsToLoad = true;
            while (moreCommitsToLoad) {
                List<HtmlElement> loadingElements = page.getByXPath("/html/body/div[1]/div[3]/div[3]/main/div[2]/div[3]");
                if (!loadingElements.isEmpty()) {
                    HtmlElement loadingElement = loadingElements.get(0);
                    String styleAttribute = loadingElement.getAttribute("style");
                    if (!"display: none;".equals(styleAttribute)) {
                        webClient.waitForBackgroundJavaScript(10000);
                        page = loadWebPage(url, webClient);
                        webClient.waitForBackgroundJavaScript(10000);
                        String pageAsXml = page.asXml();
                        Document doc = Jsoup.parse(pageAsXml);
                        Elements newCommitElements = doc.select("li.commit");
                        if (newCommitElements.size() == commitElements.size()) {
                            moreCommitsToLoad = false;
                        } else {
                            commitElements.addAll(newCommitElements);
                        }
                    } else {
                        moreCommitsToLoad = false;
                    }
                } else {
                    moreCommitsToLoad = false;
                }
            }
        } else {
            String pageAsXml = page.asXml();
            Document doc = Jsoup.parse(pageAsXml);
            commitElements = doc.select("li.commit");
        }

        return commitElements;
    }

    private Commits createCommitFromElement(Element commitElement) {
        Commits commit = new Commits();
        String message = commitElement.select(".commit-row-message.item-title").text();
        String author = commitElement.select(".commit-author-link").text();
        String date = commitElement.select("time").attr("datetime");
        commit.setMessage(message);
        commit.setAuthor(author);
        commit.setDate(date);
        return commit;
    }


    public static String getRepoNameFromURL(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        String path = url.getPath();

        String[] segments = path.split("/");

        if (segments.length > 2) {
            return segments[2];
        } else {
            return "Indeterminado";
        }
    }

}
