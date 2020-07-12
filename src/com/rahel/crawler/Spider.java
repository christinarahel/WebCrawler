package com.rahel.crawler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Spider {

	private final int MAX_PAGES_TO_SEARCH;
	private final int MAX_DEPTH;
	private List<String> wordsToCount;
	private Map<String, Map<String, Integer>> statisticStore = new HashMap<>();
	private Set<String> pagesVisited = new HashSet<String>();
	private List<NodeUrl> pagesToVisit = new LinkedList<NodeUrl>();

	public Spider(int MAX_PAGES_TO_SEARCH, int MAX_DEPTH, List<String> wordsToCount2) {
		this.MAX_PAGES_TO_SEARCH = MAX_PAGES_TO_SEARCH;
		this.MAX_DEPTH = MAX_DEPTH;
		this.wordsToCount = wordsToCount2;
	}

	public Map<String, Map<String, Integer>> getStatisticStore() {
		return statisticStore;
	}

	public void setStatisticStore(Map<String, Map<String, Integer>> statisticStore) {
		this.statisticStore = statisticStore;
	}

	public class NodeUrl {
		private String url;
		private int depth;

		NodeUrl(String url, int depth) {
			this.url = url;
			this.depth = depth;
		}

		public int getDepth() {
			return depth;
		}

		public String getUrl() {
			return url;
		}
	}

	/**
	 * Adds links to the list of links to be visited by crawler Only the links that
	 * hadn't been visited before are being added to the list Makes sure this links
	 * exceeding depth limit are not being added to the list
	 * 
	 * @param links - preliminary list of the links to add
	 * @param depth - maximal allowed depth of the link
	 */
	private void addNodeUrls(List<String> links, int depth) {
		// List<NodeUrl> nodeUrls = new LinkedList<NodeUrl>();
		if (depth < MAX_DEPTH) {
			for (String link : links) {
				if (!this.pagesVisited.contains(link)) {
					this.pagesToVisit.add(new NodeUrl(link, depth + 1));
				}
			}
		}
	}

	/**
	 * Main point of Spider's functionality. Creates spider legs that make an HTTP
	 * request, parse the response and collect all the statistics data of the search
	 * 
	 * @param url - The starting point of the search
	 */
	public void search(String url) {
		this.pagesToVisit.add(new NodeUrl(url, 0));

		while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
			NodeUrl nodeUrl;
			SpiderLeg leg = new SpiderLeg();

			if (this.pagesToVisit.isEmpty()) {
				break;
			} else {
				nodeUrl = pagesToVisit.remove(0);
				this.pagesVisited.add(nodeUrl.getUrl());
			}

			if (!leg.crawl(nodeUrl.getUrl())) {
				continue;
			}
			HashMap<String, Integer> termsNumber = new HashMap<String, Integer>();

			for (String word : wordsToCount) {
				int timesFound = leg.searchForWord(word);
				termsNumber.put(word, timesFound);
			}
			statisticStore.put(nodeUrl.getUrl(), termsNumber);

			addNodeUrls(leg.getLinks(), nodeUrl.getDepth());
		}

	}

	/**
	 * Prints (all the prepared in "search" method) statistic to CSV file
	 * 
	 * @param seedUrl - starting point of search
	 */
	public void printAllStatistic(String seedUrl) throws IOException {

		File csvOutputFile = new File("AllStatistic.csv");

		StringBuilder totalHits = new StringBuilder(seedUrl);
		int sum = 0;
		for (String term : wordsToCount) {
			int timesFound = statisticStore.entrySet().stream().mapToInt(e -> e.getValue().get(term)).sum();
			totalHits.append(" ").append(timesFound);
			sum += timesFound;
		}
		totalHits.append(" ").append(sum);

		try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
			pw.println(totalHits);
			for (Map.Entry<String, Map<String, Integer>> entry : statisticStore.entrySet()) {
				StringBuilder temp = new StringBuilder(entry.getKey());
				sum = 0;
				for (Map.Entry<String, Integer> e : entry.getValue().entrySet()) {
					sum += e.getValue();
					temp.append(" ").append(e.getValue());
				}

				temp.append(" ").append(sum);
				pw.println(temp);

			}

		}
	}

	/**
	 * Sorts (all the prepared in "search" method) statistic by summary of hints at
	 * each page Prints top ten pages with the numbers of terms repetitions to CSV
	 * file and to the console
	 */
	public void printTopTen() throws IOException {

		List<String> topPages = statisticStore.entrySet().stream()
				.sorted((e1,
						e2) -> (e2.getValue().entrySet().stream().mapToInt(e -> e.getValue()).sum()
								- e1.getValue().entrySet().stream().mapToInt(en -> en.getValue()).sum()))
				.limit(10).map(e -> e.getKey()).collect(Collectors.toList());

		File csvOutputFile = new File("TopTen.csv");
		try (PrintWriter pw = new PrintWriter(csvOutputFile)) {

			for (String topPage : topPages) {

				StringBuilder temp = new StringBuilder(topPage);
				int sum = 0;

				for (String term : wordsToCount) {
					int timesFound = statisticStore.get(topPage).get(term);
					sum += timesFound;
					temp.append(" ").append(timesFound);
				}
				temp.append(" ").append(sum);
				pw.println(temp);
				System.out.println(temp);
			}

		}
	}
}
