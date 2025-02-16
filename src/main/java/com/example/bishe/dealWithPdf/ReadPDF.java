package com.example.bishe.dealWithPdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ReadPDF {
    public static void main(String[] args) {
        List<String> temp = deal("C:\\Users\\24204\\Desktop\\paper\\1.pdf");
        if (temp != null) {
            for (String sentence : temp) {
                System.out.println(sentence);
                System.out.println();
            }
        }
    }

    public static List<String> deal(String location) {
        try {
            File file = new File(location);
            PDDocument document = PDDocument.load(file);

            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String text = pdfTextStripper.getText(document);

            text = removeHeaderFooterAndPageNumbers(text);
            text = removeTitles(text);
            text = removeReferences(text);
            text = removeTablesAndImages(text);

            List<String> sentences = splitTextIntoSentences(text);

            document.close();

            return sentences;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String removeHeaderFooterAndPageNumbers(String text) {
        String[] lines = text.split("\n");
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            if (line.length() > 10) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }

    private static String removeTitles(String text) {
        String[] lines = text.split("\n");
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            if (!(Character.isUpperCase(line.charAt(0)) && line.length() < 20)) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }

    private static String removeReferences(String text) {
        int startIndex = text.toLowerCase().indexOf("参考文献");
        if (startIndex!= -1) {
            text = text.substring(0, startIndex);
        }
        startIndex = text.toLowerCase().indexOf("references");
        if (startIndex!= -1) {
            text = text.substring(0, startIndex);
        }
        return text;
    }

    private static String removeTablesAndImages(String text) {
        String regex = "\\s+\\S+\\s+\\S+.*\n(\\s+\\S+\\s+\\S+.*\n)+";
        return Pattern.compile(regex).matcher(text).replaceAll("");
    }

    private static List<String> splitTextIntoSentences(String text) {
        List<String> sentences = new ArrayList<>();
        // 使用更精确的正则表达式分割句子
        String regex = "(?<=[。？！])|((?<=\\.)(?=\\s+[A-Z]|\\s*$)(?<![0-9]))";
        String[] parts = text.split(regex);

        // 常见缩写列表
        List<String> abbreviations = Arrays.asList("Mr", "Ms", "Mrs", "Dr", "Prof", "etc", "vs", "i.e", "e.g", "Eg");

        for (int i = 0; i < parts.length; i++) {
            String current = parts[i].trim();
            if (current.isEmpty()) continue;

            if (sentences.isEmpty()) {
                sentences.add(current);
            } else {
                String previous = sentences.get(sentences.size() - 1);
                // 检查前一句是否以缩写结尾
                boolean isAbbreviation = abbreviations.stream().anyMatch(abbr ->
                        previous.matches(".*\\b" + abbr + "\\.$")
                );

                if (isAbbreviation) {
                    // 合并到前一句
                    sentences.set(sentences.size() - 1, previous + " " + current);
                } else {
                    sentences.add(current);
                }
            }
        }

        // 清理空格和换行符
        for (int i = 0; i < sentences.size(); i++) {
            String sentence = sentences.get(i)
                    .replaceAll("[\\r\\n]+", " ")
                    .replaceAll("\\s+", " ")
                    .trim();
            sentences.set(i, sentence);
        }

        return sentences;
    }
}