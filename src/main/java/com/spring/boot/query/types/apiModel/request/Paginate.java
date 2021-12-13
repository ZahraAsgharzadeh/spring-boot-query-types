package com.spring.boot.query.types.apiModel.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Paginate {

    private final int number;

    private final int size;
}
