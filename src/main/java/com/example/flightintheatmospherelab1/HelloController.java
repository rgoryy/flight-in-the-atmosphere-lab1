package com.example.flightintheatmospherelab1;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Timer;
import java.util.TimerTask;

public class HelloController {
    @FXML
    private TextField heightTextField;

    final double g = 9.81;
    final double C = 0.15;
    final double rho = 1.29;

    double t;
    double x;
    double y;
    double a;
    double v0;
    double S;
    double m;
    double cosa;
    double sina;
    double vx;
    double vy;

    double k;

    double dt;

    double yMax;

    @FXML
    private TextField angleTextField;

    @FXML
    private TextField speedTextField;

    @FXML
    private TextField weightTextField;

    @FXML
    private TextField sizeTextField;

    @FXML
    private TextField dtTextField;

    @FXML
    private LineChart<Double, Double> mainLineChart;

    @FXML
    private TableView<ResultData> resultsTable;

    private Timer timer;
    ;

    @FXML
    protected void onLaunchButtonClick() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        resultsTable.getColumns().clear();

        t = 0;
        x = 0;
        y = Double.parseDouble(heightTextField.getText());
        a = Double.parseDouble(angleTextField.getText());
        v0 = Double.parseDouble(speedTextField.getText());
        S = Double.parseDouble(sizeTextField.getText());
        m = Double.parseDouble(weightTextField.getText());

        cosa = Math.cos(Math.toRadians(a));
        sina = Math.sin(Math.toRadians(a));

        vx = v0 * cosa;
        vy = v0 * sina;

        k = 0.5 * C * rho * S / m;

        initTable();

        dt = Double.parseDouble(dtTextField.getCharacters().toString());

        if (mainLineChart.getData().isEmpty()) {
            mainLineChart.getData().add(new XYChart.Series<>());
        }

        mainLineChart.getData().get(0).getData().clear();

        mainLineChart.getData().get(0).getData().add(new XYChart.Data<>(x, y));

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> tickTimer());
            }
        };

        timer.schedule(timerTask, 0L, 50);
    }

    private void tickTimer() {
        t += dt;
        double v = Math.sqrt(vx * vx + vy * vy);
        vx = vx - k * vx * v * dt;
        vy = vy - (g + k * vy * v) * dt;
        x = x + vx * dt;
        y = y + vy * dt;
        if (y < 0) {
            resultsTable.getItems().add(new ResultData(dt, x, yMax, v));
            timer.cancel();
        } else {
            mainLineChart.getData().get(0).getData().add(new XYChart.Data<>(x, y));
        }
    }


    private void initTable() {
        TableColumn timeStepColumn = new TableColumn<>("Time Step");
        timeStepColumn.setCellValueFactory(new PropertyValueFactory<>("timeStep"));

        TableColumn distanceColumn = new TableColumn<>("Distance");
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));

        TableColumn maxHeightColumn = new TableColumn<>("Max Height");
        maxHeightColumn.setCellValueFactory(new PropertyValueFactory<>("maxHeight"));

        TableColumn speedColumn = new TableColumn<>("Ending Speed");
        speedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        resultsTable.getColumns().addAll(timeStepColumn, distanceColumn, maxHeightColumn, speedColumn);
    }
}