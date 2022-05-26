package com.cfa.objects.letter;

import com.cfa.objects.letter.Letter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LetterRepository extends JpaRepository<Letter, Long> {
    List<Letter> findAllByCreationDate(Date creationDate);
    List<Letter> findAllByTreatmentDate(Date treatmentDate);
    List<Letter> findAllByMessage(String message);

}
