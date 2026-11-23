use asset_management;

insert into employee (name, email, department) values
('Zhang Wei', 'zhang.wei@example.com', 'IT'),
('Li Na', 'li.na@example.com', 'IT'),
('Wang Fang', 'wang.fang@example.com', 'Finance'),
('Liu Yang', 'liu.yang@example.com', 'HR');

insert into equipment (asset_tag, type, brand, purchase_date, status) values
('LAP-0001', 'LAPTOP', 'Lenovo', '2023-01-15', 'AVAILABLE'),
('LAO-0002', 'LAPTOP', 'Apple', '2023-03-20','AVAILABLE'),
('MON-0001', 'MONITOR', 'Dell', '2022-11-05', 'AVAILABLE'),
('PHO-0001', 'PHONE', 'Huawei', '2023-06-10', 'AVAILABLE');

insert into assignment (employee_id, equipment_id, assigned_date, returned_date) values
(1, 1, '2024-01-10', null);

update equipment set status = 'ASSIGNED' where id = 1;


insert into app_user (username, password_hash, role ) values
('admin', '$2a$10$wQlO7B3A5DrENs/oqkPoruTzUWpZrSMx5m9jRrldIi.eAJLv/Be5i', 'ADMIN');