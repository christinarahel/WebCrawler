# WebCrawler

App loads all the input data from the StarterData.txt file

Data prepaired as following:

https://www.baeldung.com
User, Boot, Abrakadabra, JWT
20
3

where: https://www.baeldung.com - is a seed
       User, Boot, Abrakadabra, JWT - are terms we are looking for
       20 - maximal pages to visit
       3 - maximal depth of the link to dive

Output of all statistic is to the file "AllStatistic.csv"

Output of top ten pages sorted by total hits is to the file "TopTen.csv" and to a console
