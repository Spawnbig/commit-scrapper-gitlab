package com.scrapper.commits.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "repos")
@Getter
@Setter
public class Repo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Commits> commits;
}
