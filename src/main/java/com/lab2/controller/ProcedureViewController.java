package com.lab2.controller;

import com.lab2.dao.ProcedureDao;
import com.lab2.entities.Procedure;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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

    }

    @FXML
    private void editProcedure() {

    }

    @FXML
    private void deleteProcedure() {

    }
}
