package com.rest_rpg.user.repository;

import com.ms.user.model.UserFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rest_rpg.user.model.QUser;
import com.rest_rpg.user.model.User;
import com.rest_rpg.user.querydsl.BaseQuerydslRepository;
import com.rest_rpg.user.querydsl.QuerydslPredicateBuilder;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryCustomImpl extends BaseQuerydslRepository<User>
    implements UserRepositoryCustom {

  public UserRepositoryCustomImpl(JPAQueryFactory queryFactory, EntityManager entityManager) {
    super(queryFactory, entityManager);
  }

  @Override
  public Page<User> findUsers(UserFilter filter, Pageable pageable) {
    QUser user = QUser.user;

    BooleanBuilder predicate = buildPredicate(user, filter);

    predicate.and(user.deleted.isFalse());

    return fetchPage(user, predicate, pageable);
  }

  private BooleanBuilder buildPredicate(QUser user, UserFilter filter) {
    return new QuerydslPredicateBuilder()
        .like(user.username, filter.getUsername())
        .like(user.email, filter.getEmail())
        .eq(user.enabled, filter.getEnabled())
        .in(user.role, filter.getRole())
        .isFalse(user.deleted)
        .build();
  }
}
