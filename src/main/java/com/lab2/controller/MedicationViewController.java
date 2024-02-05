package com.lab2.controller;

import java.io.IOException;

import java.util.Optional;
import com.lab2.dao.MedicationDao;
import com.lab2.entities.Medication;
import com.lab2.controller.AddMedicationViewController;
import com.lab2.controller.UpdateMedicationViewController;

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
		Medication selectedMedication = medicationTable.getSelectionModel().getSelectedItem();
		if (selectedMedication != null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateMedicationView.fxml"));
				Parent root = loader.load();
				UpdateMedicationViewController updateMedicationViewController = loader.getController();
				updateMedicationViewController.setMedication(selectedMedication);

				Stage stage = new Stage();
				stage.setTitle("Update Medication");
				stage.setScene(new Scene(root));
				stage.showAndWait();
				
				Medication updatedMedication = updateMedicationViewController.getNewlyUpdatedMedication();
				int selectedIndex = medicationTable.getSelectionModel().getSelectedIndex();
				medicationTable.getItems().set(selectedIndex, updatedMedication);
				medicationTable.refresh();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			notChosen();
		}
    }

    @FXML
    private void deleteMedication() {
		Medication selectedMedication = medicationTable.getSelectionModel().getSelectedItem();
		if (selectedMedication != null) {
			if (isConfirmed()) {
				medicationDao.delete(selectedMedication);
				medicationTable.getItems().remove(selectedMedication);
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
		alert.setHeaderText("Medication deleting");
		alert.setContentText("Are you sure to delete chosen medication?");
		final Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	private void error() {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setTitle("Deletion error");
		errorAlert.setHeaderText("An error while medication deleting");
		errorAlert.setContentText("Failed to delete the selected medication.");
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
