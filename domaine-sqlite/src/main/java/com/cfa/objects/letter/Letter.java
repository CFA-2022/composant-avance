package com.cfa.objects.letter;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "letter")
public class Letter implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String message;
    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "treatment_date")
    private Date treatmentDate;

    public Letter(String message, Date creationDate, Date treatmentDate) {
        this.message = message;
        this.creationDate = creationDate;
        this.treatmentDate = treatmentDate;
    }
}
