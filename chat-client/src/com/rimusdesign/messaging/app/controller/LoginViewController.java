package com.rimusdesign.messaging.app.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * @author Rimas Krivickas.
 */
public class LoginViewController extends AbstractController {


    @FXML
    private TextField usernameInput;

    @FXML
    private TextField chatGroupInput;

    @FXML
    private Button loginButton;


    private boolean isAllowedToLogIn () {

        boolean response = usernameInput.getText().length() >= 4 && chatGroupInput.getText().length() > 0;
        loginButton.setDisable(!response);

        return response;
    }


    private void doLogin () {

        if (isAllowedToLogIn()) {
            getPresentationModel().login(usernameInput.getText(), chatGroupInput.getText());
        }
    }


    @FXML
    private void onUsernameEnter (ActionEvent event) {

        doLogin();
    }


    @FXML
    private void onChatGroupEnter (ActionEvent event) {

        doLogin();
    }


    @FXML
    private void onUsernameKeyReleased (KeyEvent event) {

        isAllowedToLogIn();
    }


    @FXML
    private void onChatGroupKeyReleased (KeyEvent event) {

        isAllowedToLogIn();
    }


    @FXML
    private void onLoginClick (ActionEvent event) {

        doLogin();
    }


    @FXML
    private void onLoginKeyRelease (KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER) doLogin();
    }


    @Override
    protected void doSetup () {

        isAllowedToLogIn();
        getPresentationModel().username.bindBidirectional(usernameInput.textProperty());
        getPresentationModel().chatGroup.bindBidirectional(chatGroupInput.textProperty());
    }
}
