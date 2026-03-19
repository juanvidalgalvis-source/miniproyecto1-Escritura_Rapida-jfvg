package com.example.escrirapmp1.model;

import java.util.Random;

/**
 * WordProvider - Abstracción para obtener palabras del juego.
 * Esta clase prepara la lógica futura de dificultad sin colocar
 * la generación de palabras dentro del Controlador.
 */
public class WordProvider {

        /** Palabras nivel 1-10: cortas y simples. */
        private String[] basicWords = {
                "casa", "perro", "gato", "sol", "luna", "agua", "fuego", "tierra", "aire", "libro", "mesa", "silla",
                "puerta", "ventana", "árbol", "pan", "flor", "nube", "cielo", "mar", "pez", "sal", "luz", "día", "noche"
        };
        /** Palabras nivel 11-20: longitud media. */
        private String[] mediumWords = {
                "familia", "amigo", "escuela", "computadora", "televisión", "teléfono", "internet", "elefante", "jirafa", "león",
                "montaña", "ciudad", "programa", "teclado", "pantalla", "canción", "película", "ventilador", "reloj", "cuaderno"
        };
        /** Palabras nivel 21-30: acentos, mayúsculas, complejidad. */
        private String[] hardWords = {
                "inteligencia", "filosofía", "psicología", "matemáticas", "biología",
                "física cuántica", "relatividad", "algoritmo",
                "PROGRAMACIÓN", "JavaFX", "OpenAI",
                "compilador", "abstracción", "encapsulación", "herencia", "polimorfismo",
                "@", "#", "&", "*", "Code123", "Nivel1", "Test2026"
        };
        /** Palabras nivel 31-45: largas y técnicas. */
        private String[] expertWords = {
                "desarrollo", "aplicaciones", "interfaz gráfica", "Java Platform",
                "modular runtime", "escritura rápida", "velocidad tipografía",
                "PROGRAMACIÓN AVANZADA",
                "JavaFX Engine",
                "OpenAI Model",
                "programación concurrente", "sistemas distribuidos",
                "arquitectura de software", "modelo vista controlador",
                "@#%", "#$%&", "&*@",
                "Java@2026", "Code&Play", "FX_Engine", "Nivel#Final"
        };

    private Random random;

    public WordProvider() {
        this.random = new Random();
    }

    /**
     * Returns a random word for the given level.
     *
     * @param level The current game level
     * @return A random word from the word list
     */
public String getRandomWord(int level) {
        String[] selectedList;
        if (level <= 10) {
            selectedList = basicWords;
        } else if (level <= 20) {
            selectedList = mediumWords;
        } else if (level <= 30) {
            selectedList = hardWords;
        } else {
            selectedList = expertWords;
        }
        int index = random.nextInt(selectedList.length);
        return selectedList[index];
    }
}

