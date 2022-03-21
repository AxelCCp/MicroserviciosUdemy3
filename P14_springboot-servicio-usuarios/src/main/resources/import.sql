INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) VALUES('RRRR','$2a$10$.gcEefehBYU4MQ06yGTF8OIDaoH9QRj99vLMueNYY9yiJIaeezhBS',true,'RRRR','IIII','RRRR@IIII.COM');
INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) VALUES('ZZZZ','$2a$10$fmJZn.ePjgZ7VjIS9uHh2OYli.9lOsBjpbJl/tZm1glMZEC9aV2.a',true,'ZZZZ','YYYY','ZZZZ@YYYY.COM');

INSERT INTO roles (nombre) VALUES ('ROLE_USER');
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (1,1);
INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (2,2);
INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (2,1);