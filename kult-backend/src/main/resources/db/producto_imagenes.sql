-- =============================================================================
-- KULT STUDIO · Imágenes de productos almacenadas como BLOB
-- Aplicar con: mysql -u root -p kult < src/main/resources/db/producto_imagenes.sql
-- =============================================================================

CREATE TABLE IF NOT EXISTS producto_imagenes (
    id_imagen INT NOT NULL AUTO_INCREMENT,
    id_producto INT NOT NULL,
    imagen LONGBLOB NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    principal BOOLEAN NOT NULL DEFAULT FALSE,
    orden INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id_imagen),
    CONSTRAINT fk_producto_imagenes_producto
        FOREIGN KEY (id_producto) REFERENCES productos (id_producto)
        ON DELETE CASCADE
);
