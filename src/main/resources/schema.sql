CREATE TABLE if not exists orders
(
    id   bigint PRIMARY KEY not null,
    data VARCHAR(255)       not null
);

CREATE TABLE if not exists notifications
(
    order_id bigint       not null,
    user_id  bigint       not null,
    message  varchar(100) not null,
    primary key (order_id, user_id)
);
