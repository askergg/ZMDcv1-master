package graphics;

import com.sun.javafx.binding.DoubleConstant;
import core.FileBindings;
import enums.QualityType;
import enums.SamplingType;
import enums.TransformType;
import enums.YCbCrType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import jpeg.Process;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    Process process = new Process(FileBindings.defaultImage);


    @FXML
    private TextField mae;

    @FXML
    private TextField mse;

    @FXML
    private TextField mssim;
    @FXML
    private TextField psnr;

    @FXML
    private TextField sae;
    @FXML
    private ComboBox<QualityType> RGBQuality;

    @FXML
    private ComboBox<YCbCrType> SSIMQuality;

    @FXML
    private Button btoqcount1;
    @FXML
    private TextField ssim;
    @FXML
    private Button btdecodeYR;

    @FXML
    private Button btdecodeitransform;

    @FXML
    private Button btdecodeoversample;

    @FXML
    private Button btdecodequantize;

    @FXML
    private Button btencodeRY;

    @FXML
    private Button btencodedownsample;

    @FXML
    private Button btencodequantize;

    @FXML
    private Button btencodetransform;

    @FXML
    private Button btmodifiedCb;

    @FXML
    private Button btmodifiedblue;

    @FXML
    private Button btmodifiedcr;

    @FXML
    private Button btmodifiedgreen;

    @FXML
    private Button btmodifiedred;

    @FXML
    private Button btmodifiedrgb;

    @FXML
    private Button btmodifiedy;

    @FXML
    private Button btoqcount;

    @FXML
    private Button btorigY;

    @FXML
    private Button btorigblue;

    @FXML
    private Button btoriggreen;

    @FXML
    private Button btoriginalshowimage;

    @FXML
    private Button btorigred;

    @FXML
    private Button btoringCb;

    @FXML
    private Button btoringCr;

    @FXML
    private ComboBox<SamplingType> dropencodesampling;

    @FXML
    private ComboBox<TransformType> dropencodetransform;

    @FXML
    private Spinner<?> encodecounter;

    @FXML
    private TextField encodeslidershow;

    @FXML
    private CheckBox encodesteps;

    @FXML
    private CheckBox origshades;

    @FXML
    private CheckBox origshades1;

    @FXML
    private Slider sliderencode;


    @FXML
    void decodequantize(ActionEvent event) {

    }
    @FXML
    void count(ActionEvent event) {

        process.count(RGBQuality.getValue());
        mse.textProperty().set(String.format("%.4f", process.mse));
        mae.textProperty().set(String.format("%.4f", process.mae));
        sae.textProperty().set(String.format("%.4f", process.sae));
        psnr.textProperty().set(String.format("%.4f", process.psnr));
        process.count(SSIMQuality.getValue());
        ssim.textProperty().set(String.format("%.4f", process.ssim));
        mssim.textProperty().set(String.format("%.4f", process.mssim));
    }

    @FXML
    void downsample(ActionEvent event) {
        try {
            process.sampleDown(dropencodesampling.getValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void encodequantize(ActionEvent event) {

    }

    @FXML
    void itransform(ActionEvent event) {

    }

    @FXML
    void modblue(ActionEvent event) {

        try {
            Dialogs.showImageInWindow(process.showModifBlue(), "MB", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void modcb(ActionEvent event) {

        try {
            Dialogs.showImageInWindow(process.showModifCb(), "MCb", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void modcr(ActionEvent event) {

        try {
            Dialogs.showImageInWindow(process.showModifCr(), "MCr", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void modgreen(ActionEvent event) {

        try {
            Dialogs.showImageInWindow(process.showModifGreen(), "MG", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void modreg(ActionEvent event) {

        try {
            Dialogs.showImageInWindow(process.showModifRed(), "MR", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void mody(ActionEvent event) {

        try {
            Dialogs.showImageInWindow(process.showModifY(), "MY", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void origblue(ActionEvent event) {

        try {
            Dialogs.showImageInWindow(process.showOrigBlue(), "OB", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void origcb(ActionEvent event) {

        try {
            Dialogs.showImageInWindow(process.showOrigCb(), "OCb", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void origcr(ActionEvent event) {

        try {
            Dialogs.showImageInWindow(process.showOrigCr(), "OCr", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void origgreen(ActionEvent event) {
        try {
            Dialogs.showImageInWindow(process.showOrigGreen(), "OG", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void originaly(ActionEvent event) {

        try {
            Dialogs.showImageInWindow(process.showOrigY(), "OY", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void origred(ActionEvent event) throws IOException {
        try {
            Dialogs.showImageInWindow(process.showOrigRed(), "OR", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void oversample(ActionEvent event) {
        try {
            process.sampleUp(dropencodesampling.getValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void rgb(ActionEvent event) {

        try {
            Dialogs.showImageInWindow(process.getImageFromRGB(), "Modified RGB", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void rgbtoycbcr(ActionEvent event) {
        try {
            process.convertToYCbCr();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void showimage(ActionEvent event) throws IOException {

        File file = new File(FileBindings.defaultImage);
        Dialogs.showImageInWindow(ImageIO.read(file), "Original", true);
    }

    @FXML
    void transform(ActionEvent event) {

    }

    @FXML
    void ycbcrtorgb(ActionEvent event) {

        try {
            process.convertToRGB();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void origshades(ActionEvent event) {

    }

    @FXML
    void modifiedshades(ActionEvent event) {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dropencodesampling.getItems().addAll(SamplingType.values());
        dropencodetransform.getItems().addAll(TransformType.values());
        RGBQuality.getItems().addAll(QualityType.values());
        SSIMQuality.getItems().addAll(YCbCrType.Y, YCbCrType.Cb, YCbCrType.Cr);



        dropencodesampling.getSelectionModel().select(SamplingType.S_4_4_4);
        dropencodetransform.getSelectionModel().select(TransformType.DCT);
        RGBQuality.getSelectionModel().select(QualityType.RGB);
        SSIMQuality.getSelectionModel().select(YCbCrType.Y);
    }

}
