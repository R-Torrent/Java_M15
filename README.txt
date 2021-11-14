NOTA 1: Sí, ya sé que lleva meses llamándose "módulo 16", pero mi directorio queda muy raro con un hueco en el quince.

NOTA 2: Quizás tenga que editar el contenido del fichero 'application.properties' en el directorio 'src\main\resources', dependiendo de algunos parámetros de su 'host':
  Fase 1 - MySQL: Por defecto está seleccionado el puerto 3306, con usuario 'root' y contraseña "" (vacía).
  Fases 2 y 3 - MongoDB: Por defecto está seleccionado el puerto 27017.
    
NOTA 3: En el directorio 'src\' del proyecto encontrará la colección 'Postman' con todas las invocaciones http. Tras el primer POST de la Fase 3, el 'Bearer Token' queda almacenado en una variable y todas las peticiones de la colección recurren automáticamente a ella.