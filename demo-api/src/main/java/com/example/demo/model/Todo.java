package com.example.demo.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    private String id;
    @NotNull
    @Size(min = 1, max = 20)
    private String label;
    private String description;
}
