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

public class FindTestViewController {

	public List<Test> getNewlyFoundTest() {
		return foundTest;
	}

	@FXML
    private TextField testField;

	private TestDao testDao;
	private List<Test> foundTest;

    @FXML
    private void initialize() {
		testDao = new TestDao();
    }

    @FXML
    private void handleFindTest() {
		try {
			String testName = testField.getText();
			if (!isValidateInput(testName)) {
				throw new Exception("Incorrect test name input.");
			}

			Test test = new Test(1, testName);
			foundTest = testDao.findByExample(test);

			closeStage();
		} catch (Exception e) {
			showErrorAlert(e.getMessage());
		}
    }

    private void closeStage() {
        Stage stage = (Stage) testField.getScene().getWindow();
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
