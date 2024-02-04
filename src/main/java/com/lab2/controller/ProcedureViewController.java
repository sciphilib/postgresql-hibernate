package com.lab2.controller;

import java.io.IOException;

import com.lab2.dao.ProcedureDao;
import com.lab2.entities.Procedure;
import com.lab2.controller.AddProcedureViewController;

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

public class ProcedureViewController {
    @FXML
    private TableView<Procedure> procedureTable;

    @FXML
    private TableColumn<Procedure, Integer> index;

    @FXML
    private TableColumn<Procedure, String> name;

    private ProcedureDao procedureDao;

    @FXML
    private void initialize() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        setupIndexCell();

        loadProcedures();
    }

    private void loadProcedures() {
        procedureDao = new ProcedureDao();
        procedureTable.setItems(FXCollections.observableArrayList(procedureDao.findAll()));
    }

    private void setupIndexCell() {
        index.setCellFactory(column -> new TableCell<Procedure, Integer>() {
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
    private void addProcedure() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddProcedureView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Add New Procedure");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			AddProcedureViewController addProcedureViewController = loader.getController();
			Procedure newProcedure = addProcedureViewController.getNewlyAddedProcedure();
			if (newProcedure != null) {
				procedureTable.getItems().add(newProcedure);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void editProcedure() {

    }

    @FXML
    private void deleteProcedure() {

    }
}
