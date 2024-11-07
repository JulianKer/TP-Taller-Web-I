
INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);
INSERT INTO Usuario(email, password, rol, activo, nombre, apellido, telefono, fechaNacimiento, saldo, estaBloqueado) VALUES( 'tomi@gmail.com', 'tomi', 'CLIENTE', false, 'tomi', 'nania', 12345678, '2004-10-13', 100000.0, false);
INSERT INTO Usuario(email, password, rol, activo, nombre, apellido, telefono, fechaNacimiento, saldo, estaBloqueado) VALUES( 'julianschker@gmail.com', 'julian', 'CLIENTE', false, 'julian', 'schmuker', 12345678, '2004-10-13', 240000.0, false);
INSERT INTO Usuario(email, password, rol, activo, nombre, apellido, telefono, fechaNacimiento, saldo, estaBloqueado) VALUES( 'german@gmail.com', 'german', 'CLIENTE', true, 'german', 'schmuker', 12345678, '2004-10-13', 350000.0, false);
INSERT INTO Usuario(email, password, rol, activo, nombre, apellido, telefono, fechaNacimiento, saldo, estaBloqueado) VALUES( 'agustincardelli@gmail.com', 'prubaagus', 'CLIENTE', false, 'agustin', 'cardelli', 12345679, '2003-10-4', 300000.0, false);
INSERT INTO Usuario(email, password, rol, activo, nombre, apellido, telefono, fechaNacimiento, saldo, estaBloqueado) VALUES( 'germanschmuker@gmail.com', 'pruebaGerman', 'CLIENTE', false, 'german', 'schmuker', 12345675, '2004-10-13', 300000.0, false);
INSERT INTO Usuario(email, password, rol, activo, nombre, apellido, telefono, fechaNacimiento, saldo, estaBloqueado) VALUES( 'estevezgaston01@gmail.com', '12345', 'CLIENTE', false, 'gaston', 'estevez', 12345675, '2001-16-4', 100.0, false);

INSERT INTO Criptomoneda(nombre, precioActual, imagen, habilitada) VALUES
                                                   ('bitcoin',100.0, 'Bitcoin.webp', true), --sacar este punto y coma
                                                   ('ethereum', 100.0, 'Ethereum.webp', true),
                                                   ('binance-coin', 100.0, 'BNB.svg', true),
                                                   ('dogecoin', 100.0, 'Dogecoin.webp', true),
                                                   ('usd-coin', 100.0, 'USDC.png', true),
                                                   --('steth', 100.0, 'Steth.webp', true),
                                                   ('solana', 100.0, 'Solana.svg', true),
                                                   ('tether', 100.0, 'Tether.svg', true);

insert into Suscripcion (titulo, descripcion, precio, imagenUrl) values
                                                ('diamante', 'Acceso a todas las funciones', 20, 'diamante.png' ),
                                                ('esmeralda', 'Acceso a los aldeanos', 30, 'esmeralda.png');
