package com.lab2.controller;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.lab2.dao.*;
import com.lab2.entities.*;
import com.lab2.controller.AddPatientViewController;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;

public class PatientViewController {
    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, Integer> index;

    @FXML
    private TableColumn<Patient, Integer> lastName;

    @FXML
    private TableColumn<Patient, Integer> firstName;

    @FXML
    private TableColumn<Patient, Integer> middleName;

    @FXML
    private TableColumn<Patient, String> address;

	private PatientDao patientDao;


    public void showPatientTable() {
        initialize();
    }

    @FXML
    private void initialize() {
		lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		middleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
		address.setCellValueFactory(new PropertyValueFactory<>("address"));

		setupIndexCell();
		
        loadPatients();
    }

    private void setupIndexCell() {
        index.setCellFactory(column -> new TableCell<Patient, Integer>() {
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

    private void loadPatients() {
        patientDao = new PatientDao();
        patientTable.setItems(FXCollections.observableArrayList(patientDao.findAll()));
    }

    @FXML
    private void addPatient() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddPatientView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Add New Patient");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			AddPatientViewController addPatientViewController = loader.getController();
			Patient newPatient = addPatientViewController.getNewlyAddedPatient();
			if (newPatient != null) {
				patientTable.getItems().add(newPatient);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void editPatient() {

    }

    @FXML
    private void deletePatient() {
		Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
		if (selectedPatient != null) {
			if (isConfirmed()) {
				patientDao.delete(selectedPatient);
				patientTable.getItems().remove(selectedPatient);
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
		alert.setContentText("Please chose entity you want to delete");
		alert.showAndWait();
	}
}
