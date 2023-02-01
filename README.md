# SUBMARINO ASESINO

Submarino Asesino es un juego de disparos en 2D realizado con LibGDX En él controlamos un submarino que va surcando los océanos enfrentándose a distintos enemigos que intentan hundirlo.
La mecánica básica para navegar el submarino mezcla los juegos de disparos clásicos con el conocido juego Flappy bird. Podremos movernos a izquierda y derecha libremente pero en vertical el submarino sólo podrá ser impulsado hacia arriba y bajará en caída libre.
Podremos disparar proyectiles a los enemigos para eliminarlos y evitar perder vidas cuando nos toquen.

## Ejecutar el proyecto

Para instalar el proyecto y probar el juego podemos clonar el repositorio mediante `git clone https://github.com/AdrianCoso/Drop`. También serviría descargarlo en formato .zip
Después habrá que utilizar Android Studio para abrir el proyecto y sincronizarlo con Gradle para ejecutarlo como aplicación de escritorio.
Para ello será necesario ejecutar la tarea desktop>Tasks>other>run.


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

En nuestro caso añadimos un campo para guardar la velocidad vertical del submarino. En el constructor inicializamos su valor a cero y en el método `render()` añadimos la posibilidad de que pase a 400 pulsando la tecla `Input.Keys.UP`. Además haremos que este valor disminuya con el tiempo simulando una caída libre y moveremos el personaje según el valor de su velocidad vertical. Este método responde a la [integración semi-implícita de Euler](https://gafferongames.com/post/integration_basics/). Al final utilizaremos el valor de la velocidad vertical del jugador para determinar su posición en cada momento.

### Fin del juego.
Empezaremos cada partida con tres vidas. Cada vez que toquemos un enemigo el número de vidas disponibles disminuirá en una unidad en lugar de aumentar el número de gotas recogidas. Además usaremos el método `dibujarVidas()` para mostrar en pantalla de forma gráfica el número de vidas disponibles usando la clásica imágen del corazon.

Si el número de vidas disponibles llega a cero se nos muestra la pantalla de fin del juego, que nos informa de la puntuación final y nos permite volver a jugar.

## Aspecto gráfico y sonidos acordes con el nuevo tipo de juego

Hemos podido probar la jugabilidad y el funcionamiento del juego sin preocuparnos de diseñar nuevos gráficos utilizando los que ya teníamos para el tutorial de libgdx. Sin embargo queremos darle un mejor aspecto al juego y que éste tenga sentido con el nuevo tipo de mecánicas que hemos desarrollado.

### Gráficos
Para la imagen del jugador bastará con sustituir la imagen del cubo por la de un [submarino](https://www.pngwing.com/en/free-png-nudbr/) y la de las gotas por unos [submarinistas](https://www.pngwing.com/en/free-png-vtaey) que tratan de sabotearnos. También añadimos la imagen del [corazón](https://www.pngwing.com/en/free-png-zrrvr) que representará las vidas. Todas estas imágenes han sido convenientemente modificadas para adaptarlas a nuestras necesidades usando GIMP. Además hemos modificado las dimensiones de los rectángulos usados en el juego para adaptarlos a estos nuevos gráficos. Estos rectángulos se utilizan para determinar los impactos y ahora no es tan sencillo disparar a los enemigos. De este modo el nuevo aspecto gráfico también mejora la jugabilidad incrementando ligeramente la dificultad.

### Efectos sonoros y música 
También se han añadido sonidos para ambientar los [disparos](https://pixabay.com/sound-effects/laser6quick-47339/) del submarino, la [destrucción](https://pixabay.com/sound-effects/explosion-asteroid-101886/) de los enemigos, el [impacto](https://pixabay.com/sound-effects/shooting-star-101304/) del submarino con un enemigo y el [fin del juego](https://pixabay.com/sound-effects/game-over-arcade-6435/). Estos efectos se reproducen convenientemente cada vez que suceden los eventos del juego que están relacionados con ellos. Además hemos sustituido la [música](https://pixabay.com/music/electronic-submarine-electronic-music-for-machinery-and-engineering-7582/) por otra menos relajante.

Por último hemos descargado una [fuente](https://www.dafont.com/es/04b-30.font) y la hemos convertido a bitmap usando la herramienta [Hiero](https://libgdx.com/wiki/tools/hiero). Además hemos usado distintos colores para dar las indicaciones al jugador de cómo utilizar los controles y empezar a jugar. Esta misma fuente se utiliza en la pantalla del fin del juego.

