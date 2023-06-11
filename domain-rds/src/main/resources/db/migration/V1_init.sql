create table main_category
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    created_date  timestamp,
    modified_date timestamp,
    name          varchar(255),
    primary key (id)
);

create table member
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    created_date  timestamp,
    modified_date timestamp,
    deleted_at    timestamp,
    email         varchar(255),
    nickname      varchar(255) not null,
    password      varchar(255) not null,
    primary key (id)
);

create table member_answer
(
    id               BIGINT NOT NULL AUTO_INCREMENT,
    created_date     timestamp,
    modified_date    timestamp,
    member_answer    varchar(255),
    page_question_id bigint,
    primary key (id)
);
create table mid_category
(
    id               BIGINT NOT NULL AUTO_INCREMENT,
    created_date     timestamp,
    modified_date    timestamp,
    image_url        varchar(255),
    name             varchar(255),
    main_category_id bigint,
    primary key (id)
);
create table page
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    created_date  timestamp,
    modified_date timestamp,
    member_id     bigint,
    primary key (id)
);
create table page_question
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    created_date  timestamp,
    modified_date timestamp,
    page_id       bigint,
    question_id   bigint,
    primary key (id)
);
create table question
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_date       timestamp,
    modified_date      timestamp,
    answer             varchar(2000),
    parent_question_id bigint,
    question           varchar(255) not null,
    mid_category_id    bigint,
    primary key (id)
);

alter table member
    add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (email)
alter table member_answer
    add constraint FKsx3akqb127ppkkmlt8882twyy
        foreign key (page_question_id)
            references page_question
alter table mid_category
    add constraint FK5rcdxqlp0yf25iypm8dkd0opn
        foreign key (main_category_id)
            references main_category
alter table page
    add constraint FKfigeja88d57wqeotp6by7kxlw
        foreign key (member_id)
            references member
alter table page_question
    add constraint FKhvpn7kda2pond10soay1lm1n9
        foreign key (page_id)
            references page
alter table page_question
    add constraint FK2ilnyetll1gfljg91lgokshjl
        foreign key (question_id)
            references question
alter table question
    add constraint FKbfeuuyvvxlimrrqw4qgkgkvtf
        foreign key (mid_category_id)
            references mid_category
