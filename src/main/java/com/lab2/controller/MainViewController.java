package com.lab2.controller;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainViewController {

    @FXML
    private ComboBox<String> entitySelector;

    @FXML
    private StackPane contentView;

    public void setup(Stage stage) {
        entitySelector.setItems(
                FXCollections.observableArrayList("Doctors", "Specializations",
												  "Medications", "Procedures", 
												  "Tests", "Patients", "Visits",
												  "Appointments", "Diagnosis",
												  "Test Results", "Prescribed Procedures",
												  "Prescribed Medications"));
        entitySelector.setOnAction(event -> switchEntityView(entitySelector.getValue(), stage));
    }

    private void switchEntityView(String entity, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            switch (entity) {
                case "Doctors":
                    loader.setLocation(getClass().getResource("/DoctorView.fxml"));
                    break;
                case "Specializations":
                    loader.setLocation(getClass().getResource("/SpecializationView.fxml"));
                    break;
                case "Medications":
                    loader.setLocation(getClass().getResource("/MedicationView.fxml"));
                    break;
                case "Procedures":
                    loader.setLocation(getClass().getResource("/ProcedureView.fxml"));
                    break;
                case "Tests":
                    loader.setLocation(getClass().getResource("/TestView.fxml"));
                    break;
                case "Patients":
                    loader.setLocation(getClass().getResource("/PatientView.fxml"));
                    break;
                case "Visits":
                    loader.setLocation(getClass().getResource("/VisitView.fxml"));
                    break;
                case "Appointments":
                    loader.setLocation(getClass().getResource("/AppointmentView.fxml"));
                    break;
			    case "Diagnosis":
                    loader.setLocation(getClass().getResource("/DiagnosisView.fxml"));
                    break;
			    case "Test Results":
                    loader.setLocation(getClass().getResource("/TestResultView.fxml"));
                    break;
                case "Prescribed Medications":
                    loader.setLocation(getClass().getResource("/PrescribedMedicationView.fxml"));
                    break;
				case "Prescribed Procedures":
                    loader.setLocation(getClass().getResource("/PrescribedProcedureView.fxml"));
                    break;
            }

            Node view = loader.load();
            contentView.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
