package jpeg;

import Jama.Matrix;
import core.Helper;
import enums.ColorType;
import enums.SamplingType;
import graphics.Dialogs;

import java.awt.*;
import java.awt.image.BufferedImage;

import static enums.ColorType.*;


public class Process {

    private BufferedImage originalImage;
    private int imageHeight;
    private int imageWidth;

    private int[][] originalRed, modifiedRed;
    private int[][] originalGreen, modifiedGreen;
    private int[][] originalBlue, modifiedBlue;

    private Matrix originalY, modifiedY;
    private Matrix originalCb, modifiedCb;
    private Matrix originalCr, modifiedCr;

    private Quality quality;

    public Process(String path) {

        this.originalImage = Dialogs.loadImageFromPath(path);

        imageWidth = originalImage.getWidth();
        imageHeight = originalImage.getHeight();

        originalRed = new int[imageHeight][imageWidth];
        originalGreen = new int[imageHeight][imageWidth];
        originalBlue = new int[imageHeight][imageWidth];

        originalY = new Matrix(imageHeight, imageWidth);
        originalCb = new Matrix(imageHeight, imageWidth);
        originalCr = new Matrix(imageHeight, imageWidth);
        setOriginalRGB();
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
        int[][][] temp = ColorTransform.convertModifiedYcBcRtoRGB(originalY, originalCb, originalCr);
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
                        (new Color((int)color.get(h,w),
                                (int)color.get(h,w),
                                (int)color.get(h,w)).getRGB()));
            }
        }
        return bfImage;
    }

    public void sampleDown (SamplingType samplingType) {
        modifiedCb = Sampling.sampleDown(modifiedCb, samplingType);
        modifiedCr = Sampling.sampleDown(modifiedCr, samplingType);
    }

    public void sampleUp (SamplingType samplingType) {
        modifiedCb = Sampling.sampleUp(modifiedCb, samplingType);
        modifiedCr = Sampling.sampleUp(modifiedCr, samplingType);
    }

    public double calcMSE() {
        quality = new Quality();
        double a = quality.countMSE(originalRed, modifiedRed);
        double b = quality.countMSE(originalGreen, modifiedGreen);
        double c = quality.countMSE(originalBlue, modifiedBlue);
        return (Math.round(100 * (a + b + c) / 3.0) / 100.0);
    }

    public double calcMAE() {

        quality = new Quality();
        double a = quality.countMAE(originalRed, modifiedRed);
        double b = quality.countMAE(originalGreen, modifiedGreen);
        double c = quality.countMAE(originalBlue, modifiedBlue);
        return (Math.round(100 * (a + b + c) / 3.0) / 100.0);
    }

    public double calcSAE() {
        quality = new Quality();
        double a = quality.countSAE(originalRed, modifiedRed);
        double b = quality.countSAE(originalGreen, modifiedGreen);
        double c = quality.countSAE(originalBlue, modifiedBlue);
        return (Math.round(100 * (a + b + c) / 3.0) / 100.0);
    }

    public double calcPSNR() {
        double mse = calcMSE();
        return quality.countPSNR(mse);
    }

    public double PSNRforRGBcount() {
        Quality quality = new Quality();
        double mseRed = quality.countMSE(originalRed, modifiedRed);
        double mseGreen = quality.countMSE(originalGreen, modifiedGreen);
        double mseBlue = quality.countMSE(originalBlue, modifiedBlue);
        double psnrRGB = quality.countPSNRforRGB(mseRed, mseGreen, mseBlue);
        return (psnrRGB);
    }



    public BufferedImage showOrigBlue()
    {
        return showOneColorImageFromRGB(originalBlue, BLUE);
    }
    public BufferedImage showOrigGreen()
    {
        return showOneColorImageFromRGB(originalGreen, GREEN);
    }
    public BufferedImage showOrigRed()
    {
        return showOneColorImageFromRGB(originalRed,  RED);
    }
    public BufferedImage showModifBlue()
    {
        return showOneColorImageFromRGB(modifiedBlue, BLUE);
    }
    public BufferedImage showModifGreen()
    {
        return showOneColorImageFromRGB(modifiedGreen, GREEN);
    }
    public BufferedImage showModifRed()
    {
        return showOneColorImageFromRGB(modifiedRed,  RED);
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

}




