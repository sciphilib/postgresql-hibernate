package com.lab2.controller;

import java.io.IOException;

import java.util.List;
import java.util.Map;
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

    }
}
