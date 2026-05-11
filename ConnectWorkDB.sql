-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS ConnectWorkDB;
USE ConnectWorkDB;

-- 1. Tabla Base de Usuarios
CREATE TABLE Usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    tipo_usuario ENUM('CLIENTE', 'FREELANCER', 'ADMIN') NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- Encriptada
    nombre_completo VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    cui VARCHAR(13) UNIQUE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    estado BOOLEAN DEFAULT TRUE
);

-- 2. Tablas Específicas por Rol
CREATE TABLE Cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    saldo DECIMAL(10,2) DEFAULT 0.00,
    descripcion_empresa TEXT,
    sector VARCHAR(100),
    sitio_web VARCHAR(150),
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE Freelancer (
    id_freelancer INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    saldo DECIMAL(10,2) DEFAULT 0.00,
    biografia TEXT,
    nivel_experiencia ENUM('JUNIOR', 'SEMI-SENIOR', 'SENIOR'),
    tarifa_hora DECIMAL(10,2),
    calificacion_promedio DECIMAL(3,2) DEFAULT 0.00,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE Administrador (
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE
);

-- 3. Catálogos
CREATE TABLE Categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    estado BOOLEAN DEFAULT TRUE
);

CREATE TABLE Habilidad (
    id_habilidad INT AUTO_INCREMENT PRIMARY KEY,
    id_categoria INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    estado BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria) ON DELETE CASCADE
);

-- 4. Tablas Intermedias (Catálogos y Usuarios)
CREATE TABLE Freelancer_Habilidad (
    id_freelancer INT NOT NULL,
    id_habilidad INT NOT NULL,
    PRIMARY KEY (id_freelancer, id_habilidad),
    FOREIGN KEY (id_freelancer) REFERENCES Freelancer(id_freelancer) ON DELETE CASCADE,
    FOREIGN KEY (id_habilidad) REFERENCES Habilidad(id_habilidad) ON DELETE CASCADE
);

-- 5. Lógica de Negocio (Proyectos y Contratos)
CREATE TABLE Proyecto (
    id_proyecto INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_categoria INT NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    descripcion TEXT NOT NULL,
    presupuesto_maximo DECIMAL(10,2) NOT NULL,
    fecha_limite DATE NOT NULL,
    estado ENUM('ABIERTO', 'EN_REVISION', 'EN_PROGRESO', 'ENTREGA_PENDIENTE', 'COMPLETADO', 'CANCELADO') DEFAULT 'ABIERTO',
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
    FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria)
);

CREATE TABLE Proyecto_Habilidad (
    id_proyecto INT NOT NULL,
    id_habilidad INT NOT NULL,
    PRIMARY KEY (id_proyecto, id_habilidad),
    FOREIGN KEY (id_proyecto) REFERENCES Proyecto(id_proyecto) ON DELETE CASCADE,
    FOREIGN KEY (id_habilidad) REFERENCES Habilidad(id_habilidad) ON DELETE CASCADE
);

CREATE TABLE Propuesta (
    id_propuesta INT AUTO_INCREMENT PRIMARY KEY,
    id_proyecto INT NOT NULL,
    id_freelancer INT NOT NULL,
    monto_ofertado DECIMAL(10,2) NOT NULL,
    plazo_dias INT NOT NULL,
    carta_presentacion TEXT,
    estado ENUM('PENDIENTE', 'ACEPTADA', 'RECHAZADA') DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_proyecto) REFERENCES Proyecto(id_proyecto) ON DELETE CASCADE,
    FOREIGN KEY (id_freelancer) REFERENCES Freelancer(id_freelancer) ON DELETE CASCADE
);

CREATE TABLE Contrato (
    id_contrato INT AUTO_INCREMENT PRIMARY KEY,
    id_propuesta INT NOT NULL,
    monto_bloqueado DECIMAL(10,2) NOT NULL,
    estado ENUM('ACTIVO', 'FINALIZADO', 'CANCELADO') DEFAULT 'ACTIVO',
    FOREIGN KEY (id_propuesta) REFERENCES Propuesta(id_propuesta) ON DELETE CASCADE
);

CREATE TABLE Entrega (
    id_entrega INT AUTO_INCREMENT PRIMARY KEY,
    id_contrato INT NOT NULL,
    descripcion TEXT NOT NULL,
    archivos_url TEXT NOT NULL,
    fecha_subida DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('PENDIENTE', 'APROBADA', 'RECHAZADA') DEFAULT 'PENDIENTE',
    motivo_rechazo TEXT,
    FOREIGN KEY (id_contrato) REFERENCES Contrato(id_contrato) ON DELETE CASCADE
);

CREATE TABLE Calificacion (
    id_calificacion INT AUTO_INCREMENT PRIMARY KEY,
    id_contrato INT NOT NULL,
    estrellas INT CHECK (estrellas >= 1 AND estrellas <= 5),
    comentario TEXT,
    FOREIGN KEY (id_contrato) REFERENCES Contrato(id_contrato) ON DELETE CASCADE
);

-- 6. Tablas Financieras y Reportes
CREATE TABLE Recarga (
    id_recarga INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);

CREATE TABLE Historial_Comision (
    id_comision INT AUTO_INCREMENT PRIMARY KEY,
    porcentaje DECIMAL(5,2) NOT NULL,
    fecha_inicio DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_fin DATETIME NULL
);

CREATE TABLE Plataforma (
    id_plataforma INT PRIMARY KEY DEFAULT 1,
    saldo_global DECIMAL(12,2) DEFAULT 0.00
);