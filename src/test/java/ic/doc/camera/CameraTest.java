package ic.doc.camera;

  import org.jmock.Expectations;
  import org.jmock.integration.junit4.JUnitRuleMockery;
  import org.junit.Rule;
  import org.junit.Test;

public class CameraTest {

  @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

  Sensor sensor = context.mock(Sensor.class);
  MemoryCard memory = context.mock(MemoryCard.class);
  WriteListener listener = context.mock(WriteListener.class);
  Camera cameraDefaultOffInitially = new Camera(sensor, memory, listener);
  Camera cameraOnIntially = new Camera(sensor, memory, listener, true);
  Camera cameraOnInitiallyAndWrittingData = new Camera(sensor, memory, listener, true, true);

  @Test
  public void switchingTheCameraDefaultOffOnPowersUpTheSensor() {
    context.checking(
      new Expectations() {
        {
          exactly(1).of(sensor).powerUp();
        }
      });
    cameraDefaultOffInitially.powerOn();
  }

  @Test
  public void switchingTheCameraInitiallyOnOnDoesNotPowersUpTheSensor() {
    context.checking(
      new Expectations() {
        {
          exactly(0).of(sensor).powerUp();
        }
      });
    cameraOnIntially.powerOn();
  }

  @Test
  public void switchingTheCameraDefaultOffOffDoesNotPowersDownTheSensor() {
    context.checking(
      new Expectations() {
        {
          exactly(0).of(sensor).powerDown();
        }
      });
    cameraDefaultOffInitially.powerOff();
  }

  @Test
  public void switchingTheCameraInitiallyOnOffPowersDownTheSensor() {
    context.checking(
      new Expectations() {
        {
          exactly(1).of(sensor).powerDown();
        }
      });
    cameraOnIntially.powerOff();
  }

  @Test
  public void pressingTheShutterWhenPowerIsOffDoesNothing() {
    context.checking(
      new Expectations() {
        {
          byte[] bytes = exactly(0).of(sensor).readData();
          exactly(0).of(memory).write(bytes);
          exactly(0).of(listener).writeComplete();
        }
      });
    cameraDefaultOffInitially.pressShutter();
  }

  @Test
  public void pressingTheShutterWhenPowerOnCopiesDataFromSensorToMemoryCard() {
    context.checking(
      new Expectations() {
        {
          byte[] bytes = exactly(1).of(sensor).readData();
          exactly(1).of(memory).write(bytes);
          exactly(1).of(listener).writeComplete();
          exactly(1).of(sensor).powerDown();
        }
      });
    cameraOnIntially.pressShutter();
  }

  @Test
  public void switchingCameraOffWhileDataIsBeingWrittenAndCameraIsOnDoesNotPowerDownSensor() {
    context.checking(
      new Expectations() {
        {
          exactly(0).of(sensor).powerDown();
        }
      });
    cameraOnInitiallyAndWrittingData.powerOff();
  }

  @Test
  public void onceWritingDataOnCameraOnHasCompletedTheCameraPowersDownSensor() {
    context.checking(
      new Expectations() {
        {
          byte[] bytes = exactly(1).of(sensor).readData();
          exactly(1).of(memory).write(bytes);
          exactly(1).of(listener).writeComplete();
          exactly(1).of(sensor).powerDown();
        }
      });
    cameraOnIntially.pressShutter();
  }
}
