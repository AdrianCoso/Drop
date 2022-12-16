# SUBMARINO ASESINO

Submarino Asesino es un juego de disparos en 2D realizado con LibGDX En él controlamos un submarino que va surcando los océanos enfrentándose a distintos enemigos que intentan hundirlo.
La mecánica básica para navegar el submarino mezcla los juegos de disparos clásicos con el conocido juego Flappy bird. Podremos movernos a izquierda y derecha libremente pero en vertical el submarino sólo podrá ser impulsado hacia arriba y bajará en caída libre.
Podremos disparar proyectiles a los enemigos para eliminarlos y evitar perder vidas cuando nos toquen.

## Implementando las mecánicas básicas.

Para realizar este juego nos hemos basado en el que realizamos siguiendo el tutorial para aprender cómo funciona LibGDX. En aquel primer ejemplo controlábamos un cubo para recoger gotas que caían del cielo.
Basándonos en este ejemplo tendremos que añadir el movimiento en vertical para el cubo (que terminará siendo el submarino), los proyectiles que disparemos y cambiar ligeramente el comportamiento de las gotas (que serán los enemigos en nuestro juego final).

### Las gotas/enemigos.
Es bastante sencillo cambiar el comportamiento de las gotas de agua. En el ejemplo inicial éstas se movían desde la parte superior hacia abajo con velocidad uniforme. Lo que haremos en un primer momento será sencillamente generarlas en la parte derecha y moverlas hacia la izquierda. Ésto lo conseguimos modificando el código que genera las gotas en el método `spawnRaindrop()` y la parte que mueve dichas gotas en `render()`.

### Los proyectiles.
Para poder luchar contra los enemigos podremos lanzar proyectiles desde nuestro submarino. Lo primero que hacemos es generar un sencillo gráfico con GIMP para representarlos. Bastan un par de círculos superpuestos con los colores adecuados. En nuestro caso está dentro de un cuadrado de 16px de lado

Una vez que tenemos listo el gráfico que utilizaremos generamos un método `spawnBullet()` muy similar al que genera las gotas. Genera el rectángulo correspondiente a la bala en la posición adecuada con respecto al cubo y lo añade a un `Array<>` igual que hacíamos con las gotas. Llamaremos a este método cada vez que se pulse la tecla ESPACIO. Dentro del método `render()` haremos que cada elemento de la matriz de balas se desplace a la derecha con velocidad uniforme y si alguna gota lo toca eliminaremos tanto la gota como la bala que la tocó.

### Navegando con el submarino.
Podríamos sencillamente utilizar los cursores para mover nuestro personaje hacia arriba, abajo, izquierda y derecha utilizando un método similar al que se utiliza en el ejemplo inicial. Sin embargo hemos decidido aportar algo de jugabilidad extra complicando ligeramente el movimiento vertical.

En nuestro caso añadimos un campo para guardar la velocidad vertical del submarino. En el constructor inicializamos su valor a cero y en el método `render()` añadimos la posibilidad de que pase a 400 pulsando la tecla `Input.Keys.UP`. Además haremos que este valor disminuya con el tiempo simulando una caída libre y moveremos el personaje según el valor de su velocidad vertical. Este método responde a la [integración semi-implícita de Euler](https://gafferongames.com/post/integration_basics/)