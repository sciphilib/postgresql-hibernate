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

    }

    @FXML
    private void editPatient() {

    }

    @FXML
    private void deletePatient() {

    }
}
