package com.rahel.crawler.tests;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.rahel.crawler.Spider;

public class TestSpider {
	
	@Test
	public void testSearch() {	
		
		List<String> wordsToCount = new LinkedList<String>();
		wordsToCount.addAll(Arrays.asList("Spring", "Master", "Complex", "Abracadabra"));
		Spider spider= new Spider(10000, 1, wordsToCount);
		spider.search("https://www.baeldung.com/learn-spring-course");
		
		Assert.assertTrue(spider.getStatisticStore().size()==25);
		
		int ZeroRepetitions = spider.getStatisticStore().entrySet().stream().mapToInt(e -> e.getValue().get("Abracadabra")).sum();
		int moreThanTen= spider.getStatisticStore().entrySet().stream().mapToInt(e -> e.getValue().get("Spring")).sum();	
		
		Assert.assertTrue(ZeroRepetitions==0);
		Assert.assertTrue(moreThanTen>=10);
	}

}
