// This algorithm processes a list of tweets to find the top N most frequent hashtags from tweets that were posted in February 2024.
// The main idea is to filter the tweets by date, extract hashtags, count their occurrences, sort them by frequency (and lexicographically in case of ties),
// and then return the top N most frequent hashtags. The result is printed in a formatted table showing the hashtag and its count.

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;


public class Question_4_a {

    // This class represents a hashtag and its count.
    static class HashtagCount {
        String hashtag;
        int count;

        // Constructor to initialize hashtag and its count
        public HashtagCount(String hashtag, int count) {
            this.hashtag = hashtag;
            this.count = count;
        }

        // Override the toString method to display the hashtag and count in a readable format
        @Override
        public String toString() {
            return hashtag + " " + count;
        }
    }

    public static void main(String[] args) {
        // Sample Input (February 2024 tweets)
        List<String[]> tweets = new ArrayList<>();
        tweets.add(new String[]{"135", "13", "2024-02-01", "Enjoying a great start to the day. #HappyDay #MorningVibes"});
        tweets.add(new String[]{"136", "14", "2024-02-03", "Another #HappyDay with good vibes! #FeelGood"});
        tweets.add(new String[]{"137", "15", "2024-02-04", "Productivity peaks! #WorkLife #ProductiveDay"});
        tweets.add(new String[]{"138", "16", "2024-02-04", "Exploring new tech frontiers. #TechLife #Innovation"});
        tweets.add(new String[]{"139", "17", "2024-02-05", "Gratitude for today's moments. #HappyDay #Thankful"});
        tweets.add(new String[]{"140", "18", "2024-02-07", "Innovation drives us. #TechLife #FutureTech"});
        tweets.add(new String[]{"141", "19", "2024-02-09", "Connecting with nature's serenity. #Nature #Peaceful"});

        // Find top 3 hashtags
        List<HashtagCount> result = findTopHashtags(tweets, 3);

        // Print formatted output
        System.out.println("+-----------+-------+");
        System.out.println("| hashtag   | count |");
        System.out.println("+-----------+-------+");
        for (HashtagCount hc : result) {
            System.out.printf("| %-10s| %-6d|%n", hc.hashtag, hc.count);
        }
        System.out.println("+-----------+-------+");
    }

    // This function takes a list of tweets and an integer topN, and returns a list of the top N most frequent hashtags
    // from tweets posted in February 2024. It filters tweets by date, counts the occurrences of hashtags, sorts them by frequency, 
    // and returns the top N hashtags in a list.
    public static List<HashtagCount> findTopHashtags(List<String[]> tweets, int topN) {
        // Filter the tweets to only include those from February 2024
        List<String[]> filteredTweets = tweets.stream()
                .filter(tweet -> isFebruary2024(tweet[2]))
                .collect(Collectors.toList());

        // Initialize a map to store the hashtag counts
        Map<String, Integer> hashtagCounts = new HashMap<>();
        for (String[] tweet : filteredTweets) {
            String text = tweet[3];  // Get the tweet text
            String[] words = text.split("\\s+"); // Split by spaces to extract individual words
            for (String word : words) {
                if (word.startsWith("#")) {  // If the word is a hashtag
                    hashtagCounts.put(word, hashtagCounts.getOrDefault(word, 0) + 1);  // Increment the count for the hashtag
                }
            }
        }

        // Sort the hashtags by frequency in descending order, and lexicographically by hashtag in case of ties
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(hashtagCounts.entrySet());
        sortedEntries.sort((a, b) -> {
            int countCompare = b.getValue().compareTo(a.getValue()); // Compare by count in descending order
            return (countCompare != 0) ? countCompare : b.getKey().compareTo(a.getKey()); // If counts are equal, compare lexicographically
        });

        // Limit the list to the top N hashtags and return as a list of HashtagCount objects
        return sortedEntries.stream()
                .limit(topN)
                .map(entry -> new HashtagCount(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    // This function checks if a given date string represents a date in February 2024.
    private static boolean isFebruary2024(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);  // Parse the date string to a LocalDate object
            return date.getYear() == 2024 && date.getMonth() == Month.FEBRUARY;  // Check if the date is in February 2024
        } catch (DateTimeParseException e) {
            return false;  // If the date is not valid, return false
        }
    }
}

// Summary:
// This algorithm successfully solves the task of finding the top N most frequent hashtags from tweets posted in February 2024. 
// The process involves filtering tweets based on date, extracting hashtags, counting their occurrences, and sorting them by frequency. 
// It handles ties by sorting hashtags lexicographically. The algorithm returns the top N hashtags and prints them in a formatted table. 
// The algorithm works as expected, providing the correct top hashtags based on the given input tweets.
