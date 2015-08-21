package com.rimusdesign.messaging.app.controller;


import com.rimusdesign.messaging.app.AppPM;


/**
 * @author Rimas Krivickas.
 */
public abstract class AbstractController {


    private AppPM presentationModel;


    protected abstract void doSetup ();


    public void setPresentationModel (AppPM presentationModel) {

        this.presentationModel = presentationModel;
        doSetup();
    }


    protected AppPM getPresentationModel () {

        return presentationModel;
    }

}
