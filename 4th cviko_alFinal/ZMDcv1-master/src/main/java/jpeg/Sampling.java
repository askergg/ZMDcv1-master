package jpeg;

import enums.SamplingType;
import Jama.Matrix;

public class Sampling {

    public static Matrix sampleDown(Matrix inputMatrix, SamplingType samplingType) {
        Matrix sampledMatrix = null;

        switch (samplingType) {
            case S_4_4_4 -> sampledMatrix = inputMatrix;
            case S_4_2_2 -> sampledMatrix = downSample(inputMatrix);
            case S_4_2_0 -> {
                sampledMatrix = downSample(inputMatrix);
                sampledMatrix = sampledMatrix.transpose();
                sampledMatrix = downSample(sampledMatrix);
                sampledMatrix = sampledMatrix.transpose();
            }
            case S_4_1_1 -> {
                sampledMatrix = downSample(inputMatrix);
                sampledMatrix = downSample(sampledMatrix);
            }
            default -> sampledMatrix = inputMatrix;
        }

        return sampledMatrix;
    }

    public static Matrix sampleUp(Matrix inputMatrix, SamplingType samplingType){


        Matrix sampledMatrix = null;

        switch (samplingType) {
            case S_4_4_4 -> sampledMatrix = inputMatrix;
            case S_4_2_2 -> sampledMatrix = upSample(inputMatrix);
            case S_4_2_0 -> {
                sampledMatrix = upSample(inputMatrix);
                sampledMatrix = sampledMatrix.transpose();
                sampledMatrix = upSample(sampledMatrix);
                sampledMatrix = sampledMatrix.transpose();
            }
            //sampledMatrix = upSample(inputMatrix);
            case S_4_1_1 -> {
                sampledMatrix = upSample(inputMatrix);
                sampledMatrix = upSample(sampledMatrix);
            }
        }
        return sampledMatrix;
    }

    private static Matrix downSample(Matrix matrix) {
        int numbRows = matrix.getRowDimension();
        int numbColumns = matrix.getColumnDimension();
        double[][] newMatrix = new double[numbRows][numbColumns/2];

        int tempCol = 0;
        for (int row = 0; row < numbRows; row++) {
            for (int col = 0; col < numbColumns; col++) {
                if ((col+1) % 2 != 0) {
                    newMatrix[row][col-tempCol] = matrix.get(row, col);
                    tempCol++;
                }
            }

            tempCol = 0;
        }

        return new Matrix(newMatrix);
    }

    public static Matrix upSample (Matrix mat) {
        Matrix newMat = new Matrix (mat.getRowDimension(), mat.getColumnDimension()*2);
        for (int i = 0; i < mat.getColumnDimension(); i++) {
            newMat.setMatrix(0, mat.getRowDimension()-1, 2*i, 2*i, mat.getMatrix(0, mat.getRowDimension()-1, i, i));
            newMat.setMatrix(0, mat.getRowDimension()-1, 2*i+1, 2*i+1, mat.getMatrix(0, mat.getRowDimension()-1, i, i));
        }
        return newMat;

    }

}