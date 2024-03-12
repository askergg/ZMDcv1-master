package jpeg;

import Jama.Matrix;

public class Quality {



    public static double countMSE(double[][] original, double[][] modified) {
        double mse = 0;
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                mse += Math.pow(original[i][j] - modified[i][j], 2);
            }
        }
        mse /= (original.length * original[0].length);
        return mse;
    }

    public static double countMAE(double[][] original, double[][] modified) {
        double mae = 0;
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                mae += Math.abs(original[i][j] - modified[i][j]);
            }
        }
        mae /= (original.length * original[0].length);
        return mae;
    }

    public static double countSAE(double[][] original, double[][] modified) {
        double sae = 0;
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                sae += Math.abs(original[i][j] - modified[i][j]);
            }
        }
        return sae;
    }
    public static double countPSNR(double MSE) {
        double maxPixelValue = 255; // assuming pixel values are in the range 0-255
        double psnr = 20 * Math.log10(maxPixelValue) - 10 * Math.log10(MSE);
        return psnr;
    }

    public static double countPSNRforRGB(double mseRed, double mseGreen, double mseBlue) {
        double mseTotal = (mseRed + mseGreen + mseBlue) / 3;
        return countPSNR(mseTotal);
    }

    public static double countSSIM(double[][] original, double[][] modified) {
        final double C1 = (0.01 * 255) * (0.01 * 255);
        final double C2 = (0.03 * 255) * (0.03 * 255);

        double muOriginal = mean(original);
        double muModified = mean(modified);

        double sigmaOriginalSquared = variance(original, muOriginal);
        double sigmaModifiedSquared = variance(modified, muModified);
        double sigmaOriginalModified = covariance(original, modified, muOriginal, muModified);

        double numerator = (2 * muOriginal * muModified + C1) * (2 * sigmaOriginalModified + C2);
        double denominator = (muOriginal * muOriginal + muModified * muModified + C1) * (sigmaOriginalSquared + sigmaModifiedSquared + C2);

        return numerator / denominator;
    }

    private static double mean(double[][] data) {
        double sum = 0.0;
        for (double[] row : data) {
            for (double value : row) {
                sum += value;
            }
        }
        return sum / (data.length * data[0].length);
    }

    private static double variance(double[][] data, double mean) {
        double variance = 0.0;
        for (double[] row : data) {
            for (double value : row) {
                variance += (value - mean) * (value - mean);
            }
        }
        return variance / (data.length * data[0].length);
    }

    private static double covariance(double[][] original, double[][] modified, double meanOriginal, double meanModified) {
        double covariance = 0.0;
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                covariance += (original[i][j] - meanOriginal) * (modified[i][j] - meanModified);
            }
        }
        return covariance / ((original.length * original[0].length));
    }


    public static double countMSSIM(double[][] original, double[][] modified) {
        int windowSize = 8; // Example window size, could be adjusted
        double mssim = 0.0;
        int count = 0;

        for (int i = 0; i < original.length; i += windowSize) {
            for (int j = 0; j < original[0].length; j += windowSize) {
                double[][] windowOriginal = getWindow(original, i, j, windowSize);
                double[][] windowModified = getWindow(modified, i, j, windowSize);
                mssim += countSSIM(windowOriginal, windowModified);
                count++;
            }
        }

        return mssim / count;
    }

    private static double[][] getWindow(double[][] data, int startX, int startY, int windowSize) {
        double[][] window = new double[windowSize][windowSize];
        for (int i = 0; i < windowSize; i++) {
            for (int j = 0; j < windowSize; j++) {
                window[i][j] = data[startX + i][startY + j];
            }
        }
        return window;
    }


    /*
    public static double countSSIM(Matrix original, Matrix modified) {

        int M = original.getRowDimension();
        int N = original.getColumnDimension();

        double C1 = Math.pow(0.01 * 255, 2);
        double C2 = Math.pow(0.03 * 255, 2);

        double muX = original.norm1() / (M * N);
        double muY = modified.norm1() / (M * N);

        double sigmaX = 0;
        double sigmaY = 0;
        double sigmaXY = 0;

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                double x = original.get(i, j);
                double y = modified.get(i, j);
                sigmaX += Math.pow(x - muX, 2);
                sigmaY += Math.pow(y - muY, 2);
                sigmaXY += (x - muX) * (y - muY);
            }
        }

        sigmaX /= (M * N);
        sigmaY /= (M * N);
        sigmaXY /= (M * N);

        double numerator = (2 * muX * muY + C1) * (2 * sigmaXY + C2);
        double denominator = (muX * muX + muY * muY + C1) * (sigmaX + sigmaY + C2);

        return numerator / denominator;
    }

    // Method to calculate MSSIM
    public static double countMSSIM(Matrix original, Matrix modified) {

        int M = original.getRowDimension();
        int N = original.getColumnDimension();

        double[] weights = {0.0448, 0.2856, 0.3001, 0.2363, 0.1333};
        double mssim = 1;

        Matrix downsampledOriginal = original;
        Matrix downsampledModified = modified;

        for (int level = 0; level < 5; level++) {
            mssim *= Math.pow(countSSIM(downsampledOriginal, downsampledModified), weights[level]);

            if (level < 4) {
                downsampledOriginal = downsampleMatrix(downsampledOriginal);
                downsampledModified = downsampleMatrix(downsampledModified);
            }
        }

        return mssim;
    }

    private static Matrix downsampleMatrix(Matrix input) {
        int M = input.getRowDimension();
        int N = input.getColumnDimension();

        Matrix output = new Matrix(M / 2, N / 2);

        for (int i = 0; i < M / 2; i++) {
            for (int j = 0; j < N / 2; j++) {
                double value = (input.get(2 * i, 2 * j) +
                        input.get(2 * i, 2 * j + 1) +
                        input.get(2 * i + 1, 2 * j) +
                        input.get(2 * i + 1, 2 * j + 1)) / 4;
                output.set(i, j, value);
            }
        }

        return output;
    }

     */
}


