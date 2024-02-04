package com.lab2.controller;

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

public class AddMedicationViewController {

	public Medication getNewlyAddedMedication() {
		return newMedication;
	}

	@FXML
    private TextField medicationField;

	private MedicationDao medicationDao;
	private Medication newMedication;

    @FXML
    private void initialize() {
		medicationDao = new MedicationDao();
    }

    @FXML
    private void handleAddMedication() {
		try {
			String medication = medicationField.getText();
			if (!isValidateInput(medication)) {
				throw new Exception("Incorrect medication name input.");
			}
			newMedication = new Medication(1, medication);

			medicationDao.save(newMedication);
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
