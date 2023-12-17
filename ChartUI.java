import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChartUI {

    private static StringBuilder buffer = new StringBuilder();
    private static SerialPort serialPort;
    private static XYSeries series;
    private static JButton exitBTN = new JButton("Çık");

    public static void main(String[] args) {
        series = new XYSeries("Potansiyometrede Ölçülen Değer");

        openSerialPort();

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Proteus Arduino Simülasyonunda Potansiyometre Değer Ölçümü", "Zaman", "Potansiyometrede Ölçülen Değer",
                new XYSeriesCollection(series)
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);

        XYSplineRenderer renderer = new XYSplineRenderer();
        plot.setRenderer(renderer);

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(false);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, 1100);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 600));

        JFrame window = new JFrame("Arduino Potansiyometre Ölçüm Uygulaması");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.add(chartPanel, BorderLayout.CENTER);
        exitBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeSerialPort();
            }
        });
        window.add(exitBTN, BorderLayout.SOUTH);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private static void openSerialPort() {
        serialPort = SerialPort.getCommPort("COM1");
        serialPort.setBaudRate(9600);

        if (serialPort.openPort()) {
            System.out.println("Porta bağlantı sağlandı...");

            serialPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }

                @Override
                public void serialEvent(com.fazecast.jSerialComm.SerialPortEvent event) {
                    if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                        byte[] newData = new byte[serialPort.bytesAvailable()];
                        serialPort.readBytes(newData, newData.length);

                        for (byte b : newData) {
                            char c = (char) b;
                            if (c == '\n' || c == '\r') {
                                processBuffer();
                                buffer.setLength(0);
                            } else buffer.append(c);
                        }

                    }
                }
            });
        } else System.out.println("Porta bağlantı başarısız!");
    }

    private static void processBuffer() {
        String receivedData = buffer.toString().trim();
        if (!receivedData.isEmpty()) {
            int analogValue = Integer.parseInt(receivedData);
            series.addOrUpdate(series.getItemCount(), analogValue);
        }
    }

    private static void closeSerialPort() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Disconnected from Arduino");
        } System.exit(0);
    }
}
