package com.example.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Article extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Setter @Column(nullable = false) private String title; // 제목
    @Setter @Column(nullable = false, length = 10000) private String content; // 본문

    @Setter private String hashtag; // 헤시태그

    @ToString.Exclude // 순환 참조 방지
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) // 양방향 매핑
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    protected Article() {}

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // Article을 생성할 때 어떤 값을 필요로 하는지 가이드
    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        // 지금 막 만든 아직 영속화되지 않은 엔티티는 모두 동등성 검사 탈락(각각 다른 값으로 보겠다.)
        // id가 부여되지 않았다면 동등성 검사 자체가 의미 없다는 것으로 보고 다 다른 것으로 간주하거나 혹은 처리하지 않겠다.
        // id가 같다면 당연히 두 객체는 같은 객체이다.
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
