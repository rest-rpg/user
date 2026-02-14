package com.rest_rpg.user.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringPath;
import java.util.Collection;

public class QuerydslPredicateBuilder {

  private final BooleanBuilder builder = new BooleanBuilder();

  public QuerydslPredicateBuilder like(StringPath path, String value) {
    if (value != null && !value.isBlank()) {
      builder.and(path.containsIgnoreCase(value));
    }
    return this;
  }

  public QuerydslPredicateBuilder eq(BooleanPath path, Boolean value) {
    if (value != null) {
      builder.and(path.eq(value));
    }
    return this;
  }

  public <T> QuerydslPredicateBuilder in(SimpleExpression<T> path, Collection<T> values) {
    if (values != null && !values.isEmpty()) {
      builder.and(path.in(values));
    }
    return this;
  }

  public QuerydslPredicateBuilder isFalse(BooleanPath path) {
    if (path != null) {
      builder.and(path.isFalse());
    }
    return this;
  }

  public BooleanBuilder build() {
    return builder;
  }
}
