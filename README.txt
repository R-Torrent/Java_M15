NOTA 1: S�, ya s� que lleva meses llam�ndose "m�dulo 16", pero mi directorio queda muy raro con un hueco en el quince.

NOTA 2: Quiz�s tenga que editar el contenido del fichero 'application.properties' en el directorio 'src\main\resources', dependiendo de algunos par�metros de su 'host':
  Fase 1 - MySQL: Por defecto est� seleccionado el puerto 3306, con usuario 'root' y contrase�a "" (vac�a).
  Fases 2 y 3 - MongoDB: Por defecto est� seleccionado el puerto 27017.
    
NOTA 3: En el directorio 'src\' del proyecto encontrar� la colecci�n 'Postman' con todas las invocaciones http. Tras el primer POST de la Fase 3, el 'Bearer Token' queda almacenado en una variable y todas las peticiones de la colecci�n recurren autom�ticamente a ella.