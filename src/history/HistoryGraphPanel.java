package history;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HistoryGraphPanel extends JPanel {

    private final HistoryBuffer cpu;
    private final HistoryBuffer ram;
    private final HistoryBuffer disk;

    public HistoryGraphPanel(HistoryBuffer cpu, HistoryBuffer ram, HistoryBuffer disk) {
        this.cpu = cpu;
        this.ram = ram;
        this.disk = disk;
        setPreferredSize(new Dimension(500, 200));
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAxis(g);
        drawLine(g, cpu.getSnapshot(), Color.RED);
        drawLine(g, ram.getSnapshot(), Color.GREEN);
        drawLine(g, disk.getSnapshot(), Color.CYAN);
        drawLegend(g);
    }

    private void drawAxis(Graphics g) {
        g.setColor(Color.GRAY);
        g.drawLine(40, 10, 40, getHeight() - 20);
        g.drawLine(40, getHeight() - 20, getWidth() - 10, getHeight() - 20);
    }

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

    private void drawLegend(Graphics g) {
        g.setColor(Color.RED);
        g.drawString("CPU", 50, 15);
        g.setColor(Color.GREEN);
        g.drawString("RAM", 90, 15);
        g.setColor(Color.CYAN);
        g.drawString("DISK", 140, 15);
    }
}

