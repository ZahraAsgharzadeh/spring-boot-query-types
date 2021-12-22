# spring-boot-query-types

Post man collection is in resource folder .

1- Named query using specification dynamic filter(without projection), sort and pagination =>

`Page<ProductEntity> findAll(Specification<ProductEntity> specification, Pageable pageable);`

2- Named query using projection and pageable

`<T> Page<T> findAllBy(Class<T> type, Pageable pageable);`

3- Named query using projection but not with pageable, it returns all results even if it's 10 or 1000

`<T> List<T> getAllBy(Class<T> type);`

4- Native query without limit, offset and where clause

`@Query(value = "SELECT p.id, p.en_name, b.id AS 'brandId', b.en_name AS 'brandEnName' FROM product p LEFT OUTER JOIN brand b ON b.id = p.brand_id;", nativeQuery = true)
    List<GetProductIdEnNameAndBrandDto> findAllByNativeQuery();`
    
5- From a file

`@Query(value = "CALL spGetAllProducts(); ", nativeQuery = true)
    List<GetProductIdAndEnNameDto> getAllFromFile();`
    
6- JPQL

`@Query(value = "select a from ProductEntity a where a.id = :id")
    GetProductIdAndEnNameDto findByIdJPQL(@Param("id") Long id);`

7- Entity graph

`@EntityGraph(attributePaths = { "brand" })
Optional<ProductEntity> findById(Long id);`

8- JPQL

`@Query(value = "select a from ProductEntity a where a.enName like %:enName%")
    List<GetProductIdAndEnNameDto> findAllByEnNameLikeJPQL(@Param("enName") String enName);`

9- Named query

`List<GetProductIdAndEnNameDto> getAllByEnNameContains(String enName);`
