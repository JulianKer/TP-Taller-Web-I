
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

INSERT INTO PrecioCripto (criptomoneda_id, precioActual, fechaDelPrecio) VALUES
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'bitcoin'), 68591.0, '2024-11-01 08:15:42'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'bitcoin'), 69951.451, '2024-11-02 14:30:55'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'bitcoin'), 71564.0, '2024-11-05 05:45:33'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'bitcoin'), 65587.5620, '2024-11-08 20:50:18'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'bitcoin'), 83862.0, '2024-11-10 12:10:44'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'bitcoin'), 92700.0, '2024-11-12 16:00:09'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'bitcoin'), 90400.0, '2024-11-14 23:11:39'),

                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'ethereum'), 3000.5, '2024-11-01 05:20:11'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'ethereum'), 2950.5, '2024-11-02 18:25:00'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'ethereum'), 2900.588, '2024-11-05 22:45:15'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'ethereum'), 3020.001, '2024-11-07 07:12:33'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'ethereum'), 3100.0, '2024-11-10 11:33:27'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'ethereum'), 3500.0, '2024-11-14 01:05:55'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'ethereum'), 3654.0, '2024-11-16 15:47:48'),

                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'tether'), 0.31, '2024-11-01 10:30:25'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'tether'), 0.25, '2024-11-02 14:05:37'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'tether'), 0.28, '2024-11-05 17:45:50'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'tether'), 0.30, '2024-11-10 09:11:05'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'tether'), 0.50, '2024-11-11 22:50:30'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'tether'), 0.68, '2024-11-14 13:45:22'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'tether'), 0.42, '2024-11-16 20:31:18'),

                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'binance-coin'), 0.31, '2024-11-01 02:15:47'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'binance-coin'), 0.25, '2024-11-02 18:33:16'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'binance-coin'), 0.28, '2024-11-05 23:59:59'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'binance-coin'), 0.30, '2024-11-10 05:05:45'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'binance-coin'), 0.50, '2024-11-11 14:20:33'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'binance-coin'), 0.68, '2024-11-14 12:44:11'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'binance-coin'), 0.42, '2024-11-16 22:10:02'),

                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'solana'), 215.3, '2024-11-01 06:40:25'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'solana'), 215.63, '2024-11-02 19:50:40'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'solana'), 201.33253, '2024-11-05 14:10:05'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'solana'), 200.63, '2024-11-10 03:33:33'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'solana'), 218.0, '2024-11-11 16:45:58'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'solana'), 220.0, '2024-11-14 11:50:45'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'solana'), 235.33257284892363, '2024-11-16 23:05:55'),

                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'dogecoin'), 0.4, '2024-11-01 07:15:55'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'dogecoin'), 0.3555, '2024-11-02 20:20:45'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'dogecoin'), 0.3856, '2024-11-05 12:50:35'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'dogecoin'), 0.401, '2024-11-10 00:11:11'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'dogecoin'), 0.3151904505072, '2024-11-11 21:50:59'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'dogecoin'), 0.72, '2024-11-14 09:15:30'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'dogecoin'), 0.7288, '2024-11-16 18:44:20'),

                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'usd-coin'), 0.9994048802905455, '2024-11-01 13:10:59'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'usd-coin'), 0.9905455, '2024-11-02 16:55:33'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'usd-coin'), 0.902905455, '2024-11-05 10:22:11'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'usd-coin'), 0.95455, '2024-11-10 08:33:45'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'usd-coin'), 1.0, '2024-11-14 22:10:10'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'usd-coin'), 1.5488, '2024-11-14 19:20:20'),
                                                                             ((SELECT id FROM criptomoneda WHERE nombre = 'usd-coin'), 1.3356495, '2024-11-16 23:50:55');