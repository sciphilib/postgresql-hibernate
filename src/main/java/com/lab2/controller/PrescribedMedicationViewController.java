package com.lab2.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lab2.dao.*;
import com.lab2.entities.*;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
										   observableArrayList(prescribedMedicationDao.
															   findAll()));
    }

    @FXML
    private void addPrescribedMedication() {

    }

    @FXML
    private void editPrescribedMedication() {

    }

    @FXML
    private void deletePrescribedMedication() {

    }
}
