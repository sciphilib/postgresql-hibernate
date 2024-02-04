package com.lab2.controller;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lab2.dao.*;
import com.lab2.entities.*;
import com.lab2.controller.AddDiagnosisViewController;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DiagnosisViewController {
    @FXML
    private TableView<Diagnosis> diagnosisTable;

    @FXML
    private TableColumn<Diagnosis, Integer> index;

    @FXML
    private TableColumn<Diagnosis, String> visitPatient;

	@FXML
    private TableColumn<Diagnosis, String> visitDoctor;

	@FXML
    private TableColumn<Diagnosis, String> diagnosis;

	private VisitDao visitDao;
	private DoctorDao doctorDao;
	private PatientDao patientDao;
	private DiagnosisDao diagnosisDao;
	private Map<Integer, String> diagnosisMap;

    public void showDiagnosisTable() {
        initialize();
    }

    @FXML
    private void initialize() {
		diagnosis.setCellValueFactory(new PropertyValueFactory<>("description"));

		visitDao = new VisitDao();

		setupIndexCell();
		setupVisitPatientCell();
		setupVisitDoctorCell();
		
        loadDiagnosis();
    }

	private void setupVisitPatientCell() {
		patientDao = new PatientDao();
		visitPatient.setCellFactory(column -> new TableCell<Diagnosis, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
			
            if (empty) {
                setText(null);
            } else {
                Diagnosis diagnosis = getTableView().getItems().get(getIndex());
                Visit visit = visitDao.findById(diagnosis.getIdVisit());
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
		visitDoctor.setCellFactory(column -> new TableCell<Diagnosis, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
			
            if (empty) {
                setText(null);
            } else {
                Diagnosis diagnosis = getTableView().getItems().get(getIndex());
                Visit visit = visitDao.findById(diagnosis.getIdVisit());
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

	private Map<Integer, String> loadDiagnosisMap() {
		diagnosisDao = new DiagnosisDao();
        List<Diagnosis> diagnosis = diagnosisDao.findAll();
        return diagnosis.stream()
			.collect(Collectors.
					 toMap(Diagnosis::getId, Diagnosis::getDescription));
    }

    private void setupIndexCell() {
        index.setCellFactory(column -> new TableCell<Diagnosis, Integer>() {
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

    private void loadDiagnosis() {
        diagnosisDao = new DiagnosisDao();
        diagnosisTable.setItems(FXCollections.
								observableArrayList(diagnosisDao.
													findAll()));
    }

    @FXML
    private void addDiagnosis() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddDiagnosisView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Add New Diagnosis");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			AddDiagnosisViewController addDiagnosisViewController = loader.getController();
			Diagnosis newDiagnosis = addDiagnosisViewController.getNewlyAddedDiagnosis();
			if (newDiagnosis != null) {
				diagnosisTable.getItems().add(newDiagnosis);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void editDiagnosis() {

    }

    @FXML
    private void deleteDiagnosis() {

    }
}
