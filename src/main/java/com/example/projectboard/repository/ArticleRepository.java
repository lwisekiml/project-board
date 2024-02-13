package com.example.projectboard.repository;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, // 이 엔티티 안에 있는 모든 필드에 대한 기본 검색 기능을 추가 해줌
        QuerydslBinderCustomizer<QArticle> {

    Page<Article> findByTitle(String title, Pageable pageable);

    @Override // 검색에 대한 세부적인 규칙이 다시 재구성 됨(선택적 검색)
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true); // 리스팅하지 않은 프로퍼티는 검색에서 제외시키는 것을 true로
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy); // 원하는 필드 추가
//        bindings.bind(root.title).first(SimpleExpression::eq); // 아규먼트를 하나만 받아서 first() 사용 (path가 동일한가) / (path, value) -> path.eq(value)
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // like '${v}'
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
