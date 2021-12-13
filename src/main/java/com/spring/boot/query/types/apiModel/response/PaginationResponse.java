package com.spring.boot.query.types.apiModel.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class PaginationResponse {

    private Object result;

    private int totalElements;

    private int totalPages;
}
