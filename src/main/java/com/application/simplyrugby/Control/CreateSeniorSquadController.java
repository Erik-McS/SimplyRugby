package com.application.simplyrugby.Control;

import com.application.simplyrugby.System.ObsListFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CreateSeniorSquadController {

    @FXML
    ComboBox<String> cblooseHP, cbThightHead, cbHooker, cbLock1, cbLock2, cbBlindside, cbNumber8, cbOpenside, cbLeftwing, cbScrumhalf,
            cbFlyhalf, cbincentre, cboutcentre, cbrightwing, cbfullback, cbplayer1, cbplayer2, cbplayer3, cbplayer4, cbplayer5;
    @FXML
    Button bCreateSquad, bCancel;
    @FXML
    Pane firstPane;
    @FXML
    TextField txSquadName;

    public void initialize() {

        firstPane.getStyleClass().add("bckg1");
        bCancel.setOnAction((event) -> {
            firstPane.getChildren().clear();
        });

        ArrayList<ComboBox<String>> boxes = new ArrayList<>();
        Collections.addAll(boxes, cblooseHP, cbThightHead, cbHooker, cbLock1, cbLock2, cbBlindside, cbNumber8, cbOpenside, cbLeftwing, cbScrumhalf,
                cbFlyhalf, cbincentre, cboutcentre, cbrightwing, cbfullback, cbplayer1, cbplayer2, cbplayer3, cbplayer4, cbplayer5);

        for (ComboBox<String> cbx : boxes) {
            cbx.getStyleClass().add("bckg5");
            cbx.setItems(ObsListFactory.createObsList("AvailablePlayers"));
            cbx.getSelectionModel().select(0);
        }
    }
}
