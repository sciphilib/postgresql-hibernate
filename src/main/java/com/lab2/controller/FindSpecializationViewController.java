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

public class FindSpecializationViewController {

	public List<Specialization> getNewlyFoundSpecialization() {
		return foundSpecialization;
	}

	@FXML
    private TextField specializationField;

	private SpecializationDao specializationDao;
	private List<Specialization> foundSpecialization;

    @FXML
    private void initialize() {
		specializationDao = new SpecializationDao();
    }

    @FXML
    private void handleFindSpecialization() {
		try {
			String specializationString = specializationField.getText();
			String specialization;
			if (specializationString != "") {
				specialization = specializationString;
			} else {
				specialization = null;
			}

			Specialization exampleSpecialization = new Specialization(1, specialization);
			foundSpecialization = specializationDao.findByExample(exampleSpecialization);

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
