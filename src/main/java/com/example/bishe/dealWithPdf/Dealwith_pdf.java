package com.example.bishe.dealWithPdf;

import com.example.bishe.SpringContextUtil.SpringContextUtil;
import com.example.bishe.service.NodeService;
import com.example.bishe.service.PaperService;

import java.io.File;
import java.util.List;

public class Dealwith_pdf {
    PaperService paperService;
    String username;

    public Dealwith_pdf(String username){
        paperService = SpringContextUtil.getBean(PaperService.class);
        this.username = username;
    }

    public void readpdf() {
        String directoryPath = "G:\\code\\bishe\\bishe\\src\\main\\java\\com\\example\\bishe\\pdf_paper";
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            traversePdfFiles(directory);
        } else {
            System.out.println("指定的目录不存在或不是一个有效的目录。");
        }
    }

    public void traversePdfFiles(File directory) {
        Entity_Relation_reco entity_Relation_reco;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归遍历子目录
                    traversePdfFiles(file);
                } else if (file.getName().toLowerCase().endsWith(".pdf")) {
                    // 处理 .pdf 文件
                    String fileNameWithoutExtension = file.getName().substring(0, file.getName().lastIndexOf("."));

                    String title = add_to_sql(file.getAbsolutePath(),fileNameWithoutExtension);

                    entity_Relation_reco = new Entity_Relation_reco(file.getAbsolutePath(),fileNameWithoutExtension);
                    System.out.println(entity_Relation_reco.sentences.size());
                    entity_Relation_reco.main();
                }
            }
        }
    }

    public String add_to_sql(String path,String filename){
        List<String> detail_info = PDFMetadataExtractor.extractMetadata(path);
        paperService.add_paper(username,filename,detail_info.get(1),detail_info.get(2),detail_info.get(3),detail_info.get(4),detail_info.get(5));
        return filename;
    }

}
