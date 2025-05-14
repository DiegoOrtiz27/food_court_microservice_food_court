package com.foodquart.microservicefoodcourt.domain.util;

import lombok.Getter;

import java.util.List;

@Getter
public class Pagination<T> {
    private final List<T> items;
    private final int page;
    private final int size;
    private final long totalItems;
    private final int totalPages;
    private final boolean hasNext;
    private final boolean hasPrevious;

    public Pagination(List<T> items, int page, int size, long totalItems) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalItems = totalItems;
        this.totalPages = calculateTotalPages(totalItems, size);
        this.hasNext = calculateHasNext(page, totalPages);
        this.hasPrevious = calculateHasPrevious(page);
    }

    private int calculateTotalPages(long totalItems, int size) {
        return size == 0 ? 0 : (int) Math.ceil((double) totalItems / size);
    }

    private boolean calculateHasNext(int page, int totalPages) {
        return page < totalPages - 1;
    }

    private boolean calculateHasPrevious(int page) {
        return page > 0;
    }
}
