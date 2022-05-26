package com.cfa.jobs.letterjob;

import com.cfa.objects.letter.Letter;
import com.cfa.objects.letter.LetterRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LetterWriter implements ItemWriter<Letter> {

    @Autowired
    public LetterRepository letterRepository;

    @Override
    public void write(List<? extends Letter> list) throws Exception {
        letterRepository.saveAll(list);
        Thread.sleep(5000);
    }
}
