INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);
INSERT INTO Usuario(email, password, rol, activo, nombre, apellido, telefono, fechaNacimiento, saldo) VALUES( 'german@gmail.com', 'german', 'ADMIN', true, 'german', 'schmuker', 12345678, '2004-10-13', 1500.0);
INSERT INTO Criptomoneda(nombre, precioActual, imagen) VALUES
                                                   ('bitcoin',100.0, 'Bitcoin.webp'), --sacar este punto y coma
                                                   ('ethereum', 100.0, 'Ethereum.webp'),
                                                   ('binance-coin', 100.0, 'BNB.svg'),
                                                   ('dogecoin', 100.0, 'Dogecoin.webp'),
                                                   ('steth', 100.0, 'Steth.webp'),
                                                   ('solana', 100.0, 'Solana.svg'),
                                                   ('tether', 100.0, 'Tether.svg');






