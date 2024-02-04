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

public class AddDiagnosisViewController {

	public Diagnosis getNewlyAddedDiagnosis() {
		return newDiagnosis;
	}

    @FXML
    private ComboBox<Visit> visitComboBox;

	@FXML
    private TextField diagnosisField;

	private VisitDao visitDao;
	private DiagnosisDao diagnosisDao;
	private Diagnosis newDiagnosis;

    @FXML
    private void initialize() {
		diagnosisDao = new DiagnosisDao();
		loadVisits();
    }

	private void loadVisits() {
		visitDao = new VisitDao();
		visitComboBox.setItems(FXCollections.observableArrayList(visitDao.findAll()));
		visitComboBox.setConverter(new VisitStringConverter());
	}

    @FXML
    private void handleAddDiagnosis() {
		try {
			Visit visit = visitComboBox.getValue();
			String diagnosis = diagnosisField.getText();
			if (!isValidateInput(diagnosis)) {
				throw new Exception("Incorrect diagnosis description input.");
			}
			newDiagnosis = new Diagnosis(1, visit.getId(), diagnosis);

			diagnosisDao.save(newDiagnosis);
			closeStage();
		} catch (Exception e) {
			showErrorAlert(e.getMessage());
		}
    }

    private void closeStage() {
        Stage stage = (Stage) visitComboBox.getScene().getWindow();
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
