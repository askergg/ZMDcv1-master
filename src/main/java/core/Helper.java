package core;

import Jama.Matrix;

public class Helper {
    /**
     * Vrátí šířku matice.
     */
    public static int GetWidth(Matrix matrix) {
        if(matrix == null) return 0;
        return matrix.getColumnDimension();
    }

    /**
     * Vrátí šířku pole.
     */
    public static int GetWidth(int[][] array) {
        if(array == null || array.length == 0) return 0;
        return array[0].length;
    }

    /**
     * Vrátí výšku matice.
     */
    public static int GetHeight(Matrix matrix) {
        if(matrix == null) return 0;
        return matrix.getRowDimension();
    }

    /**
     * Vrátí výšku pole.
     */
    public static int GetHeight(int[][] array) {
        if(array == null) return 0;
        return array.length;
    }
    public static double[][] convertIntToDouble(int[][] intArray) {
        double[][] doubleArray = new double[intArray.length][intArray[0].length];
        for (int i = 0; i < intArray.length; i++) {
            for (int j = 0; j < intArray[0].length; j++) {
                doubleArray[i][j] = (double) intArray[i][j];
            }
        }
        return doubleArray;
    }

}
