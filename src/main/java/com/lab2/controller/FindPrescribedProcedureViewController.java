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

public class FindPrescribedProcedureViewController {

	public List<PrescribedProcedure> getNewlyFoundPrescribedProcedure() {
		return foundPrescribedProcedure;
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
	private List<PrescribedProcedure> foundPrescribedProcedure;

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
    private void handleFindPrescribedProcedure() {
		try {
			Visit visit = visitComboBox.getValue();
			Integer idVisit;
			if (visit != null) {
				idVisit = visit.getId();
			} else {
				idVisit = null;
			}

			Procedure procedure = procedureComboBox.getValue();
			Integer idProcedure;
			if (procedure != null) {
				idProcedure = procedure.getId();
			} else {
				idProcedure = null;
			}

			String countString = countField.getText();
			Integer count;
			if (countString != "") {
				count = Integer.parseInt(countString);
			} else {
				count = null;
			}

			PrescribedProcedure examplePrescriedProcedure =
				new PrescribedProcedure(1, idVisit, idProcedure, count);
			foundPrescribedProcedure = prescribedProcedureDao.findByExample(examplePrescriedProcedure);

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
