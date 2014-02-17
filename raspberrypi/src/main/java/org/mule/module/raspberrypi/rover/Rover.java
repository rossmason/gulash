package org.mule.module.raspberrypi.rover;

public interface Rover
{

    void startEngine();

    void forward();

    void rotateLeft();

    void rotateRight();

    void backwards();

    void stop();

    Direction getCurrentDirection();

    void stopEngine();

}
