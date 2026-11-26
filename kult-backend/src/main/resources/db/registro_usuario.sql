-- KULT STUDIO · Migración manual para el módulo de registro
-- Aplicar en la base de datos `kult` antes de arrancar el backend.
-- spring.jpa.hibernate.ddl-auto=validate no crea columnas automáticamente.

ALTER TABLE usuarios
    ADD COLUMN password_hash VARCHAR(100) NULL,
    ADD COLUMN ciudad VARCHAR(60) NULL,
    ADD COLUMN talla VARCHAR(5) NULL,
    ADD COLUMN acepta_sms TINYINT(1) NOT NULL DEFAULT 0,
    ADD COLUMN acepta_terminos TINYINT(1) NOT NULL DEFAULT 0;

-- Los usuarios anteriores no tenían contraseña: no podrán iniciar sesión
-- hasta que se les asigne un hash válido.
UPDATE usuarios SET password_hash = '' WHERE password_hash IS NULL;

ALTER TABLE usuarios MODIFY password_hash VARCHAR(100) NOT NULL;
