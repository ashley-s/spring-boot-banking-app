create table customers
(
    id             bigint auto_increment not null,
    ref_id         varchar (20) unique not null,
    name           varchar (20) null,
    is_active      tinyint not null,
    primary key (id)
);

create table accounts
(
    id              bigint auto_increment not null,
    customer_id bigint not null,
    account_number  varchar (20) unique not null,
    current_balance numeric (20,2) null,
    primary key (id)
);

create table transactions
(
    id                 bigint auto_increment not null,
    accounts_id        bigint not null,
    source_account_id  bigint not null,
    target_account_id  bigint not null,
    amount             numeric (20,2) null,
    reference          varchar (36) not null,
    completion_date    datetime not null,
    primary key (id)
);

create table account_transfers
(
    id                      bigint auto_increment not null,
    account_sender_id       bigint not null,
    account_receiver_id     bigint not null,
    amount                  numeric (20,2) null,
    date_created            datetime null,
    transfer_type           varchar (20) not null,
    status                  varchar (20) not null,
    reference               varchar (36) not null,
    primary key (id)
);

alter table accounts
    add constraint accounts_customers_fk_1 foreign key (customer_id) references customers (id) on delete cascade;
alter table transactions
    add constraint transactions_accounts_fk_1 foreign key (accounts_id) references accounts (id) on delete cascade;
alter table account_transfers
    add constraint accountstransfer_sender_fk_1 foreign key (account_sender_id) references accounts (id) on delete cascade;
alter table account_transfers
    add constraint accountstransfer_receiver_fk_1 foreign key (account_receiver_id) references accounts (id) on delete cascade;

insert into customers(ref_id, name, is_active) values ( '1022', 'Ashley Shookhye', 1 );
insert into customers(ref_id, name, is_active) values ( '1024', 'Don Jones', 1 );
insert into customers(ref_id, name, is_active) values ( '1025', 'Tom Hanks', 0);
insert into accounts(customer_id, account_number, current_balance) values ( 1, '00123456789', 100.00 );
insert into accounts(customer_id, account_number, current_balance) values ( 1, '00777456789', 100.00 );
insert into accounts(customer_id, account_number, current_balance) values ( 2, '00456789425', 200.00 );
insert into transactions(accounts_id, source_account_id, target_account_id, amount, reference, completion_date)
values (1, 1, 2, 100.00, 'TX123','2020-01-01');