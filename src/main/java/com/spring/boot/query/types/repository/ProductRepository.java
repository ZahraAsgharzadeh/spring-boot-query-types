package com.spring.boot.query.types.repository;

import com.spring.boot.query.types.entity.ProductEntity;
import com.spring.boot.query.types.projection.product.GetProductIdAndEnNameDto;
import com.spring.boot.query.types.projection.product.GetProductIdEnNameAndBrandDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    /**
     * Select all records with Specification filter(without projection), sort and pagination
     * @param specification specification
     * @param pageable contains paging and sorting
     * @return list of records, count of total elements , count of all pages
     */
    @Override
    Page<ProductEntity> findAll(Specification<ProductEntity> specification, Pageable pageable);

    /**
     * Select all records with projection and pageable
     * @param type projection type
     * @param pageable contains paging and sorting
     * @param <T> generic type of projection type
     * @return page of requested records
     */
    <T> Page<T> findAllBy(Class<T> type, Pageable pageable);

    /**
     * Select all records with projection but not with pageable, it returns all results even if it's 10 or 1000
     * @param type projection type
     * @param <T> generic type of projection type
     * @return list of all records
     */
    <T> List<T> getAllBy(Class<T> type);

    /**
     * Select all records with native query without limit, offset and where clause
     * @return list of all records
     */
    @Query(value = "SELECT p.id, p.en_name, b.id AS 'brandId', b.en_name AS 'brandEnName' FROM product p LEFT OUTER JOIN brand b ON b.id = p.brand_id;", nativeQuery = true)
    List<GetProductIdEnNameAndBrandDto> findAllByNativeQuery();

    /**
     * Select all from product from a file
     * @return list of id and enName
     */
    @Query(value = "CALL spGetAllProducts(); ", nativeQuery = true)
    List<GetProductIdAndEnNameDto> getAllFromFile();

    @Query(value = "select a from ProductEntity a where a.id = :id")
    GetProductIdAndEnNameDto findByIdJPQL(@Param("id") Long id);

    /**
     * Use entity graph to join on brand too because brand relation ship is .Lazy fetch type
     * @param id id
     * @return product entity if exists
     */
    @EntityGraph(attributePaths = { "brand" })
    Optional<ProductEntity> findById(Long id);

    @Query(value = "select a from ProductEntity a where a.enName like %:enName%")
    List<GetProductIdAndEnNameDto> findAllByEnNameLikeJPQL(@Param("enName") String enName);

    List<GetProductIdAndEnNameDto> getAllByEnNameContains(String enName);

}
