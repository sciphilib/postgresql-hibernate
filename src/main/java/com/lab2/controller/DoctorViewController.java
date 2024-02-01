package com.lab2.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lab2.dao.DoctorDao;
import com.lab2.dao.SpecializationDao;
import com.lab2.entities.Doctor;
import com.lab2.entities.Specialization;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DoctorViewController {
    @FXML
    private TableView<Doctor> doctorTable;

    @FXML
    private TableColumn<Doctor, Integer> index;

    @FXML
    private TableColumn<Doctor, String> firstName;

    @FXML
    private TableColumn<Doctor, String> lastName;

    @FXML
    private TableColumn<Doctor, String> middleName;

    @FXML
    private TableColumn<Doctor, Integer> specialization;

    private DoctorDao doctorDao;
    private SpecializationDao specializationDao;
    private Map<Integer, String> specializationMap;

    public void showDoctorTable() {
        initialize();
    }

    @FXML
    private void initialize() {
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        middleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        specialization.setCellValueFactory(new PropertyValueFactory<>("idSpec"));

		setupIndexCell();
		setupSpecializationCell();
		
        loadDoctors();
    }

	private void setupSpecializationCell() {
		specializationMap = loadSpecializationMap();
        specialization.setCellFactory(column -> new TableCell<Doctor, Integer>() {
            @Override
            protected void updateItem(Integer idSpecialization, boolean empty) {
                super.updateItem(idSpecialization, empty);

                if (empty || idSpecialization == null) {
                    setText(null);
                } else {
                    String specializationName = specializationMap.getOrDefault(idSpecialization, "Unknown");
                    setText(specializationName);
                }
            }
        });
	}

    private void setupIndexCell() {
        index.setCellFactory(column -> new TableCell<Doctor, Integer>() {
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

    private void loadDoctors() {
        doctorDao = new DoctorDao();
        doctorTable.setItems(FXCollections.observableArrayList(doctorDao.findAll()));
    }

    private Map<Integer, String> loadSpecializationMap() {
        specializationDao = new SpecializationDao();
        List<Specialization> specializations = specializationDao.findAll();
        return specializations.stream()
                .collect(Collectors.toMap(Specialization::getId, Specialization::getName));
    }

    @FXML
    private void addDoctor() {

    }

    @FXML
    private void editDoctor() {

    }

    @FXML
    private void deleteDoctor() {

    }
}
