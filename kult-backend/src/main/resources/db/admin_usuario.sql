-- KULT STUDIO · Usuario administrador inicial
-- Ejecutar en la base de datos `kult` DESPUÉS de registro_usuario.sql y admin_rol_sedes.sql
-- (dichos scripts crean password_hash, ciudad, talla, acepta_sms, acepta_terminos y rol).
--
-- Credenciales de acceso:
--   Correo:      admin@mail.com
--   Contraseña:  admin12345
--   Rol:         ADMIN
--
-- La contraseña se guarda como hash BCrypt (coste 10), compatible con
-- BCryptPasswordEncoder configurado en PasswordConfig.java. Nunca guardar la contraseña en texto plano.

SET @admin_password_hash := '$2a$10$.Q/ufMrGpnbYQTTwjvSYquRxREqgO7hjsIn1asfnOU3vUzFThwPZy';

-- Alta del administrador sólo si no existe ese correo ni esa identificación.
-- "Bogotá" (con tilde) es el valor que espera AuthService para la ciudad de despacho.
INSERT INTO usuarios (
    nombre, apellido, correo, celular, documento,
    password_hash, ciudad, talla, acepta_sms, acepta_terminos, rol
)
SELECT
    'Fabian', 'Garcia', 'admin@mail.com', '3226222222', '1234567',
    @admin_password_hash, 'Bogotá', 'XL', 0, 1, 'ADMIN'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1
    FROM usuarios
    WHERE correo = 'admin@mail.com'
       OR documento = '1234567'
);

-- Si el correo ya estaba registrado, se garantiza que tenga rol ADMIN y la contraseña indicada.
UPDATE usuarios
SET rol = 'ADMIN',
    password_hash = @admin_password_hash
WHERE correo = 'admin@mail.com';

-- Verificación del resultado.
SELECT id_usuario, nombre, apellido, correo, celular, documento, ciudad, talla, rol
FROM usuarios
WHERE correo = 'admin@mail.com';
