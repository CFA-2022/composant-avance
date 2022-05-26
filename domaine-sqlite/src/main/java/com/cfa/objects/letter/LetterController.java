package com.cfa.objects.letter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lettres")
@Slf4j
@AllArgsConstructor
public class LetterController {
    @GetMapping("/")
    public List<Letter> getAll(@RequestParam(required = false) String message,
                               @RequestParam(required = false) Date creationDate,
                               @RequestParam(required = false) Date treatmentDate){
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public Letter getById(@PathVariable("id") long id){
        return null;
    }
}
