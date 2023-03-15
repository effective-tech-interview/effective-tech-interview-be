package com.sparcs.teamf.domain.page;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "page")
    private List<PageQuestion> pageQuestions = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "page_id")
    private List<PageMemberAnswer> pageMemberAnswers = new ArrayList<>();
}
