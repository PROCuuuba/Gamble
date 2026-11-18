package org.example.gamble;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.EventQueue;
import java.awt.Taskbar;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class GambleApplication extends Application {
    private static final double DESIGN_WIDTH = 780;
    private static final double DESIGN_HEIGHT = 590;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GambleApplication.class.getResource("gamble-view.fxml"));
        VBox machine = fxmlLoader.load();
        machine.setManaged(false);
        Scale scale = new Scale();
        machine.getTransforms().add(scale);

        Pane viewport = new Pane(machine);
        viewport.widthProperty().addListener(observable -> resizeMachine(viewport, machine, scale));
        viewport.heightProperty().addListener(observable -> resizeMachine(viewport, machine, scale));

        Scene scene = new Scene(viewport, DESIGN_WIDTH, DESIGN_HEIGHT);
        stage.setTitle("Три мешочка");
        stage.setMinWidth(540);
        stage.setMinHeight(430);
        stage.setScene(scene);
        URL icon = GambleApplication.class.getResource("app-icon.png");
        stage.getIcons().add(new Image(icon.toExternalForm()));
        stage.show();
        setTaskbarIcon(ImageIO.read(icon));
    }

    @Override
    public void stop() {
        GameSound.close();
    }

    private void setTaskbarIcon(BufferedImage icon) {
        EventQueue.invokeLater(() -> {
            if (!Taskbar.isTaskbarSupported())
                return;

            Taskbar taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE))
                taskbar.setIconImage(icon);
        });
    }

    private void resizeMachine(Pane viewport, VBox machine, Scale scale) {
        double width = viewport.getWidth();
        double height = viewport.getHeight();
        double factor = Math.min(width / DESIGN_WIDTH, height / DESIGN_HEIGHT);
        if (factor <= 0)
            return;

        machine.resize(width / factor, height / factor);
        scale.setX(factor);
        scale.setY(factor);
    }
}
