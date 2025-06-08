package com.example.bishe.auto_update;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class RunPythonScript {

    public static void get_papers(String num){
        try {
            // 清空指定文件夹下的内容
            clearFolder(new File("G:\\code\\bishe\\bishe\\src\\main\\java\\com\\example\\bishe\\pdf_paper"));

            // 构建 Python 命令
            ProcessBuilder pb = new ProcessBuilder("python", "G:\\code\\bishe\\bishe\\src\\main\\java\\com\\example\\bishe\\python\\pachong.py","https://www.nature.com/nature/articles?type=article",num);
            // 启动进程
            Process process = pb.start();

            // 读取 Python 脚本的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 读取错误输出
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println(errorLine);
            }

            // 等待进程执行完毕
            int exitCode = process.waitFor();
            System.out.println("Python script exited with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 清空文件夹的方法
    private static void clearFolder(File folder) {
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归清空子文件夹
                        clearFolder(file);
                    }
                    // 删除文件或空文件夹
                    if (file.delete()) {
                        System.out.println("Deleted: " + file.getAbsolutePath());
                    } else {
                        System.err.println("Failed to delete: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }

//    public static void main(String[] args) {
//        try {
//            // 构建 Python 命令
//            ProcessBuilder pb = new ProcessBuilder("python", "G:\\code\\bishe\\bishe\\src\\main\\java\\com\\example\\bishe\\python\\pachong.py","https://www.nature.com/nature/articles?type=article","2");
//            // 启动进程
//            Process process = pb.start();
//
//            // 读取 Python 脚本的输出
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            // 读取错误输出
//            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            String errorLine;
//            while ((errorLine = errorReader.readLine()) != null) {
//                System.err.println(errorLine);
//            }
//
//            // 等待进程执行完毕
//            int exitCode = process.waitFor();
//            System.out.println("Python script exited with code " + exitCode);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}