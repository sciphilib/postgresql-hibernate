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

public class AddPrescribedProcedureViewController {

	public PrescribedProcedure getNewlyAddedPrescribedProcedure() {
		return newPrescribedProcedure;
	}
	
	@FXML
    private ComboBox<Visit> visitComboBox;

	@FXML
    private ComboBox<Procedure> procedureComboBox;

	@FXML
    private TextField countField;

	private VisitDao visitDao;
	private ProcedureDao procedureDao;
	private PrescribedProcedureDao prescribedProcedureDao;
	private PrescribedProcedure newPrescribedProcedure;

    @FXML
    private void initialize() {
		prescribedProcedureDao = new PrescribedProcedureDao();
		loadVisits();
		loadProcedures();
    }

	private void loadVisits() {
		visitDao = new VisitDao();
		visitComboBox.setItems(FXCollections.observableArrayList(visitDao.findAll()));
		visitComboBox.setConverter(new VisitStringConverter());
	}

	private void loadProcedures() {
		procedureDao = new ProcedureDao();
		procedureComboBox.setItems(FXCollections.observableArrayList(procedureDao.findAll()));
		procedureComboBox.setConverter(new ProcedureStringConverter());
	}

    @FXML
    private void handleAddPrescribedProcedure() {
		try {
		Visit visit = visitComboBox.getValue();
		Procedure procedure = procedureComboBox.getValue();
		String count = countField.getText();
		if (!isValidateInput(count)) {
			throw new Exception("Incorrect count input.");
		}
		Integer procedureCount = Integer.parseInt(count);
		newPrescribedProcedure = new PrescribedProcedure(1,
														 visit.getId(),
														 procedure.getId(),
														 procedureCount);

		prescribedProcedureDao.save(newPrescribedProcedure);
		closeStage();
		} catch (Exception e) {
			showErrorAlert(e.getMessage());
		}
    }

    private void closeStage() {
        Stage stage = (Stage) visitComboBox.getScene().getWindow();
        stage.close();
    }

	private boolean isValidateInput(String input) {
        if(input.isEmpty()) {
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
