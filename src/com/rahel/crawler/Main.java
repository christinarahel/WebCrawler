package com.rahel.crawler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		List<String> wordsToCount = new LinkedList<String>();
		String seedUrl = "";
		int maxPages = 10000;
		int depth = 8;
		try (Scanner scanner = new Scanner(new File("StarterData.txt"))) {
			seedUrl = scanner.nextLine();
			wordsToCount.addAll(Arrays.asList(scanner.nextLine().split(", ")));
			if (scanner.hasNext()) {
				maxPages = scanner.nextInt();
			}
			if (scanner.hasNext()) {
				depth = Integer.valueOf(scanner.next());
			}
		}
		Spider spider = new Spider(maxPages, depth, wordsToCount);
		spider.search(seedUrl);
		spider.printAllStatistic(seedUrl);
		spider.printTopTen();
	}

}
