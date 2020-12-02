# Artículo de investigación

## Autores

Yann Le Lorier  
Marcelo Schonbrunn  
Fernando  
Simón Metta  
Isaac Harari  
Christian Dalma  

## Resumen

Con este proyecto, proponemos un traductor de consultas que permite pasar de GraphQL a SQL, basado en ANTLR y algunas clases diseñadas para procesar distintos tipos de datos como ```FLOAT```, ```STRING```, entre otros, con condiciones y proyecciones.

## Introducción

El uso de bases de datos se ha diversificado a lo largo de los años. Por lo tanto, no es raro ver proyectos que utilizan varias bases de datos. Un caso de uso común sería (simultáneamente):

- Una base de datos para la administración de los usuarios en una página web (como Redis)
- Otra base de datos para el monitoreo de actividad dentro de una aplicación web

Por lo tanto, es importante hablar de GraphQL. Se trata de un DSL que puede ser adaptado a cualquier tipo de base de datos, independientemente de su sintaxis, podemos realizar traducciones de GraphQL a SQL y a otros DSLs. Esto implica que ya no tenemos que estar pensando en administrar diferentes DSLs para las numerosas Bases de datos que estamos manejando en nuestro proyecto.

## Estado del arte

De acuerdo con la página de [GraphQL](https://graphql.org/learn/), Esta herramienta es un lenguaje de consultas, hecho específicamente para las APIs de las aplicaciones. El principal beneficio estando en que no está atado a alguna base de datos específica o algún motor de almacenamiento.

Al momento de escritura, GraphQL ofrece soporte para los siguientes lenguajes:

- JavaScript
- Go
- PHP
- Python
- Java/Kotlin
- C#/.NET
- Ruby
- Elixir
- Rust
- Swift/Objective-C
- Scala
- Flutter
- Clojure
- C/C++
- Elm
- OCaml/Reason
- Haskell
- Erlang
- Groovy
- R
- Julia
- Perl
- D

Se puede obtener más información sobre el soporte de cada lenguaje con esta herramienta en el siguiente enlace: [GraphQL-Languages](https://graphql.org/code/#languages).  
Es posible analizar el estado de GraphQL al analizar la fundación GraphQL, que apoya las contribuciones del aprendizaje de nuevas tecnologías, construcción de nuevas contribuciones tecnológicas, así como para ofrecer soporte para GraphQL, todo con el apoyo de la Linux Foundation.  
El razonamiento detrás de esta tecnología es que estamos moviéndonos hacia una arquitectura orientada a microservicios, y que la diversificación de los servicios en sí son lo que están causando que sea difícil administrar los diferentes productos, frameworks y librerías que usamos para un proyecto. 

Toda la información de @graphql.

## Desarrollo
Para poder realizar un query de forma correcta es muy importante llegar a comprender cómo este programa logra traducir la entrada hecha a base de la gramática de GraphQL y traducirla a un ejecutable que toma la forma de una cadena en lenguaje SQL. Primero vino la creación del archivo g4, es decir la gramática que se obtiene a partir de la entrada dada y la descomposición de esta misma para la generación de micro bloques los cuales afectarán de forma distinta al programa. Posteriormente nos encontramos con los programas realizados en java, principalmente nuestro archivo que funciona como listener del programa, propiamente llamado 'MiListener.java', a diferencia del archivo g4, el cual deconstruye la entrada, este listener utiliza los bloques generados a partir de los diferentes elementos encontrados en el input poco a poco va concatenando un string el cual simula una relación directa entre la gramática propuesta en GraphQL y SQL. En pocas palabras, el g4 deconstruye la entrada y el listener genera un string SQL a partir de esta de construcción.

### g4
Para el g4 principalmente se tiene una expresión *expr* la cual produce un *query* o *fragmentDef*, los cuales explicaremos más tarde. De *expr* se deriva toda la gramática. Por un decir, *expr* es el corazón de la parte léxica.

Se tiene que un *query* comienza con la palabra 'query', la cual le puede seguir un ID que es una palabra. Este ID para efectos de SQL no es útil por lo que realizar consultas con un ID para SQL es simplemente introducir ruido en la consulta de base de datos. Posteriormente, en el query pueden venir condiciones. En caso de que venga una condición antes de un *queryblock*, el cual explicaremos más adelante, es porque se desea realizar un INNER JOIN.
Las condiciones (*condition* en la gramática) son muy importantes a la hora de realizar una consulta en la base de datos. Estas ayudan a filtrar los registros que se desean obtener. Un ejemplo de esto es el siguiente: Se tiene una base de datos con las películas de toda la historia, y queremos solamente las películas en las que Quentin Tarantino fue el director de la película. La gramática maneja la posibilidad de manejar varias condiciones. Agarrándose del caso pasado, queremos obtener las películas las cuales su género es Miedo y el año en que se filmó es mayor a 1998. Asimismo, para las condiciones se puede incluir una variable a la que se le compara con el atributo de la tabla.

Para realizar una consulta se tienen esencialmente dos casos, una consulta sobre una tabla con la opción de *n* cantidad de filtros o la opción de realizar un INNER JOIN entre dos en el cual se entrega el producto cartesiano donde los atributos correspondientes a cada tabla tengan el mismo valor.

- Consulta básica sobre una tabla:
  En este caso, el usuario solamente desea ver registros de una sola tabla con la opción de filtros y proyectando los atributos que desea. Primero se debe de escribir la palabra 'query' seguido de el *queryblock*. Después el nombre de la tabla (con la opción de que tenga un aligas) y con la opción de que agregue filtros dentro de paréntesis. Por último las proyecciones (con la opción de que tengan aliases) encapsuladas en '{}'.

- INNER JOIN entre dos tablas: 
El INNER JOIN, en SQL, sirve para hacer una consulta en donde un atributo de una tabla tenga el mismo valor que el de un atributo de otra tabla. El resultado es el producto cartesiano de las tablas. Para lograr esto, primero se debe de escribir la palabra 'query', seguido de los nombres de los atributos de las tablas los cuales se van a comparar, es decir, donde el valor de ambos atributos de las tablas sea el mismo. Posterior a eso se debe de incluir entre '{}' los *queryblocks* de las dos tablas los cuales pueden llevar condiciones al. La forma de escribir el *queryblock* es el nombre de la tabla (con la opción de que tenga un aligas) y con la opción de que agregue filtros dentro de paréntesis y sus proyecciones dentro de '{}'. Al final se pone la segunda tabla de la misma forma. Es pertinente mencionar que solamente se puede realizar un INNER JOIN por consulta.

Los filtros se pueden ubicar en el g4 como los *logop*. Para los filtros se pueden hacer las siguientes operaciones lógicas. Para los ejemplos de cada operador lógico se tiene una tabla llamada 'movies' que tiene id, nombre, protagonista y anio_filmada

- ':': Si una variable es igual a un valor
  - SELECT FROM movies WHERE nombre='Joker'

- '_eq': Si una variable es igual a un valor
  - SELECT FROM movies WHERE nombre='Joker'
- '_gt': Si una variable es mayor a un valor
  - SELECT FROM movies WHERE anio_filmada>1990
- '_lt': Si una variable es menor a un valor
  - SELECT FROM movies WHERE anio_filmada<1990
- '_gte': Si una variable es mayor o igual a un valor
  - SELECT FROM movies WHERE anio_filmada>=1990
- '_lte': Si una variable es menor o igual a un valor
  - SELECT FROM movies WHERE anio_filmada<=1990

Para realizar las proyecciones, existe un productor importante llamado *params* que produce *param params*. Las proyecciones son las columnas o los nombres de los atributos especificados que el query debe de regresar. Por ejemplo en la siguiente consulta de SQL **SELECT a, b, c FROM foobar WHERE x=3;** la proyección es **SELECT a, b, c FROM foobar**. Solamente se desea recuperar **a**, **b** y **c** de la tabla **foobar**.

Se pueden escribir comentarios cuando se desea realizar la traducción pero simplemente se ignoran, es decir, no se incluyen en el resultado.

### Creación de query
A la hora de intentar comprender como es que el programa logra generar un query adecuado para una consulta realizada en GraphQL es muy importante como funciona el mismo código del listener, y como es que maneja cada tipo de elemento a la hora de ser deconstruido para generar un string adecuado. El principal programa por analizar es el ```MiListener.java```, este en conjunto con una variedad de clases, las cuales expresan los distintos tipos de objetos que pueden ser creados y utilizados a la hora de generar el query se describen de la siguiente forma:

Lo primero es comprender que es lo que puede llegar a recibir el programa y que es lo que debe de mantener como variables globales a compartir entre todos los distintos procesos generados a partir de la entrada y salida de distintas expresiones, este programa genera una variable string el cual almacenará el query final a realizar sobre la base de datos SQL. Además, se cuenta con 3 distintas listas las cuales almacenan distintos elementos como lo podrían ser queries o consultas a realizar, distintas tablas a partir de todo lo que se recibe y finalmente las condiciones que se aplicaran sobre la consulta para brindar un resultado apropiado. También se cuenta con un entero el cual mantiene el índice actual de las tablas que se está revisando.

En la función `exitQuery` la cual se implementa cuando se sale de un bloque de query establecido en el archivo g4, se detecta cuantos queries se desean realizar, por el momento únicamente se han implementado 2 hasta 2 de estos, esto funciona para realizar la operación 'INNER JOIN' entre tablas, si se reciben 2 queries el programa realiza la operación mencionada, en el caso de solo obtener 1 query el programa sabe de forma automática que la consulta será realizada sobre una única tabla. Es importante notar que actualmente el programa solo puede hacer relaciones entre máximo 2 tablas y el aumentar esta cantidad se tiene planeado para el futuro de la aplicación.

La siguiente función por notar seria `enterCondition` ya que esta es la que realiza las distintas condiciones sobre las cuales se ejecutara la consulta o en otros lenguajes seria lo que sigue después del parámetro 'WHERE' en SQL. Lo principal que realiza esta función es obtener los valores o identificadores que se utilizaran y compararan, para esto encuentra el nombre de estos valores o identificadores y posteriormente consulta que tipo de valor son, de esta forma el programa puede saber cómo realizara la comparación próxima y como mostrara los datos, después de todo esto convierte el operador lógico de la gramática de GraphQL y lo convierte a algún operador comprendido por SQL, al terminar todo esto el programa genera un string en donde se comparan ambos valores por medio de un operador lógico SQL y lo concatena a nuestro query.

Posteriormente podemos comparar en conjunto las funciones de `exitTable` y `exitField` ya que estas realizan operaciones parecidas, pero sobre diferentes tipos de objetos, la primera función ejecuta la función que se explicara a continuación sobre las tablas mientras que la segunda función lo hace sobre las columnas que se desean obtener. Esta función mencionada es el simplemente mostrar el nombre de estas tablas o columnas como un alias o simplemente aplicarles la operación SQL 'AS' para renombrar los datos a la hora de mostrárselos al usuario. Las funciones simplemente detectan si a los elementos se les incluyo un alias el cual se implementará sobre la consulta o no, posteriormente envía todos los campos al query para obtenerlos como el usuario lo desea.

Finalmente tenemos otro método el cual es mucho más simple y del cual su uso ya se mencionó previamente, este siendo `checkValue` al cual simplemente se le introduce un objeto de tipo Value y detecta a que subclase pertenece, esto con el fin de saber si las operaciones a realizar son gramaticales, matemáticas o lógicas.

Es importante notar que también se generaron clases para cada tipo de objeto pero ir por estas una por una no necesariamente vale la pena ya que la mayoría de estas son simplemente para que el sistema o programa logre diferenciar entre lo que se recibe y de esta forma al generar el string del query cada dato se encontrara en la posición correcta y la base de datos podrá comprender la consulta y brindar una respuesta apropiada.

## Resultados Obtenidos
Consulta básica
```
Entrada:
query{
	persona{
		id
		nombre
    edad
	}
}

Salida:
SELECT persona.edad, persona.id, 
persona.nombre 
FROM persona
```

Consulta con filtros de una tabla
```
Entrada:
query{
	persona(id:20, nombre:'Jorge'){
		id
		nombre
    edad
	}
}

Salida:
SELECT persona.nombre, persona.id, 
persona.edad 
FROM persona 
WHERE persona.id = 20 
AND persona.nombre = "Jorge"
```

Consulta de INNER JOIN
```
Entrada:
query(movie.directorid:director.id) {
	movie{
		id
		title
	}
	director{
		name
	}
}

Salida:
SELECT movie.id, movie.title, director.name 
FROM movie INNER JOIN director 
ON movie.directorid = director.id
```

Consulta de INNER JOIN con filtro de una tabla
```
Entrada:
query(movie.directorid:director.id) {
	movie(id:11){
		id
		title
	}
	director{
		name
	}
}

Salida:
SELECT movie.title, movie.id, 
director.name 
FROM movie INNER JOIN director 
ON movie.directorid = director.id 
WHERE movie.id = 11
```

Consulta de una tabla con filtro y aliases en el nombre de la tabla un atributos
```
Entrada:
query{
	movie(title:"Joker"){
		aliasid:id
		title
	}
}

Salida:
SELECT movie.title, movie.id AS aliasid 
FROM movie
WHERE movie.title = "Joker"
```

## Conclusiones y trabajos futuros
### Conclusion
Nuestra conclusión se basa principalmente sobre que es lo aprendido y lo implementado a lo largo de la realización de este proyecto y lo que nos gustaría decir mas que nada es que tanto la materia como los conocimientos que se nos fueron otorgados a lo largo del semestre demuestran como un programa se puede convertir en otro, como el uso de herramientas ya existentes nos dan infinitas posibilidades, y que comprender las bases de cualquier sistema a nivel arquitectónico es fundamental para comprender como este funciona y poder sacar el máximo de cualquier programa.

El reto presentado en este proyecto nos pareció muy interesante, no solamente porque nos introdujo a una gramática que no conocíamos, lo cual era GraphQL, sino que también pidió de nosotros el poder adaptar una entrada de este tipo para que se pueda reflejar por medio de una conexión SQL, como se menciono previamente, el convertir un concepto en otro totalmente diferente, y esto abre muchísimo las puertas para poder facilitar procesos y comprender como se crean los distintos compiladores, tal vez en el futuro hasta ayudarnos a poder mejorar los lenguajes de programación ya existentes o crear nuevos que puedan solucionar nuevos problemas de maneras mas eficientes tanto en velocidad como en el manejo de la memoria.

Pero a pesar de todo esto uno no puede limitarse a preguntarse a sí mismo, ¿acaso vale la pena todo esto? Es decir, porque intentar generar un interprete entre lenguajes, no es mas simple utilizar lo ya existente, y la respuesta corta es no, pero ¿Por qué? Bueno simplemente el hecho de generar un interprete de un lenguaje a otro abre las puertas para que muchos usuarios puedan utilizar mas herramientas a partir de los conocimientos que ya tienen, si yo se utilizar GraphQL pero requiero del uso de una base de datos SQL un interprete facilita el uso de estas herramientas a pesar de mi limitado conocimiento acerca de un tema.

### Trabajos futuros
Finalmente, ya realizamos la primera versión de este programa e incluimos algunas funciones, pero a partir de lo que tenemos, que nos espera al ampliar la gramática que podemos utilizar, que nuevas herramientas podremos introducir a los usuarios para poder maximizar el uso de este programa y poder sacarle jugo a cada consulta. Para esto es importante recordar que es lo que el programa logra hacer para poder ver en qué área podemos mejorar. Actualmente el programa logra utilizar aliases, condiciones a partir del uso de ‘WHERE’ y finalmente relacionar tablas utilizando ‘INNER JOIN’, aunque esto último presenta un gran problema, ya que en su estado actual el programa solamente puede relacionar dos tablas por consulta. Para mejorar este proceso debemos de pensar en la relación de tablas infinitas, es decir que una sola consulta pueda contener múltiples uniones.

Posteriormente podemos implementar mas tipos de uniones como lo podría ser el ‘OUTER JOIN’, ‘LEFT JOIN’ o ‘RIGHT JOIN’, ya que tenemos este tipo de uniones el siguiente paso podría ser el implementar las operaciones con mas tipos de datos, tal vez el manejo de fechas y tiempos como lo podría ser las variables ‘DATETIME’. Algo mas que se le podría agregar al programa que son algunas de las funciones básicas de SQL son las agrupaciones a partir de sumas, promedios, máximos, etc., el poder agrupar a partir de alguna variable y poder realizar operaciones matemáticas seria una gran función a implementar e introducir en la siguiente entrega.

Finalmente, se nos ocurrió aplicar el uso de ordenes como lo seria la función ya existente en SQL ‘ORDER BY’ con esto podremos ordenar los datos y mostrarlos al usuario para su mejor organización y comprensión. Todas estas funciones aportaran con su parte al programa y al preparar la gramática para que pueda manejar y controlar todos estos tipos de parámetros podemos mejorar el programa y de esta forma aumentar su utilidad.

## Bibliografía
