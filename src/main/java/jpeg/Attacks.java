package jpeg;

import graphics.Dialogs;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class Attacks {
    public static int jpegQuality;

    public static void rotateImage(int degrees) {
        BufferedImage image = LSBWatermark.getMarked();
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, image.getType());

        Graphics2D graphics2D = newImage.createGraphics();
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(degrees), width / 2, height / 2);
        graphics2D.drawImage(image, transform, null);
        graphics2D.dispose();

        LSBWatermark.setMarked(newImage);
        Dialogs.showImageInWindow(newImage, "Rotated image");

    }

    public static void rotateImage45() {rotateImage(45);}

    public static void rotateImage90() {rotateImage(90);
    }

    public static void jpegCompression() {
        BufferedImage sourceImage = LSBWatermark.getMarked();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (ImageOutputStream imageOutput = ImageIO.createImageOutputStream(outputStream)) {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("JPEG").next();
            ImageWriteParam param = writer.getDefaultWriteParam();

            // Nastavení JPEG komprese kvality
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(jpegQuality / 100.0f); // Adjust compression quality dynamically

            writer.setOutput(imageOutput);
            writer.write(null, new javax.imageio.IIOImage(sourceImage, null, null), param);
            writer.dispose();

            // Výstup na vstupní proud pro čteni
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream);
            BufferedImage compressedImage = ImageIO.read(imageInputStream);

            // Aktualizace obrázku a nastavení jmena
            LSBWatermark.setMarked(compressedImage);
            Dialogs.showImageInWindow(compressedImage, "JPEG Compressed Image");

        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void mirroring(){
        BufferedImage image = LSBWatermark.getMarked();
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage mirroredImage = new BufferedImage(width, height, image.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mirroredImage.setRGB((width - 1 - x), y, image.getRGB(x, y)); // Zrcadlení podle osy X
            }
        }

        LSBWatermark.setMarked(mirroredImage);
        Dialogs.showImageInWindow(mirroredImage, "Mirrored");
    }

}
