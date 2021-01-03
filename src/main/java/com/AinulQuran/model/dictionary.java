package com.AinulQuran.model;


import javax.persistence.*;

@Entity
@Table(name = "dictionary")
public class dictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    @Column(name = "Word_P")
    private String wordp;


    private String word;


}
