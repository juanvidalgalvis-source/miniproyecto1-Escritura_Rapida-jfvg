package com.example.escrirapmp1.model;

import java.util.Random;

/**
 * WordProvider - Abstracción para obtener palabras del juego.
 * Esta clase prepara la lógica futura de dificultad sin colocar
 * la generación de palabras dentro del Controlador.
 */
public class WordProvider {

    private String[] wordList = {
            "casa",
            "perro",
            "gato",
            "sol",
            "luna",
            "agua",
            "fuego",
            "tierra",
            "aire",
            "libro"
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
        int index = random.nextInt(wordList.length);
        return wordList[index];
    }
}

