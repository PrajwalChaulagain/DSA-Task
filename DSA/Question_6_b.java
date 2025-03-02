/**
 * This program implements a simple multi-threaded web crawler using a pool of worker threads. 
 * The web crawler starts by crawling a list of seed URLs and, for each URL, it retrieves the page's content and extracts 
 * links from it. The crawler then follows those links to discover more pages. 
 * The crawling process continues until all reachable pages are crawled.
 *
 * The crawler uses an ExecutorService with a fixed thread pool to manage the worker threads, and a ConcurrentLinkedQueue to 
 * store the URLs to be crawled. A ConcurrentHashMap is used to store the crawled data and keep track of visited URLs to avoid 
 * revisiting the same page.
 *
 * The `CrawlTask` class is responsible for fetching the URL content and extracting links from it.
 * Each thread processes one URL at a time and submits additional tasks to crawl new links discovered on the page.
 * The program will gracefully shut down the thread pool once all tasks are completed.
 */

 import java.util.*;
 import java.util.concurrent.*;
 import java.util.regex.*;
 import java.net.*;
 import java.io.IOException;
 import java.util.concurrent.atomic.AtomicInteger;
 
 public class Question_6_b {
 
     // Executor for managing threads and a queue for URLs to crawl
     private final ExecutorService executor;
     private final ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<>();
     
     // Set to track visited URLs to avoid revisiting the same URL
     private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
     
     // Atomic counter for the number of active tasks
     private final AtomicInteger taskCount = new AtomicInteger(0);
     
     // Map to store crawled URLs and their content
     private final ConcurrentHashMap<String, String> crawledData = new ConcurrentHashMap<>();
 
     /**
      * Constructor that initializes the crawler with a given number of threads.
      * @param maxThreads the maximum number of worker threads to use in the executor pool
      */
     public Question_6_b(int maxThreads) {
         executor = Executors.newFixedThreadPool(maxThreads);
     }
 
     /**
      * Starts the crawling process by adding the seed URLs to the queue and 
      * submitting the first set of tasks.
      * @param seedUrls the initial list of URLs to start crawling from
      */
     public void startCrawling(List<String> seedUrls) {
         seedUrls.forEach(url -> {
             if (visitedUrls.add(url)) {
                 urlQueue.add(url);
                 submitTask(); // Submit the first task to crawl the seed URL
             }
         });
     }
 
     /**
      * Submits a new crawling task to the executor. Each task will crawl one URL, 
      * extract links, and submit further tasks if new links are found.
      */
     private void submitTask() {
         taskCount.incrementAndGet(); // Increment the task count
         executor.submit(new CrawlTask()); // Submit a new CrawlTask
     }
 
     /**
      * The CrawlTask is responsible for processing one URL: fetching its content, 
      * extracting the links, and submitting tasks to crawl the discovered links.
      */
     private class CrawlTask implements Runnable {
         
         @Override
         public void run() {
             try {
                 String url = urlQueue.poll(); // Get the next URL from the queue
                 if (url == null) return; // No more URLs to crawl
                 
                 String content = fetchUrl(url); // Fetch the content of the URL
                 crawledData.put(url, content); // Store the crawled data in the map
                 
                 // Parse and extract links from the fetched content
                 List<String> links = parseLinks(content);
 
                 // For each link, if it has not been visited, add it to the queue and submit a new task
                 for (String link : links) {
                     if (visitedUrls.add(link)) {
                         urlQueue.add(link);
                         submitTask();
                     }
                 }
             } catch (IOException e) {
                 System.err.println("Error fetching URL: " + e.getMessage());
             } finally {
                 // Check if all tasks are done and shut down the executor if necessary
                 if (taskCount.decrementAndGet() == 0 && urlQueue.isEmpty()) {
                     executor.shutdown();
                 }
             }
         }
 
         /**
          * Fetches the content of the given URL using an HTTP GET request.
          * @param url the URL to fetch
          * @return the content of the URL as a string
          * @throws IOException if there is an error during the fetch operation
          */
         private String fetchUrl(String url) throws IOException {
             HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
             conn.setRequestMethod("GET");
             try (Scanner scanner = new Scanner(conn.getInputStream())) {
                 scanner.useDelimiter("\\A");
                 return scanner.hasNext() ? scanner.next() : ""; // Return the content of the URL
             }
         }
 
         /**
          * Parses the HTML content of a page and extracts all the links found within it.
          * @param content the HTML content of the page
          * @return a list of URLs (links) found in the content
          */
         private List<String> parseLinks(String content) {
             List<String> links = new ArrayList<>();
             Pattern pattern = Pattern.compile("href=\"(.*?)\""); // Regex to extract href links
             Matcher matcher = pattern.matcher(content);
             while (matcher.find()) {
                 links.add(matcher.group(1)); // Add each found link to the list
             }
             return links;
         }
     }
 
     /**
      * Waits for the completion of all crawling tasks by blocking until the executor is shut down.
      * @throws InterruptedException if the thread is interrupted while waiting
      */
     public void awaitTermination() throws InterruptedException {
         executor.awaitTermination(1, TimeUnit.MINUTES); // Wait for up to 1 minute for all tasks to finish
     }
 
     /**
      * Returns the crawled data, i.e., the URLs and their corresponding content.
      * @return a map containing the crawled URLs and their content
      */
     public Map<String, String> getCrawledData() {
         return crawledData;
     }
 
     /**
      * Main method to start the crawler with a set of seed URLs and a fixed number of threads.
      * The crawling process is initiated and the program waits for the completion of all tasks.
      * @param args command-line arguments (not used)
      * @throws InterruptedException if the thread is interrupted while waiting
      */
     public static void main(String[] args) throws InterruptedException {
         List<String> seedUrls = Arrays.asList("http://example.com", "http://example.org");
         Question_6_b crawler = new Question_6_b(4); // Initialize crawler with 4 threads
         crawler.startCrawling(seedUrls); // Start crawling from seed URLs
         crawler.awaitTermination(); // Wait for all tasks to complete
         System.out.println("Crawled URLs: " + crawler.getCrawledData().size()); // Print number of crawled URLs
     }
 }
 
 /**
  * Summary:
  * The algorithm implements a multi-threaded web crawler that efficiently fetches and processes URLs using worker threads.
  * We used an ExecutorService with a fixed thread pool to manage the crawling tasks. Each task crawls one URL, retrieves 
  * the page content, extracts links, and submits new tasks for the discovered links. 
  * The process continues until all reachable pages are crawled or the queue is empty.
  * 
  * The algorithm works as expected, and all tasks are synchronized to ensure no URL is crawled more than once.
  * The program efficiently crawls the provided seed URLs and follows links discovered on those pages.
  */
 