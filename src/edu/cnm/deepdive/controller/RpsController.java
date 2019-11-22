package edu.cnm.deepdive.controller;

import edu.cnm.deepdive.res.model.Arena;
import edu.cnm.deepdive.view.TerrainView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;

public class RpsController {

  @FXML
  private Button reset;
  @FXML
  private ToggleButton toggleRun;
  @FXML
  private TerrainView terrainView;

  private Arena arena;
  @FXML
  private void initialize(){
    arena = new Arena.Builder().setNumBreeds((byte) 5).setArenaSize(30).build();
    arena.init();
    arena.draw();
  }
  private void
}
