package com.cfa.objects.letter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lettres")
@Slf4j
@AllArgsConstructor
public class LetterController {

    private final LetterService letterService;

    @GetMapping("/")
    public List<Letter> getAll(@RequestParam(required = false) String message,
                               @RequestParam(required = false) Date creationDate,
                               @RequestParam(required = false) Date treatmentDate) {
        return letterService.getAllLetters(message, creationDate, treatmentDate);
    }

    @GetMapping("/{id}")
    public Letter getById(@PathVariable("id") long id) {
        return letterService.getLetterById(id);
    }

    @PostMapping("")
    public ResponseEntity<List<Letter>> saveLetters(@RequestBody List<Letter> letters) {
        return ResponseEntity.status(HttpStatus.CREATED).body(letterService.saveLetters(letters));
    }

    /*
    @PostMapping("")
    public long saveLetter(@RequestBody Letter letter) {
        return letterService.saveLetter(letter);
    }
     */
}
