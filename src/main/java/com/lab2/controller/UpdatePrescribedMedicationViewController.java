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

public class UpdatePrescribedMedicationViewController {

	public PrescribedMedication getNewlyUpdatedPrescribedMedication() {
		return newPrescribedMedication;
	}

	public void setPrescribedMedication(PrescribedMedication prescribedMedication) {
		newPrescribedMedication = prescribedMedication;
		Visit visit = visitDao.findById(prescribedMedication.getIdVisit());
		visitComboBox.setValue(visit);
		Medication medication = medicationDao.findById(prescribedMedication.getIdMedication());
		medicationComboBox.setValue(medication);
	}
	
	@FXML
    private ComboBox<Visit> visitComboBox;

	@FXML
    private ComboBox<Medication> medicationComboBox;

	private VisitDao visitDao;
	private MedicationDao medicationDao;
	private PrescribedMedicationDao prescribedMedicationDao;
	private PrescribedMedication newPrescribedMedication;

    @FXML
    private void initialize() {
		prescribedMedicationDao = new PrescribedMedicationDao();
		loadVisits();
		loadMedications();
    }

	private void loadVisits() {
		visitDao = new VisitDao();
		visitComboBox.setItems(FXCollections.observableArrayList(visitDao.findAll()));
		visitComboBox.setConverter(new VisitStringConverter());
	}

	private void loadMedications() {
		medicationDao = new MedicationDao();
		medicationComboBox.setItems(FXCollections.observableArrayList(medicationDao.findAll()));
		medicationComboBox.setConverter(new MedicationStringConverter());
	}

    @FXML
    private void handleUpdatePrescribedMedication() {
		Visit visit = visitComboBox.getValue();
		Medication medication = medicationComboBox.getValue();
		newPrescribedMedication = new PrescribedMedication(newPrescribedMedication.getId(),
														   visit.getId(), medication.getId());

		prescribedMedicationDao.save(newPrescribedMedication);
		closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) medicationComboBox.getScene().getWindow();
        stage.close();
    }
}
