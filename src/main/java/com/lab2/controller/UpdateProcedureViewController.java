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

public class UpdateProcedureViewController {

	public Procedure getNewlyUpdatedProcedure() {
		return newProcedure;
	}

	public void setProcedure(Procedure procedure) {
		newProcedure = procedure;
		procedureField.setText(procedure.getName());
	}

	@FXML
    private TextField procedureField;

	private ProcedureDao procedureDao;
	private Procedure newProcedure;

    @FXML
    private void initialize() {
		procedureDao = new ProcedureDao();
    }

    @FXML
    private void handleUpdateProcedure() {
		try {
			String procedure = procedureField.getText();
			if (!isValidateInput(procedure)) {
				throw new Exception("Incorrect procedure name input.");
			}
			newProcedure = new Procedure(newProcedure.getId(), procedure);

			procedureDao.update(newProcedure);
			closeStage();
		} catch (Exception e) {
			showErrorAlert(e.getMessage());
		}
    }

    private void closeStage() {
        Stage stage = (Stage) procedureField.getScene().getWindow();
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
