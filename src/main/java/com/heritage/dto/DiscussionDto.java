package com.heritage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionDto {
    private Long   id;
    private Long   userId;
    private String userName;
    private Long   monumentId;
    private String text;
    private String timestamp;
}
