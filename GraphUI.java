import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

class GraphUI {
    private static StringBuilder buffer = new StringBuilder();
    private static final String portName = "COM1";

    public static void grapUI() {

        SerialPort serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(9600);
        serialPort.openPort();

        if (serialPort.openPort()) {
            System.out.println("Porta bağlantı sağlandı...");

            serialPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return;

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
            });

            try {
                Thread.sleep(30000); // İşlem devam ederken bekle
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            serialPort.closePort();
        } else System.out.println("Porta bağlantı başarısız!");
    }

    private static void processBuffer() {
        String receivedData = buffer.toString().trim();
        if (!receivedData.isEmpty()) System.out.println("Arduino'dan alınan veri: " + receivedData);
    }
}
