package Project.controller;

import Project.model.Layer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import Project.MainApp;

import java.io.IOException;

public class OverViewController {
    @FXML
    private TextField nrLayers;
    @FXML
    private TextField temperatureInt;
    @FXML
    private TextField humidityInt;
    @FXML
    private TextField temperatureExt;
    @FXML
    private TextField humidityExt;

    /* Reference to the main application. */
    private MainApp mainApp;

    /**
     * The constructor initializes an OverViewController object.
     * Is called before the initialize() method.
     */
    public OverViewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    }

    /**
     * Called when the user clicks "Adauga date".
     * Saves the input(if it is correct) from the user.
     */
    @FXML
    private void handleAddInfo() throws IOException {
        if(isInputValid()) {
            mainApp.nrLayers = Integer.parseInt(nrLayers.getText());
            mainApp.ti = Integer.parseInt(temperatureInt.getText());
            mainApp.te = Integer.parseInt(temperatureExt.getText());
            mainApp.ui = Integer.parseInt(humidityInt.getText());
            mainApp.ue = Integer.parseInt(humidityExt.getText());
            for(int i = 0; i < mainApp.nrLayers; i++){
                mainApp.layers[i] = new Layer();
            }
            mainApp.showWallDetails();
        }
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";
        String verifiedText = nrLayers.getText();
        if (verifiedText == null || verifiedText.length() == 0) {
            errorMessage += "Numarul de straturi este necompletat!\n";
        } else {
            /* Try to parse the text into an int. */
            try {
                Integer.parseInt(verifiedText);
                if(Integer.parseInt(verifiedText) < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                errorMessage += "Numarul de straturi este gresit(doar numere naturale)!\n";
            }
        }

        verifiedText=temperatureInt.getText();
        if (verifiedText == null || verifiedText.length() == 0) {
            errorMessage += "Temperatura interioara este necompletata!\n";
        } else {
            /* Try to parse the text into a double. */
            try {
                Double.parseDouble(verifiedText);
            } catch (NumberFormatException e) {
                errorMessage += "Temperatura interioara este gresita(doar numere reale)!\n";
            }
        }

        verifiedText=humidityInt.getText();
        if (verifiedText == null || verifiedText.length() == 0) {
            errorMessage += "Umiditate interioara este necompletata!\n";
        } else {
            /* Try to parse the text into a double. */
            try {
                Double.parseDouble(verifiedText);
                if(Double.parseDouble(verifiedText) < 0 || Double.parseDouble(verifiedText) > 100){
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                errorMessage += "Umiditate interioara este gresita(doar numere intre [0-100])!\n";
            }
        }

        verifiedText=temperatureExt.getText();
        if (verifiedText == null || verifiedText.length() == 0) {
            errorMessage += "Temperatura exterioara este necompletata!\n";
        }  else {
            /* Try to parse the text into an int. */
            try {
                Double.parseDouble(verifiedText);
            } catch (NumberFormatException e) {
                errorMessage += "Temperatura exterioara este gresita(doar numere reale)!\n";
            }
        }

        verifiedText=humidityExt.getText();
        if (verifiedText == null || verifiedText.length() == 0) {
            errorMessage += "Umiditate exterioara este necompletata!\n";
        } else {
            /* Try to parse the text into a double. */
            try {
                Double.parseDouble(verifiedText);
                if(Double.parseDouble(verifiedText) < 0 || Double.parseDouble(verifiedText) > 100){
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                errorMessage += "Umiditate exterioara este gresita(doar numere intre [0-100])!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            /* Show the error message. */
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Campuri invalide");
            alert.setHeaderText("Corectati campurile invalide");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    /**
     * Is called by the main application to give a reference to itself.
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
