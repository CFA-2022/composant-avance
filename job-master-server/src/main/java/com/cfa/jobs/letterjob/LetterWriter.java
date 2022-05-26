package com.cfa.jobs.letterjob;

import com.cfa.objects.letter.Letter;
import com.cfa.objects.letter.LetterRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class LetterWriter implements ItemWriter<Letter> {

    @Autowired
    public LetterRepository letterRepository;

    @Autowired
    public RestTemplate restTemplate;

    @Override
    public void write(List<? extends Letter> list) throws Exception {
        restTemplate.postForEntity("http://localhost:9623/api/v1/lettres", list, ResponseEntity.class);
    }
}
