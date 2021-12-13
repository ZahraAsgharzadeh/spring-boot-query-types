package com.spring.boot.query.types.apiModel.request;

import com.spring.boot.query.types.enums.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Sort {

    private final String property;

    private final SortDirection direction;
}
