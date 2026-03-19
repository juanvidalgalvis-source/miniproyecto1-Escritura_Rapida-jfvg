package com.example.escrirapmp1.model;

import java.util.Random;

/**
 * WordProvider - Abstracción para obtener palabras del juego.
 * Esta clase prepara la lógica futura de dificultad sin colocar
 * la generación de palabras dentro del Controlador.
 */
public class WordProvider {

private String[] basicWords = { // Nivel 1-10: cortas
            "casa", "perro", "gato", "sol", "luna", "agua", "fuego", "tierra", "aire", "libro",
            "mesa", "silla", "puerta", "ventana", "árbol"
        };
        private String[] mediumWords = { // Nivel 11-20: medias
            "familia", "amigo", "escuela", "computadora", "televisión", "teléfono", "internet", "elefante", "jirafa", "león"
        };
        private String[] hardWords = { // Nivel 21-30: acentos/mayúsculas
            "inteligencia", "filosofía", "psicología", "matemáticas", "biología", "física cuántica", "relatividad", "algoritmo", "PROGRAMACIÓN", "JavaFX"
        };
        private String[] expertWords = { // Nivel 31-45: largas/frases
            "desarrollo", "aplicaciones", "interfaz gráfica", "Java Platform", "modular runtime", "escritura rápida", "velocidad tipografía", "juego educativo"
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

