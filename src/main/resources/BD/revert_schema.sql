-- Eliminar columnas agregadas recientemente
ALTER TABLE productos DROP COLUMN IF EXISTS descuento;
ALTER TABLE ventas DROP COLUMN IF EXISTS subtotal;
ALTER TABLE ventas DROP COLUMN IF EXISTS iva;
ALTER TABLE ventas DROP COLUMN IF EXISTS estado;
ALTER TABLE ventas DROP COLUMN IF EXISTS referencia_pago;
ALTER TABLE ventas DROP COLUMN IF EXISTS monto_iva; -- Eliminar si existe (agregado en código pero no solicitado explícitamente, pero es bueno limpiar)
