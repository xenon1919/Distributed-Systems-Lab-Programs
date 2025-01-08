import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PresentationTier extends Application {
    private ApplicationLogic appLogic;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        appLogic = new ApplicationLogic();
        primaryStage.setTitle("3-Tier App for BookSelf");

        VBox layout = new VBox(10);
        Scene scene = new Scene(layout, 300, 200);

        Button addButton = new Button("Add Book");
        Button listButton = new Button("List Books");
        ListView<String> itemList = new ListView<>();

        addButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Book");
            dialog.setHeaderText("Enter Book name:");
            dialog.setContentText("Book Name:");
            dialog.showAndWait().ifPresent(item -> {
                appLogic.addItem(item);
            });
        });

        listButton.setOnAction(e -> {
            itemList.getItems().setAll(appLogic.getItems());
        });

        layout.getChildren().addAll(addButton, listButton, itemList);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

