package jpeg;

import Jama.Matrix;
import core.Helper;
import enums.*;
import graphics.Dialogs;

import java.awt.*;
import java.awt.image.BufferedImage;

import static enums.ColorType.*;


public class Process {

    private Quality quality;

    private int[][] originalRed, modifiedRed;
    private int[][] originalGreen, modifiedGreen;
    private int[][] originalBlue, modifiedBlue;

    private BufferedImage originalImage;
    private int imageHeight;
    private int imageWidth;

    private Matrix originalY, modifiedY;
    private Matrix originalCb, modifiedCb;
    private Matrix originalCr, modifiedCr;

    public double mse, sae, mae, psnr, ssim, mssim;
    public Process(String path) {

        this.originalImage = Dialogs.loadImageFromPath(path);

        imageWidth = originalImage.getWidth();
        imageHeight = originalImage.getHeight();

        originalRed = new int[imageHeight][imageWidth];
        modifiedRed = new int[imageHeight][imageWidth];
        originalGreen = new int[imageHeight][imageWidth];
        modifiedGreen = new int[imageHeight][imageWidth];
        originalBlue = new int[imageHeight][imageWidth];
        modifiedBlue = new int[imageHeight][imageWidth];

        originalY = new Matrix(imageHeight, imageWidth, 0);
        modifiedY = new Matrix(imageHeight, imageHeight, 0);
        originalCb = new Matrix(imageHeight, imageWidth, 0);
        modifiedCb = new Matrix(imageHeight, imageHeight, 0);
        originalCr = new Matrix(imageHeight, imageWidth, 0);
        modifiedCr = new Matrix(imageHeight, imageHeight, 0);
        setOriginalRGB();
    }

    public void transform(TransformType transformType, int blockSize){
        modifiedY = Transform.transform(modifiedY,transformType,blockSize);
        modifiedCb = Transform.transform(modifiedCb,transformType,blockSize);
        modifiedCr = Transform.transform(modifiedCr,transformType,blockSize);
    }

    public void inverseTransform(TransformType transformType, int blockSize){
        modifiedY = Transform.inverseTransform(modifiedY,transformType,blockSize);
        modifiedCb = Transform.inverseTransform(modifiedCb,transformType,blockSize);
        modifiedCr = Transform.inverseTransform(modifiedCr,transformType,blockSize);
    }
    public void quantizeImage (int blockSize, double quality, Boolean inverse){
        if(inverse){
            modifiedY = Quantization.inverseQuantize(modifiedY, blockSize, quality, true);
            modifiedCb = Quantization.inverseQuantize(modifiedCb, blockSize, quality, false);
            modifiedCr = Quantization.inverseQuantize(modifiedCr, blockSize, quality, false);
        }
        else{
            modifiedY = Quantization.quantize(modifiedY, blockSize, quality, true);
            modifiedCb = Quantization.quantize(modifiedCb, blockSize, quality, false);
            modifiedCr = Quantization.quantize(modifiedCr, blockSize, quality, false);
        }
    }


    public void setOriginalRGB() {
        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                Color color = new Color(originalImage.getRGB(w, h));
                originalRed[h][w] = color.getRed();
                originalBlue[h][w] = color.getBlue();
                originalGreen[h][w] = color.getGreen();
            }

        }
    }
    public BufferedImage getImageFromRGB() {
        BufferedImage bfImage = new BufferedImage(
                imageWidth, imageWidth, BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                bfImage.setRGB(w, h, (new Color(modifiedRed[h][w], modifiedGreen[h][w], modifiedBlue[h][w])).getRGB());
            }
        }

        return bfImage;
    }
    
    public void convertToYCbCr() {

        Matrix[] temp = ColorTransform.convertOriginalRGBtoYcBcR(originalRed, originalGreen, originalBlue);
        originalY = temp[0];
        originalCb = temp[1];
        originalCr = temp[2];

        modifiedY = originalY;
        modifiedCb = originalCb;
        modifiedCr = originalCr;
        
    }

    public void convertToRGB()
    {
        int[][][] temp = ColorTransform.convertModifiedYcBcRtoRGB(modifiedY, modifiedCb, modifiedCr);
        modifiedRed = temp[0];
        modifiedGreen = temp[1];
        modifiedBlue = temp[2];
    }

    public BufferedImage showOneColorImageFromRGB(int[][] color, ColorType type) {

        BufferedImage bfImage = new BufferedImage(
                imageWidth, imageWidth, BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                switch (type) {
                    case RED:
                        bfImage.setRGB(w, h, (new Color(color[h][w], 0, 0)).getRGB());
                        break;
                    case GREEN:
                        bfImage.setRGB(w, h, (new Color(0, color[h][w], 0)).getRGB());
                        break;
                    case BLUE:
                        bfImage.setRGB(w, h, (new Color(0, 0, color[h][w])).getRGB());
                        break;
                }
            }
        }

        return bfImage;
    }

    public BufferedImage showOneColorFromYCbCr(Matrix color) {

        int width = Helper.GetWidth(color);
        int height = Helper.GetHeight(color);

        BufferedImage bfImage = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_RGB);

        double [][] colorOrigin = color.getArray();
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                bfImage.setRGB(w,h,
                        (new Color(roundRange(color.get(h,w)),
                                roundRange(color.get(h,w)),
                                roundRange(color.get(h,w))).getRGB()));
            }
        }
        return bfImage;
    }

    public static int roundRange(double num) {
        return Math.min(Math.max((int) Math.round(num), 0), 255);
    }
    public void sampleDown (SamplingType samplingType) {
        modifiedY = Sampling.sampleDown(modifiedY, samplingType);
        modifiedCb = Sampling.sampleDown(modifiedCb, samplingType);
        modifiedCr = Sampling.sampleDown(modifiedCr, samplingType);
    }
    public void sampleUp (SamplingType samplingType) {
        modifiedY = Sampling.sampleUp(modifiedY, samplingType);
        modifiedCb = Sampling.sampleUp(modifiedCb, samplingType);
        modifiedCr = Sampling.sampleUp(modifiedCr, samplingType);
    }

    public void count(QualityType qualityType) {
        double[][] originalArray, modifiedArray;
        double totalMSE = 0.0;
        double totalMAE = 0.0;
        double totalSAE = 0.0;

        switch (qualityType) {
            case Y:
                originalArray = originalY.getArray();
                modifiedArray = modifiedY.getArray();
                break;
            case Cb:
                originalArray = originalCb.getArray();
                modifiedArray = modifiedCb.getArray();
                break;
            case Cr:
                originalArray = originalCr.getArray();
                modifiedArray = modifiedCr.getArray();
                break;
            case Red:
                originalArray = Helper.IntToDouble(originalRed);
                modifiedArray = Helper.IntToDouble(modifiedRed);
                break;
            case Blue:
                originalArray = Helper.IntToDouble(originalBlue);
                modifiedArray = Helper.IntToDouble(modifiedBlue);
                break;
            case Green:
                originalArray = Helper.IntToDouble(originalGreen);
                modifiedArray = Helper.IntToDouble(modifiedGreen);
                break;
            case RGB:
                double[][] originalRedArray = Helper.IntToDouble(originalRed);
                double[][] originalBlueArray = Helper.IntToDouble(originalBlue);
                double[][] originalGreenArray = Helper.IntToDouble(originalGreen);

                double[][] modifiedRedArray = Helper.IntToDouble(modifiedRed);
                double[][] modifiedBlueArray = Helper.IntToDouble(modifiedBlue);
                double[][] modifiedGreenArray = Helper.IntToDouble(modifiedGreen);

                double mseRed = Quality.countMSE(originalRedArray, modifiedRedArray);
                double mseBlue = Quality.countMSE(originalBlueArray, modifiedBlueArray);
                double mseGreen = Quality.countMSE(originalGreenArray, modifiedGreenArray);

                mse = (mseRed + mseBlue + mseGreen) / 3;

                double maeRed = Quality.countMAE(originalRedArray, modifiedRedArray);
                double maeBlue = Quality.countMAE(originalBlueArray, modifiedBlueArray);
                double maeGreen = Quality.countMAE(originalGreenArray, modifiedGreenArray);

                mae = (maeRed + maeBlue + maeGreen) / 3;

                double saeRed = Quality.countSAE(originalRedArray, modifiedRedArray);
                double saeBlue = Quality.countSAE(originalBlueArray, modifiedBlueArray);
                double saeGreen = Quality.countSAE(originalGreenArray, modifiedGreenArray);

                sae = (saeRed + saeBlue + saeGreen) / 3;
                psnr = Quality.countPSNRforRGB(mseRed, mseBlue, saeBlue);
                return;
            case YCbCr:
                double mseY = Quality.countMSE(originalY.getArray(), modifiedY.getArray());
                double maeY = Quality.countMAE(originalY.getArray(), modifiedY.getArray());
                double saeY = Quality.countSAE(originalY.getArray(), modifiedY.getArray());

                double mseCb = Quality.countMSE(originalCb.getArray(), modifiedCb.getArray());
                double maeCb = Quality.countMAE(originalCb.getArray(), modifiedCb.getArray());
                double saeCb = Quality.countSAE(originalCb.getArray(), modifiedCb.getArray());

                double mseCr = Quality.countMSE(originalCr.getArray(), modifiedCr.getArray());
                double maeCr = Quality.countMAE(originalCr.getArray(), modifiedCr.getArray());
                double saeCr = Quality.countSAE(originalCr.getArray(), modifiedCr.getArray());

                totalMSE = (mseY + mseCb + mseCr) / 3; //averaga
                totalMAE = (maeY + maeCb + maeCr) / 3;
                totalSAE = (saeY + saeCb + saeCr) / 3;
                mse = totalMSE;
                mae = totalMAE;
                sae = totalSAE;
                psnr = Math.abs(Quality.countPSNR(mse));
                return;
            default:
                return;
        }

        mse = Quality.countMSE(originalArray, modifiedArray);
        mae = Quality.countMAE(originalArray, modifiedArray);
        sae = Quality.countSAE(originalArray, modifiedArray);
        psnr = Math.abs(Quality.countPSNR(mse));

    }
    public void count(YCbCrType yCbCrType) {

        switch (yCbCrType) {
            case Y:
                ssim = Quality.countSSIM(originalY.getArray(), modifiedY.getArray());
                mssim = Quality.countMSSIM(originalY.getArray(), modifiedY.getArray());
                System.out.println("SSIM (Y): " + ssim);
                System.out.println("MSSIM (Y): " + mssim);
                break;
            case Cb:
                ssim = Quality.countSSIM(originalCb.getArray(), modifiedCb.getArray());
                mssim = Quality.countMSSIM(originalCb.getArray(), modifiedCb.getArray());
                System.out.println("SSIM (Cb): " + ssim);
                System.out.println("MSSIM (Cb): " + mssim);
                break;
            case Cr:
                ssim = Quality.countSSIM(originalCr.getArray(), modifiedCr.getArray());
                mssim = Quality.countMSSIM(originalCr.getArray(), modifiedCr.getArray());
                System.out.println("SSIM (Cr): " + ssim);
                System.out.println("MSSIM (Cr): " + mssim);
                break;
            default:
                System.out.println("Unknown YCbCrType");
                break;
        }
        //ssim = Quality.countSSIM();
    }

    public BufferedImage showOrigY()
    {
        return  showOneColorFromYCbCr(originalY);
    }
    public BufferedImage showModifY()
    {
        return  showOneColorFromYCbCr(modifiedY);
    }
    public BufferedImage showOrigCb()
    {
        return  showOneColorFromYCbCr(originalCb);
    }
    public BufferedImage showModifCb()
    {
        return  showOneColorFromYCbCr(modifiedCb);
    }
    public BufferedImage showOrigCr()
    {
        return  showOneColorFromYCbCr(originalCr);
    }
    public BufferedImage showModifCr()
    {
        return  showOneColorFromYCbCr(modifiedCr);
    }
    public BufferedImage showOrigRed()
    {
        return showOneColorImageFromRGB(originalRed,  RED);
    }
    public BufferedImage showOrigGreen()
    {
        return showOneColorImageFromRGB(originalGreen, GREEN);
    }
    public BufferedImage showOrigBlue()
    {
        return showOneColorImageFromRGB(originalBlue, BLUE);
    }


    public BufferedImage showModifRed()
    {
        return showOneColorImageFromRGB(modifiedRed,  RED);
    }
    public BufferedImage showModifGreen()
    {
        return showOneColorImageFromRGB(modifiedGreen, GREEN);
    }
    public BufferedImage showModifBlue()
    {
        return showOneColorImageFromRGB(modifiedBlue, BLUE);
    }
    




}




