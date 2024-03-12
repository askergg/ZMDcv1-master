package core;

import Jama.Matrix;

public class Helper {
    /**
     * Vrátí šířku matice.
     * @param matrix matice
     * @return šířka matice
     */
    public static int GetWidth(Matrix matrix) {
        if(matrix == null) return 0;
        return matrix.getColumnDimension();
    }

    /**
     * Vrátí šířku pole.
     * @param array pole
     * @return šířka pole
     */
    public static int GetWidth(int[][] array) {
        if(array == null || array.length == 0) return 0;
        return array[0].length;
    }

    /**
     * Vrátí výšku matice.
     * @param matrix matice
     * @return výška matice
     */
    public static int GetHeight(Matrix matrix) {
        if(matrix == null) return 0;
        return matrix.getRowDimension();
    }

    /**
     * Vrátí výšku pole.
     * @param array pole
     * @return výška pole
     */
    public static int GetHeight(int[][] array) {
        if(array == null) return 0;
        return array.length;
    }
}
