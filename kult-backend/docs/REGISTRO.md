# Módulo de registro y acceso (kult-backend)

## 1. Base de datos

El proyecto usa `spring.jpa.hibernate.ddl-auto=validate`, por lo que las columnas nuevas deben crearse a mano:

```bash
mysql -u root -p kult < src/main/resources/db/registro_usuario.sql
```

El script agrega `password_hash`, `ciudad`, `talla`, `acepta_sms` y `acepta_terminos` a la tabla `usuarios`.

## 2. Arranque

```bash
./mvnw spring-boot:run
```

El servicio escucha en **http://localhost:8080** (igual que el frontend). CORS permite `http://localhost:5173`.

## 3. Endpoints

### `POST /api/auth/registro` → 201

```json
{
  "nombre": "Mateo",
  "apellido": "Restrepo Cruz",
  "identificacion": "1018459201",
  "correo": "mateo@correo.co",
  "celular": "+57 300 000 0000",
  "ciudad": "Bogotá",
  "password": "Kult2026!",
  "confirmacionPassword": "Kult2026!",
  "talla": "M",
  "aceptaSms": true,
  "aceptaTerminos": true
}
```

Respuesta:

```json
{
  "idUsuario": 1,
  "nombre": "Mateo",
  "identificacion": "1018459201",
  "correo": "mateo@correo.co",
  "ciudad": "Bogotá",
  "talla": "M",
  "mensaje": "Identificador de miembro creado correctamente"
}
```

Errores: `400` datos inválidos, `409` correo o identificación duplicados (el `detail` indica cuál).

### `POST /api/auth/login` → 200

```json
{ "email": "mateo@correo.co", "password": "Kult2026!", "rememberSession": false }
```

Errores: `400` payload inválido, `401` credenciales incorrectas.

## 4. Notas

- La contraseña se almacena con BCrypt (`BCryptPasswordEncoder`) y nunca se devuelve en las respuestas.
- El login devuelve la identidad del miembro. **Aún no se emite token ni sesión persistente**; el frontend tampoco la almacena.
- El indicativo `+57` se elimina antes de guardar el móvil: se conservan 10 dígitos.
- La identificación se normaliza (sin espacios, puntos ni guiones; en mayúsculas) y se guarda en la columna existente `usuarios.documento`, que es única. No requiere migración de BD.
