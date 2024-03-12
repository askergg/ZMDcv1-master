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
        int windowSize = 8;
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

}


