package edu.wgu.d387_sample_code.rest;

import edu.wgu.d387_sample_code.lang.DisplayTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/resources")
public class TimeController {
    @GetMapping("times")
    public ResponseEntity<List<String>> getTimes(){
        DisplayTime DisplayTimes;
        List<String> str_out = DisplayTime.displayTimes();
        return ResponseEntity.ok(str_out);
    }
}