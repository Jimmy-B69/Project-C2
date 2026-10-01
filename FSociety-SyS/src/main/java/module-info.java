module pe.edu.upeu.fsocietysys {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires static lombok;
    requires java.logging;
    requires jakarta.validation;

    opens pe.edu.upeu.fsocietysys.controller to javafx.fxml;
    opens pe.edu.upeu.fsocietysys.model;
    opens pe.edu.upeu.fsocietysys.dto;
    opens pe.edu.upeu.fsocietysys to javafx.fxml, javafx.graphics;
    
    exports pe.edu.upeu.fsocietysys;
    exports pe.edu.upeu.fsocietysys.config;
    exports pe.edu.upeu.fsocietysys.controller;
    exports pe.edu.upeu.fsocietysys.model;
    exports pe.edu.upeu.fsocietysys.repository;
    exports pe.edu.upeu.fsocietysys.service;
    exports pe.edu.upeu.fsocietysys.components;
    exports pe.edu.upeu.fsocietysys.dto;
    exports pe.edu.upeu.fsocietysys.enums;
}

