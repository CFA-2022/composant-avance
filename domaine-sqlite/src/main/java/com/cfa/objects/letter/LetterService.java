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

    public List<Letter> getAllLettersByCreationDate(final Date creationDate){
        return letterRepository.findAllByCreationDate(creationDate);
    }

    public List<Letter> getAllLettersByTreatmentDate(final Date treatmentDate){
        return letterRepository.findAllByTreatmentDate(treatmentDate);
    }

    public List<Letter> findAllByMessage(final String message){
        return letterRepository.findAllByMessage(message);
    }
    public Letter getLetterById(final long id){
        return letterRepository.findById(id).orElse(null);
    }

    public long saveLetter(Letter letter){
        return letterRepository.save(letter).getId();
    }

    public List<Letter> saveLetters(List<Letter> letters){
        return letterRepository.saveAll(letters);
    }
}
