package Project.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import Project.MainApp;

import java.io.*;

public class WallDetailsController {
    @FXML
    private ChoiceBox indexWall;
    @FXML
    private ChoiceBox materialType;
    @FXML
    private TextField thickness;

    /* Reference to the main application. */
    private MainApp mainApp;

    private int nrLayers;

    /**
     * The constructor initializes an WallDetailsController object.
     * Is called before the initialize() method.
     */
    public WallDetailsController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() throws IOException {
        /* Adding options for choice boxes */
        materialType.getItems().add("");
        materialType.getItems().add("Beton Armat");
        materialType.getItems().add("Zidarie BCA");
        materialType.getItems().add("Zidarie cpp.");
        materialType.getItems().add("Mortar");
        materialType.getItems().add("Polistiren");
        materialType.getItems().add("Vata minerala");
        materialType.getItems().add("Gips/Carton");
        materialType.getItems().add("Folie polietilena");
    }

    /**
     * Called when the user clicks "Adauga date".
     * Saves the input(if it is correct) from the user.
     */
    @FXML
    private void handleAddInfo() throws IOException {
        if(isInputValid()){
            int i=Integer.parseInt(indexWall.getSelectionModel().getSelectedItem().toString())-1;
            String s = materialType.getSelectionModel().getSelectedItem().toString();
            mainApp.layers[i].setThickness(Double.parseDouble(thickness.getText()));
            switch (s) {
                case "Beton Armat": {
                    mainApp.layers[i].setNiu(21.3);
                    mainApp.layers[i].setLambda(1.74);
                    break;
                }
                case "Zidarie BCA": {
                    mainApp.layers[i].setNiu(2.1);
                    mainApp.layers[i].setLambda(0.3);
                    break;
                }
                case "Vata minerala": {
                    mainApp.layers[i].setNiu(2.1);
                    mainApp.layers[i].setLambda(0.04);
                    break;
                }
                case "Zidarie cpp.": {
                    mainApp.layers[i].setNiu(6.1);
                    mainApp.layers[i].setLambda(0.8);
                    break;
                }
                case "Gips/Carton": {
                    mainApp.layers[i].setNiu(6.1);
                    mainApp.layers[i].setLambda(2.1);
                    break;
                }
                case "Mortar": {
                    mainApp.layers[i].setNiu(7.1);
                    mainApp.layers[i].setLambda(0.93);
                    break;
                }
                case "Polistiren": {
                    mainApp.layers[i].setNiu(30);
                    mainApp.layers[i].setLambda(0.04);
                    break;
                }
                case "Folie polietilena": {
                    mainApp.layers[i].setNiu(50000);
                    mainApp.layers[i].setLambda(0.04);
                    break;
                }
                default: {
                    break;
                }
            }
        }
        thickness.setText("");
        indexWall.getSelectionModel().selectNext();
        materialType.getSelectionModel().selectFirst();
    }

    /**
     * Validates the user input in the fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";
        String toVerifyText;
        if (indexWall.getSelectionModel().getSelectedItem() == null ) {
            errorMessage += "Numarul stratului este neselectat!\n";
        }

        if (materialType.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Materialul stratului este neselectat\n";
        }

        toVerifyText=thickness.getText();
        if (toVerifyText == null || toVerifyText.length() == 0) {
            errorMessage += "Grosimea este necompletata!\n";
        } else {
            /* Try to parse the text into a double. */
            try {
                Double.parseDouble(toVerifyText);
                if(Double.parseDouble(toVerifyText) < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                errorMessage += "Grosimea gresita(doar numere reale pozitive)!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
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
     * Called when the user clicks "Formeaza diagrama"
     * Calls a method, which computes pressures and shows diagram
     */
    @FXML
    private void handleCreatingDiagram(){
        if(isInputComplete()) {
            mainApp.showCreatedDiagram();
        }
    }

    /**
     * Validates if the user input is complete
     *
     * @return true if the input is valid
     */
    private boolean isInputComplete(){
        boolean isComplete = true;
        String errorMessage="";
        for(int i = 0; i < mainApp.nrLayers; i++){
            if(mainApp.layers[i].getNiu() == 0 || mainApp.layers[i].getThickness() == 0){
                isComplete = false;
                errorMessage += (i + 1) + ", ";
            }
        }

        if(isComplete){
            return true;
        } else {
            /* Show the error message. */
            errorMessage = errorMessage.substring( 0, errorMessage.length() - 2);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Straturi incomplete");
            alert.setHeaderText("Cormpletati straturile incomplete" );
            if(errorMessage.length()==1){
                alert.setContentText("Stratul " + errorMessage + " nu contine toate informatiile necesare");
            } else{
                alert.setContentText("Straturile " + errorMessage + " nu contin toate informatiile necesare.");
            }
            alert.showAndWait();

            return false;
        }
    }

    /**
     * Called when the user clicks "Inapoi".
     * Calls a method, which computes pressures and shows diagram.
     */
    @FXML
    private void handleBack(){
        mainApp.showOverview();
    }

    /**
     * Updates data, in order to display valid information for the user.
     * Sets the number of layers.
     *
     * @param nrLayers
     */
    public void update(int nrLayers)
    {
        this.nrLayers =nrLayers;
        for(int i = 0; i < nrLayers; i++){
            indexWall.getItems().add(i + 1);
        }
    }

    /**
     * Is called by the main application to give a reference to itself.
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
