package com.spring.boot.query.types.service;

import com.spring.boot.query.types.apiModel.request.Filter;
import com.spring.boot.query.types.apiModel.request.Paginate;
import com.spring.boot.query.types.apiModel.request.PaginationRequest;
import com.spring.boot.query.types.apiModel.response.PaginationResponse;
import com.spring.boot.query.types.entity.ProductEntity;
import com.spring.boot.query.types.enums.SortDirection;
import com.spring.boot.query.types.projection.product.GetProductIdAndEnNameDto;
import com.spring.boot.query.types.projection.product.GetProductIdEnNameAndBrandDto;
import com.spring.boot.query.types.projection.product.GetProductsListDto;
import com.spring.boot.query.types.repository.ProductRepository;
import org.hibernate.query.criteria.internal.BasicPathUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public PaginationResponse getAllWithPaginationAndFilter(PaginationRequest model) {

        Pageable pageable;

        Page page;
        Object content;

        pageable = PageRequest.of(model.getPaginate().getNumber(), model.getPaginate().getSize(), this.getSort(model.getSort()));

        if (!model.getFilters().isEmpty()) {

            page = repository.findAll(this.getSpecification(model.getFilters()), pageable);

            // Exact query result type is entity, Specification not support projection and we can map response to entity
             SpelAwareProxyProjectionFactory pf = new SpelAwareProxyProjectionFactory();
             content = page
                     .stream()
                     .map(c -> pf.createProjection(GetProductsListDto.class, c))
                     .collect(Collectors.toList());

        } else {

            page = repository.findAllBy(GetProductsListDto.class, pageable);
            content = page.getContent();
        }

        return PaginationResponse.builder()
                .result(content)
                .totalElements((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

    }

    @Override
    public PaginationResponse getAllWithPagination(Paginate paginate) {

        Page page = repository.findAllBy(GetProductsListDto.class, PageRequest.of(paginate.getNumber(), paginate.getSize()));

        return PaginationResponse.builder()
                .result(page.getContent())
                .totalElements((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public List<GetProductsListDto> getAllWithoutPagination() {
        return repository.getAllBy(GetProductsListDto.class);
    }

    @Override
    public List<GetProductIdEnNameAndBrandDto> getAllByNativeQuery() {
        return repository.findAllByNativeQuery();
    }

    @Override
    public GetProductIdAndEnNameDto getByIdJPQL(Long id) {
        return repository.findByIdJPQL(id);
    }

    @Override
    public ProductEntity getById(Long id) {
        Optional<ProductEntity> foundEntity = repository.findById(id);
        return foundEntity.orElse(null);
    }

    @Override
    public List<GetProductIdAndEnNameDto> getAllByEnNameLikeJPQL(String enName) {
        return repository.findAllByEnNameLikeJPQL(enName);
    }

    @Override
    public List<GetProductIdAndEnNameDto> getAllByEnNameContains(String enName) {
        return repository.getAllByEnNameContains(enName);
    }

    @Override
    public ProductEntity getByIdCriteriaQuery(Long id) {

        // Use entity graph to join on brand too because brand relation ship is .Lazy fetch type
        EntityGraph<ProductEntity> entityGraph = entityManager.createEntityGraph(ProductEntity.class);
        entityGraph.addAttributeNodes("brand");

        return entityManager.createQuery("select e from ProductEntity e where e.id = :id", ProductEntity.class)
                .setParameter("id", id)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getSingleResult();
    }

    @Override
    public List<GetProductIdAndEnNameDto> getAllFromFile() {
        return repository.getAllFromFile();
    }

    /**
     * Generate sort for returning data
     *
     * @param requestedSort client requested sort
     * @return domain sort object
     */
    private Sort getSort(com.spring.boot.query.types.apiModel.request.Sort requestedSort) {

        Sort sort;

        if (requestedSort != null) {

            sort = Sort.by(Sort.Direction.valueOf(requestedSort.getDirection().name()), requestedSort.getProperty());
        } else {
            sort = Sort.by(Sort.Direction.valueOf(SortDirection.DESC.name()), "id");
        }

        return sort;
    }

    /**
     * Generate specification for creating query using criteria builder to send to repository
     *
     * @param filters user requested filters
     * @return created query using criteria builder
     */
    private Specification<ProductEntity> getSpecification(List<Filter> filters) {

        return (root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Iterate filter params to check some condition
            for (Filter filter : filters) {

                try {
                    String[] relationShipName = filter.getProperty().split("\\.");
                    Join<Object, Object> relationShips = root.join(relationShipName[0], JoinType.LEFT);

                    String filterParameter = "";

                    if (relationShipName.length > 1)
                        filterParameter = relationShipName[1];

                    // Check if relation ship parameter is id , builder should be equal to that id not like
                    switch (filterParameter) {

                        case "id":
                            predicates.add(builder.equal(relationShips.get(filterParameter).as(Long.class), Long.valueOf(filter.getValue())));
                            break;

                        case "":

                            if (Boolean.valueOf(filter.getValue()).equals(true))
                                predicates.add(builder.isNotNull(relationShips));
                            else
                                predicates.add(builder.isNull(relationShips));

                            break;

                        default:
                            predicates.add(builder.like(relationShips.get(filterParameter).as(String.class), "%" + filter.getValue() + "%"));
                            break;
                    }

                } catch (BasicPathUsageException exception) {

                    // Filter is base on attribute not relation ship
                    if (root.get(filter.getProperty()).getJavaType().equals(Boolean.class)) {
                        predicates.add(builder.equal(root.get(filter.getProperty()).as(Boolean.class), Boolean.valueOf(filter.getValue())));

                    } else if (root.get(filter.getProperty()).getJavaType().equals(Date.class)) {

                        // filter on Date type
                        // predicates.add(builder.and(builder.between(root.get(filter.getProperty()).as(Date.class), from, to)));

                    } else if (root.get(filter.getProperty()).getJavaType().equals(BigInteger.class)) {

                        // filter on Biginteger type
                        // predicates.add(builder.between(root.get(filter.getProperty()).as(BigInteger.class), from, to));

                    } else {

                        predicates.add(builder.like(root.get(filter.getProperty()).as(String.class), "%" + filter.getValue() + "%"));
                    }
                }
            }

            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

}


