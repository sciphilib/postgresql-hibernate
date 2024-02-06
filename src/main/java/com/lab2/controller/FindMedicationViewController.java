package com.lab2.controller;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import com.lab2.dao.*;
import com.lab2.entities.*;
import com.lab2.util.*;
import java.util.regex.Pattern;

public class FindMedicationViewController {

	public List<Medication> getNewlyFoundMedication() {
		return foundMedication;
	}

	@FXML
    private TextField medicationField;

	private MedicationDao medicationDao;
	private List<Medication> foundMedication;

    @FXML
    private void initialize() {
		medicationDao = new MedicationDao();
    }

    @FXML
    private void handleFindMedication() {
		try {
			String medicationName = medicationField.getText();
			if (!isValidateInput(medicationName)) {
				throw new Exception("Incorrect medication name input.");
			}

			Medication medication = new Medication(1, medicationName);
			foundMedication = medicationDao.findByExample(medication);

			closeStage();
		} catch (Exception e) {
			showErrorAlert(e.getMessage());
		}
    }

    private void closeStage() {
        Stage stage = (Stage) medicationField.getScene().getWindow();
        stage.close();
    }

	private boolean isValidateInput(String time) {
        if(time.isEmpty()) {
			return false;
		}
		return true;
    }

	private void showErrorAlert(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(errorMessage);
		
		alert.showAndWait();
	}

}
