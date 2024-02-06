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

public class FindPrescribedMedicationViewController {

	public List<PrescribedMedication> getNewlyFoundPrescribedMedication() {
		return foundPrescribedMedication;
	}
	
	@FXML
    private ComboBox<Visit> visitComboBox;

	@FXML
    private ComboBox<Medication> medicationComboBox;

	private VisitDao visitDao;
	private MedicationDao medicationDao;
	private PrescribedMedicationDao prescribedMedicationDao;
	private List<PrescribedMedication> foundPrescribedMedication;

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
    private void handleFindPrescribedMedication() {
		Visit visit = visitComboBox.getValue();
		Integer idVisit;
		if (visit != null) {
			idVisit = visit.getId();
		} else {
			idVisit = null;
		}

		Medication medication = medicationComboBox.getValue();
		Integer idMedication;
		if (medication != null) {
			idMedication = medication.getId();
		} else {
			idMedication = null;
		}

		PrescribedMedication examplePrescribedMedication =
			new PrescribedMedication(1, idVisit, idMedication);
		foundPrescribedMedication = prescribedMedicationDao.findByExample(examplePrescribedMedication);

		closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) medicationComboBox.getScene().getWindow();
        stage.close();
    }
}
