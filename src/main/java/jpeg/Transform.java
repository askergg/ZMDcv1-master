package jpeg;

import enums.TransformType;

public class Transform {


    // Transformace předané barevné složky
    public static Matrix transform(Matrix input, TransformType type, int blockSize) {
        // Získání transformační matice
        Matrix transformMatrix = getTransformMatrix(type, blockSize);

        // Provedení transformace
        // Příklad použití: transformMatrix * input * transformMatrix.transpose()
        // Implementaci doplníte podle vaší maticové knihovny

        return null; // Vrátí transformovanou matici
    }

    // Inverzní transformace
    public static Matrix inverseTransform(Matrix input, TransformType type, int blockSize) {
        // Získání inverzní transformační matice (pro WHT je to stejná matice, pro DCT transponovaná)
        Matrix transformMatrix = getTransformMatrix(type, blockSize);

        // Provedení inverzní transformace
        // Příklad použití: transformMatrix.transpose() * input * transformMatrix
        // Implementaci doplníte podle vaší maticové knihovny

        return null; // Vrátí původní matici před transformací
    }

    // Získání transformační matice, generované podle typu a velikosti bloku
    public static Matrix getTransformMatrix(TransformType type, int blockSize) {
        switch (type) {
            case DCT:
                return generateDctMatrix(blockSize);
            case WHT:
                return generateWhtMatrix(blockSize);
            default:
                throw new IllegalArgumentException("Neznámý typ transformace");
        }
    }

    // Pomocné metody pro generování transformačních matic
    private static Matrix generateDctMatrix(int n) {
        Matrix matrix = new Matrix(n, n);
        double c;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0) c = Math.sqrt(1.0 / n);
                else c = Math.sqrt(2.0 / n);
                matrix.set(i, j, c * Math.cos(((2 * j + 1) * i * Math.PI) / (2.0 * n)));
            }
        }
        return matrix;
    }

    private static Matrix generateWhtMatrix(int n) {
        Matrix matrix = new Matrix(n, n);
        // Tento kód je zjednodušený a předpokládá, že n je mocnina dvou
        int fillVal = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int xored = i & j;
                int bitCount = Integer.bitCount(xored);
                if (bitCount % 2 == 0) matrix.set(i, j, fillVal / Math.sqrt(n));
                else matrix.set(i, j, -fillVal / Math.sqrt(n));
            }
        }
        return matrix;
    }

    // Placeholder pro maticovou knihovnu
    public static class Matrix {
        // Metody maticové knihovny (např. set, get, times, transpose)
    }
}