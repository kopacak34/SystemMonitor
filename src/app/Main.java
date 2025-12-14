package app;

import ui.MainWindow;

/**
 * Vstupní bod aplikace System Monitor.
 * <p>
 * Třída {@code Main} obsahuje metodu {@code main}, která slouží
 * ke spuštění celé aplikace. Inicializuje hlavní okno aplikace
 * a předá řízení grafickému rozhraní.
 * </p>
 */
public class Main {

    /**
     * Hlavní metoda aplikace.
     * <p>
     * Spouští grafické uživatelské rozhraní systému monitorování
     * prostřednictvím třídy {@link MainWindow}.
     * </p>
     *
     * @param args argumenty příkazové řádky (nejsou využívány)
     */
    public static void main(String[] args) {
        new MainWindow().start();
    }
}
