create table crashes
(
    id serial
        constraint crashes_pk
            primary key,
    path_on_disc varchar not null,
    ts timestamp default now() not null,
    crash_in_version varchar,
    fixed_in_version int,
    user_description varchar,
    fixer_description varchar
);

comment on table crashes is 'metadata about crashes, the actual data is on disk';

create unique index on crashes (path_on_disc);

