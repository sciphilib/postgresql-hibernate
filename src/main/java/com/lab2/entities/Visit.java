package com.lab2.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "visits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_patient")
    private Integer idPatient;

    @Column(name = "id_doctor")
    private Integer idDoctor;

    @Column(name = "complaints")
    private String complaints;

    @Column(name = "date_visit")
    private LocalDateTime dateVisit;

    @Column(name = "date_discharge")
    private LocalDateTime dateDischarge;

    @Column(name = "date_close")
    private LocalDateTime dateClose;
}
