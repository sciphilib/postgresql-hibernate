package com.lab2.controller;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.lab2.dao.*;
import com.lab2.entities.*;
import com.lab2.controller.AddAppointmentViewController;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;

public class AppointmentViewController {
    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, Integer> index;

    @FXML
    private TableColumn<Appointment, Integer> weekday;

    @FXML
    private TableColumn<Appointment, Integer> doctor;

	@FXML
    private TableColumn<Appointment, LocalTime> beginDate;

	@FXML
    private TableColumn<Appointment, LocalTime> endDate;

    @FXML
    private TableColumn<Appointment, Integer> office;

    @FXML
    private TableColumn<Appointment, Integer> district;

    private DoctorDao doctorDao;	
    private WeekdayDao weekdayDao;
	private AppointmentDao appointmentDao;
    private Map<Integer, String> doctorMap;
    private Map<Integer, String> weekdayMap;

    public void showAppointmentTable() {
        initialize();
    }

    @FXML
    private void initialize() {
		doctor.setCellValueFactory(new PropertyValueFactory<>("idDoctor"));
		weekday.setCellValueFactory(new PropertyValueFactory<>("idWeekday"));
		beginDate.setCellValueFactory(new PropertyValueFactory<>("beginDate"));
		endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		office.setCellValueFactory(new PropertyValueFactory<>("office"));
		district.setCellValueFactory(new PropertyValueFactory<>("district"));

		setupIndexCell();
		setupDoctorCell();
		setupWeekdayCell();
		setupTimeCell(beginDate);
		setupTimeCell(endDate);
		
        loadAppointments();
    }

	private void setupTimeCell(TableColumn<Appointment, LocalTime> date) {
		date.setCellFactory(column -> new TableCell<Appointment, LocalTime>() {
				@Override
				protected void updateItem(LocalTime item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
					} else {
						setText(item.format(DateTimeFormatter.ofPattern("HH:mm")));
					}
				}
		});
	}

	private void setupWeekdayCell() {
		weekdayMap = loadWeekdayMap();
        weekday.setCellFactory(column -> new TableCell<Appointment, Integer>() {
			@Override
            protected void updateItem(Integer idWeekday, boolean empty) {
                super.updateItem(idWeekday, empty);
				
                if (empty || idWeekday == null) {
                    setText(null);
                } else {
                    String weekday =
						weekdayMap.getOrDefault(idWeekday, "Unknown");
                    setText(weekday);
                }
            }
		});
	}

	private void setupDoctorCell() {
		doctorMap = loadDoctorMap();
        doctor.setCellFactory(column -> new TableCell<Appointment, Integer>() {
            @Override
            protected void updateItem(Integer idDoctor, boolean empty) {
                super.updateItem(idDoctor, empty);

                if (empty || idDoctor == null) {
                    setText(null);
                } else {
                    String doctor =
						doctorMap.getOrDefault(idDoctor, "Unknown");
                    setText(doctor);
                }
            }
		});
	}

    private void setupIndexCell() {
        index.setCellFactory(column -> new TableCell<Appointment, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });
    }

    private void loadAppointments() {
        appointmentDao = new AppointmentDao();
        appointmentTable.setItems(FXCollections.observableArrayList(appointmentDao.findAll()));
    }

    private Map<Integer, String> loadDoctorMap() {
        doctorDao = new DoctorDao();
        List<Doctor> doctors = doctorDao.findAll();
        return doctors.stream()
			.collect(Collectors.
					 toMap(Doctor::getId, doctor ->
						   doctor.getLastName()
						   + " " + doctor.getFirstName()
						   + " " + doctor.getMiddleName()));
    }

	private Map<Integer, String> loadWeekdayMap() {
        weekdayDao = new WeekdayDao();
        List<Weekday> weekdays = weekdayDao.findAll();
        return weekdays.stream()
			.collect(Collectors.
					 toMap(Weekday::getId, Weekday::getName));
    }

    @FXML
    private void addAppointment() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddAppointmentView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Add New Appointment");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			AddAppointmentViewController addAppointmentViewController = loader.getController();
			Appointment newAppointment = addAppointmentViewController.getNewlyAddedAppointment();
			if (newAppointment != null) {
				appointmentTable.getItems().add(newAppointment);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void editAppointment() {
		Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
		if (selectedAppointment != null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateAppointmentView.fxml"));
				Parent root = loader.load();
				UpdateAppointmentViewController updateAppointmentViewController = loader.getController();
				updateAppointmentViewController.setAppointment(selectedAppointment);

				Stage stage = new Stage();
				stage.setTitle("Update Appointment");
				stage.setScene(new Scene(root));
				stage.showAndWait();
				
				Appointment updatedAppointment =
					updateAppointmentViewController.getNewlyUpdatedAppointment();
				int selectedIndex = appointmentTable.getSelectionModel().getSelectedIndex();
				appointmentTable.getItems().set(selectedIndex, updatedAppointment);
				appointmentTable.refresh();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			notChosen();
		}
    }

    @FXML
    private void deleteAppointment() {
		Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
		if (selectedAppointment != null) {
			if (isConfirmed()) {
				appointmentDao.delete(selectedAppointment);
				appointmentTable.getItems().remove(selectedAppointment);
			} else {
				error();
			}
		} else {
			notChosen();
		}
    }

	private boolean isConfirmed() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Deletion confirmation");
		alert.setHeaderText("Appointment deleting");
		alert.setContentText("Are you sure to delete chosen appointment?");
		final Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	private void error() {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setTitle("Deletion error");
		errorAlert.setHeaderText("An error while appointment deleting");
		errorAlert.setContentText("Failed to delete the selected appointment.");
		errorAlert.showAndWait();
	}

	private void notChosen() {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Nothing was chosen");
		alert.setHeaderText("Entity was not chosen");
		alert.setContentText("Please chose entity you want to delete or update");
		alert.showAndWait();
	}
}
