# Reproductor de música MP3

Un reproductor de música MP3 de escritorio desarrollado en Java utlizando Swing.

---

## Requisitos

- Java JDK: Versión 17
- Maven

---

## Tecnologías y dependencias

1. Jlayer 1.0.3: Biblioteca para decodificar y reproducir streams de MP3. Se utiliza a traves de Jitpack.
2. mp3agic 0.9.1: Biblioteca para leer metadatos (ID3 tags) y obtener la duración total del archivo.

### Configuración

Configuración del `pom.xml`

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.umjammer</groupId>
        <artifactId>jlayer</artifactId>
        <version>1.0.3</version>
    </dependency>

    <dependency>
        <groupId>com.mpatric</groupId>
        <artifactId>mp3agic</artifactId>
        <version>0.9.1</version>
    </dependency>
</dependencies>
```
---

## Estructura del proyecto

```plaintext
src/
└── main/
    ├── java/
    │   └── zuzz/
    │       └── projects/
    │           └── music_player/
    │               ├── Mp3Player.java      <-- Wrapper de bajo nivel
    │               ├── MusicPlayer.java    <-- Lógica de reproducción
    │               └── MusicPlayerUI.java  <-- Interfaz Gráfica
```

