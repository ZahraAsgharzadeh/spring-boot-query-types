package com.spring.boot.query.types.service;

import com.spring.boot.query.types.apiModel.request.Paginate;
import com.spring.boot.query.types.apiModel.request.PaginationRequest;
import com.spring.boot.query.types.apiModel.response.PaginationResponse;
import com.spring.boot.query.types.entity.ProductEntity;
import com.spring.boot.query.types.projection.product.GetProductIdAndEnNameDto;
import com.spring.boot.query.types.projection.product.GetProductIdEnNameAndBrandDto;
import com.spring.boot.query.types.projection.product.GetProductsListDto;

import java.util.List;

public interface ProductService {

    PaginationResponse getAllWithPaginationAndFilter(PaginationRequest model);

    PaginationResponse getAllWithPagination(Paginate paginate);

    List<GetProductsListDto> getAllWithoutPagination();

    List<GetProductIdEnNameAndBrandDto> getAllByNativeQuery();

    GetProductIdAndEnNameDto getByIdJPQL(Long id);

    ProductEntity getById(Long id);

    List<GetProductIdAndEnNameDto> getAllByEnNameLikeJPQL(String enName);

    List<GetProductIdAndEnNameDto> getAllByEnNameContains(String enName);

    ProductEntity getByIdCriteriaQuery(Long id);

    List<GetProductIdAndEnNameDto> getAllFromFile();

}


