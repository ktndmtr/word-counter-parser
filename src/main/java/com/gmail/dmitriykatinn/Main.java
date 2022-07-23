package com.gmail.dmitriykatinn;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Main {

    private static final String separator = "[ ,.!?\";:\\[\\]()\\n\\r\\t]+";
    private static final String filename = "page.html";
    private static final String encoding = "UTF-8";

    public static void main(String[] args) {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, encoding));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            String urlString = getInputUrl();
            File page = download(urlString);
            String pageText = getTextFromHTML(page);
            Map<String, Integer> wordCounter = countWords(pageText);
            wordCounter.forEach((word, count) -> System.out.println(word + " - " + count));
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private static String getInputUrl() {
        Scanner scanner = new Scanner(System.in);
        String urlString = scanner.nextLine();
        scanner.close();
        return urlString;
    }

    private static File download(String urlString) throws IOException {
        URL url = null;
        InputStreamReader urlStreamReader = null;
        FileWriter fileWriter = null;
        File file = new File(filename);

        try {
            url = new URL(urlString);
            urlStreamReader = new InputStreamReader(url.openStream());
        } catch (IOException e){
            throw (new IOException("Invalid URL. Correct URL example: http://www.example.com/index", e));
        }
        try{
            fileWriter = new FileWriter(file);
        } catch(IOException e){
            throw (new IOException("Couldn't create/write on '" + filename + "' at " + System.getProperty("user.dir"), e));
        }

        BufferedReader reader = new BufferedReader(urlStreamReader);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(line);
        }

        reader.close();
        writer.close();
        urlStreamReader.close();
        fileWriter.close();

        return file;
    }

    private static String getTextFromHTML(File file) throws IOException {
        Document doc = null;
        doc = Jsoup.parse(file, encoding);
        return doc.body().text();
    }

    private static Map<String, Integer> countWords(String pageText){
        Map<String, Integer> map = new HashMap<>();
        for(String rawWord : pageText.split(separator)){
            String word = rawWord.toUpperCase();
            if(!map.containsKey(word)){
                map.put(word, 1);
            }
            else{
                int count = map.get(word);
                map.put(word, count + 1);
            }
        }
        return map;
    }
}