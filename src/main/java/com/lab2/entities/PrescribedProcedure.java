package com.lab2.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "prescribed_procedures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PrescribedProcedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_visit")
    private Integer idVisit;

    @Column(name = "id_procedure")
    private Integer idMedication;

    @Column(name = "count")
    private Integer count;
}