package com.lab2.controller;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.lab2.controller.AddPrescribedMedicationViewController;
import com.lab2.controller.UpdatePrescribedMedicationViewController;
import com.lab2.controller.FindPrescribedMedicationViewController;

import com.lab2.dao.*;
import com.lab2.entities.*;

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

public class PrescribedMedicationViewController {
    @FXML
    private TableView<PrescribedMedication> prescribedMedicationTable;

    @FXML
    private TableColumn<PrescribedMedication, Integer> index;

    @FXML
    private TableColumn<PrescribedMedication, String> visitPatient;

	@FXML
    private TableColumn<PrescribedMedication, String> visitDoctor;

	@FXML
    private TableColumn<PrescribedMedication, Integer> medication;

	private VisitDao visitDao;
	private DoctorDao doctorDao;
	private PatientDao patientDao;
	private MedicationDao medicationDao;
	private PrescribedMedicationDao prescribedMedicationDao;
	private Map<Integer, String> medicationMap;

    public void showPrescribedMedicationTable() {
        initialize();
    }

    @FXML
    private void initialize() {
		medication.setCellValueFactory(new PropertyValueFactory<>("idMedication"));

		visitDao = new VisitDao();

		setupIndexCell();
		setupVisitPatientCell();
		setupVisitDoctorCell();
		setupMedicationCell();
		
        loadPrescribedMedications();
    }

	private void setupMedicationCell() {
		medicationMap = loadMedicationMap();
        medication.setCellFactory(column -> new TableCell<PrescribedMedication, Integer>() {
            @Override
            protected void updateItem(Integer idMedication, boolean empty) {
                super.updateItem(idMedication, empty);

                if (empty || idMedication == null) {
                    setText(null);
                } else {
                    String medication =
						medicationMap.getOrDefault(idMedication, "Unknown");
                    setText(medication);
                }
            }
		});
	}

	private void setupVisitPatientCell() {
		patientDao = new PatientDao();
		visitPatient.setCellFactory(column -> new TableCell<PrescribedMedication, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
			
            if (empty) {
                setText(null);
            } else {
                PrescribedMedication prescribedMedication = getTableView().getItems().get(getIndex());
                Visit visit = visitDao.findById(prescribedMedication.getIdVisit());
                if (visit != null) {
                    Integer idPatient = visit.getIdPatient();
                    Patient patient = patientDao.findById(idPatient);
                    if (patient != null) {
						String firstName = patient.getFirstName();
						String lastName = patient.getLastName();
						String middleName = patient.getMiddleName();
                        String fullName = lastName + " " + firstName + " " + middleName;
                        setText(fullName);
                    } else {
                        setText("Patient not found");
                    }
                } else {
                    setText("Visit not found");
                }
            }
        }
		});
	}

	private void setupVisitDoctorCell() {
		doctorDao = new DoctorDao();
		visitDoctor.setCellFactory(column -> new TableCell<PrescribedMedication, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
			
            if (empty) {
                setText(null);
            } else {
                PrescribedMedication prescribedMedication = getTableView().getItems().get(getIndex());
                Visit visit = visitDao.findById(prescribedMedication.getIdVisit());
                if (visit != null) {
                    Integer idDoctor = visit.getIdDoctor();
                    Doctor doctor = doctorDao.findById(idDoctor);
                    if (doctor != null) {
						String firstName = doctor.getFirstName();
						String lastName = doctor.getLastName();
						String middleName = doctor.getMiddleName();
                        String fullName = lastName + " " + firstName + " " + middleName;
                        setText(fullName);
                    } else {
                        setText("Doctor not found");
                    }
                } else {
                    setText("Visit not found");
                }
            }
        }
		});
	}

	private Map<Integer, String> loadMedicationMap() {
		medicationDao = new MedicationDao();
        List<Medication> medications = medicationDao.findAll();
        return medications.stream()
			.collect(Collectors.
					 toMap(Medication::getId, Medication::getName));
    }

    private void setupIndexCell() {
        index.setCellFactory(column -> new TableCell<PrescribedMedication, Integer>() {
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

    private void loadPrescribedMedications() {
        prescribedMedicationDao = new PrescribedMedicationDao();
        prescribedMedicationTable.setItems(FXCollections.
										   observableArrayList(prescribedMedicationDao.findAll()));
    }

    @FXML
    private void addPrescribedMedication() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddPrescribedMedicationView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Add New PrescribedMedication");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			AddPrescribedMedicationViewController addPrescribedMedicationViewController = 
				loader.getController();
			PrescribedMedication newPrescribedMedication = 
				addPrescribedMedicationViewController.getNewlyAddedPrescribedMedication();
			if (newPrescribedMedication != null) {
				prescribedMedicationTable.getItems().add(newPrescribedMedication);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void editPrescribedMedication() {
		PrescribedMedication selectedPrescribedMedication =
			prescribedMedicationTable.getSelectionModel().getSelectedItem();
		if (selectedPrescribedMedication != null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().
												   getResource("/UpdatePrescribedMedicationView.fxml"));
				Parent root = loader.load();
				UpdatePrescribedMedicationViewController updatePrescribedMedicationViewController =
					loader.getController();
				updatePrescribedMedicationViewController.
					setPrescribedMedication(selectedPrescribedMedication);

				Stage stage = new Stage();
				stage.setTitle("Update PrescribedMedication");
				stage.setScene(new Scene(root));
				stage.showAndWait();
				
				PrescribedMedication updatedPrescribedMedication =
					updatePrescribedMedicationViewController.getNewlyUpdatedPrescribedMedication();
				int selectedIndex = prescribedMedicationTable.getSelectionModel().getSelectedIndex();
				prescribedMedicationTable.getItems().set(selectedIndex, updatedPrescribedMedication);
				prescribedMedicationTable.refresh();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			notChosen();
		}
    }

    @FXML
    private void deletePrescribedMedication() {
		PrescribedMedication selectedPrescribedMedication =
			prescribedMedicationTable.getSelectionModel().getSelectedItem();
		if (selectedPrescribedMedication != null) {
			if (isConfirmed()) {
				prescribedMedicationDao.delete(selectedPrescribedMedication);
				prescribedMedicationTable.getItems().remove(selectedPrescribedMedication);
			} else {
				error();
			}
		} else {
			notChosen();
		}
	}

	@FXML
	private void findPrescribedMedication() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FindPrescribedMedicationView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Find Prescribed Medication");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			FindPrescribedMedicationViewController findPrescribedMedicationViewController =
				loader.getController();
			List<PrescribedMedication> foundPrescribedMedication =
				findPrescribedMedicationViewController.getNewlyFoundPrescribedMedication();

			if (foundPrescribedMedication != null && !foundPrescribedMedication.isEmpty()) {
				prescribedMedicationTable.setItems(FXCollections.
												  observableArrayList(foundPrescribedMedication));
			} else {
				prescribedMedicationTable.setItems(FXCollections.observableArrayList());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void refreshPrescribedMedication() {
		loadPrescribedMedications();
	}
	
	private boolean isConfirmed() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Deletion confirmation");
		alert.setHeaderText("Prescribed medication deleting");
		alert.setContentText("Are you sure to delete chosen prescribed medication?");
		final Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	private void error() {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setTitle("Deletion error");
		errorAlert.setHeaderText("An error while prescribed medication deleting");
		errorAlert.setContentText("Failed to delete the selected prescribed medication.");
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
