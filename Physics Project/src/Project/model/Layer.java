package Project.model;

public class Layer {
    /*
     * Lambda and niu are material constants
     * rv is the resistance to steam permeability
     * r is thermal resistance
     */
    private double niu;
    private double lambda;
    private double thickness;
    private double resistanceSteam;
    private double thermalResistance;

    public Layer()
    {

    }

    /**
     * Computes resistance to steam permeability and thermal resistance
     */
    public void compute(){
        resistanceSteam =thickness*niu;
        thermalResistance =thickness/lambda;
    }

    /* Getters and setters */

    public double getNiu() {
        return niu;
    }

    public void setNiu(double niu) {
        this.niu = niu;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public double getResistanceSteam() {
        return resistanceSteam;
    }

    public void setResistanceSteam(double resistanceSteam) {
        this.resistanceSteam = resistanceSteam;
    }

    public double getThermalResistance() {
        return thermalResistance;
    }

    public void setThermalResistance(double thermalResistance) {
        this.thermalResistance = thermalResistance;
    }
}
