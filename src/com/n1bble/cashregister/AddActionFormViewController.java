package com.n1bble.cashregister;

import com.n1bble.cashregister.models.CashVoucher;
import com.n1bble.cashregister.models.Product;
import com.n1bble.cashregister.models.VoucherPosition;
import com.n1bble.cashregister.statics.Global;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddActionFormViewController {

    private Stage dialogStage;
    private CashVoucher cashVoucher;
    private boolean okClicked = false;
    private Main main;
    @FXML
    private Label priceLabel, amountDiscLabel, priceWithNds;

    @FXML
    private TextField quantityField;

    @FXML
    private ChoiceBox<Product> productChoiceBox;

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        ChangeListener<Product> changeListener = new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue<? extends Product> observable, //
                                Product oldValue, Product newValue) {
                if (newValue != null) {
//                    priceLabel.setText(String.valueOf(newValue.getPrice()));
//                    System.out.println(newValue.getAmountDiscount());
                    amountDiscLabel.setText(String.valueOf(newValue.getAmountDiscount()));
                    if(newValue.isHaveDiscount()){
                        priceLabel.setText(String.valueOf(Global.round(newValue.getPrice()-(newValue.getPrice()*newValue.getAmountDiscount()/100), 2)));
                        priceWithNds.setText(String.valueOf(Global.round(newValue.getPrice()-(newValue.getPrice()*newValue.getAmountDiscount()/100), 2)));
                    }else{
                        priceLabel.setText(String.valueOf(newValue.getPrice()));
                        priceWithNds.setText(String.valueOf(Global.round(newValue.getPrice()+(newValue.getPrice()*newValue.getNds()/100), 2)));
                    }

                }
            }
        };
        // Selected Item Changed.
        productChoiceBox.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }


    /**
     * Устанавливает сцену для этого окна.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Задаёт адресата, информацию о котором будем менять.
     *
     * @param voucherPosition
     */
    public void setCashVoucher(VoucherPosition voucherPosition) {
        this.cashVoucher = cashVoucher;
    }

    /**
     * Returns true, если пользователь кликнул OK, в другом случае false.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке OK.
     */
    @FXML
    private void handleAdd() {
        if (isInputValid()) {
            okClicked = true;
//            System.out.println(productChoiceBox.getValue().getName());
            //заполняем массив позиций чека)0)00)0
            main.getPositions().add(new VoucherPosition(
                    Float.valueOf(priceLabel.getText()),
                    String.valueOf(productChoiceBox.getValue().getName()),
                    Integer.valueOf(quantityField.getText()),
                    Float.valueOf(amountDiscLabel.getText()),
                    Float.valueOf(priceWithNds.getText())
            ));
            dialogStage.close();
        }
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке Cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Проверяет пользовательский ввод в текстовых полях.
     *
     * @return true, если пользовательский ввод корректен
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (productChoiceBox.getValue() == null || productChoiceBox.getValue().getName().length() == 0) {
            errorMessage += "Не выбран продукт!\n";
        }
        if (priceLabel.getText() == null || priceLabel.getText().length() == 0) {
            errorMessage += "Неверно задана цена!\n";
        }
        if (quantityField.getText() == null || quantityField.getText().length() == 0) {
            errorMessage += "Неверно задано количество!\n";
        }else {
            // пытаемся преобразовать почтовый код в int.
            try {
                if(Integer.parseInt(quantityField.getText())<0){
                    errorMessage += "Невозможно добавить отрицательное количество позиции!\n";
                }else{
                    Integer.parseInt(quantityField.getText());
                }
            } catch (NumberFormatException e) {
                errorMessage += "Поле 'Количество', является численным!\n"
                        + "Введите число пожалуйста.\n";
            }
        }
        if (amountDiscLabel.getText() == null || amountDiscLabel.getText().length() == 0) {
            errorMessage += "Неверно задана процентная ставка!\n";
        }
//
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Показываем сообщение об ошибке.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Ошибка в заполнении");
            alert.setHeaderText("Пожалуйста заполните поля корректно");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }


    /**
     * Вызывается главным приложением, чтобы оставить ссылку на самого себя.
     *
     * @param main
     */
    public void setMainApp(Main main) {
        this.main = main;
        productChoiceBox.setItems(main.getProducts());

    }
}
