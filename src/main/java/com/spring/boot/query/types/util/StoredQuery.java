package com.spring.boot.query.types.util;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class StoredQuery {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        jdbcTemplate.execute(DROP_GET_ALL_PRODUCTS_PROCEDURE);
        jdbcTemplate.execute(CREATE_GET_ALL_PRODUCTS_PROCEDURE);

    }

    public static final String CREATE_GET_ALL_PRODUCTS_PROCEDURE =
            "CREATE DEFINER=`root`@`localhost` PROCEDURE `spGetAllProducts`()\n" +
                    "BEGIN \n" +
                    "SELECT id, en_name AS enName FROM `product` ORDER BY id ASC;\n" +
                    "END ";

    public static final String DROP_GET_ALL_PRODUCTS_PROCEDURE = "DROP PROCEDURE IF EXISTS spGetAllProducts";

}
