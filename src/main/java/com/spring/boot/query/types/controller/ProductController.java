package com.spring.boot.query.types.controller;

import com.spring.boot.query.types.apiModel.request.Paginate;
import com.spring.boot.query.types.apiModel.request.PaginationRequest;
import com.spring.boot.query.types.apiModel.response.PaginationResponse;
import com.spring.boot.query.types.entity.ProductEntity;
import com.spring.boot.query.types.projection.product.GetProductIdAndEnNameDto;
import com.spring.boot.query.types.projection.product.GetProductIdEnNameAndBrandDto;
import com.spring.boot.query.types.projection.product.GetProductsListDto;
import com.spring.boot.query.types.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "products-pagination-filter")
    public ResponseEntity<PaginationResponse> getAllWithPaginationAndFilter(@RequestBody PaginationRequest requestModel) {

        PaginationResponse response = productService.getAllWithPaginationAndFilter(requestModel);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "products-pagination")
    public ResponseEntity<PaginationResponse> getAllWithPagination(@RequestBody Paginate requestModel) {

        PaginationResponse response = productService.getAllWithPagination(requestModel);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "products-without-pagination")
    public ResponseEntity<List<GetProductsListDto>> getAllWithoutPagination() {

        List<GetProductsListDto> response = productService.getAllWithoutPagination();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "products-without-pagination-native-query")
    public ResponseEntity<List<GetProductIdEnNameAndBrandDto>> getAllByNativeQuery() {

        List<GetProductIdEnNameAndBrandDto> response = productService.getAllByNativeQuery();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "products-jpql/{id}")
    public ResponseEntity<GetProductIdAndEnNameDto> getByIdJPQL(@PathVariable("id") Long id) {

        GetProductIdAndEnNameDto response = productService.getByIdJPQL(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "products/{id}")
    public ResponseEntity<ProductEntity> getById(@PathVariable("id") Long id) {

        ProductEntity response = productService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "products-jpql-en-name-contains/{enName}")
    public ResponseEntity<List<GetProductIdAndEnNameDto>> getAllByEnNameLikeJPQL(@PathVariable("enName") String enName) {

        List<GetProductIdAndEnNameDto> response = productService.getAllByEnNameLikeJPQL(enName);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "products-en-name-contains/{enName}")
    public ResponseEntity<List<GetProductIdAndEnNameDto>> getAllByEnNameContains(@PathVariable("enName") String enName) {

        List<GetProductIdAndEnNameDto> response = productService.getAllByEnNameContains(enName);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "products-criteria-query/{id}")
    public ResponseEntity<ProductEntity> getByIdCriteriaQuery(@PathVariable("id") Long id) {

        ProductEntity response = productService.getByIdCriteriaQuery(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "products-from-file")
    public ResponseEntity<List<GetProductIdAndEnNameDto>> getAllFromFile() {

        List<GetProductIdAndEnNameDto> response = productService.getAllFromFile();
        return ResponseEntity.ok(response);
    }
}
