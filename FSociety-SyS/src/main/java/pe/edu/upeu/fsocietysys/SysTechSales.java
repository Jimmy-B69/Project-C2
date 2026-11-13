package pe.edu.upeu.fsocietysys;

import javafx.application.Application;

/**
 * Clase principal de inicio SysTechSales referenciada en la especificación técnica base.
 * Hereda de TechStore permitiendo iniciar la aplicación JavaFX bajo cualquiera de las dos denominaciones.
 */
public class SysTechSales extends TechStore {

    public static void main(String[] args) {
        Application.launch(SysTechSales.class, args);
    }
}
