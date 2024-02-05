package com.lab2.controller;

import java.io.IOException;

import java.util.Optional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.lab2.controller.AddTestResultViewController;
import com.lab2.controller.UpdateTestResultViewController;

import com.lab2.dao.*;
import com.lab2.entities.*;

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

public class TestResultViewController {
    @FXML
    private TableView<TestResult> testResultTable;

    @FXML
    private TableColumn<TestResult, Integer> index;

    @FXML
    private TableColumn<TestResult, String> visitPatient;

	@FXML
    private TableColumn<TestResult, String> visitDoctor;

	@FXML
    private TableColumn<TestResult, String> testName;

	@FXML
    private TableColumn<TestResult, String> testResult;

	private TestDao testDao;
	private VisitDao visitDao;
	private DoctorDao doctorDao;
	private PatientDao patientDao;
	private TestResultDao testResultDao;

    public void showTestResultTable() {
        initialize();
    }

    @FXML
    private void initialize() {
		testResult.setCellValueFactory(new PropertyValueFactory<>("result"));

		visitDao = new VisitDao();

		setupIndexCell();
		setupVisitPatientCell();
		setupVisitDoctorCell();
		setupTestCell();
		
        loadTestResults();
    }

	private void setupTestCell() {
		testDao = new TestDao();
		testName.setCellFactory(column -> new TableCell<TestResult, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
			
            if (empty) {
                setText(null);
            } else {
                TestResult testResult = getTableView().getItems().get(getIndex());
                Test test = testDao.findById(testResult.getIdVisit());
                if (test != null) {
					    setText(test.getName());
                } else {
                    setText("Test not found");
                }
			}
		}
		});
	}

	private void setupVisitPatientCell() {
		patientDao = new PatientDao();
		visitPatient.setCellFactory(column -> new TableCell<TestResult, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
			
            if (empty) {
                setText(null);
            } else {
                TestResult testResult = getTableView().getItems().get(getIndex());
                Visit visit = visitDao.findById(testResult.getIdVisit());
                if (visit != null) {
                    Integer idPatient = visit.getIdPatient();
                    Patient patient = patientDao.findById(idPatient);
                    if (patient != null) {
						String firstName = patient.getFirstName();
						String lastName = patient.getLastName();
						String middleName = patient.getMiddleName();
                        String fullName = lastName + " " + firstName + " " + middleName;
                        setText(fullName);
                    } else {
                        setText("Patient not found");
                    }
                } else {
                    setText("Visit not found");
                }
            }
        }
		});
	}

	private void setupVisitDoctorCell() {
		doctorDao = new DoctorDao();
		visitDoctor.setCellFactory(column -> new TableCell<TestResult, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
			
            if (empty) {
                setText(null);
            } else {
                TestResult testResult = getTableView().getItems().get(getIndex());
                Visit visit = visitDao.findById(testResult.getIdVisit());
                if (visit != null) {
                    Integer idDoctor = visit.getIdDoctor();
                    Doctor doctor = doctorDao.findById(idDoctor);
                    if (doctor != null) {
						String firstName = doctor.getFirstName();
						String lastName = doctor.getLastName();
						String middleName = doctor.getMiddleName();
                        String fullName = lastName + " " + firstName + " " + middleName;
                        setText(fullName);
                    } else {
                        setText("Doctor not found");
                    }
                } else {
                    setText("Visit not found");
                }
            }
        }
		});
	}

	private Map<Integer, String> loadTestMap() {
		testDao = new TestDao();
        List<Test> tests = testDao.findAll();
        return tests.stream()
			.collect(Collectors.
					 toMap(Test::getId, Test::getName));
    }

    private void setupIndexCell() {
        index.setCellFactory(column -> new TableCell<TestResult, Integer>() {
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

    private void loadTestResults() {
        testResultDao = new TestResultDao();
        testResultTable.setItems(FXCollections.
								 observableArrayList(testResultDao.findAll()));
    }

    @FXML
    private void addTestResult() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddTestResultView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Add New Test Result");
			stage.setScene(new Scene(root));
			stage.showAndWait();

			AddTestResultViewController addTestResultViewController = loader.getController();
			TestResult newTestResult = addTestResultViewController.getNewlyAddedTestResult();
			if (newTestResult != null) {
				testResultTable.getItems().add(newTestResult);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void editTestResult() {
		TestResult selectedTestResult = testResultTable.getSelectionModel().getSelectedItem();
		if (selectedTestResult != null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateTestResultView.fxml"));
				Parent root = loader.load();
				UpdateTestResultViewController updateTestResultViewController = loader.getController();
				updateTestResultViewController.setTestResult(selectedTestResult);

				Stage stage = new Stage();
				stage.setTitle("Update TestResult");
				stage.setScene(new Scene(root));
				stage.showAndWait();
				
				TestResult updatedTestResult = updateTestResultViewController.getNewlyUpdatedTestResult();
				int selectedIndex = testResultTable.getSelectionModel().getSelectedIndex();
				testResultTable.getItems().set(selectedIndex, updatedTestResult);
				testResultTable.refresh();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			notChosen();
		}
    }

    @FXML
    private void deleteTestResult() {
		TestResult selectedTestResult = testResultTable.getSelectionModel().getSelectedItem();
		if (selectedTestResult != null) {
			if (isConfirmed()) {
				testResultDao.delete(selectedTestResult);
				testResultTable.getItems().remove(selectedTestResult);
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
		alert.setHeaderText("Test result deleting");
		alert.setContentText("Are you sure to delete chosen test result?");
		final Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	private void error() {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setTitle("Deletion error");
		errorAlert.setHeaderText("An error while test result deleting");
		errorAlert.setContentText("Failed to delete the selected test result.");
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
