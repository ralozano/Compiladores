# Ejercicio 1

## Intrucciones
Realizar el programa del analizador LR dónde la pila la pueden considerar como parte del analizador LR. Su programa deberá realizar las impresiones de las acciones que de acuerdo conlas tablas de análisis  y  la  entrada  se  realicenpara  analizar  la  entrada. Su  programa  deberá  ser  invocado  de  la siguiente manera:
``` sh
python Ejercicio1.py action.csv goto.csv gramaticaNumerada.txt entrada.txt
```

## Código
Se tienen las siguientes variables destacadas:
- **symbols**: Es una lista de los símbolos terminales. Un ejemplo de matriz utilizando el archivo action1.csv es el siguiente:
```python
['+', '*', 'a', '$']
```
$$\begin{bmatrix}
      '+' & '*' & 'a' & '\$'
\end{bmatrix}$$


- **actions**: Una mariz de las acciones El cual nos ayuda a saber dependiendo del estado que nos encontremos en el stack, y el índice del símbolo, que realizar. Un ejemplo de matriz utilizando el archivo action1.csv es el siguiente:
```python
[
    ['s3', 's4', 's2', ''], 
    ['', '', '', 'accept'], 
    ['r3', 'r3', 'r3', 'r3'], 
    ['s3', 's4', 's2', ''], 
    ['s3', 's4', 's2', ''], 
    ['s3', 's4', 's2', ''], 
    ['s3', 's4', 's2', ''], 
    ['r1', 'r1', 'r1', 'r1'], 
    ['r2', 'r2', 'r2', 'r2']
]
```

$$
  \begin{bmatrix}
      's3' & 's4' & 's2' & '' \\
      '' & '' & '' & 'accept' \\
      'r3' & 'r3' & 'r3' & 'r3' \\
      's3' & 's4' & 's2' & '' \\
      's3' & 's4' & 's2' & '' \\
      's3' & 's4' & 's2' & '' \\
      's3' & 's4' & 's2' & '' \\
      'r1' & 'r1' & 'r1' & 'r1' \\
      'r2' & 'r2' & 'r2' & 'r2'
  \end{bmatrix}
$$

- **goto_symbols**: Es una lista de los símbolos no terminales. Un ejemplo de la lista utilizando el archivo goto1.csv es el siguiente:
```python
['S']
```

$$
  \begin{bmatrix}
      'S' \\
  \end{bmatrix}
$$

- **gotos**: Diccionario de los gotos, Se tiene una lista de las acciones, un diccionario de los GOTO, el cual está compuesto de una llave la cual es un símbolo no terminal y su valor es una lista de estados a donde debe de ir. Un ejemplo del diccionario utilizando el archivo goto1.csv es el siguiente:
```python
{
    'S': [1, '', '', 5, 6, 7, 8]
}
```

$$
\begin{bmatrix} 
  S \\
\end{bmatrix}
\begin{bmatrix} 
  1 & '' & '' & 5 & 6 & 7 & 8 \\
\end{bmatrix}
$$ 

- **map_symbols_actions**: Es un diccionario el cual su llave está compuesta por una tupla y el valor es un estado al cual se debe de ir. La tupla está hecha de dos elementos: (\<Estado actual>, <Símbolo no termial relacionado con la matriz de *actions*>). El siguinte diccionario contiene ejemplos:
```python
{
    (1, '+'): 's3',
    (1, '*'): 's4',
    (1, 'a'): 's2', 
    (1, '$'): '', 
    (2, '+'): '', 
    (2, '*'): '', 
    (2, 'a'): '', 
    (2, '$'): 'accept'
} 
```
Como podemos observar se muestra el estado y el simbolo de action los cuales en conjunto llegan a un nuevo estado. Basicamente se expresan las distintas transiciones a partir de un estado y lo que se debe de hacer al recibir un simbolo action.

- **productions**: Es una lista de tuplas que contiene (\<Símbolo no terminal>, <Producción>, <Número de pops>): Un ejemplo utilizando el archivo producciones1.txt es elsiguiente:

```python
[(), ('S', '+SS', 3), ('S', '*SS', 3), ('S', 'a', 1)]
```
Podemos observar que se ignora la primera produccion ya que esta se relaciona con S', y por ende no es parte de nuestras producciones, despues en nuestro arreglo podemos ver que cada entrada contiene primero el valor por el cual se sustituira, en segundo lugar el valor sustituido y finalmente la cantidad de pops que se haran sobre la lista de estados a partir de esta transicion.

## Algoritmo

Después de tener las variables listadas anteriormente se lee el *input* el cual se analiza en el ```while True:```. 
1. Se inicializa la variable i=0 la cual ayuda a recorrer el input y de esta forma poder analizarlo caracter por caracter.
2. Se inicializa la variable ```states_stack``` para guardar una lista de los estados. A esta lista de le agrega el estado 0.
3. Se ingresa al loop
4. Se recorre el input caracter por caracter
5. Se llega a nuevos estados a partir de mapear caracter detectado con el último estado del  ```states_stack```.
6. Verifica si lo obtenido por el mapeo da como resultado una aceptación, un error, transición de estado o una reducción por realizar
   1. Si esncuentra una aceptación, el input se acepta y el programa se termina.
   2. Si encuentra un error, se imprime un mensaje de error y el programa se termina.
   3. Si es una trancisión de estado, agrega el nuevo estado mapeado al ```states_stack``` y recorre el input un caracter, es decir, suma 1 a *i*.
   4. Si es una reducción, cambia la producción por lo adecuado, realiza los pops al ```states_stack``` y finalmente verifica el último estado encontrado en el ```states_stack``` y busca en los goto el estado que debe agregar al ```states_stack```.

## Output
La salida del programa al ingresar el comando descrito en las intrucciones es:
```shell
[0] s3
[0, 3] s2
[0, 3, 2] S->a
[0, 3, 5] s4
[0, 3, 5, 4] s2
[0, 3, 5, 4, 2] S->a
[0, 3, 5, 4, 6] s2
[0, 3, 5, 4, 6, 2] S->a
[0, 3, 5, 4, 6, 8] S->*SS
[0, 3, 5, 7] S->+SS
[0, 1] accept
```