package com.application.common.domain.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDto<T> {
    private List<T> someList;
    private int totalPages;
    private long totalCount;
}
