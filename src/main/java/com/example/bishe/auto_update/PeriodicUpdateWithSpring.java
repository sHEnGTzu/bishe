package com.example.bishe.auto_update;

import com.example.bishe.dealWithPdf.Dealwith_pdf;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class PeriodicUpdateWithSpring {

    // 延迟 86400000 毫秒（即 24 小时）后开始第一次执行，之后每 86400000 毫秒执行一次
    @Scheduled(initialDelay = 86400000, fixedRate = 86400000)
    public void performUpdate() {
        // 这里编写更新逻辑
        System.out.println("Performing update...");
        RunPythonScript.get_papers("1");
        Dealwith_pdf dealwith_pdf = new Dealwith_pdf("*");
        dealwith_pdf.readpdf();
    }

}
