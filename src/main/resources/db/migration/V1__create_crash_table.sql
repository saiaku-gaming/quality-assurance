create table crashes
(
    id serial
        constraint crashes_pk
            primary key,
    path_on_disc text not null,
    ts timestamp default now() not null,
    crash_in_version text,
    fixed_in_version int,
    user_description text,
    fixer_description text
);

comment on table crashes is 'metadata about crashes, the actual data is on disk';

create unique index on crashes (path_on_disc);

