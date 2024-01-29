package com.lab2.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "prescribed_medications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PrescribedMedication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_visit")
    private Integer idVisit;

    @Column(name = "id_medication")
    private Integer idMedication;
}