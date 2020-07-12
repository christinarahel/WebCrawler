package com.rahel.crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg {

	private List<String> links = new LinkedList<String>();
	private Document htmlDocument;

	Logger log = Logger.getAnonymousLogger();
	
	public Document getHtmlDocument() {
		return htmlDocument;
	}

	public void setHtmlDocument(Document htmlDocument) {
		this.htmlDocument = htmlDocument;
	}

	public List<String> getLinks() {
		return this.links;
	}

	 /**
     * Makes an HTTP request by the given URL, 
     * gets htmlDocument to work with in case of correct response,
     * collects all the links on it
     * 
     * @param url -  URL to visit
     * @return whether or not the crawl was successful
     */
	public boolean crawl(String url) {
		
		try {

			if (url.isEmpty()) {
				return false;
			}
			Connection connection = Jsoup.connect(url);
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;

			if (!connection.response().contentType().contains("text/html")) {
				log.info("Retrieved something other than HTML");
				return false;
			}

			Elements linksOnPage = htmlDocument.select("a[href]");

			for (Element link : linksOnPage) {
				this.links.add(link.absUrl("href"));
			}
			return true;

		} catch (IOException ioe) {
			log.info("ERROR! "+ioe);
			return false;
		}

	}

	/**
     * Search for a given word on the body of on the received (as a result of "crawl" method) HTML document
     * and calculate number of repetitions of the word on the page
     * 
     * @param term - the word to be found and counted
     * @return number of repetitions of the word on the page
     */
	public int searchForWord(String term) {
		if (this.htmlDocument == null) {
			log.info("ERROR! Call crawl() first");
			return 0;
		}
		String bodyText = this.htmlDocument.body().text();
		int fromIndex = 0;
		int counter = 0;
		while ((fromIndex = bodyText.toLowerCase().indexOf(term.toLowerCase(), fromIndex)) > 0) {
			counter++;
			fromIndex++;
		}
		return counter;
	}

}
