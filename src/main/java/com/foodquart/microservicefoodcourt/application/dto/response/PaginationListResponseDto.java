package com.foodquart.microservicefoodcourt.application.dto.response;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginationListResponseDto<T>{
    private List<T> content;
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
