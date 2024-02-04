package com.lab2.controller;

import java.io.IOException;

import com.lab2.dao.MedicationDao;
import com.lab2.entities.Medication;
import com.lab2.controller.AddMedicationViewController;

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

public class MedicationViewController {
    @FXML
    private TableView<Medication> medicationTable;

    @FXML
    private TableColumn<Medication, Integer> index;

    @FXML
    private TableColumn<Medication, Integer> id;

    @FXML
    private TableColumn<Medication, String> name;

    private MedicationDao medicationDao;

    @FXML
    private void initialize() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        setupIndexCell();

        loadMedications();
    }

    private void loadMedications() {
        medicationDao = new MedicationDao();
        medicationTable.setItems(FXCollections.observableArrayList(medicationDao.findAll()));
    }

    private void setupIndexCell() {
        index.setCellFactory(column -> new TableCell<Medication, Integer>() {
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

    @FXML
    private void addMedication() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddMedicationView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Add New Medication");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			AddMedicationViewController addMedicationViewController = loader.getController();
			Medication newMedication = addMedicationViewController.getNewlyAddedMedication();
			if (newMedication != null) {
				medicationTable.getItems().add(newMedication);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void editMedication() {

    }

    @FXML
    private void deleteMedication() {

    }
}
