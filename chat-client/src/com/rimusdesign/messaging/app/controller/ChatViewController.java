package com.rimusdesign.messaging.app.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * @author Rimas Krivickas.
 */
public class ChatViewController extends AbstractController {


    @FXML
    private Label nameLabel;

    @FXML
    private TextField inputField;

    @FXML
    private TextArea messagesDisplay;


    @FXML
    private void onDisconnectBtnClick (ActionEvent event) {

        getPresentationModel().disconnect();
    }


    @FXML
    private void onSendBtnClick (ActionEvent event) {

        sendMessage();
    }


    @FXML
    private void onInputFieldEnter (ActionEvent event) {

        sendMessage();
    }


    @FXML
    private void onSendBtnKeyRelease (KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER) sendMessage();
    }


    private void sendMessage () {

        if (inputField.getText().isEmpty()) return;
        getPresentationModel().sendMessage(inputField.getText());
        inputField.setText("");
    }


    @Override
    protected void doSetup () {

        nameLabel.textProperty().bind(getPresentationModel().username);
        messagesDisplay.textProperty().bind(getPresentationModel().messages);
    }
}
