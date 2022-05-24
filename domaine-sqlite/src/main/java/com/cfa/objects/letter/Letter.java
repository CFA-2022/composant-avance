package com.cfa.objects.letter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Letter {
    private String message;
    private Date creationDate;
    private Date treatmentDate;
}
