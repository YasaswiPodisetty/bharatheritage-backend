package com.heritage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonumentDto {
    private Long         id;
    private String       name;
    private String       city;
    private String       state;
    private String       era;
    private String       year;
    private String       category;
    private String       description;
    private String       image;
    private String       thumbnail;
    private List<String> facts;
    private List<String> tourPoints;
}
