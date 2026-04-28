package com.heritage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonumentVisitDto {
    private Long   id;
    private String name;
    private String city;
    private String visitedAt;
}
