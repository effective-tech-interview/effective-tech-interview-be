CREATE TABLE feedback
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    created_date  timestamp,
    modified_date timestamp,
    feedback      VARCHAR(2048) NOT NULL,
    member_id     bigint        NOT NULL,
    question_id   bigint        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (question_id) REFERENCES question (id)
)
