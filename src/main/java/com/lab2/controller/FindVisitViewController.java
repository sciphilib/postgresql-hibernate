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
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

import java.lang.System;

public class FindVisitViewController {

	public List<Visit> getNewlyFoundVisit() {
		return foundVisit;
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
	private List<Visit> foundVisit;

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
    private void handleFindVisit() {
		try {			
			String dateVisit = dateVisitField.getText();
			String dateDischarge = dateDischargeField.getText();
			String dateClose = dateCloseField.getText();
			if (!isValidateDateInput(dateVisit)
				|| !isValidateDateInput(dateDischarge)
				|| !isValidateDateInput(dateClose)) {
				throw new Exception("Incorrect date input.");
			}

			String timeVisit = timeVisitField.getText();
			String timeDischarge = timeDischargeField.getText();
			String timeClose = timeCloseField.getText();
			if (!isValidateTimeInput(timeVisit)
				|| !isValidateTimeInput(timeDischarge)
				|| !isValidateTimeInput(timeClose)) {
				throw new Exception("Incorrect time input.");
			}

			Patient patient = patientComboBox.getValue();
			Integer idPatient;
			if (patient != null) {
				idPatient = patient.getId();
			} else {
				idPatient = null;
			}

			Doctor doctor = doctorComboBox.getValue();
			Integer idDoctor;
			if (doctor != null) {
				idDoctor = doctor.getId();
			} else {
				idDoctor = null;
			}

			String complaintsString = complaintsField.getText();
			String complaints;
			if (complaintsString != "") {
				complaints = complaintsString;
			} else {
				complaints = null;
			}
			
			String[] parsedDateVisit;
			Integer visitYear;
			Integer visitMonth;
			Integer visitDay;
			LocalDate visitDate;
			if (!(dateVisit == "")) {
				parsedDateVisit = dateVisit.split("-");
				visitYear = Integer.parseInt(parsedDateVisit[0]);
				visitMonth = Integer.parseInt(parsedDateVisit[1]);
				visitDay = Integer.parseInt(parsedDateVisit[2]);
				visitDate = LocalDate.of(visitYear, visitMonth, visitDay);
			} else {
				visitDate = null;
			}

			String[] parsedDateDischarge;
			Integer dischargeYear;
			Integer dischargeMonth;
			Integer dischargeDay;
			LocalDate dischargeDate;
			if (!(dateDischarge == "")) {
				parsedDateDischarge = dateDischarge.split("-");
				dischargeYear = Integer.parseInt(parsedDateDischarge[0]);
				dischargeMonth = Integer.parseInt(parsedDateDischarge[1]);
				dischargeDay = Integer.parseInt(parsedDateDischarge[2]);
				dischargeDate = LocalDate.of(dischargeYear, dischargeMonth, dischargeDay);
			} else {
				dischargeDate = null;
			}

			String[] parsedDateClose;
			Integer closeYear;
			Integer closeMonth;
			Integer closeDay;
			LocalDate closeDate;
			if (!(dateClose == "")) {
				parsedDateClose = dateClose.split("-");
				closeYear = Integer.parseInt(parsedDateClose[0]);
				closeMonth = Integer.parseInt(parsedDateClose[1]);
				closeDay = Integer.parseInt(parsedDateClose[2]);
				closeDate = LocalDate.of(closeYear, closeMonth, closeDay);
			} else {
				closeDate = null;
			}

			String[] parsedVisitTime;
			Integer visitHours;
			Integer visitMinutes;
			LocalTime visitTime;
			if (!(timeVisit == "")) {
				parsedVisitTime = timeVisit.split(":");
				visitHours = Integer.parseInt(parsedVisitTime[0]);
				visitMinutes = Integer.parseInt(parsedVisitTime[1]);
				visitTime = LocalTime.of(visitHours, visitMinutes);
			} else {
				visitTime = null;
			}

			String[] parsedDischargeTime;
			Integer dischargeHours;
			Integer dischargeMinutes;
			LocalTime dischargeTime;
			if (!(timeDischarge == "")) {
				parsedDischargeTime = timeDischarge.split(":");
				dischargeHours = Integer.parseInt(parsedDischargeTime[0]);
				dischargeMinutes = Integer.parseInt(parsedDischargeTime[1]);
				dischargeTime = LocalTime.of(dischargeHours, dischargeMinutes);
			} else {
				dischargeTime = null;
			}

			String[] parsedCloseTime;
			Integer closeHours;
			Integer closeMinutes;
			LocalTime closeTime;
			if (!(timeClose == "")) {
				parsedCloseTime = timeClose.split(":");
				closeHours = Integer.parseInt(parsedCloseTime[0]);
				closeMinutes = Integer.parseInt(parsedCloseTime[1]);
				closeTime = LocalTime.of(closeHours, closeMinutes);
			} else {
				closeTime = null;
			}

			LocalDateTime visit;
			if (visitDate != null && visitTime != null) {
				visit = LocalDateTime.of(visitDate, visitTime);
			} else {
				visit = null;
			}

			LocalDateTime discharge;
			if (dischargeDate != null && dischargeTime != null) {
				discharge = LocalDateTime.of(dischargeDate, dischargeTime);
			} else {
				discharge = null;
			}

			LocalDateTime close;
			if (closeDate != null && closeTime != null) {
				close = LocalDateTime.of(closeDate, closeTime);
			} else {
				close = null;
			}
			
			Visit exampleVisit = new Visit(1, idPatient, idDoctor, complaints, visit, discharge, close);
			foundVisit = visitDao.findByExample(exampleVisit);

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
        if(!date.isEmpty()) {
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
		}
		return true;
    }

	private boolean isValidateTimeInput(String time) {
        if(!time.isEmpty()) {
			String[] parsedTime = time.split(":");
			Integer hours = Integer.parseInt(parsedTime[0]);
			Integer minutes = Integer.parseInt(parsedTime[1]);
			if (!(hours >= 0 && hours <= 23) || parsedTime[0].length() != 2) {
				return false;
			}
			if (!(minutes >= 0 && minutes <= 60) || parsedTime[1].length() != 2) {
				return false;
			}
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
