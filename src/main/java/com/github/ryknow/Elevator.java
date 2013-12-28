package com.github.ryknow;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Elevator implements Runnable {
  private String name;
  private int currentFloor;
  private Random rand = new Random();
  private static volatile LinkedList<Integer> _callQueue = new LinkedList<Integer>();
  private LinkedList<Integer> passengerDests = new LinkedList<Integer>();

  public Elevator(int capacity, String name) {
    this.name = name;
  }

  @Override
  public void run() {
    while(_callQueue.size() > 0 || passengerDests.size() > 0) {
      int floor = findClosestFloor(passengerDests.size() > 0 ? passengerDests : _callQueue);
      moveToFloor(floor);
      if (passengerDests.contains(currentFloor)) removePassengers();
    }
  }

  public static synchronized void pressCallButton(int floor) {
    System.out.println("Call button pressed for floor " + floor);
    if (!_callQueue.contains(floor)) _callQueue.add(floor);
  }

  public static synchronized void removeFromCallQueue(int currFloor) {
    if (_callQueue.contains(currFloor)) {
      System.out.println("Removing floor " + currFloor + " from call queue");
      _callQueue.remove((Integer)currFloor);
    }
    System.out.println("CALL QUEUE: " + _callQueue);
  }

  public boolean stopAtFloor(int floor) {
    return passengerDests.contains(floor);
  }

  public void addPassenger() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    int destinationFloor = chooseFloor();
    System.out.println(name + ": Passenger chose floor " + destinationFloor);

    if (destinationFloor != currentFloor) {
      if (!passengerDests.contains(destinationFloor)) {
        passengerDests.add(destinationFloor);
      }
    }
    System.out.println(this);
  }

  public void removePassengers() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println(name + ": Passengers getting off on floor " + currentFloor);
    if (passengerDests.contains(currentFloor))
      passengerDests.remove((Integer)currentFloor);

    System.out.println(this);
  }

  public int findClosestFloor(List<Integer> destinationFloors) {
    System.out.println(name + ": Destination Floors: " + destinationFloors);
    int closestFloor = 99;
    for (int floor : destinationFloors) {
      int diff = Math.abs(currentFloor - floor);
      if (diff < closestFloor) closestFloor = floor;
    }
    System.out.println(name + ": Current floor " + currentFloor);
    System.out.println(name + ": Closest floor " + closestFloor);
    return closestFloor == 99 ? 1 : closestFloor;
  }

  public int chooseFloor() {
    return rand.nextInt(8) + 1;
  }

  public void moveUp() {
    try {
      Thread.sleep(3000);
      System.out.println(name + ": Moving up a floor");
      currentFloor++;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void moveDown() {
    try {
      Thread.sleep(3000);
      System.out.println(name + ": Moving down a floor");
      currentFloor--;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void moveToFloor(int destinationFloor) {
    System.out.println(name + ": CALL QUEUE: " + _callQueue);

    while(destinationFloor != currentFloor) {
      if (stopAtFloor(currentFloor)) removePassengers();

      if (_callQueue.contains(currentFloor)) {
        removeFromCallQueue(currentFloor);
        addPassenger();
      }
    }

    if (_callQueue.contains(currentFloor)) {
      removeFromCallQueue(currentFloor);
      addPassenger();
    }
    System.out.println(name + ": Arrived at destination floor " + destinationFloor);
  }

  public String toString() {
    return name + ": Passengers: " + passengerDests.toString();
  }
}
