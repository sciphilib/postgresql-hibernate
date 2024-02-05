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
import java.time.LocalDateTime;

import java.lang.System;

public class UpdateVisitViewController {

	public Visit getNewlyUpdatedVisit() {
		return newVisit;
	}

	public void setVisit(Visit visit) {
		newVisit = visit;
		Patient patient = patientDao.findById(visit.getIdPatient());
		patientComboBox.setValue(patient);
		Doctor doctor = doctorDao.findById(visit.getIdDoctor());
		doctorComboBox.setValue(doctor);
		complaintsField.setText(visit.getComplaints());
		String[] parsedVisit = (visit.getDateVisit().toString()).split("T");
		dateVisitField.setText(parsedVisit[0]);
		timeVisitField.setText(parsedVisit[1]);
		String[] parsedDischarge = visit.getDateDischarge().toString().split("T");
		dateDischargeField.setText(parsedDischarge[0]);
		timeDischargeField.setText(parsedDischarge[1]);
		String[] parsedClose = visit.getDateClose().toString().split("T");
		dateCloseField.setText(parsedClose[0]);
		timeCloseField.setText(parsedClose[1]);
	}

    @FXML
    private ComboBox<Patient> patientComboBox;

	@FXML
    private ComboBox<Doctor> doctorComboBox;

	@FXML
    private TextField complaintsField;

	@FXML
    private TextField dateVisitField;

	@FXML
    private TextField dateDischargeField;

	@FXML
    private TextField dateCloseField;

	@FXML
    private TextField timeVisitField;

	@FXML
    private TextField timeDischargeField;

	@FXML
    private TextField timeCloseField;

	private VisitDao visitDao;
	private PatientDao patientDao;
	private DoctorDao doctorDao;
	private Visit newVisit;

    @FXML
    private void initialize() {
		visitDao = new VisitDao();
		loadPatients();
		loadDoctors();
		loadDate(dateVisitField);
		loadTime(timeVisitField);
		loadDate(dateCloseField);
		loadTime(timeCloseField);
		loadDate(dateDischargeField);
		loadTime(timeDischargeField);
    }

	private void loadPatients() {
		patientDao = new PatientDao();
		patientComboBox.setItems(FXCollections.observableArrayList(patientDao.findAll()));
		patientComboBox.setConverter(new PatientStringConverter());
	}

	private void loadDoctors() {
		doctorDao = new DoctorDao();
		doctorComboBox.setItems(FXCollections.observableArrayList(doctorDao.findAll()));
		doctorComboBox.setConverter(new DoctorStringConverter());
	}

	private void loadTime(TextField field) {
		Pattern pattern = Pattern.compile("^(\\d{0,2}:?\\d{0,2})$");
		TextFormatter<?> formatter = new TextFormatter<>((change) -> {
				String newText = change.getControlNewText();
				if (newText.isEmpty()) {
					return change;
				} else if (newText.matches("\\d{2}") && change.getText().matches("\\d")) {
					change.setText(newText.substring(1) + ":");
					change.setCaretPosition(change.getCaretPosition() + 1);
					change.setAnchor(change.getCaretPosition());
					return change;
				} else if (newText.matches("\\d{3}")) {
					change.setText(newText.substring(3) + ":");
					change.setCaretPosition(change.getCaretPosition() + 1);
					change.setAnchor(change.getCaretPosition());
					return change;
				} else if (pattern.matcher(newText).matches()) {
					return change;
				}
				return null;
		});
		field.setTextFormatter(formatter);
	}

	private void loadDate(TextField field) {
		Pattern pattern = Pattern.compile("^(\\d{0,4}-?\\d{0,2}-?\\d{0,2})$");
		TextFormatter<?> formatter = new TextFormatter<>((change) -> {
				String newText = change.getControlNewText();
				if (newText.isEmpty()) {
					return change;
				} else if (newText.matches("\\d{4}") && change.getText().matches("\\d")) {
					change.setText(newText.substring(3) + "-");
					change.setCaretPosition(change.getCaretPosition() + 1);
					change.setAnchor(change.getCaretPosition());
					return change;
				} else if (newText.matches("\\d{4}-\\d{2}") && change.getText().matches("\\d")) {
					change.setText(newText.substring(6) + "-");
					change.setCaretPosition(change.getCaretPosition() + 1);
					change.setAnchor(change.getCaretPosition());
					return change;
				} else if (newText.matches("\\d{4}-\\d{3}") && change.getText().matches("\\d")) {
					return null;
				} else if (newText.matches("\\d{5}") && change.getText().matches("\\d")) {
					return null;
				} else if (pattern.matcher(newText).matches()) {
					return change;
				}
				return null;
		});
		field.setTextFormatter(formatter);
	}

    @FXML
    private void handleUpdateVisit() {
		try {			
			String dateVisit = dateVisitField.getText();
			String timeVisit = timeVisitField.getText();
			String dateDischarge = dateDischargeField.getText();
			String timeDischarge = timeDischargeField.getText();
			String dateClose = dateCloseField.getText();
			String timeClose = timeCloseField.getText();

			if (!isValidateTimeInput(timeVisit)
				|| !isValidateTimeInput(timeDischarge)
				|| !isValidateTimeInput(timeClose)) {
				throw new Exception("Incorrect time input.");
			}
			if (!isValidateDateInput(dateVisit)
				|| !isValidateDateInput(dateDischarge)
				|| !isValidateDateInput(dateClose)) {
				throw new Exception("Incorrect date input.");
			}

			Patient patient = patientComboBox.getValue();
			Doctor doctor = doctorComboBox.getValue();
			String complaints = complaintsField.getText();

			String[] parsedDateVisit = dateVisit.split("-");
			Integer visitYear = Integer.parseInt(parsedDateVisit[0]);
			Integer visitMonth = Integer.parseInt(parsedDateVisit[1]);
			Integer visitDay = Integer.parseInt(parsedDateVisit[2]);
			String[] parsedDateDischarge = dateDischarge.split("-");
			Integer dischargeYear = Integer.parseInt(parsedDateDischarge[0]);
			Integer dischargeMonth = Integer.parseInt(parsedDateDischarge[1]);
			Integer dischargeDay = Integer.parseInt(parsedDateDischarge[2]);
			String[] parsedDateClose = dateClose.split("-");
			Integer closeYear = Integer.parseInt(parsedDateClose[0]);
			Integer closeMonth = Integer.parseInt(parsedDateClose[1]);
			Integer closeDay = Integer.parseInt(parsedDateClose[2]);

			String[] parsedVisitTime = timeVisit.split(":");
			Integer visitHours = Integer.parseInt(parsedVisitTime[0]);
			Integer visitMinutes = Integer.parseInt(parsedVisitTime[1]);
			String[] parsedDischargeTime = timeDischarge.split(":");
			Integer dischargeHours = Integer.parseInt(parsedDischargeTime[0]);
			Integer dischargeMinutes = Integer.parseInt(parsedDischargeTime[1]);
			String[] parsedCloseTime = timeClose.split(":");
			Integer closeHours = Integer.parseInt(parsedCloseTime[0]);
			Integer closeMinutes = Integer.parseInt(parsedCloseTime[1]);
			Integer id = newVisit.getId();
			newVisit = new Visit(id, patient.getId(), doctor.getId(), complaints,
								 LocalDateTime.of(visitYear, visitMonth, visitDay, visitHours, visitMinutes),
								 LocalDateTime.of(dischargeYear, dischargeMonth, dischargeDay,
												  dischargeHours, dischargeMinutes),
								 LocalDateTime.of(closeYear, closeMonth, closeDay, closeHours, closeMinutes));

			visitDao.update(newVisit);
			closeStage();
		} catch (Exception e) {
			showErrorAlert(e.getMessage());
		}
    }

    private void closeStage() {
        Stage stage = (Stage) patientComboBox.getScene().getWindow();
        stage.close();
    }

    private boolean isValidateInput(String string) {
        return !string.isEmpty();
    }

	private boolean isValidateDateInput(String date) {
        if(date.isEmpty()) {
			return false;
		}

		String[] parsedDate = date.split("-");
		Integer year = Integer.parseInt(parsedDate[0]);
		Integer month = Integer.parseInt(parsedDate[1]);
		Integer day = Integer.parseInt(parsedDate[2]);
		if (!(year >= 0) || parsedDate[0].length() != 4) {
			return false;
		}
		if (!(month >= 0 && month <= 12) || parsedDate[1].length() != 2) {
			return false;
		}
		if (!(day >= 0 && month <= 31) || parsedDate[2].length() != 2) {
			return false;
		}

		return true;
    }

	private boolean isValidateTimeInput(String time) {
        if(time.isEmpty()) {
			return false;
		}

		String[] parsedTime = time.split(":");
		Integer hours = Integer.parseInt(parsedTime[0]);
		Integer minutes = Integer.parseInt(parsedTime[1]);
		if (!(hours >= 0 && hours <= 23) || parsedTime[0].length() != 2) {
			return false;
		}
		if (!(minutes >= 0 && minutes <= 60) || parsedTime[1].length() != 2) {
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
