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

public class UpdateTestViewController {

	public Test getNewlyUpdatedTest() {
		return newTest;
	}
	
	public void setTest(Test test) {
		newTest = test;
		testField.setText(test.getName());
	}

	@FXML
    private TextField testField;

	private TestDao testDao;
	private Test newTest;

    @FXML
    private void initialize() {
		testDao = new TestDao();
    }

    @FXML
    private void handleUpdateTest() {
		try {
			String test = testField.getText();
			if (!isValidateInput(test)) {
				throw new Exception("Incorrect test name input.");
			}
			newTest = new Test(newTest.getId(), test);

			testDao.update(newTest);
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
