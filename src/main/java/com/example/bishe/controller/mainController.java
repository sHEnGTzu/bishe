package com.example.bishe.controller;

import com.example.bishe.dealWithPdf.Dealwith_pdf;
import com.example.bishe.entity.User;
import com.example.bishe.response.Response;
import com.example.bishe.service.PaperService;
import com.example.bishe.service.UserService;
import com.example.bishe.userQuestion.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;

@RestController
public class mainController {

    Chat chat;

    @Autowired
    UserService userService;

    @Autowired
    PaperService paperService;


    @PostMapping("/question")
    public Response question(@RequestBody Map<String,String> map){
        chat = new Chat(map.get("message"),"deepseek-r1:1.5b");
        String papername = map.get("papername");

        return new Response(chat.allNodesQuestion(papername));
    }

    @PostMapping("/login")
    public Response login(@RequestBody Map<String,String> map){
        String username = map.get("username");
        String password = map.get("password");
        return userService.login(username,password);
    }

    @PostMapping("/register")
    public Response register(@RequestBody Map<String,String> map){
        String username = map.get("username");
        String password = map.get("password");
        String email = map.get("email");
        return userService.register(username,password,email);
    }

    @PostMapping("/getPapers")
    public Response getPapers(@RequestBody Map<String,String> map){
        String username = map.get("username");
        return paperService.find_paper_by_username(username);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("username") String username) {
        // 定义文件保存路径
        String uploadDir = "G:\\code\\bishe\\bishe\\src\\main\\java\\com\\example\\bishe\\pdf_paper\\";

        // 确保上传目录存在
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        } else {
            // 如果目录存在，检查是否为空，如果不为空，则清空目录中的文件
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                for (File fileToDelete : files) {
                    if (fileToDelete.isFile()) {
                        fileToDelete.delete();  // 删除文件
                    }
                }
            }
        }

        try {
            // 获取文件原始名称
            String fileName = file.getOriginalFilename();

            // 保存文件到指定路径
            File dest = new File(uploadDir + fileName);
            file.transferTo(dest);

            Dealwith_pdf dealwith_pdf = new Dealwith_pdf(username);
            dealwith_pdf.readpdf();

            return ResponseEntity.ok("文件上传并读取成功");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("文件上传失败");
        }


    }


}
