package com.rest_rpg.user.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;

@RequiredArgsConstructor
public abstract class BaseQuerydslRepository<T> {

  protected final JPAQueryFactory queryFactory;
  protected final EntityManager entityManager;
  protected final PathBuilderFactory pathBuilderFactory = new PathBuilderFactory();

  protected Page<T> fetchPage(EntityPathBase<T> root, BooleanBuilder predicate, Pageable pageable) {
    PathBuilder<? extends T> pathBuilder = pathBuilderFactory.create(root.getType());

    Querydsl querydsl = new Querydsl(entityManager, pathBuilder);

    JPAQuery<Long> idQuery =
        queryFactory.select(pathBuilder.getNumber("id", Long.class)).from(root).where(predicate);

    querydsl.applyPagination(pageable, idQuery);

    List<Long> ids = idQuery.fetch();

    JPAQuery<T> query =
        queryFactory.selectFrom(root).where(pathBuilder.getNumber("id", Long.class).in(ids));

    //        if (entityGraphName != null) {
    //            query.setHint("javax.persistence.fetchgraph",
    //                    entityManager.getEntityGraph(entityGraphName));
    //        }

    querydsl.applySorting(pageable.getSort(), query);

    List<T> content = query.fetch();
    //noinspection deprecation
    long total = idQuery.fetchCount();

    return new PageImpl<>(content, pageable, total);
  }
}
