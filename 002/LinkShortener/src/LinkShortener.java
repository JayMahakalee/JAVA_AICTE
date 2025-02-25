import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class LinkShortener {
    private final HashMap<String, String> urlMap = new HashMap<>();
    private final HashMap<String, String> reverseMap = new HashMap<>();
    private final String baseURL = "http://short.ly/";
    private final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int shortURLLength = 15;

    public String shortenURL(String longURL) {
        if (reverseMap.containsKey(longURL)) {
            return baseURL + reverseMap.get(longURL);
        }

        String shortKey;
        do {
            shortKey = generateShortKey();
        } while (urlMap.containsKey(shortKey));

        urlMap.put(shortKey, longURL);
        reverseMap.put(longURL, shortKey);

        return baseURL + shortKey;
    }

    public String expandURL(String shortURL) {
        if (!shortURL.startsWith(baseURL)) {
            return "Error: Invalid short URL format.";
        }
        String shortKey = shortURL.substring(baseURL.length()).trim();
        return urlMap.getOrDefault(shortKey, "Error: Short URL not found.");
    }


    private String generateShortKey() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < shortURLLength; i++) {
            key.append(characters.charAt(random.nextInt(characters.length())));
        }
        return key.toString();
    }

    public static void main(String[] args) {
        LinkShortener shortener = new LinkShortener();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nLink Shortener Menu:");
            System.out.println("1. Shorten URL");
            System.out.println("2. Expand URL");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter the long URL: ");
                    String longURL = scanner.nextLine();
                    String shortURL = shortener.shortenURL(longURL);
                    System.out.println("Shortened URL: " + shortURL);
                    break;
                case 2:
                    System.out.print("Enter the short URL: ");
                    String inputShortURL = scanner.nextLine();
                    String expandedURL = shortener.expandURL(inputShortURL);
                    System.out.println("Original URL: " + expandedURL);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
