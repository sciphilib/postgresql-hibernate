package com.lab2.controller;

import com.lab2.dao.MedicationDao;
import com.lab2.entities.Medication;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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

    }

    @FXML
    private void editMedication() {

    }

    @FXML
    private void deleteMedication() {

    }
}
