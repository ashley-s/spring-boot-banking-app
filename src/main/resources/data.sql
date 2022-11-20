insert into customers(ref_id, name, is_active) values ( '1022', 'Ashley Shookhye', 1 );
insert into customers(ref_id, name, is_active) values ( '1024', 'Don Jones', 1 );
insert into accounts(customer_id, account_number, current_balance) values ( 1, '00123456789', 100.00 );
insert into accounts(customer_id, account_number, current_balance) values ( 1, '00777456789', 100.00 );
insert into accounts(customer_id, account_number, current_balance) values ( 2, '00456789425', 200.00 );
insert into transactions(accounts_id, source_account_id, target_account_id, amount, reference, completion_date)
    values (1, 1, 2, 100.00, 'TX123','2020-01-01');
