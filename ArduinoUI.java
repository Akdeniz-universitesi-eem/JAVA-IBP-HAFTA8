import com.fazecast.jSerialComm.SerialPort;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ArduinoUI {
    private static JLabel lastChanges = new JLabel("deneme");
    private static JButton led1BTN = new JButton("LED 1");
    private static JButton led2BTN = new JButton("LED 2");
    private static JButton led3BTN = new JButton("LED 3");
    private static JButton led4BTN = new JButton("LED 4");
    private static JButton led5BTN = new JButton("LED 5");
    private static JButton exitBTN = new JButton("Çık");

    public static void arduinoUI() {
        SerialPort serialPort = SerialPort.getCommPort("COM1");
        serialPort.setComPortParameters(9600, 8, 1, 0); // Baudrate: 9600, Data bits: 8, Stop bits: 1, Parity: None
        serialPort.openPort(); //

        JFrame appScreen = new JFrame("Arduino LED Kontrol Uygulaması");

        led1BTN.setBounds(18, 25, 200, 24);
        led2BTN.setBounds(18, 57, 200, 24);
        led3BTN.setBounds(18, 89, 200, 24);
        led4BTN.setBounds(18, 121, 200, 24);
        led5BTN.setBounds(18, 153, 200, 24);
        lastChanges.setBounds(18, 300, 200, 24);
        exitBTN.setBounds(18, 325, 200, 24);

        appScreen.add(led1BTN);
        appScreen.add(led2BTN);
        appScreen.add(led3BTN);
        appScreen.add(led4BTN);
        appScreen.add(led5BTN);
        appScreen.add(lastChanges);
        appScreen.add(exitBTN);

        led1BTN.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String ledNum = "1";
                lastChanges.setText("LED 1 durumu değiştirildi.");
                serialPort.writeBytes(ledNum.getBytes(), ledNum.length());
            }
        });
        led2BTN.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String ledNum = "2";
                lastChanges.setText("LED 2 durumu değiştirildi.");
                serialPort.writeBytes(ledNum.getBytes(), ledNum.length());
            }
        });
        led3BTN.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String ledNum = "3";
                lastChanges.setText("LED 3 durumu değiştirildi.");
                serialPort.writeBytes(ledNum.getBytes(), ledNum.length());
            }
        });
        led4BTN.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String ledNum = "4";
                lastChanges.setText("LED 4 durumu değiştirildi.");
                serialPort.writeBytes(ledNum.getBytes(), ledNum.length());
            }
        });
        led5BTN.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String ledNum = "5";
                lastChanges.setText("LED 5 durumu değiştirildi.");
                serialPort.writeBytes(ledNum.getBytes(), ledNum.length());
            }
        });

        appScreen.setSize(248,400);
        appScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        appScreen.setLayout(null);
        appScreen.setVisible(true);

        exitBTN.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                serialPort.closePort();
                System.exit(0);
            }
        });
    }
}
