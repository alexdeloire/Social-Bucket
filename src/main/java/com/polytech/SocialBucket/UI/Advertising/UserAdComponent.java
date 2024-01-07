package com.polytech.SocialBucket.UI.Advertising;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.time.LocalDate;

import com.polytech.SocialBucket.Logic.Advertising;
import com.polytech.SocialBucket.Logic.Post;
import com.polytech.SocialBucket.Logic.Facade.AdvertisingFacade;
import com.polytech.SocialBucket.UI.FXRouter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class UserAdComponent {

    @FXML
    private Button deleteButton;

    @FXML
    private Label expired;

    @FXML
    private Label resumeText;

    @FXML
    private Label description;

    @FXML
    private VBox details;

    @FXML
    private VBox resume;

    @FXML
    private VBox confirmDeleteBox;

    @FXML
    private ImageView imageContainer;

    @FXML
    private Label link;

    @FXML
    private Label beginDate;

    @FXML
    private Label endDate;

    @FXML
    private Button plusInfoButton;

    @FXML
    private Button hideInfoButton;

    private Runnable refreshAds;

    private AdvertisingFacade advertisingFacade = AdvertisingFacade.getInstance();

    private Advertising advertising;

    @FXML
    private void initialize() {
        resume.setVisible(true);
        details.setVisible(false);
        confirmDeleteBox.setVisible(false);
        plusInfoButton.setVisible(true);
        hideInfoButton.setVisible(false);
        modifyBoxHeight();

    }

    private void modifyBoxHeight() {
        resume.setManaged(resume.isVisible());
        details.setManaged(details.isVisible());
        confirmDeleteBox.setManaged(confirmDeleteBox.isVisible());
    }

    @FXML
    public void handleAdBox() {
        resume.setVisible(!resume.isVisible());
        details.setVisible(!details.isVisible());
        plusInfoButton.setVisible(!plusInfoButton.isVisible());
        hideInfoButton.setVisible(!hideInfoButton.isVisible());
        modifyBoxHeight();
    }

    @FXML
    public void handleConfirmDeleteBox() {
        resume.setVisible(!resume.isVisible());
        details.setVisible(false);
        confirmDeleteBox.setVisible(!confirmDeleteBox.isVisible());
        modifyBoxHeight();
    }

    @FXML
    public void loadAdvertising(Advertising advertising, Runnable refreshAds) {
        this.refreshAds = refreshAds;
        this.advertising = advertising;

        description.setText(advertising.getText());
        resumeText.setText(advertising.getText());
        link.setText("Link : " + advertising.getLink());
        beginDate.setText("Start date : " + advertising.getBegindate().toString());
        endDate.setText("Expired : " + advertising.getEnddate().toString());

        LocalDate today = LocalDate.now();
        if (today.isAfter(advertising.getEnddate())) {
        expired.setVisible(true);
        deleteButton.setVisible(false);
        } else {
        expired.setVisible(false);
        deleteButton.setVisible(true);
        }

        byte[] fileBytes = advertising.getImage();
        ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
        Image image = new Image(bis);
        imageContainer.setImage(image);
    }

    @FXML
        public void handleDeleteAdvertising() {
        int advertisingId = advertising.getId();
        advertisingFacade.deleteAdvertising(advertisingId);
        refreshAds.run();
    }

}
