package com.lab2.controller;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.lab2.controller.AddPrescribedProcedureViewController;
import com.lab2.controller.UpdatePrescribedProcedureViewController;
import com.lab2.controller.FindPrescribedProcedureViewController;

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

public class PrescribedProcedureViewController {
    @FXML
    private TableView<PrescribedProcedure> prescribedProcedureTable;

    @FXML
    private TableColumn<PrescribedProcedure, Integer> index;

    @FXML
    private TableColumn<PrescribedProcedure, String> visitPatient;

	@FXML
    private TableColumn<PrescribedProcedure, String> visitDoctor;

	@FXML
    private TableColumn<PrescribedProcedure, Integer> procedure;

	@FXML
    private TableColumn<PrescribedProcedure, Integer> count;

	private VisitDao visitDao;
	private DoctorDao doctorDao;
	private PatientDao patientDao;
	private ProcedureDao procedureDao;
	private PrescribedProcedureDao prescribedProcedureDao;
	private Map<Integer, String> procedureMap;

    public void showPrescribedProcedureTable() {
        initialize();
    }

    @FXML
    private void initialize() {
		procedure.setCellValueFactory(new PropertyValueFactory<>("idProcedure"));
		count.setCellValueFactory(new PropertyValueFactory<>("count"));

		visitDao = new VisitDao();

		setupIndexCell();
		setupVisitPatientCell();
		setupVisitDoctorCell();
		setupProcedureCell();
		
        loadPrescribedProcedures();
    }

	private void setupProcedureCell() {
		procedureMap = loadProcedureMap();
        procedure.setCellFactory(column -> new TableCell<PrescribedProcedure, Integer>() {
            @Override
            protected void updateItem(Integer idProcedure, boolean empty) {
                super.updateItem(idProcedure, empty);

                if (empty || idProcedure == null) {
                    setText(null);
                } else {
                    String procedure =
						procedureMap.getOrDefault(idProcedure, "Unknown");
                    setText(procedure);
                }
            }
		});
	}

	private void setupVisitPatientCell() {
		patientDao = new PatientDao();
		visitPatient.setCellFactory(column -> new TableCell<PrescribedProcedure, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
			
            if (empty) {
                setText(null);
            } else {
                PrescribedProcedure prescribedProcedure = getTableView().getItems().get(getIndex());
                Visit visit = visitDao.findById(prescribedProcedure.getIdVisit());
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
		visitDoctor.setCellFactory(column -> new TableCell<PrescribedProcedure, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
			
            if (empty) {
                setText(null);
            } else {
                PrescribedProcedure prescribedProcedure = getTableView().getItems().get(getIndex());
                Visit visit = visitDao.findById(prescribedProcedure.getIdVisit());
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

	private Map<Integer, String> loadProcedureMap() {
		procedureDao = new ProcedureDao();
        List<Procedure> procedures = procedureDao.findAll();
        return procedures.stream()
			.collect(Collectors.
					 toMap(Procedure::getId, Procedure::getName));
    }

    private void setupIndexCell() {
        index.setCellFactory(column -> new TableCell<PrescribedProcedure, Integer>() {
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

    private void loadPrescribedProcedures() {
        prescribedProcedureDao = new PrescribedProcedureDao();
        prescribedProcedureTable.setItems(FXCollections.
										  observableArrayList(prescribedProcedureDao.
															  findAll()));
    }

    @FXML
    private void addPrescribedProcedure() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddPrescribedProcedureView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Add New PrescribedProcedure");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			AddPrescribedProcedureViewController addPrescribedProcedureViewController = 
				loader.getController();
			PrescribedProcedure newPrescribedProcedure = 
				addPrescribedProcedureViewController.getNewlyAddedPrescribedProcedure();
			if (newPrescribedProcedure != null) {
				prescribedProcedureTable.getItems().add(newPrescribedProcedure);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void editPrescribedProcedure() {
		PrescribedProcedure selectedPrescribedProcedure =
			prescribedProcedureTable.getSelectionModel().getSelectedItem();
		if (selectedPrescribedProcedure != null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().
												   getResource("/UpdatePrescribedProcedureView.fxml"));
				Parent root = loader.load();
				UpdatePrescribedProcedureViewController updatePrescribedProcedureViewController = 
					loader.getController();
				updatePrescribedProcedureViewController.setPrescribedProcedure(selectedPrescribedProcedure);

				Stage stage = new Stage();
				stage.setTitle("Update PrescribedProcedure");
				stage.setScene(new Scene(root));
				stage.showAndWait();
				
				PrescribedProcedure updatedPrescribedProcedure =
					updatePrescribedProcedureViewController.getNewlyUpdatedPrescribedProcedure();
				int selectedIndex = prescribedProcedureTable.getSelectionModel().getSelectedIndex();
				prescribedProcedureTable.getItems().set(selectedIndex, updatedPrescribedProcedure);
				prescribedProcedureTable.refresh();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			notChosen();
		}
    }

    @FXML
    private void deletePrescribedProcedure() {
		PrescribedProcedure selectedPrescribedProcedure =
			prescribedProcedureTable.getSelectionModel().getSelectedItem();
		if (selectedPrescribedProcedure != null) {
			if (isConfirmed()) {
				prescribedProcedureDao.delete(selectedPrescribedProcedure);
				prescribedProcedureTable.getItems().remove(selectedPrescribedProcedure);
			} else {
				error();
			}
		} else {
			notChosen();
		}
	}

	@FXML
	private void findPrescribedProcedure() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FindPrescribedProcedureView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Find PrescribedProcedure");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			FindPrescribedProcedureViewController findPrescribedProcedureViewController =
				loader.getController();
			List<PrescribedProcedure> foundPrescribedProcedure =
				findPrescribedProcedureViewController.getNewlyFoundPrescribedProcedure();

			if (foundPrescribedProcedure != null && !foundPrescribedProcedure.isEmpty()) {
				prescribedProcedureTable.setItems(FXCollections.
												  observableArrayList(foundPrescribedProcedure));
			} else {
				prescribedProcedureTable.setItems(FXCollections.observableArrayList());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void refreshPrescribedProcedure() {
		loadPrescribedProcedures();
	}
	
	private boolean isConfirmed() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Deletion confirmation");
		alert.setHeaderText("PrescribedProcedure deleting");
		alert.setContentText("Are you sure to delete chosen prescribedProcedure?");
		final Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	private void error() {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setTitle("Deletion error");
		errorAlert.setHeaderText("An error while prescribedProcedure deleting");
		errorAlert.setContentText("Failed to delete the selected prescribedProcedure.");
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
