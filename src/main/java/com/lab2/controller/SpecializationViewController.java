package com.lab2.controller;

import java.io.IOException;

import com.lab2.dao.SpecializationDao;
import com.lab2.entities.Specialization;
import com.lab2.controller.AddSpecializationViewController;

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

    }
}
