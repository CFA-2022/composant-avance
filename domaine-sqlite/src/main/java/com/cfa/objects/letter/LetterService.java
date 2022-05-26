package com.cfa.objects.letter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class LetterService {
    @Autowired
    private final LetterRepository letterRepository;

    public List<Letter> getAllLetters(final String message, final Date creationDate, final Date treatmentDate){
        return letterRepository.findAll();
    }

    public Letter getLetterById(final long id){
        return letterRepository.findById(id).orElse(null);
    }

    public long saveLetter(Letter letter){
        return letterRepository.save(letter).getId();
    }
}
