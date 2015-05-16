package it.polimi.socket;

import it.polimi.controller.Controller;
import it.polimi.model.Model;
import it.polimi.view.View;

public class GameRoom {
    
    private Model model;
    private Controller controller;
    private View view;
    private ClientManager manager;
    
    GameRoom(){
        this.model = new Model(manager.numeroGiocatori());
        this.view = new View()
    }

}
