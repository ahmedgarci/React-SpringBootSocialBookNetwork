package com.example.demo.Book.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {

    private Integer id;
    private String title;
    private String authorName;
    private String synopsis;
    private String owner;
    private byte[] cover;
    private Double rate;
    private boolean archived;
    private boolean shareable;
}
