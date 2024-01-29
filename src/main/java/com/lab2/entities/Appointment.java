package com.lab2.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import java.time.LocalTime;

import javax.persistence.*;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_doctor")
    private Integer idDoctor;

    @Column(name = "id_weekday")
    private Integer idWeekday;

    @Column(name = "begin_date")
    private LocalTime beginDate;

    @Column(name = "end_date")
    private LocalTime endDate;

    @Column(name = "office")
    private Integer office;

    @Column(name = "district")
    private Integer district;
}
