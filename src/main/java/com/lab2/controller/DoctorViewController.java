package com.lab2.controller;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.lab2.dao.DoctorDao;
import com.lab2.dao.SpecializationDao;
import com.lab2.entities.Doctor;
import com.lab2.entities.Specialization;
import com.lab2.controller.AddDoctorViewController;
import com.lab2.controller.UpdateDoctorViewController;
import com.lab2.controller.FindDoctorViewController;

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
		try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddDoctorView.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Add New Doctor");
        stage.setScene(new Scene(root));
        stage.showAndWait();

		AddDoctorViewController addDoctorViewController = loader.getController();
		Doctor newDoctor = addDoctorViewController.getNewlyAddedDoctor();
        if (newDoctor != null) {
            doctorTable.getItems().add(newDoctor);
        }

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void editDoctor() {
		Doctor selectedDoctor = doctorTable.getSelectionModel().getSelectedItem();
		if (selectedDoctor != null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateDoctorView.fxml"));
				Parent root = loader.load();
				UpdateDoctorViewController updateDoctorViewController = loader.getController();
				updateDoctorViewController.setDoctor(selectedDoctor);

				Stage stage = new Stage();
				stage.setTitle("Update Doctor");
				stage.setScene(new Scene(root));
				stage.showAndWait();
				
				Doctor updatedDoctor = updateDoctorViewController.getNewlyUpdatedDoctor();
				int selectedIndex = doctorTable.getSelectionModel().getSelectedIndex();
				doctorTable.getItems().set(selectedIndex, updatedDoctor);
				doctorTable.refresh();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			notChosen();
		}
    }

    @FXML
    private void deleteDoctor() {
		Doctor selectedDoctor = doctorTable.getSelectionModel().getSelectedItem();
		if (selectedDoctor != null) {
			if (isConfirmed()) {
				doctorDao.delete(selectedDoctor);
				doctorTable.getItems().remove(selectedDoctor);
			} else {
				error();
			}
		} else {
			notChosen();
		}
	}

	@FXML
	private void findDoctor() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FindDoctorView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Find Doctor");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			FindDoctorViewController findDoctorViewController = loader.getController();
			List<Doctor> foundDoctor = findDoctorViewController.getNewlyFoundDoctor();

			if (foundDoctor != null && !foundDoctor.isEmpty()) {
				doctorTable.setItems(FXCollections.observableArrayList(foundDoctor));
			} else {
				doctorTable.setItems(FXCollections.observableArrayList());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void refreshDoctor() {
		loadDoctors();
	}
	
	private boolean isConfirmed() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Deletion confirmation");
		alert.setHeaderText("Doctor deleting");
		alert.setContentText("Are you sure to delete chosen doctor?");
		final Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	private void error() {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setTitle("Deletion error");
		errorAlert.setHeaderText("An error while doctor deleting");
		errorAlert.setContentText("Failed to delete the selected doctor.");
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
