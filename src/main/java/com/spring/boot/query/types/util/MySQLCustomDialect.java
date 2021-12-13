package com.spring.boot.query.types.util;

import org.hibernate.dialect.MySQL8Dialect;
import org.springframework.stereotype.Component;

@Component
public class MySQLCustomDialect extends MySQL8Dialect {

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    }

}
