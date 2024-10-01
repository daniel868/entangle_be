-- init tables
create table ent_user
(
    id                     numeric not null primary key,
    username               varchar(255),
    password               varchar(255),
    email                  varchar(255),
    info                   text,
    validation_email_token varchar(255),
    account_activate       boolean
);

create table ent_disease
(
    id   numeric not null primary key,
    name text
);

create table ent_treatment
(
    id          numeric not null primary key,
    description text,
    user_id     numeric default null,
    disease_id  numeric default null,
    constraint fk_user_id foreign key (user_id) references ent_user (id),
    constraint fk_disease foreign key (disease_id) references ent_disease (id)
);

create table ent_competence
(
    id      numeric not null primary key,
    type    varchar(255),
    name    varchar(255),
    user_id numeric default null,
    constraint fk_competence_user_id foreign key (user_id) references ent_user (id)
);

create table ent_treatment_competence
(
    competence_id numeric default null,
    treatment_id  numeric default null,
    primary key (competence_id, treatment_id),
    constraint fk_competence_id foreign key (competence_id) references ent_competence (id),
    constraint fk_treatment_id foreign key (treatment_id) references ent_treatment (id)
);

commit;

create sequence ent_user_sequence increment 1 minvalue 1;
create sequence treatment_sequence_generator increment 1 minvalue 1;
create sequence disease_sequence_generator increment 1 minvalue 1;
create sequence competence_sequence_generator increment 1 minvalue 1;

-- init data
-- insert into ent_user(id, username)
-- values (1, 'test');

commit;

-- insert into ent_disease(id, name)
-- values (1, 'disease1');
-- insert into ent_disease(id, name)
-- values (2, 'disease2');
-- insert into ent_disease(id, name)
-- values (3, 'disease3');

commit;

-- insert competences for the user
-- insert into ent_competence (id, type, user_id, name)
-- values (1, 'QUALIFICATION', 1, 'D1');
--
-- insert into ent_competence (id, type, user_id, name)
-- values (2, 'QUALIFICATION', 1, 'D2');

commit;

-- 2 treatments from the same user for the same disease
-- insert into ent_treatment (id, description, user_id, disease_id)
-- values (1, 'solve1_1', 1, 1);
-- insert into ent_treatment (id, description, user_id, disease_id)
-- values (2, 'solve1_2', 1, 1);

commit;

-- connect between treatment and competence like
-- treatment_1 -> D1
-- treatment_1 -> D2
-- insert into ent_treatment_competence (id, treatment_id, competence_id)
-- values (1, 1, 1);
-- insert into ent_treatment_competence (id, treatment_id, competence_id)
-- values (2, 1, 2);

commit;

-- select D.name,
--        T.description as treatment_description,
--        TC.competence_id,
--        C.name as treatment_category
-- from ent_disease D
--          -- Left join to include all diseases, even those without treatments
--          left join ent_treatment T on D.ID = T.disease_id
--     -- Left join to include treatments without competences, but exclude them later
--          left join ent_treatment_competence TC on TC.treatment_id = T.ID
--     -- Left join to fetch competences but restrict them to the user
--          left join ent_competence C on C.ID = TC.competence_id and C.user_id = 2
-- where C.ID is not null or T.ID is null;