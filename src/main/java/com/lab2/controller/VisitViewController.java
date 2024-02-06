package com.lab2.controller;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.lab2.dao.*;
import com.lab2.entities.*;
import com.lab2.controller.AddVisitViewController;
import com.lab2.controller.UpdateVisitViewController;
import com.lab2.controller.FindVisitViewController;

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

public class VisitViewController {
    @FXML
    private TableView<Visit> visitTable;

    @FXML
    private TableColumn<Visit, Integer> index;

    @FXML
    private TableColumn<Visit, Integer> patient;

    @FXML
    private TableColumn<Visit, Integer> doctor;

    @FXML
    private TableColumn<Visit, String> complaints;

	@FXML
    private TableColumn<Visit, LocalDateTime> dateVisit;

	@FXML
    private TableColumn<Visit, LocalDateTime> dateClose;

	@FXML
    private TableColumn<Visit, LocalDateTime> dateDischarge;

	private VisitDao visitDao;
    private DoctorDao doctorDao;	
    private PatientDao patientDao;
    private Map<Integer, String> doctorMap;
    private Map<Integer, String> patientMap;

    public void showVisitTable() {
        initialize();
    }

    @FXML
    private void initialize() {
		doctor.setCellValueFactory(new PropertyValueFactory<>("idDoctor"));
		patient.setCellValueFactory(new PropertyValueFactory<>("idPatient"));
		complaints.setCellValueFactory(new PropertyValueFactory<>("complaints"));
		dateVisit.setCellValueFactory(new PropertyValueFactory<>("dateVisit"));
		dateClose.setCellValueFactory(new PropertyValueFactory<>("dateClose"));
		dateDischarge.setCellValueFactory(new PropertyValueFactory<>("dateDischarge"));

        setupIndexCell();
		setupDateCell(dateVisit);
		setupDateCell(dateClose);
		setupDateCell(dateDischarge);
		setupDoctorCell();
		setupPatientCell();
		
        loadVisits();
    }

	private void setupDateCell(TableColumn<Visit, LocalDateTime> date) {
		date.setCellFactory(column -> new TableCell<Visit, LocalDateTime>() {
				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
					} else {
						setText(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
					}
				}
		});
	}

	private void setupPatientCell() {
		patientMap = loadPatientMap();
        patient.setCellFactory(column -> new TableCell<Visit, Integer>() {
			@Override
            protected void updateItem(Integer idPatient, boolean empty) {
                super.updateItem(idPatient, empty);
				
                if (empty || idPatient == null) {
                    setText(null);
                } else {
                    String patient =
						patientMap.getOrDefault(idPatient, "Unknown");
                    setText(patient);
                }
            }
		});
	}

	private void setupDoctorCell() {
		doctorMap = loadDoctorMap();
        doctor.setCellFactory(column -> new TableCell<Visit, Integer>() {
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
        index.setCellFactory(column -> new TableCell<Visit, Integer>() {
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

    private void loadVisits() {
        visitDao = new VisitDao();
        visitTable.setItems(FXCollections.observableArrayList(visitDao.findAll()));
    }

    private Map<Integer, String> loadDoctorMap() {
        doctorDao = new DoctorDao();
        List<Doctor> doctors = doctorDao.findAll();
        return doctors.stream()
			.collect(Collectors.
					 toMap(Doctor::getId,
						   doctor ->
						   doctor.getLastName()
						   + " " + doctor.getFirstName()
						   + " " + doctor.getMiddleName()));
    }

	private Map<Integer, String> loadPatientMap() {
        patientDao = new PatientDao();
        List<Patient> patients = patientDao.findAll();
        return patients.stream()
			.collect(Collectors.
					 toMap(Patient::getId,
						   patient ->
						   patient.getLastName()
						   + " " + patient.getFirstName()
						   + " " + patient.getMiddleName()));
    }

    @FXML
    private void addVisit() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddVisitView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Add New Visit");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			AddVisitViewController addVisitViewController = loader.getController();
			Visit newVisit = addVisitViewController.getNewlyAddedVisit();
			if (newVisit != null) {
				visitTable.getItems().add(newVisit);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void editVisit() {
		Visit selectedVisit = visitTable.getSelectionModel().getSelectedItem();
		if (selectedVisit != null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateVisitView.fxml"));
				Parent root = loader.load();
				UpdateVisitViewController updateVisitViewController = loader.getController();
				updateVisitViewController.setVisit(selectedVisit);

				Stage stage = new Stage();
				stage.setTitle("Update Visit");
				stage.setScene(new Scene(root));
				stage.showAndWait();
				
				Visit updatedVisit = updateVisitViewController.getNewlyUpdatedVisit();
				int selectedIndex = visitTable.getSelectionModel().getSelectedIndex();
				visitTable.getItems().set(selectedIndex, updatedVisit);
				visitTable.refresh();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			notChosen();
		}
    }

    @FXML
    private void deleteVisit() {
		Visit selectedVisit = visitTable.getSelectionModel().getSelectedItem();
		if (selectedVisit != null) {
			if (isConfirmed()) {
				visitDao.delete(selectedVisit);
				visitTable.getItems().remove(selectedVisit);
			} else {
				error();
			}
		} else {
			notChosen();
		}
    }

	@FXML
	private void findVisit() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FindVisitView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Find Visit");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			FindVisitViewController findVisitViewController = loader.getController();
			List<Visit> foundVisit = findVisitViewController.getNewlyFoundVisit();

			if (foundVisit != null && !foundVisit.isEmpty()) {
				visitTable.setItems(FXCollections.observableArrayList(foundVisit));
			} else {
				visitTable.setItems(FXCollections.observableArrayList());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void refreshVisit() {
		loadVisits();
	}

	private boolean isConfirmed() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Deletion confirmation");
		alert.setHeaderText("Patient deleting");
		alert.setContentText("Are you sure to delete chosen patient?");
		final Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	private void error() {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setTitle("Deletion error");
		errorAlert.setHeaderText("An error while patient deleting");
		errorAlert.setContentText("Failed to delete the selected patient.");
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
