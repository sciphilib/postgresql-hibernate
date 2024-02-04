package com.lab2.controller;

import java.io.IOException;

import java.util.Optional;

import com.lab2.dao.SpecializationDao;
import com.lab2.entities.Specialization;
import com.lab2.controller.AddSpecializationViewController;

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

public class SpecializationViewController {
    @FXML
    private TableView<Specialization> specializationTable;

    @FXML
    private TableColumn<Specialization, Integer> index;

    @FXML
    private TableColumn<Specialization, String> name;

    private SpecializationDao specializationDao;

    @FXML
    private void initialize() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        setupIndexCell();

        loadSpecializations();
    }

    private void loadSpecializations() {
        specializationDao = new SpecializationDao();
        specializationTable.setItems(FXCollections.observableArrayList(specializationDao.findAll()));
    }

    private void setupIndexCell() {
        index.setCellFactory(column -> new TableCell<Specialization, Integer>() {
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
    private void addSpecialization() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddSpecializationView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Add New Specialization");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			AddSpecializationViewController addSpecializationViewController = loader.getController();
			Specialization newSpecialization = addSpecializationViewController.getNewlyAddedSpecialization();
			if (newSpecialization != null) {
				specializationTable.getItems().add(newSpecialization);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void editSpecialization() {

    }

    @FXML
    private void deleteSpecialization() {
		Specialization selectedSpecialization = specializationTable.getSelectionModel().getSelectedItem();
		if (selectedSpecialization != null) {
			if (isConfirmed()) {
				specializationDao.delete(selectedSpecialization);
				specializationTable.getItems().remove(selectedSpecialization);
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
		alert.setHeaderText("Specialization deleting");
		alert.setContentText("Are you sure to delete chosen specialization?");
		final Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	private void error() {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setTitle("Deletion error");
		errorAlert.setHeaderText("An error while specialization deleting");
		errorAlert.setContentText("Failed to delete the selected specialization.");
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
