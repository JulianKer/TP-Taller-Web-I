INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);
INSERT INTO Usuario(email, password, rol, activo, nombre, apellido, telefono, fechaNacimiento, saldo) VALUES( 'german@gmail.com', 'german', 'CLIENTE', false, 'german', 'schmuker', 12345678, '2004-10-13', 1500.0);
INSERT INTO Usuario(email, password, rol, activo, nombre, apellido, telefono, fechaNacimiento, saldo) VALUES( 'germanschmuker@gmail.com', 'pruebaGerman', 'CLIENTE', false, 'german', 'schmuker', 12345675, '2004-10-13', 300000.0);
INSERT INTO Criptomoneda(nombre, precioActual, imagen, habilitada) VALUES
                                                   ('bitcoin',100.0, 'Bitcoin.webp', true), --sacar este punto y coma
                                                   ('ethereum', 100.0, 'Ethereum.webp', true),
                                                   ('binance-coin', 100.0, 'BNB.svg', true),
                                                   ('dogecoin', 100.0, 'Dogecoin.webp', true),
                                                   ('steth', 100.0, 'Steth.webp', true),
                                                   ('solana', 100.0, 'Solana.svg', true),
                                                   ('tether', 100.0, 'Tether.svg', true);






