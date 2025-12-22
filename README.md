Hola, este es el repositorio con el desarrollo del backend para la tarea de construcción de software. Básicamente, implementé el módulo de facturación siguiendo la arquitectura de 4 capas que se pedía en las instrucciones

Así organicé el código para que sea fácil de revisar:

modelos : 
 Aquí puse las entidades (las tablas de la base de datos). Son: `Factura`, `DetalleFactura`, `Cliente`, `Producto` y `Pago`. Como pidió, mantuve los nombres de las clases en el estándar pero las variables y métodos internos los dejé en español para no confundirme.

Repositories: Son las interfaces para conectar con la base de datos (JPA), si acaso la necesitara, pero podria cambiarla por otra bdd,y bueno ahi agrego las entidades cuando se necesite

Servicios 

Aquí está toda la lógica. Lo más importante es que aquí valido si hay stock antes de guardar una factura; si no hay productos, el sistema no te deja vender.


Controllers

 Aquí están los endpoints para consumir la API. Ojo: Todos devuelven el objeto `ResponseRest` (con `data`, `mensaje` y `errors`) tal cual como estaba en el diagrama de la tarea, para que las respuestas sean siempre iguales.


Control de stock 
Resta la cantidad de productos automáticamente al crear la factura.

Manejo de erroes
 Traté de que si algo falla, el mensaje sea claro (ej: "Stock insuficiente").

Juan Baez Grupo 10


