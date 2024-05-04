package jpeg;

import Jama.Matrix;
import core.FileBindings;
import enums.YCbCrType;
import graphics.Dialogs;

import java.awt.*;
import java.awt.image.BufferedImage;


public class LSBWatermark {

    private BufferedImage watermarkImage;
    private Matrix lennaY, lennaCb, lennaCr, markedMatrix, watermark, recoveredWatermark;
    private static BufferedImage marked;

    public static BufferedImage getMarked() {
        return marked;
    }

    public static void setMarked(BufferedImage newmarked) {
        marked = newmarked;
    }

    public static BufferedImage getUnmarked() {
        return unmarked;
    }

    public static void setUnmarked(BufferedImage newunmarked) {
        unmarked = newunmarked;
    }

    private static BufferedImage unmarked;
    private Process process;
    public LSBWatermark(){
        process = new Process(FileBindings.defaultImage);

        watermarkImage = Dialogs.loadImageFromPath(FileBindings.waterMark);
        lennaY = process.originalY;
        lennaCb = process.originalCb;
        lennaCr = process.originalCr;
        setWatermarkMatrix();

    }

    public void insertWatermark(YCbCrType type, int bitdepth){
        switch(type){
            case Y:
                insertWatermark(bitdepth, lennaY);
                break;
            case Cb:
                insertWatermark(bitdepth, lennaCb);
                break;
            case Cr:
                insertWatermark(bitdepth, lennaCr);
                break;
            default:
                break;
        }
    }

    public void extractWatermark(YCbCrType type, int bitdepth){
        switch(type){
            case Y:
                extractWatermark(bitdepth, lennaY);
                break;
            case Cb:
                extractWatermark(bitdepth, lennaCb);
                break;
            case Cr:
                extractWatermark(bitdepth, lennaCr);
                break;
            default:
                break;
        }
    }

    private void insertWatermark(int bitdepth, Matrix input){
        markedMatrix = new Matrix(input.getRowDimension(), input.getColumnDimension());
        for(int i = 0; i < input.getRowDimension(); i++){
            for(int j = 0; j < input.getColumnDimension(); j++){
                int bitnum = (int) (Math.pow(2,bitdepth)-1);
                int lenna = Math.toIntExact(Math.round(input.get(i,j)));
                int watermrk = Math.toIntExact(Math.round(watermark.get(i,j)));
                lenna &=~ bitnum; //logicky AND
                watermrk &= bitnum; //logicky


                markedMatrix.set(i,j, lenna+watermrk);
            }
        }

    }

    private void extractWatermark(int bitdepth, Matrix input){
        recoveredWatermark = new Matrix(input.getRowDimension(), input.getColumnDimension());
        for(int i = 0; i < input.getRowDimension(); i++){
            for(int j = 0; j < input.getColumnDimension(); j++){
                int bitnum = (int) (Math.pow(2,bitdepth)-1);

                Color color = new Color(marked.getRGB(j,i));
                double Y = color.getRed() & color.getBlue() & color.getGreen();
                int watermrk = Math.toIntExact(Math.round(Y));
                watermrk &= bitnum;
                int posun = Math.toIntExact(Math.round(Math.log(Integer.MAX_VALUE) / Math.log(2)-bitnum));
                watermrk = watermrk << posun;

                recoveredWatermark.set(i,j, watermrk);
            }
        }
    }

    private void imageFromMatrix(){
        int imageWidth = process.imageWidth;
        int imageHeight = process.imageHeight;
        BufferedImage bfImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                int clr;

                clr = restrictRange(markedMatrix.get(h,w));
                bfImage.setRGB(w, h, (new Color(clr, clr, clr).getRGB()));
            }
        }
        marked = bfImage;
    }

    private void unmarkImageFromMatrix(){
        int imageWidth = process.imageWidth;
        int imageHeight = process.imageHeight;
        BufferedImage bfImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                int clr;

                clr = restrictRange(recoveredWatermark.get(h,w));
                bfImage.setRGB(w, h, (new Color(clr, clr, clr).getRGB()));
            }
        }
        unmarked = bfImage;
    }
    private void setWatermarkMatrix() {
        int imageWidth = process.imageWidth;
        int imageHeight = process.imageHeight;
        watermark = new Matrix(imageHeight, imageWidth);
        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                Color color = new Color(watermarkImage.getRGB(w,h));
                int val = color.getRed() & color.getBlue() & color.getGreen();
                watermark.set(h,w,val);
            }
        }
    }

    public void showMarkedImage(){
        imageFromMatrix();
        Dialogs.showImageInWindow(marked, "Watermarked image");

    }
    public void showUnMarkedImage(){
        unmarkImageFromMatrix();
        Dialogs.showImageInWindow(unmarked, "Watermarked image");
    }
    public static int restrictRange(double i) {
        if (i < 0) return 0;
        if (i > 255) return 255;
        else return (int) (i);
    }
}
