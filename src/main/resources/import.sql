INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Miguel', 'Chinchay', 'miguel@tuna.pe', '2019-10-21', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES ('Jhon', 'Doe', 'jhon@tuna.pe', '2019-10-20', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Andres', 'Guzman', 'profesor@bolsadeideas.com', '2017-08-01', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('John', 'Doe', 'john.doe@gmail.com', '2017-08-02', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Linus', 'Torvalds', 'linus.torvalds@gmail.com', '2017-08-03', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Jane', 'Doe', 'jane.doe@gmail.com', '2017-08-04', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Rasmus', 'Lerdorf', 'rasmus.lerdorf@gmail.com', '2017-08-05', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Erich', 'Gamma', 'erich.gamma@gmail.com', '2017-08-06', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Richard', 'Helm', 'richard.helm@gmail.com', '2017-08-07', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Ralph', 'Johnson', 'ralph.johnson@gmail.com', '2017-08-08', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('John', 'Vlissides', 'john.vlissides@gmail.com', '2017-08-09', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('James', 'Gosling', 'james.gosling@gmail.com', '2017-08-010', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Bruce', 'Lee', 'bruce.lee@gmail.com', '2017-08-11', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Johnny', 'Doe', 'johnny.doe@gmail.com', '2017-08-12', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('John', 'Roe', 'john.roe@gmail.com', '2017-08-13', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Jane', 'Roe', 'jane.roe@gmail.com', '2017-08-14', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Richard', 'Doe', 'richard.doe@gmail.com', '2017-08-15', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Janie', 'Doe', 'janie.doe@gmail.com', '2017-08-16', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Phillip', 'Webb', 'phillip.webb@gmail.com', '2017-08-17', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Stephane', 'Nicoll', 'stephane.nicoll@gmail.com', '2017-08-18', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Sam', 'Brannen', 'sam.brannen@gmail.com', '2017-08-19', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Juergen', 'Hoeller', 'juergen.Hoeller@gmail.com', '2017-08-20', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Janie', 'Roe', 'janie.roe@gmail.com', '2017-08-21', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('John', 'Smith', 'john.smith@gmail.com', '2017-08-22', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Joe', 'Bloggs', 'joe.bloggs@gmail.com', '2017-08-23', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('John', 'Stiles', 'john.stiles@gmail.com', '2017-08-24', '');
INSERT INTO clientes(nombre, apellido, email, create_at, foto) VALUES('Richard', 'Roe', 'stiles.roe@gmail.com', '2017-08-25', '');

INSERT INTO productos(nombre, precio, create_at) VALUES ('Panasonic Pantalla LED', 2156.23, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Sony camara digital', 156.23, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Apple Ipod Shuffle', 56.23, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Sony Notebook Z110', 223.23, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Hewlett Packard multifuncional', 876.23, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Bianchi bicicleta Aro 26', 517.23, NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES ('Mica comoda 5 cajones', 983.23, NOW());

INSERT INTO facturas(descripcion, observacion, cliente_id, create_at) VALUES ('Factura equipos de oficina', null, 1, NOW());
INSERT INTO facturas_items(cantidad, factura_id, producto_id) VALUES (1, 1, 1);
INSERT INTO facturas_items(cantidad, factura_id, producto_id) VALUES (2, 1, 4);
INSERT INTO facturas_items(cantidad, factura_id, producto_id) VALUES (1, 1, 5);
INSERT INTO facturas_items(cantidad, factura_id, producto_id) VALUES (1, 1, 7);

INSERT INTO facturas(descripcion, observacion, cliente_id, create_at) VALUES ('Factura bicicleta', 'Alguna nota importante', 1, NOW());
INSERT INTO facturas_items(cantidad, factura_id, producto_id) VALUES (3, 2, 6);

INSERT INTO users(username, password, enable) VALUES('miguel', '$2a$10$99g4rbmGa8YCoYq0KB1jMuce1LZlL9VBr9UtaWIVPnybKgHBSpX8G', 1);
INSERT INTO users(username, password, enable) VALUES('admin', '$2a$10$aamdj1e2duhcsIHUDpRJCOImVVzU31sHR/AaGGAQlhmgX1YjWU7Bm', 1);

INSERT INTO authorities(user_id, authority) VALUES(1, 'ROLE_USER'),(2, 'ROLE_USER'), (2, 'ROLE_ADMIN');