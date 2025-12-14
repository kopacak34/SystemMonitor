package history;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel pro grafické zobrazení historických dat systému.
 * <p>
 * Zobrazuje průběh využití CPU, RAM a disku v čase
 * pomocí čárového grafu. Data jsou čerpána z objektů
 * {@link HistoryBuffer}, které uchovávají poslední hodnoty.
 * </p>
 *
 * <p>
 * Barvy grafů:
 * <ul>
 *     <li>CPU – červená</li>
 *     <li>RAM – zelená</li>
 *     <li>Disk – tyrkysová</li>
 * </ul>
 * </p>
 *
 * <p>
 * Panel je určen k pravidelnému překreslování (např. pomocí
 * {@link javax.swing.Timer}) v hlavním okně aplikace.
 * </p>
 */
public class HistoryGraphPanel extends JPanel {

    /** Historie využití CPU */
    private final HistoryBuffer cpu;

    /** Historie využití RAM */
    private final HistoryBuffer ram;

    /** Historie využití disku */
    private final HistoryBuffer disk;

    /**
     * Vytvoří panel pro vykreslení grafů historie systému.
     *
     * @param cpu  historie hodnot CPU
     * @param ram  historie hodnot RAM
     * @param disk historie hodnot disku
     */
    public HistoryGraphPanel(HistoryBuffer cpu, HistoryBuffer ram, HistoryBuffer disk) {
        this.cpu = cpu;
        this.ram = ram;
        this.disk = disk;
        setPreferredSize(new Dimension(500, 200));
        setBackground(Color.BLACK);
    }

    /**
     * Překreslení komponenty.
     * <p>
     * Vykreslí osy grafu, jednotlivé datové řady
     * a legendu.
     * </p>
     *
     * @param g grafický kontext
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAxis(g);
        drawLine(g, cpu.getSnapshot(), Color.RED);
        drawLine(g, ram.getSnapshot(), Color.GREEN);
        drawLine(g, disk.getSnapshot(), Color.CYAN);
        drawLegend(g);
    }

    /**
     * Vykreslí osy grafu (X a Y).
     *
     * @param g grafický kontext
     */
    private void drawAxis(Graphics g) {
        g.setColor(Color.GRAY);
        g.drawLine(40, 10, 40, getHeight() - 20);               // osa Y
        g.drawLine(40, getHeight() - 20, getWidth() - 10, getHeight() - 20); // osa X
    }

    /**
     * Vykreslí jednu datovou řadu do grafu.
     *
     * @param g     grafický kontext
     * @param data  seznam hodnot (0–100)
     * @param color barva čáry
     */
    private void drawLine(Graphics g, List<Integer> data, Color color) {
        if (data.size() < 2) return;

        g.setColor(color);
        int w = getWidth() - 50;
        int h = getHeight() - 30;
        int step = w / (data.size() - 1);

        for (int i = 0; i < data.size() - 1; i++) {
            int x1 = 40 + i * step;
            int y1 = getHeight() - 20 - data.get(i) * h / 100;
            int x2 = 40 + (i + 1) * step;
            int y2 = getHeight() - 20 - data.get(i + 1) * h / 100;
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * Vykreslí legendu grafu s popisem jednotlivých křivek.
     *
     * @param g grafický kontext
     */
    private void drawLegend(Graphics g) {
        g.setColor(Color.RED);
        g.drawString("CPU", 50, 15);
        g.setColor(Color.GREEN);
        g.drawString("RAM", 90, 15);
        g.setColor(Color.CYAN);
        g.drawString("DISK", 140, 15);
    }
}
