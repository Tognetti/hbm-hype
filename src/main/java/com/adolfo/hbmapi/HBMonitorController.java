package com.adolfo.hbmapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class HBMonitorController {

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        return modelAndView;
    }

    @GetMapping(value = "/data-stream")
    public SseEmitter dataStream(@RequestParam int timeInterval) {
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                long start = System.currentTimeMillis();
                while (true) {
                    long elapsedTime = System.currentTimeMillis() - start;
                    emitter.send(-0.06366 +
                            0.12613 * Math.cos(Math.PI * (elapsedTime / 500.0)) +
                            0.12258 * Math.cos(Math.PI * (elapsedTime / 250.0)) +
                            0.01593 * Math.sin(Math.PI * (elapsedTime / 500.0)) +
                            0.03147 * Math.sin(Math.PI * (elapsedTime / 250.0)));

                    Thread.sleep(timeInterval);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });

        return emitter;
    }

}
