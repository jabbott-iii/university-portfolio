package edu.wgu.d387_sample_code.rest;

import edu.wgu.d387_sample_code.lang.DisplayMessages;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/resources")
@CrossOrigin
public class WelcomeController {

    private Executor executor = Executors.newFixedThreadPool(2);

    //Displays both the french and english translations of the welcome message
    @GetMapping("welcome")
    public ResponseEntity<List<String>>getWelcomeMessage() {
        List<String> str_out = new ArrayList<String>();
        executor.execute(() -> {
            DisplayMessages enCA = new DisplayMessages("en", "CA");
            System.out.println("Thread 1 " + enCA.getWelcomeMessages());
            str_out.add(enCA.getWelcomeMessages());
        });

        executor.execute(()->{
            DisplayMessages frCA = new DisplayMessages("fr", "CA");
            System.out.println("Thread 2 " + frCA.getWelcomeMessages());
            str_out.add(frCA.getWelcomeMessages());
        });
        return ResponseEntity.ok(str_out);
    }
}
