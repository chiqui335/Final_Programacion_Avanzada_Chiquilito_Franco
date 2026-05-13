CREATE DATABASE IF NOT EXISTS bibliotecadigital;
USE bibliotecadigital;

CREATE TABLE libro (
    id int primary key AUTO_INCREMENT,
    titulo varchar(50) not null,
    autor varchar(50) not null, 
    tipo varchar(50) not null,
    tapaDura boolean DEFAULT false, 
    activo boolean DEFAULT true,
    cantidadDisponible int DEFAULT 1,
    cantidadTotal int DEFAULT 1
);

CREATE TABLE usuario (
    id int primary key AUTO_INCREMENT,
    nombre varchar(50) not null,
    username varchar(50) not null,
    password varchar(50) not null,
    dni varchar(50) not null,
    email varchar(50),
    prestamosActuales int DEFAULT 0,
    totalPrestamos int DEFAULT 0,
    estaRetrasado boolean DEFAULT false,
    activo boolean DEFAULT true,
    rol ENUM('ADMIN', 'REGULAR') DEFAULT 'REGULAR'
);

CREATE TABLE prestamo (
    id int primary key AUTO_INCREMENT,
    fechaInicio Date NOT NULL,
    fechaDevolucion Date,
    retrasado boolean DEFAULT false,
    finalizado boolean DEFAULT false,
    usuario_id int NOT NULL,
    libro_id int NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (libro_id) REFERENCES libro(id)
);