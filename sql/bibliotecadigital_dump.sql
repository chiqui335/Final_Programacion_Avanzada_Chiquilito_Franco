-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 03, 2026 at 12:00 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bibliotecadigital`
--

-- --------------------------------------------------------

--
-- Table structure for table `libro`
--

CREATE TABLE `libro` (
  `id` int(11) NOT NULL,
  `titulo` varchar(50) NOT NULL,
  `autor` varchar(50) NOT NULL,
  `tipo` varchar(50) NOT NULL,
  `tapaDura` tinyint(1) DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1,
  `cantidadDisponible` int(11) DEFAULT 1,
  `cantidadTotal` int(11) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `libro`
--

INSERT INTO `libro` (`id`, `titulo`, `autor`, `tipo`, `tapaDura`, `activo`, `cantidadDisponible`, `cantidadTotal`) VALUES
(1, 'Cien años de soledad', 'Gabriel García Márquez', 'Novela', 1, 1, 3, 3),
(2, '1984', 'George Orwell', 'Ciencia Ficción', 0, 1, 0, 2),
(3, 'El nombre del viento', 'Patrick Rothfuss', 'Fantasía', 1, 1, 1, 1),
(4, 'Sapiens', 'Yuval Noah Harari', 'Ensayo', 1, 1, 2, 3),
(5, 'Fahrenheit 451', 'Ray Bradbury', 'Ciencia Ficción', 0, 1, 1, 1),
(6, 'Rayuela', 'Julio Cortázar', 'Novela', 0, 1, 0, 1),
(7, 'El principito', 'Antoine de Saint-Exupéry', 'Infantil', 1, 1, 3, 4),
(8, 'Crónica de una muerte anunciada', 'Gabriel García Márquez', 'Novela', 0, 1, 1, 2),
(9, 'Dune', 'Frank Herbert', 'Ciencia Ficción', 1, 1, 2, 2),
(10, 'El día que se perdió la cordura', 'Javier Castillo', 'Thriller', 0, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `prestamo`
--

CREATE TABLE `prestamo` (
  `id` int(11) NOT NULL,
  `fechaInicio` date NOT NULL,
  `fechaDevolucion` date DEFAULT NULL,
  `retrasado` tinyint(1) DEFAULT 0,
  `finalizado` tinyint(1) DEFAULT 0,
  `usuario_id` int(11) NOT NULL,
  `libro_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prestamo`
--

INSERT INTO `prestamo` (`id`, `fechaInicio`, `fechaDevolucion`, `retrasado`, `finalizado`, `usuario_id`, `libro_id`) VALUES
(1, '2026-07-30', '2026-08-29', 0, 1, 4, 5),
(2, '2026-06-01', '2026-06-30', 1, 0, 5, 2),
(3, '2026-07-20', '2026-08-19', 0, 0, 6, 4),
(4, '2026-05-01', '2026-05-31', 0, 1, 7, 9),
(5, '2026-07-15', '2026-08-14', 0, 0, 5, 7),
(6, '2026-04-01', '2026-04-30', 1, 1, 6, 8),
(7, '2026-07-22', '2026-08-21', 0, 0, 7, 2);

-- --------------------------------------------------------

--
-- Table structure for table `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `dni` varchar(50) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `prestamosActuales` int(11) DEFAULT 0,
  `totalPrestamos` int(11) DEFAULT 0,
  `estaRetrasado` tinyint(1) DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1,
  `rol` enum('ADMIN','REGULAR') DEFAULT 'REGULAR'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `usuario`
--

INSERT INTO `usuario` (`id`, `nombre`, `username`, `password`, `dni`, `email`, `prestamosActuales`, `totalPrestamos`, `estaRetrasado`, `activo`, `rol`) VALUES
(1, 'Admin', 'admin', '65536:kmq0PpypBfYuqWciR1S0oA==:dA1+3veLlEXvoSOfYJ0Glb47QeX4niZrNy67WjG7g38=', '00000000', 'admin@test.com', 0, 0, 0, 1, 'ADMIN'),
(3, '', '', '', '', '', 0, 0, 0, 0, 'REGULAR'),
(4, 'pepe', 'pepe1', '65536:oLF+CiYaafKOIiQevhYL5g==:Ac0+qqHFcSpuzbkJgcDA3yzjCHLA0ne+lWmWqhhmvm4=', '99999999', 'pepe1@gmail.com.ar', 0, 1, 0, 1, 'REGULAR'),
(5, 'Juan Pérez', 'juanp', '65536:6erGV5pmg29QUGfIHoBwWg==:+SEMzn6+9OZ8oL/TzZ1mgGfKfq0STIXlwcmmnwCyNXk=', '11111111', 'juanp@test.com', 2, 2, 1, 1, 'REGULAR'),
(6, 'Maria Gomez', 'mariag', '65536:3OHqZI/mWsC1BQ/z2eGLfw==:CPmw/IIuUzZa24A06TMvnTv+0KGIL3hz41x7VvlIY9E=', '22222222', 'mariag@test.com', 1, 2, 0, 1, 'REGULAR'),
(7, 'Carlos Ruiz', 'carlosr', '65536:jOr0N65YVsNCgQy1fX796A==:Ycu5tj+IgvxvIQwC5XM2ZrGONjmQ7jMP6FS/xdvbDEc=', '33333333', 'carlosr@test.com', 1, 2, 0, 1, 'REGULAR');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `libro`
--
ALTER TABLE `libro`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `prestamo`
--
ALTER TABLE `prestamo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `usuario_id` (`usuario_id`),
  ADD KEY `libro_id` (`libro_id`);

--
-- Indexes for table `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `libro`
--
ALTER TABLE `libro`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `prestamo`
--
ALTER TABLE `prestamo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `prestamo`
--
ALTER TABLE `prestamo`
  ADD CONSTRAINT `prestamo_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `prestamo_ibfk_2` FOREIGN KEY (`libro_id`) REFERENCES `libro` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
