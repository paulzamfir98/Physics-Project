package Project.controller;

import Project.MainApp;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class DiagramController {

    @FXML
    Pane pane;
    @FXML
    Text info;

    /* Reference to the main application. */
    private MainApp mainApp;

    private Stage diagramStage;

    /**
     * The constructor initializes an OverViewController object.
     * Is called before the initialize() method.
     */
    public DiagramController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    }

    /**
     * Saves the diagram with one of the available extensions.
     */
    @FXML
    public void handleSaveAsPng() {
        WritableImage image = diagramStage.getScene().snapshot(null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("GIF Files", "*.gif"));
        File file = fileChooser.showSaveDialog(null);
        if(file != null){
            String name = file.getName();
            String formatName= name.substring(1+name.lastIndexOf(".")).toLowerCase();
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), formatName, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates data, in order to display valid information for the user.
     *
     * @param lineChart
     * @param badLayers
     */
    public void update(LineChart<Number,Number> lineChart, String badLayers){
        lineChart.setMinSize(pane.getMinWidth(),pane.getMinHeight());
        pane.getChildren().clear();
        pane.getChildren().add(lineChart);
        if(badLayers.length()==0){
            info.setFill(Color.GREEN);
            info.setText("Peretele nu va produce mucegai, in conditiile date!");
        } else {
            info.setFill(Color.RED);
            badLayers=badLayers.substring(0, badLayers.length() - 2);
            if(badLayers.length()==3){
                info.setText("Peretele va produce mucegai in stratul " + badLayers + "!");
            }
            info.setText("Peretele va produe mucegai in straturile " + badLayers + "!");
        }
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param diagramStage
     */
    public void setDiagramStage(Stage diagramStage) {
        this.diagramStage = diagramStage;
    }

    /**
     * Is called by the main application to give a reference to itself.
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
