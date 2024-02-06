package com.lab2.controller;

import java.io.IOException;

import java.util.Optional;
import java.util.List;

import com.lab2.dao.ProcedureDao;
import com.lab2.entities.Procedure;
import com.lab2.controller.AddProcedureViewController;
import com.lab2.controller.UpdateProcedureViewController;
import com.lab2.controller.FindProcedureViewController;

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
		Procedure selectedProcedure = procedureTable.getSelectionModel().getSelectedItem();
		if (selectedProcedure != null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateProcedureView.fxml"));
				Parent root = loader.load();
				UpdateProcedureViewController updateProcedureViewController = loader.getController();
				updateProcedureViewController.setProcedure(selectedProcedure);

				Stage stage = new Stage();
				stage.setTitle("Update Procedure");
				stage.setScene(new Scene(root));
				stage.showAndWait();
				
				Procedure updatedProcedure = updateProcedureViewController.getNewlyUpdatedProcedure();
				int selectedIndex = procedureTable.getSelectionModel().getSelectedIndex();
				procedureTable.getItems().set(selectedIndex, updatedProcedure);
				procedureTable.refresh();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			notChosen();
		}
    }

    @FXML
    private void deleteProcedure() {
		Procedure selectedProcedure = procedureTable.getSelectionModel().getSelectedItem();
		if (selectedProcedure != null) {
			if (isConfirmed()) {
				procedureDao.delete(selectedProcedure);
				procedureTable.getItems().remove(selectedProcedure);
			} else {
				error();
			}
		} else {
			notChosen();
		}
    }

	@FXML
	private void findProcedure() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FindProcedureView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Find Procedure");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			FindProcedureViewController findProcedureViewController = loader.getController();
			List<Procedure> foundProcedure = findProcedureViewController.getNewlyFoundProcedure();

			if (foundProcedure != null && !foundProcedure.isEmpty()) {
				procedureTable.setItems(FXCollections.observableArrayList(foundProcedure));
			} else {
				procedureTable.setItems(FXCollections.observableArrayList());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void refreshProcedure() {
		loadProcedures();
	}

	private boolean isConfirmed() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Deletion confirmation");
		alert.setHeaderText("Procedure deleting");
		alert.setContentText("Are you sure to delete chosen procedure?");
		final Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	private void error() {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setTitle("Deletion error");
		errorAlert.setHeaderText("An error while procedure deleting");
		errorAlert.setContentText("Failed to delete the selected procedure.");
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
