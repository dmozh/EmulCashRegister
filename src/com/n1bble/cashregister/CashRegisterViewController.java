package com.n1bble.cashregister;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n1bble.cashregister.models.CashVoucher;
import com.n1bble.cashregister.models.VoucherPosition;
import com.n1bble.cashregister.statics.Global;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


public class CashRegisterViewController {

    // Ссылка на главное приложение
    private Main main;

    @FXML
    public ListView<String> listProductsView;

    @FXML
    private ChoiceBox<String> payMethChoiceBox;

    @FXML
    private  Label voucherSum;

    @FXML
    private TextArea addressArea;

    @FXML
    private TextField cashier;

    float sum = 0;
    int backCount = 0;
    int responseCode;
    private final static ObservableList<String> paymentMethods = FXCollections.observableArrayList();

    public CashRegisterViewController(){

    }

    @FXML
    private void addAction(){
//        System.out.println("This is add action");
        //Тут открываем окно и регистрируем новую позицию в чек
        boolean okClicked = main.showCashVoucherDialog();
        //Заполнениет списка очень тупое, в качестве ид я использую размера массива с инкрементом -1
        //КАТЫЛИ КАСТЫЛИ ХРОМОЙ ПРОЕКТ ОПЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯТЬ
        if(okClicked){
            //текущая позиция которая будет добавлена в листвь но уже есть в маинпозишен
            VoucherPosition curPos = main.getPositions().get(main.getPositions().size()-1);
            backCount = 0;
            int currentCount = 0;
            //цикл
            //пробегаемся по всему массиву
            //сравниваем предыдующие позиции
            //с новой
            if(main.getPositions().size()>1) {
                VoucherPosition backPos = main.getPositions().get(main.getPositions().size()-2);
                for (int i = 0; i < main.getPositions().size() - 1; i++) {
                    if (main.getPositions().get(i).getName() == curPos.getName()) {
                        System.out.println("Повторяющиеся позиции: " + main.getPositions().get(i).getName() + " " + i);
                        currentCount = currentCount + main.getPositions().get(i).getCount();
                        main.getPositions().remove(i);
//                        System.out.println("size main.positions = " + main.getPositions().size());
//                        System.out.println("size listview = "+ listProductsView.getItems().size());
//                        System.out.println("i = "+i);
                        listProductsView.getItems().remove(i);
                    }

                }
                //backCount это прошлое количество товара
                //требуется для создании суммы и ну собственно для удаления товара
                backCount = currentCount;
                currentCount = currentCount + curPos.getCount();
                System.out.println(currentCount);
                curPos.setCount(currentCount);

                listProductsView.getItems().add(main.getPositions().get(main.getPositions().size()-1).getName() + " " +main.getPositions().get(main.getPositions().size()-1).getPriceWithNDS() + " "
                        + main.getPositions().get(main.getPositions().size()-1).getCount());

            }else{
                listProductsView.getItems().add(main.getPositions().get(main.getPositions().size()-1).getName() + " " +main.getPositions().get(main.getPositions().size()-1).getPriceWithNDS() + " "
                        + main.getPositions().get(main.getPositions().size()-1).getCount());
            }


            System.out.println(main.getPositions().size());
            sum = summary(sum, main.getPositions().size()-1, "sum", backCount);
            voucherSum.setText(String.valueOf(Global.round(sum, 2)));
        }
    }

    private String getComputerName() {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "Unknown Computer";
    }


    @FXML
    private void sendAction(){
        boolean shipped = false;
        if(isValidInput()) {
            //увелечение номера чека
            main.setNumVoucher(main.getNumVoucher() + 1);
            //Тут происходит генерация пост запрос
            //Но сначала генерируется json при помощи либы gson
            // а перед этим собираются все остальные данные в модель CashVoucher
            //Получение даты
            LocalDateTime dateTime = LocalDateTime.now();
            //стандартный формат даты
//            System.out.println(dateTime.format(DateTimeFormatter.ofPattern("d.MMM.uuuu HH:mm:ss")));

            //Создание эксземпляра чека
            main.getCashVouchers().add(new CashVoucher(
                    main.getPositions(),
                    dateTime.format(DateTimeFormatter.ofPattern("d.MM.uuuu")),
                    dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    payMethChoiceBox.getValue(),
                    sum,
                    addressArea.getText(),
                    getComputerName(),
                    cashier.getText(),
                    main.getNumVoucher()
            ));

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            System.out.println(gson.toJson(main.getCashVouchers().get(0)));
            System.out.println("This is send action");

            if(sendingVoucher(gson.toJson(main.getCashVouchers().get(0)))){
                shipped = true;

                Global.showAlert("Успешно",
                        "Чек успешно отправлен",
                        "=========================================",
                        Alert.AlertType.INFORMATION);
            }else {
                shipped = false;

                Global.showAlert("Ошибка",
                        "Чек не был отправлен",
                        "Один из компонентов системы временно не работает.\n"+
                        "Сообщите о проблеме системнону администратору или службе технической поддержки по телефону +6(666)666-66-66",
                        Alert.AlertType.ERROR);
            }

        }
        //проверка, если успешно отправлено, мы очищаем поля, и удаляем все нннннннннннахер
        if(shipped) {
            //Очищаем все нахер чтобы было четко красиво и ваще ахуенно
            listProductsView.getItems().clear();
            main.getPositions().clear();
            sum = 0;
            voucherSum.setText("0.0");
            addressArea.setText("");
            main.getCashVouchers().remove(0);
            shipped = false;
        }
    }


    /**
     * Функция на удаление позиции
     * Внутри используется еще одна функция summary, где передается ключ rem, текущая сумма, индекс позиции, и прошлое количество
     */
    @FXML
    private void deleteAction(){
        System.out.println("This is delete action");
        int selectedIndex = listProductsView.getSelectionModel().getSelectedIndex();
        System.out.println(selectedIndex);
        if(selectedIndex != -1){
            listProductsView.getItems().remove(selectedIndex);
            sum = Global.round(summary(sum, selectedIndex, "rem", backCount),2);
            main.getPositions().remove(selectedIndex);
            voucherSum.setText(String.valueOf(Global.round(sum, 2)));
        }else{
            Global.showAlert("Ошибка!!!",
                    "Не выбрана позиция для удаления",
                    "Выберите позицию которую хотите удалить.",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void modifyAction(){
        System.out.println("This is modify action");
        System.out.println(getComputerName());
    }

    @FXML
    private void initialize() {
        paymentMethods.addAll("Безналичный расчет", "Наличный расчет");
    }
    /**
     * Вызывается главным приложением, чтобы оставить ссылку на самого себя.
     *
     * @param main
     */
    public void setMainApp(Main main) {
        this.main = main;
        payMethChoiceBox.setItems(paymentMethods);
    }

    private boolean isValidInput(){
        String errorMessage = "";
        if (addressArea.getText() == null || addressArea.getText().length() == 0) {
            errorMessage += "Не заполнено поле адрес!\n";
        }
        if (payMethChoiceBox.getValue() == null || payMethChoiceBox.getValue().length() == 0){
            errorMessage += "Не выбран тип оплаты!\n";
        }
        if(listProductsView.getItems()==null || listProductsView.getItems().size() == 0){
            errorMessage += "Пустой чек! Нет ни одной позиции в чеке!\n";
        }

        if(errorMessage.length()==0){
            return true;
        }else{
            Global.showAlert("Ошибка в заполнении!!!",
                    "Пожалуйста заполните поля корректно",
                    errorMessage,
                    Alert.AlertType.ERROR);
            return false;
        }
    }

    /**
     * Вызывается для расчета итоговой суммы
     *
     * @param sum, index, key, backCount
     */
    private float summary(float sum,  int index, String key, int backCount){
        if(key.equals("sum")) {
//            if()
            sum = (sum + main.getPositions().get(index).getPriceWithNDS() * main.getPositions().get(index).getCount())-main.getPositions().get(index).getPriceWithNDS()*backCount;
        }else{
            sum = sum - main.getPositions().get(index).getPriceWithNDS() * main.getPositions().get(index).getCount();
        }
        return sum;
    }



    /**
     * Вызывается для http post запроса
     *
     * @param json
     */
    private boolean sendingVoucher(String json) {
        String api = "senddatatofirestore/";
        HttpURLConnection con = null;
        try {
            URL url = new URL(Global.HOST + api);
            con = (HttpURLConnection) url.openConnection();

            // optional default is POST
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("User-Agent", Global.USER_AGENT);
            con.setRequestProperty("Accept-Charset", "UTF-8");

            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            //Send request
            OutputStreamWriter wr = new OutputStreamWriter(
                    con.getOutputStream(), "utf-8");
            wr.write(json);
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + Global.HOST + api);
            System.out.println("Response Code : " + responseCode);



            //Get Response
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            System.out.println(response.toString());
//            JSONObject obje = new JSONObject(response.toString());
//            System.out.println("JSONobj " + obje);
//            System.out.println(con.getHeaderFields());
//            System.out.println(con.getResponseMessage());

//            return response.toString();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            Global.showAlert("Ошибка!!!",
                    "Ошибка соединения с http-сервером",
                    "Сервер вернул код: " + responseCode + "\n"
                            +"",
                    Alert.AlertType.ERROR);
            return false;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

}
