package ic.doc.camera;

public class Camera {

  private final Sensor cameraSensor;
  private final MemoryCard memoryCard;
  private final WriteListener writeListener;
  private boolean power = false;
  private boolean dataIsBeingWritten;

  public Camera(Sensor sensor, MemoryCard memory, WriteListener listener, boolean powerCamera) {
    cameraSensor = sensor;
    memoryCard = memory;
    writeListener = listener;
    power = powerCamera;
  }

  public Camera(
      Sensor sensor,
      MemoryCard memory,
      WriteListener listener,
      boolean powerCamera,
      boolean dataCopyState) {
    cameraSensor = sensor;
    memoryCard = memory;
    writeListener = listener;
    power = powerCamera;
    dataIsBeingWritten = dataCopyState;
  }

  public Camera(Sensor sensor, MemoryCard memory, WriteListener listener) {
    cameraSensor = sensor;
    memoryCard = memory;
    writeListener = listener;
  }

  public void pressShutter() {
    if (power) {
      byte[] data = cameraSensor.readData();
      dataIsBeingWritten = true;
      memoryCard.write(data);
      writeListener.writeComplete();
      dataIsBeingWritten = false;
      powerOff();
    }
  }

  public void powerOn() {
    if (!power) {
      power = true;
      cameraSensor.powerUp();
    }
  }

  public void powerOff() {
    if (power) {
      power = false;
      if (!dataIsBeingWritten) {
        cameraSensor.powerDown();
      }
    }
  }
}
