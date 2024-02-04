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

public class AddSpecializationViewController {

	public Specialization getNewlyAddedSpecialization() {
		return newSpecialization;
	}

	@FXML
    private TextField specializationField;

	private SpecializationDao specializationDao;
	private Specialization newSpecialization;

    @FXML
    private void initialize() {
		specializationDao = new SpecializationDao();
    }

    @FXML
    private void handleAddSpecialization() {
		try {
			String specialization = specializationField.getText();
			if (!isValidateInput(specialization)) {
				throw new Exception("Incorrect specialization name input.");
			}
			newSpecialization = new Specialization(1, specialization);

			specializationDao.save(newSpecialization);
			closeStage();
		} catch (Exception e) {
			showErrorAlert(e.getMessage());
		}
    }

    private void closeStage() {
        Stage stage = (Stage) specializationField.getScene().getWindow();
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
