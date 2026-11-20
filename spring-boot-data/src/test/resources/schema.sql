drop table if exists tb_account;
create table tb_account (
    id bigint primary key,
    account_name varchar(64) not null,
    balance decimal(12, 2) not null,
    updated_at timestamp not null default current_timestamp on update current_timestamp
) engine = InnoDB;
