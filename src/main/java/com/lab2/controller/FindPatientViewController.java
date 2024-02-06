package com.lab2.controller;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import com.lab2.dao.*;
import com.lab2.entities.*;
import com.lab2.util.*;

public class FindPatientViewController {

	public List<Patient> getNewlyFoundPatient() {
		return foundPatient;
	}

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField middleNameField;

	@FXML
    private TextField cityNameField;

	private PatientDao patientDao;
	private List<Patient> foundPatient;

    @FXML
    private void initialize() {
		patientDao = new PatientDao();
    }

    @FXML
    private void handleFindPatient() {
		try {
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String middleName = middleNameField.getText();
			String cityName = cityNameField.getText();
			String address;
			if (!cityName.isEmpty()) {
				address = String.format("%s", cityName);
			} else {
				address = null;
			}

			Patient patient = new Patient(1, lastName, firstName, middleName, address);
			foundPatient = patientDao.findByExample(patient);

			closeStage();
		} catch (Exception e) {
			showErrorAlert(e.getMessage());
		}
    }

    private void closeStage() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }

    private boolean isValidateInput(String string) {
        return !string.isEmpty();
    }

	private void showErrorAlert(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(errorMessage);
		
		alert.showAndWait();
	}

}
