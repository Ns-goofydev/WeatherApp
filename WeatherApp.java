package Weather;
//GUI Imports
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
//Connection and Input Imports
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//Data pulling Imports
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherApp extends Application {
    private TextField locationField;
    private Button getWeatherButton;
    private TextArea weatherArea;
//GUI Design
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Weather App");

        locationField = new TextField();
        getWeatherButton = new Button("Get Weather");
        weatherArea = new TextArea();

        getWeatherButton.setOnAction(e -> {
            String location = locationField.getText();
            String weatherInfo = getWeatherInfo(location);
            weatherArea.setText(weatherInfo);
        });

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(locationField, getWeatherButton, weatherArea);

        BorderPane root = new BorderPane();
        root.setCenter(vBox);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
//GUI end
    //Connection and API
    private static String getWeatherInfo(String location) {
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=20a71dac574ae8fc20694ac31b1b924c");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            //Data Pulling
            JSONObject json = new JSONObject(response.toString());
            JSONObject main = json.getJSONObject("main");
            JSONArray weatherArray = json.getJSONArray("weather");
            JSONObject weather = weatherArray.getJSONObject(0);

            String cityName = json.getString("name");
            double temperatureKelvin = main.getDouble("temp");
            double temperatureCelsius = temperatureKelvin - 273.15;
            String weatherDescription = weather.getString("description");
            JSONObject coord = json.getJSONObject("coord");
            double longitude = coord.getDouble("lon");
            double latitude = coord.getDouble("lat");

            StringBuilder formattedWeather = new StringBuilder();
            formattedWeather.append("City: ").append(cityName).append("\n");
            formattedWeather.append("Temperature: ").append(temperatureCelsius).append("°C\n");
            formattedWeather.append("Weather: ").append(weatherDescription).append("\n");
            formattedWeather.append("Longitude: ").append(longitude).append("\n");
            formattedWeather.append("Latitude: ").append(latitude).append("\n");

            return formattedWeather.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}