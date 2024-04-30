package com.scrapper.commits.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestDTO {

    @NotNull
    @Size(min = 1, max = 1000)
    private String url;

    @NotNull
    private boolean allCommits;
}
