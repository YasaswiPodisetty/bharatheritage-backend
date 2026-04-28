package com.heritage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySummaryDto {
    private Map<String, Integer>       pageVisits;
    private List<TourRegistrationDto>  toursRegistered;
    private List<MonumentVisitDto>     monumentsVisited;
}
