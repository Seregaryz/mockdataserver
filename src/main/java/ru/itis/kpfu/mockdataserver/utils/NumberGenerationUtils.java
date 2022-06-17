package ru.itis.kpfu.mockdataserver.utils;

import java.util.Random;

public class NumberGenerationUtils {

    public NumberGenerationUtils() {
    }

    public double generateRandomDouble(Random random) {
        int numberOfMultiplication = getRandomNumberInRange(1, 9);
        double result = random.nextDouble();
        for (int i = 0; i < numberOfMultiplication; i++) {
            result = result * 10;
        }
        return result;
    }

    public float generateRandomFloat(Random random) {
        int numberOfMultiplication = getRandomNumberInRange(1, 6);
        float result = random.nextFloat();
        for (int i = 0; i < numberOfMultiplication; i++) {
            result = result * 10;
        }
        return result;
    }

    public int getRandomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
