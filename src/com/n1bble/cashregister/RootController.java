package com.n1bble.cashregister;

public class RootController {
    // Ссылка на главное приложение
    private Main main;

    /**
     * Вызывается главным приложением, чтобы оставить ссылку на самого себя.
     *
     * @param main
     */
    public void setMainApp(Main main) {
        this.main = main;
    }
}
