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

public class AddDoctorViewController {

	public Doctor getNewlyAddedDoctor() {
		return newDoctor;
	}

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField middleNameField;

    @FXML
    private ComboBox<Specialization> specializationComboBox;

    private SpecializationDao specializationDao;
	private DoctorDao doctorDao;
	private Doctor newDoctor;

    @FXML
    private void initialize() {
		doctorDao = new DoctorDao();
        loadSpecializations();
    }

    private void loadSpecializations() {
        specializationDao = new SpecializationDao();
        specializationComboBox.setItems(FXCollections.observableArrayList(specializationDao.findAll()));
        specializationComboBox.setConverter(new SpecializationStringConverter());
    }

    @FXML
    private void handleAddDoctor() {
		try {
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String middleName = middleNameField.getText();
			Specialization specialization = specializationComboBox.getValue();
			String specializationName = specialization.getName();
			if (!isValidateInput(firstName)
				|| !isValidateInput(lastName)
				|| !isValidateInput(middleName)) {
				throw new Exception("Incorrect first name, second name or middle name input.");
			}
			newDoctor = new Doctor(1, lastName, firstName, middleName, specialization.getId());
			doctorDao.save(newDoctor);
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
