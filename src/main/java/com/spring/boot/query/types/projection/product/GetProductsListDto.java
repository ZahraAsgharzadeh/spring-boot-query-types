package com.spring.boot.query.types.projection.product;

import com.spring.boot.query.types.projection.brand.GetBrandIdAndEnNameDto;

public interface GetProductsListDto extends GetProductIdAndEnNameDto {

    GetBrandIdAndEnNameDto getBrand();
}
