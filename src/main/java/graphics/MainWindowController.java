package graphics;

import com.sun.javafx.binding.DoubleConstant;
import core.FileBindings;
import core.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import enums.QualityType;
import enums.SamplingType;
import enums.TransformType;
import enums.YCbCrType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import jpeg.Attacks;
import jpeg.LSBWatermark;
import jpeg.Process;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    Process process = new Process(FileBindings.defaultImage);
    public static LSBWatermark lsbwatermark = new LSBWatermark();

    @FXML
    public ComboBox<YCbCrType> watermarkPart;

    @FXML
    private RadioButton rotate90;

    @FXML
    private RadioButton rotate45;
    private ToggleGroup rotation;

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
    private Spinner<Integer> encodecounter;

    @FXML
    private CheckBox encodesteps;

    @FXML
    private CheckBox origshades;

    @FXML
    private CheckBox origshades1;

    @FXML
    private Slider sliderencode;

    @FXML
    private TextField encodeslidershow;

    @FXML
    private Spinner<Integer> watermarkBitdepth;


    @FXML
    void decodequantize(ActionEvent event) {
        process.quantizeImage(encodecounter.getValue(), sliderencode.getValue(), true);

    }

    @FXML
    void encodequantize(ActionEvent event) {
        process.quantizeImage(encodecounter.getValue(), sliderencode.getValue(), false);
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
    void itransform(ActionEvent event) {
        process.inverseTransform(dropencodetransform.getValue(),encodecounter.getValue());
    }

    @FXML
    void transform(ActionEvent event) {
        process.transform(dropencodetransform.getValue(), encodecounter.getValue());

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
    @FXML
    void insertLSBWatermark(ActionEvent event){
        //insertion of WM LSB
        lsbwatermark.insertWatermark(watermarkPart.getValue(), watermarkBitdepth.getValue());
        lsbwatermark.showMarkedImage();
    }

    @FXML
    void extractLSBWatermark(ActionEvent event){
        //extraction of WM LSB
        lsbwatermark.extractWatermark(watermarkPart.getValue(), watermarkBitdepth.getValue());
        lsbwatermark.showUnMarkedImage();
    }


    @FXML
    void attackCompress(ActionEvent event){
        Attacks.jpegCompression();
    }

    @FXML
    void attackMirror(ActionEvent event){
        Attacks.mirroring();
    }

    @FXML
    void attackRotate(ActionEvent event){
        RadioButton selected =  (RadioButton) rotation.getSelectedToggle();
        switch(selected.getText()){
            case "45°":
                Attacks.rotateImage45();
                break;
            case "90°":
                Attacks.rotateImage90();
                break;
            default:
                break;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rotation = new ToggleGroup();
        dropencodesampling.getItems().addAll(SamplingType.values());
        dropencodetransform.getItems().addAll(TransformType.values());
        RGBQuality.getItems().addAll(QualityType.values());
        SSIMQuality.getItems().addAll(YCbCrType.Y, YCbCrType.Cb, YCbCrType.Cr);
        //dropencodetransform.getItems().addAll(TransformType.values());
        ObservableList<Integer> blocks = FXCollections.observableArrayList(2, 4, 8, 16, 32, 64, 128, 256, 512);
        SpinnerValueFactory<Integer> spinnerValues = new SpinnerValueFactory.ListSpinnerValueFactory<>(blocks);
        spinnerValues.setValue(8);
        watermarkBitdepth.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 1));
        watermarkBitdepth.setEditable(true);
        encodeslidershow.textProperty().bindBidirectional(sliderencode.valueProperty(), NumberFormat.getIntegerInstance());
        watermarkPart.getItems().addAll(YCbCrType.values());
        watermarkPart.getSelectionModel().select(YCbCrType.Y);
        rotate90.setToggleGroup(rotation);
        rotate45.setToggleGroup(rotation);
        rotate90.setSelected(true);



        //encodeslidershow.setTextFormatter((new TextFormatter<>(Helper.NUMBER_FORMATER)));
        dropencodesampling.getSelectionModel().select(SamplingType.S_4_4_4);
        dropencodetransform.getSelectionModel().select(TransformType.DCT);
        RGBQuality.getSelectionModel().select(QualityType.RGB);
        SSIMQuality.getSelectionModel().select(YCbCrType.Y);
        dropencodetransform.getSelectionModel().select(0);
        encodecounter.setValueFactory(spinnerValues);
        //encodecounter.getSelectionModel().select(0);
    }

}

