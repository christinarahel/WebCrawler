package com.rahel.tests;

import java.io.File;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import com.rahel.crawler.SpiderLeg;

import org.junit.Assert;

public class TestSpiderLeg {
	
	@Test
	public void testSearchForWord() throws IOException {
		SpiderLeg sl = new SpiderLeg();
		Connection connection = Jsoup.connect("https://www.baeldung.com/learn-spring-course");
		Document htmlDocument1 = connection.get();
		File file =  new File ("Learn Spring Course | Baeldung.html");
		Document htmlDocument = Jsoup.parse(file,"UTF-8") ; 
		
		sl.setHtmlDocument(htmlDocument);
	
		int c1 = sl.searchForWord("Spring");
		int c2 = sl.searchForWord("master");
        int c3 = sl.searchForWord("complex");
		Assert.assertEquals(69, c1);
		Assert.assertEquals(7, c2);
		Assert.assertEquals(2, c3);
		
	}
	
	@Test
	public void testCrawl() throws Exception {
		SpiderLeg sl = new SpiderLeg();
		Assert.assertFalse(sl.crawl(""));
		Assert.assertTrue(sl.crawl("https://www.baeldung.com/learn-spring-course"));
		Assert.assertEquals(35,sl.getLinks().size());
	}

}
