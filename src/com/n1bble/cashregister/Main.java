package com.n1bble.cashregister;

import com.n1bble.cashregister.models.CashVoucher;
import com.n1bble.cashregister.models.Product;
import com.n1bble.cashregister.models.VoucherPosition;
import com.n1bble.cashregister.statics.Global;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
//import jdk.incubator.http.HttpClient;
//import jdk.incubator.http.HttpRequest;
//import jdk.incubator.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Main extends Application{
    private Stage       primaryStage;
    private BorderPane  rootLayout;

    private ObservableList<Product>         products =      FXCollections.observableArrayList();
    private ObservableList<CashVoucher>     cashVouchers =  FXCollections.observableArrayList();
    private ObservableList<VoucherPosition> positions =     FXCollections.observableArrayList();
    private int numVoucher = 0;

    public int getNumVoucher(){
        return numVoucher;
    }

    public void setNumVoucher(int numVoucher){
        this.numVoucher = numVoucher;
    }

    public ObservableList<VoucherPosition> getPositions() {
        return positions;
    }

    public ObservableList<CashVoucher> getCashVouchers(){
        return cashVouchers;
    }
    public ObservableList<Product> getProducts(){
        return products;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Emul CashRegister");
        try {
            initProductsData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRootLayout();
        showCashRegisterOverview();
    }

    /**
     * Конструктор
     */
    public Main() {
    }

    /**
     * Инициализирует корневой макет.
     */
    public void initRootLayout() {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/Root.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Даём контроллеру доступ к главному прилодению.
            RootController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показываем в руте сцену с инфо о products
     */
    public void showCashRegisterOverview() {
        try {
            // Загружаем сведения об products.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/CashRegisterView.fxml"));
            AnchorPane cashRegisterOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(cashRegisterOverview);

            // Даём контроллеру доступ к главному приложению.
            CashRegisterViewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
            Global.showAlert(
                    "Ошибка!",
                    "Ошибка соединения с сервером",
                    "Сервер вернул код ответа: " + e.getMessage(),
                    Alert.AlertType.ERROR
            );
        }
    }

    /**
     * Возвращает главную сцену.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    // HTTP GET request
    private void initProductsData() throws Exception {

        String api = "getdatafromfirestore/";

        URL obj = new URL(Global.HOST+api);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", Global.USER_AGENT);
        con.setRequestProperty("Accept-Charset", "cp1251");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + Global.HOST+api);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "Cp1251"));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            response.append("\n");
        }
        in.close();

//        System.out.println(response);
//        System.out.println(response.toString());

        JSONObject obje = new JSONObject(response.toString());
        System.out.println("JSONobj " + obje);

        JSONArray array = new JSONArray(obje.getJSONArray("products").toString());
        System.out.println("JArray "+array);

        for(int i = 0; i<array.length(); i++){
            System.out.println(array.getJSONObject(i));
            JSONObject ob = array.getJSONObject(i);
//            System.out.println(ob);
            System.out.println("id = " + ob.getInt("id"));
            System.out.println("name = " + ob.getString("name"));
            System.out.println("price = " +ob.getFloat("price"));

            products.add(new Product(ob.getInt("id"), ob.getString("name"), ob.getFloat("price"), ob.getBoolean("discount"), ob.getInt("amount_discount"), ob.getInt("nds_value")));
        }

        for(int i = 0; i<products.size(); i++){
            System.out.println(products.get(i).getId() + " " + products.get(i).getName() + " " +
                    products.get(i).getPrice() + " " + products.get(i).isHaveDiscount()+ " " +
                    products.get(i).getAmountDiscount());
        }
    }


    public boolean showCashVoucherDialog(/*VoucherPosition voucherPosition*/) {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("views/AddActionFormView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add product");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаём адресата в контроллер.
            AddActionFormViewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
//            controller.setCashVoucher(voucherPosition);
            controller.setMainApp(this);

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
