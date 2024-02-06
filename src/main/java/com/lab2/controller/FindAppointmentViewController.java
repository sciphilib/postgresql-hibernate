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
import java.time.LocalTime;

public class FindAppointmentViewController {

	public List<Appointment> getNewlyFoundAppointment() {
		return foundAppointment;
	}

    @FXML
    private ComboBox<Doctor> doctorComboBox;

	@FXML
    private ComboBox<Weekday> weekdayComboBox;

	@FXML
    private TextField beginTimeField;

	@FXML
    private TextField endTimeField;

	@FXML
    private TextField officeField;

	@FXML
    private TextField districtField;

	private AppointmentDao appointmentDao;
	private DoctorDao doctorDao;
	private WeekdayDao weekdayDao;
	private List<Appointment> foundAppointment;

    @FXML
    private void initialize() {
		appointmentDao = new AppointmentDao();
		loadDoctors();
		loadWeekdays();
		loadTime(beginTimeField);
		loadTime(endTimeField);
		loadNumber(officeField);
		loadNumber(districtField);
    }

	private void loadDoctors() {
		doctorDao = new DoctorDao();
		doctorComboBox.setItems(FXCollections.observableArrayList(doctorDao.findAll()));
		doctorComboBox.setConverter(new DoctorStringConverter());
	}

	private void loadWeekdays() {
		weekdayDao = new WeekdayDao();
		weekdayComboBox.setItems(FXCollections.observableArrayList(weekdayDao.findAll()));
		weekdayComboBox.setConverter(new WeekdayStringConverter());
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

	private void loadNumber(TextField field) {
		Pattern pattern = Pattern.compile("^(\\d+)$");
		TextFormatter<?> formatter = new TextFormatter<>((change) -> {
				String newText = change.getControlNewText();
				if (newText.isEmpty()) {
					return change;
				} else if (pattern.matcher(newText).matches()) {
					return change;
				}
				return null;
		});
		field.setTextFormatter(formatter);
	}

    @FXML
    private void handleFindAppointment() {
		try {
			String beginTime = beginTimeField.getText();
			String endTime = endTimeField.getText();
			if (!isValidateTimeInput(beginTime)
				|| !isValidateTimeInput(endTime)) {
				throw new Exception("Incorrect time input.");
			}

			Doctor doctor = doctorComboBox.getValue();
			Integer idDoctor;
			if (doctor != null) {
				idDoctor = doctor.getId();
			} else {
				idDoctor = null;
			}
			
			Weekday weekday = weekdayComboBox.getValue();
			Integer idWeekday;
			if (weekday != null) {
				idWeekday = weekday.getId();
			} else {
				idWeekday = null;
			}

			String[] parsedBeginTime;
			Integer beginHours;
			Integer beginMinutes;
			LocalTime begin;
			if (!(beginTime == "")) {
				parsedBeginTime = beginTime.split(":");
				beginHours = Integer.parseInt(parsedBeginTime[0]);
				beginMinutes = Integer.parseInt(parsedBeginTime[1]);
				begin = LocalTime.of(beginHours, beginMinutes);
			} else {
				begin = null;
			}

			String[] parsedEndTime;
			Integer endHours;
			Integer endMinutes;
			LocalTime end;
			if (!(endTime == "")) {
				parsedEndTime = endTime.split(":");
				endHours = Integer.parseInt(parsedEndTime[0]);
				endMinutes = Integer.parseInt(parsedEndTime[1]);
				end = LocalTime.of(endHours, endMinutes);
			} else {
				end = null;
			}

			String officeString = officeField.getText();
			Integer office;
			if (!(officeString == "")) {
				office = Integer.parseInt(officeString);
			} else {
				office = null;
			}
			
			String districtString = districtField.getText();
			Integer district;
			if (!(districtString == "")) {
				district = Integer.parseInt(districtString);
			} else {
				district = null;
			}

			Appointment appointment = new Appointment(1, idDoctor, idWeekday,
													  begin, end, office, district);
			foundAppointment = appointmentDao.findByExample(appointment);

			closeStage();
		} catch (Exception e) {
			showErrorAlert(e.getMessage());
		}
    }

    private void closeStage() {
        Stage stage = (Stage) doctorComboBox.getScene().getWindow();
        stage.close();
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
