package com.example.honework001.ticketdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListResponse<T> {
    private List<ListResponse> items;
    private Pagination pagination;
}