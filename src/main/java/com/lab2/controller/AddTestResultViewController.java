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

public class AddTestResultViewController {

	public TestResult getNewlyAddedTestResult() {
		return newTestResult;
	}
	
	@FXML
    private ComboBox<Visit> visitComboBox;

	@FXML
    private ComboBox<Test> testComboBox;

	@FXML
    private TextField testResultField;

	private VisitDao visitDao;
	private TestDao testDao;
	private TestResultDao testResultDao;
	private TestResult newTestResult;

    @FXML
    private void initialize() {
		testResultDao = new TestResultDao();
		loadVisits();
		loadTests();
    }

	private void loadVisits() {
		visitDao = new VisitDao();
		visitComboBox.setItems(FXCollections.observableArrayList(visitDao.findAll()));
		visitComboBox.setConverter(new VisitStringConverter());
	}

	private void loadTests() {
		testDao = new TestDao();
		testComboBox.setItems(FXCollections.observableArrayList(testDao.findAll()));
		testComboBox.setConverter(new TestStringConverter());
	}

    @FXML
    private void handleAddTestResult() {
		try {
			String testResult = testResultField.getText();
			if (!isValidateInput(testResult)) {
				throw new Exception("Incorrect test result description input.");
			}
			Visit visit = visitComboBox.getValue();
			Test test = testComboBox.getValue();
			newTestResult = new TestResult(1, visit.getId(), test.getId(), testResult);

			testResultDao.save(newTestResult);
			closeStage();
		} catch (Exception e) {
			showErrorAlert(e.getMessage());
		}
    }

    private void closeStage() {
        Stage stage = (Stage) testResultField.getScene().getWindow();
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
