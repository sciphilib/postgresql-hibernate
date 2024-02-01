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

    }

    @FXML
    private void editPrescribedProcedure() {

    }

    @FXML
    private void deletePrescribedProcedure() {

    }
}
