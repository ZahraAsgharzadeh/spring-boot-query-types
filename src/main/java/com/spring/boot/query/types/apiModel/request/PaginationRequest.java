package com.spring.boot.query.types.apiModel.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginationRequest {

    private final Paginate paginate;

    private final Sort sort;

    private final List<Filter> filters;
}
