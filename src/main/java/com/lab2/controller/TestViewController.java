package com.lab2.controller;

import java.io.IOException;

import java.util.Optional;
import java.util.List;

import com.lab2.dao.TestDao;
import com.lab2.entities.Test;
import com.lab2.controller.AddTestViewController;
import com.lab2.controller.UpdateTestViewController;
import com.lab2.controller.FindTestViewController;

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

public class TestViewController {
    @FXML
    private TableView<Test> testTable;

    @FXML
    private TableColumn<Test, Integer> index;

    @FXML
    private TableColumn<Test, String> name;

    private TestDao testDao;

    @FXML
    private void initialize() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        setupIndexCell();

        loadTests();
    }

    private void loadTests() {
        testDao = new TestDao();
        testTable.setItems(FXCollections.observableArrayList(testDao.findAll()));
    }

    private void setupIndexCell() {
        index.setCellFactory(column -> new TableCell<Test, Integer>() {
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
    private void addTest() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddTestView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Add New Test");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			AddTestViewController addTestViewController = loader.getController();
			Test newTest = addTestViewController.getNewlyAddedTest();
			if (newTest != null) {
				testTable.getItems().add(newTest);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void editTest() {
		Test selectedTest = testTable.getSelectionModel().getSelectedItem();
		if (selectedTest != null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateTestView.fxml"));
				Parent root = loader.load();
				UpdateTestViewController updateTestViewController = loader.getController();
				updateTestViewController.setTest(selectedTest);

				Stage stage = new Stage();
				stage.setTitle("Update Test");
				stage.setScene(new Scene(root));
				stage.showAndWait();
				
				Test updatedTest = updateTestViewController.getNewlyUpdatedTest();
				int selectedIndex = testTable.getSelectionModel().getSelectedIndex();
				testTable.getItems().set(selectedIndex, updatedTest);
				testTable.refresh();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			notChosen();
		}
    }

    @FXML
    private void deleteTest() {
		Test selectedTest = testTable.getSelectionModel().getSelectedItem();
		if (selectedTest != null) {
			if (isConfirmed()) {
				testDao.delete(selectedTest);
				testTable.getItems().remove(selectedTest);
			} else {
				error();
			}
		} else {
			notChosen();
		}
	}

	@FXML
	private void findTest() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FindTestView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Find Test");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			FindTestViewController findTestViewController = loader.getController();
			List<Test> foundTest = findTestViewController.getNewlyFoundTest();

			if (foundTest != null && !foundTest.isEmpty()) {
				testTable.setItems(FXCollections.observableArrayList(foundTest));
			} else {
				testTable.setItems(FXCollections.observableArrayList());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void refreshTest() {
		loadTests();
	}
	
	private boolean isConfirmed() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Deletion confirmation");
		alert.setHeaderText("Test deleting");
		alert.setContentText("Are you sure to delete chosen test?");
		final Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	private void error() {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setTitle("Deletion error");
		errorAlert.setHeaderText("An error while test deleting");
		errorAlert.setContentText("Failed to delete the selected test.");
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
