package com.lab2.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import com.lab2.dao.*;
import com.lab2.entities.*;
import com.lab2.util.*;

public class AddPatientViewController {

	public Patient getNewlyAddedPatient() {
		return newPatient;
	}

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField middleNameField;

	@FXML
    private TextField cityNameField;

	@FXML
    private TextField streetNameField;

	@FXML
    private TextField buildingNameField;

	@FXML
    private TextField flatNameField;

	private PatientDao patientDao;
	private Patient newPatient;

    @FXML
    private void initialize() {
		patientDao = new PatientDao();
    }

    @FXML
    private void handleAddPatient() {
		try {
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String middleName = middleNameField.getText();
			String cityName = cityNameField.getText();
			String streetName = streetNameField.getText();
			String buildingName = buildingNameField.getText();
			String flatName = flatNameField.getText();
			String address = String.format("%s, %s st.," +
                               " building %s, flat %s",
                               cityName, streetName, buildingName, flatName);
			if (!isValidateInput(firstName)
				|| !isValidateInput(lastName)
				|| !isValidateInput(middleName)) {
				throw new Exception("Incorrect first name, second name or middle name input.");
			}
			newPatient = new Patient(1, lastName, firstName, middleName, address);
			patientDao.save(newPatient);
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
