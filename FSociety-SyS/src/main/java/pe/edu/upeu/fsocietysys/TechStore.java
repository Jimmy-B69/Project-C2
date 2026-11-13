package pe.edu.upeu.fsocietysys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pe.edu.upeu.fsocietysys.config.AppContext;

import java.io.IOException;

public class TechStore extends Application {
    private Parent parent;
    private FXMLLoader loader;

    @Override
    public void init() throws Exception {
        System.out.println("Iniciando AppContext de TechStore...");
        AppContext context = AppContext.getInstance();
        System.out.println("Cargando vista principal maingui.fxml...");
        loader = new FXMLLoader(getClass().getResource("/view/maingui.fxml"));
        loader.setControllerFactory(context::getBean);
        parent = loader.load();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        Scene scene = new Scene(parent, Math.min(1280, bounds.getWidth()), Math.min(800, bounds.getHeight() - 40));
        
        if (getClass().getResource("/css/style.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        }
        
        stage.setScene(scene);
        stage.setTitle("TechStore — Sistema de Gestión Comercial Tecnológico");
        stage.setResizable(true);
        stage.show();
    }
}
