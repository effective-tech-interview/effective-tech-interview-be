package com.sparcs.teamf.domain.emailauth;

import com.sparcs.teamf.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer verificationCode;

    @Column(nullable = false)
    private Boolean isAuthenticated;

    public EmailAuth(String email, Integer verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.isAuthenticated = false;
    }

    public static EmailAuth of(String email, Integer verificationCode) {
        return new EmailAuth(email, verificationCode);
    }

    public void authenticate() {
        this.isAuthenticated = true;
    }
}
