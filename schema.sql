-- asset-management database schema
-- Run against MySQL 8.x: mysql -u root -p < schema.sql

create database if not exists asset_management
    character set utf8mb4 collate utf8mb4_0900_ai_ci;

use asset_management;

create table employee
(
    id         bigint auto_increment primary key,
    name       varchar(100) not null,
    email      varchar(150) not null unique,
    department varchar(50),
    created_at datetime     not null default current_timestamp
);

create table equipment
(
    id            bigint auto_increment primary key,
    asset_tag     varchar(50) not null unique,
    type          varchar(50) not null,                     -- laptop, monitor, phone
    brand         varchar(100),
    purchase_date date,
    status        varchar(20) not null default 'available', -- available | assigned | retired
    created_at    datetime    not null default current_timestamp
);

create table assignment
(
    id            bigint auto_increment primary key,
    employee_id   bigint not null,
    equipment_id  bigint not null,
    assigned_date date   not null,
    returned_date date,
    active_flag   tinyint generated always as (if(returned_date is null, 1, null)) stored,
    unique key uk_equipmenta_active (equipment_id, active_flag),
    constraint fk_assignment_employee
        foreign key (employee_id) references employee (id),
    constraint fk_assignment_equipment
        foreign key (equipment_id) references equipment (id)
);

-- indexes that real queries will use
create index idx_equipment_status on equipment (status);
create index idx_assignment_employee on assignment (employee_id);
create index idx_assignment_equipment on assignment (equipment_id);
create index idx_assignment_active on assignment (equipment_id, returned_date);

create table app_user (
    id bigint auto_increment primary key ,
    username varchar(100) not null unique ,
    password_hash varchar(100) not null,
    role varchar(20) not null, -- ADMIN or OPERATOR
    enabled boolean not null default true
)

