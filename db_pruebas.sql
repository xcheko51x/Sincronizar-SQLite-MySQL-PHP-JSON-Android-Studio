create database pruebas;

use pruebas;

create table ventas(
	idVenta int primary key auto_increment,
	producto varchar(50) not null,
	fechaVenta varchar(50) not null,
	precio varchar(5) not null,
	sincronizado varchar(5) not null
);
