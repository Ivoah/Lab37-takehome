create table "order" (
    id         text not null,
    updated    datetime not null,
    firstName  text not null,
    lastName   text not null,
    items      text not null,
    notes      text not null,
    dispatched boolean not null,
    scheduled  datetime not null,
    meta       text not null,
    primary key (id, updated)
);
