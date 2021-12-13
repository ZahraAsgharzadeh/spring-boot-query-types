package com.spring.boot.query.types.apiModel.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Filter {

    private final String property;

    private final String value;
}
