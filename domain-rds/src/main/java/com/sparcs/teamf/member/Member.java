package com.sparcs.teamf.member;

import com.sparcs.teamf.BaseEntity;
import com.sparcs.teamf.page.Page;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted_at is null")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<Page> pages = new ArrayList<>();

    @Column(nullable = false)
    private String nickname;
    @Column(unique = true)
    private String email;
    @Column
    @Enumerated(EnumType.STRING)
    private ProviderType provider;
    @Column
    private String providerId;
    @Column
    private String password;
    private LocalDateTime deletedAt;

    public Member(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public static Member ofOauth(String nickname, String provider, String providerId) {
        return Member.builder()
            .nickname(nickname)
            .provider(ProviderType.from(provider))
            .providerId(providerId)
            .build();
    }

    public static Member of(String nickname, String email, String password) {
        return new Member(nickname, email, password);
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
