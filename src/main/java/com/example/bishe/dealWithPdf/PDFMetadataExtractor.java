package com.example.bishe.dealWithPdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFMetadataExtractor {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\24204\\Desktop\\paper\\7.pdf";
        extractMetadata(filePath);
    }

    public static List<String> extractMetadata(String pdfPath) {
        try {
            File file = new File(pdfPath);
            PDDocument document = PDDocument.load(file);

            // 1. 提取基本元数据
            PDDocumentInformation info = document.getDocumentInformation();
            String title = info.getTitle();
            String author = info.getAuthor();
            String subject = info.getSubject();
            String keywords = info.getKeywords();

            System.out.println("==== 元数据 Metadata ====");
            System.out.println("标题 Title: " + (title != null ? title : "未找到"));
            System.out.println("作者 Author: " + (author != null ? author : "未找到"));
            System.out.println("主题 Subject: " + (subject != null ? subject : "未找到"));
            System.out.println("关键词 Keywords: " + (keywords != null ? keywords : "未找到"));

            // 2. 提取正文文本
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // 3. 额外提取 DOI 和 期刊名（从正文找）
            String doi = extractDOI(text);
            String journal = extractJournalName(text);

            System.out.println("\n==== 正文分析 Extracted Info ====");
            System.out.println("DOI: " + (doi != null ? doi : "未找到"));
            System.out.println("期刊名 Journal: " + (journal != null ? journal : "未找到"));

            List<String> detail_info = new ArrayList<>();
            detail_info.add((title != null && !title.equals("")) ? title : "未找到");
            detail_info.add((author != null && !author.equals("")) ? author : "未找到");
            detail_info.add((subject != null && !subject.equals("")) ? subject : "未找到");
            detail_info.add((keywords != null && !keywords.equals("")) ? keywords : "未找到");
            detail_info.add((doi != null && !doi.equals("")) ? doi : "未找到");
            detail_info.add((journal != null && !journal.equals("")) ? journal : "未找到");

            document.close();
            return detail_info;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 提取 DOI
    private static String extractDOI(String text) {
        Pattern pattern = Pattern.compile("\\b10\\.\\d{4,9}/[-._;()/:A-Z0-9]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    // 简单提取期刊名（规则可以根据你的文档格式优化）
    private static String extractJournalName(String text) {
        Pattern pattern = Pattern.compile("(Journal of [A-Za-z \\-]+)|(IEEE Transactions on [A-Za-z \\-]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
