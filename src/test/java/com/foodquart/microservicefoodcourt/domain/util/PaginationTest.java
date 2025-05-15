package com.foodquart.microservicefoodcourt.domain.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaginationTest {

    private final List<String> items = Arrays.asList("item1", "item2", "item3", "item4", "item5");

    @Test
    void shouldCreatePaginationWithCorrectValues() {
        int page = 0;
        int size = 2;
        long totalItems = 5;
        Pagination<String> pagination = new Pagination<>(items.subList(0, Math.min((page + 1) * size, items.size())), page, size, totalItems);

        assertEquals(items.subList(0, 2), pagination.getItems());
        assertEquals(page, pagination.getPage());
        assertEquals(size, pagination.getSize());
        assertEquals(totalItems, pagination.getTotalItems());
        assertEquals(3, pagination.getTotalPages());
        assertTrue(pagination.isHasNext());
        assertFalse(pagination.isHasPrevious());
    }

    @Test
    void shouldCalculateTotalPagesCorrectly() {
        assertEquals(3, new Pagination<>(Collections.emptyList(), 0, 2, 5).getTotalPages());
        assertEquals(1, new Pagination<>(Collections.emptyList(), 0, 5, 5).getTotalPages());
        assertEquals(0, new Pagination<>(Collections.emptyList(), 0, 0, 5).getTotalPages());
        assertEquals(0, new Pagination<>(Collections.emptyList(), 0, 2, 0).getTotalPages());
    }

    @Test
    void shouldCalculateHasNextCorrectly() {
        assertTrue(new Pagination<>(Collections.emptyList(), 0, 2, 5).isHasNext());
        assertTrue(new Pagination<>(Collections.emptyList(), 1, 2, 5).isHasNext());
        assertFalse(new Pagination<>(Collections.emptyList(), 2, 2, 5).isHasNext());
        assertFalse(new Pagination<>(Collections.emptyList(), 0, 5, 5).isHasNext());
        assertFalse(new Pagination<>(Collections.emptyList(), 0, 2, 0).isHasNext());
    }

    @Test
    void shouldCalculateHasPreviousCorrectly() {
        assertFalse(new Pagination<>(Collections.emptyList(), 0, 2, 5).isHasPrevious());
        assertTrue(new Pagination<>(Collections.emptyList(), 1, 2, 5).isHasPrevious());
        assertTrue(new Pagination<>(Collections.emptyList(), 2, 2, 5).isHasPrevious());
        assertFalse(new Pagination<>(Collections.emptyList(), 0, 5, 5).isHasPrevious());
        assertFalse(new Pagination<>(Collections.emptyList(), 0, 2, 0).isHasPrevious());
    }

    @Test
    void shouldHandleEmptyItemList() {
        Pagination<String> pagination = new Pagination<>(Collections.emptyList(), 0, 2, 0);
        assertTrue(pagination.getItems().isEmpty());
        assertEquals(0, pagination.getTotalPages());
        assertFalse(pagination.isHasNext());
        assertFalse(pagination.isHasPrevious());
    }

    @Test
    void shouldHandleSizeZero() {
        List<String> emptyList = Collections.emptyList();
        Pagination<String> pagination = new Pagination<>(emptyList, 0, 0, 5);
        assertEquals(Collections.emptyList(), pagination.getItems());
        assertEquals(0, pagination.getSize());
        assertEquals(0, pagination.getTotalPages());
        assertFalse(pagination.isHasNext());
        assertFalse(pagination.isHasPrevious());
    }

    @Test
    void shouldHandlePageAtTheEnd() {
        int page = 2;
        int size = 2;
        long totalItems = 5;
        Pagination<String> pagination = new Pagination<>(items.subList(page * size, items.size()), page, size, totalItems);

        assertEquals(List.of("item5"), pagination.getItems());
        assertEquals(page, pagination.getPage());
        assertEquals(size, pagination.getSize());
        assertEquals(totalItems, pagination.getTotalItems());
        assertEquals(3, pagination.getTotalPages());
        assertFalse(pagination.isHasNext());
        assertTrue(pagination.isHasPrevious());
    }

    @Test
    void shouldHandleSinglePage() {
        Pagination<String> pagination = new Pagination<>(items, 0, 10, 5);
        assertEquals(items, pagination.getItems());
        assertEquals(0, pagination.getPage());
        assertEquals(10, pagination.getSize());
        assertEquals(5, pagination.getTotalItems());
        assertEquals(1, pagination.getTotalPages());
        assertFalse(pagination.isHasNext());
        assertFalse(pagination.isHasPrevious());
    }

}