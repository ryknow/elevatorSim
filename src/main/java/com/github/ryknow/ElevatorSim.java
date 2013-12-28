package com.github.ryknow;

public class ElevatorSim {

  public static void main(String[] args) {
    Elevator elevator1 = new Elevator(10, "*****************");
    Elevator elevator2 = new Elevator(10, "*********");

    Elevator.pressCallButton(6);
    Elevator.pressCallButton(3);
    Elevator.pressCallButton(2);
    Elevator.pressCallButton(1);
    Elevator.pressCallButton(6);

    Thread t  = new Thread(elevator1);
    Thread t2 = new Thread(elevator2);
    t.start();
    t2.start();

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Elevator.pressCallButton(5);
    Elevator.pressCallButton(8);
    Elevator.pressCallButton(1);
  }
}
