package Project;

import java.io.IOException;

import Project.model.Layer;
import Project.controller.DiagramController;
import Project.controller.OverViewController;
import Project.controller.WallDetailsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

    /* Constants for pressure compute */
    private static final double c1 = 17.269;
    private static final double c2 = 237.3;
    private static final double c3 = 21.875;
    private static final double c4 = 265.5;

    private Stage primaryStage;
    private BorderPane rootLayout;
    public int nrLayers;
    public double ti,te;
    public double ui,ue;
    public double pi,pe;
    public double pim,pem;
    public double rvt,rt;
    public Layer [] layers = new Layer [50];
    public LineChart<Number,Number> lineChart;
    public double [] t= new double [50];
    public double [] p= new double [50];
    public double [] ps = new double [50];


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("ProjectApp");

        /* Set the application icon. */
        this.primaryStage.getIcons().add(new Image("file:resources/icons8-einstein-80.png"));
        initRootLayout();
        showOverview();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            /* Load root layout from fxml file. */
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            /* Show the scene containing the root layout. */
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the OverView inside the root layout.
     * The user must complete the TextFields with data. If he clicks "Adauga Date",
     * the information is saved and showWallDetails() is called.
     */
    public void showOverview() {
        try {
            /* Load OverView from fxml file. */
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/OverView.fxml"));
            AnchorPane OverView = (AnchorPane) loader.load();

            /* Set OverView into the center of root layout. */
            rootLayout.setCenter(OverView);

            /* Give the controller access to the main app. */
            OverViewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the WallDetails inside the root layout.
     * The user must complete the details for each layer of the wall.
     * If he clicks "Formeaza Diagrama", the information is saved and showCreatedDiagram() is called.
     */
    public void showWallDetails(){
        try {
            /* Load WallDetails from fxml file. */
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/WallDetails.fxml"));
            AnchorPane WallDetails = (AnchorPane) loader.load();

            /* Set WallDetails into the center of root layout. */
            rootLayout.setCenter(WallDetails);

            /* Give the controller access to the main app. */
            WallDetailsController controller = loader.getController();
            controller.setMainApp(this);
            controller.update(nrLayers);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Computes pressures and shows diagram.
     */
    public void showCreatedDiagram(){
        /* Computing pressures */
        String badLayers = "";
        double aux = ti > 0 ? (c1 * ti / (c2 + ti)) : (c3 * ti / (c4 + ti));
        pim = Math.exp(aux) * 610.5;
        aux = te > 0 ? (c1 * te / (c2 + te)) : (c3 * te /(c4 + te));
        pem = Math.exp(aux) * 610.5;

        pi = ui * pim / 100;
        pe = ue * pem / 100;
        rvt = 0;
        rt=0;
        for(int i = 0; i < nrLayers; i++){
            layers[i].compute();
            rvt = rvt + layers[i].getResistanceSteam();
            rt= rt + layers[i].getThermalResistance();
        }

        double rvs = 0;
        double rts = 0;
        for(int i = 0; i < nrLayers - 1; i++){
            rvs = rvs + layers[i].getResistanceSteam();
            rts= rts + layers[i].getThermalResistance();
            p[i] = pi - (rvs / rvt) * (pi - pe);
            t[i] = ti - (rts / rt) * (ti - te);
            aux = t[i] > 0 ? (c1 * t[i] / (c2 + t[i])) : (c3 * t[i] /(c4 + t[i]));
            ps[i] = Math.exp(aux) * 610.5;
            if(ps[i] < p[i]){
                badLayers += i + 1 + ", ";
            }
        }

        double [] dcm = new double[50];
        double dcmt = 0;
        for(int i = 0; i < nrLayers; i++){
            dcm[i] = layers[i].getThickness() * 100;
            dcmt = dcmt + dcm[i];
        }

        /* Defining the axes */
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Grosime(cm)");
        yAxis.setLabel("Presiune(Pa)");

        /* Creating the chart */
        lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setTitle("Diagrama");

        /* Complete the charts with information */

        /* Defining pressures */
        XYChart.Series pressure = new XYChart.Series();
        pressure.setName("Presiune");
        XYChart.Series saturationPressure = new XYChart.Series();
        saturationPressure.setName("Presiune de saturatie");

        /* Defining walls */
        XYChart.Series [] walls= new XYChart.Series[100];

        /* Adding data */
        pressure.getData().add(new XYChart.Data(0, pi));
        pressure.getData().add(new XYChart.Data(20, pi));
        saturationPressure.getData().add(new XYChart.Data(0, pim));
        saturationPressure.getData().add(new XYChart.Data(20, pim));
        double xt = 20;
        for(int i = 0; i < nrLayers - 1; i++){
            xt = xt + dcm[i];
            pressure.getData().add(new XYChart.Data(xt, p[i]));
            saturationPressure.getData().add(new XYChart.Data(xt, ps[i]));
        }
        pressure.getData().add(new XYChart.Data(xt + dcm[nrLayers - 1], pe));
        pressure.getData().add(new XYChart.Data(xt + dcm[nrLayers - 1] + 10, pe));
        saturationPressure.getData().add(new XYChart.Data(xt + dcm[nrLayers - 1], pem));
        saturationPressure.getData().add(new XYChart.Data(xt + dcm[nrLayers - 1] + 10, pem));
        xt = 20;
        for(int i = 0; i < nrLayers - 1; i++){
            walls[i] = new XYChart.Series();
            walls[i].getData().add(new XYChart.Data(xt + dcm[i],0));
            walls[i].getData().add(new XYChart.Data(xt + dcm[i],pim+200));
            walls[i].setName("S" + (i + 1));
            xt = xt + dcm[i];
            lineChart.getData().add(walls[i]);
        }
        /* This is not valid information,
         * just a wall contour for a better visualization */

        XYChart.Series contour = new XYChart.Series();
        contour.getData().add(new XYChart.Data(20,0));
        contour.getData().add(new XYChart.Data(20,pim+200));
        contour.getData().add(new XYChart.Data(dcmt+20,pim+200));
        contour.getData().add(new XYChart.Data(dcmt+20,0));
        contour.setName("Conturul Peretelui");
        lineChart.getData().add(contour);
        lineChart.getData().add(pressure);
        lineChart.getData().add(saturationPressure);

        try {
            /* Load the fxml file and create a new stage for the popup diagram */
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Diagram.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            /* Create the diagram Stage. */
            Stage diagramStage = new Stage();
            diagramStage.setTitle("Diagram");
            diagramStage.getIcons().add(new Image("file:resources/icons8-combo-chart-64.png"));
            diagramStage.initOwner(primaryStage);
            diagramStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page,800,600);
            diagramStage.setScene(scene);

            /* Set the lineChart into the controller. */
            DiagramController controller = loader.getController();
            controller.setDiagramStage(diagramStage);
            controller.update(lineChart,badLayers);
            controller.setMainApp(this);

            /* Show the diagram and wait until the user closes it */
            diagramStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}