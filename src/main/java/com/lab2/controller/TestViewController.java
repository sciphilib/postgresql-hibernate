package com.lab2.controller;

import java.io.IOException;

import com.lab2.dao.TestDao;
import com.lab2.entities.Test;
import com.lab2.controller.AddTestViewController;

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

    }

    @FXML
    private void deleteTest() {

    }
}
