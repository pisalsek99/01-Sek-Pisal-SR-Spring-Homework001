package com.example.honework001.ticketdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pagination {
    private long totalElements;
    private int currentPage;
    private int pageSize;
    private int totalPages;
}
