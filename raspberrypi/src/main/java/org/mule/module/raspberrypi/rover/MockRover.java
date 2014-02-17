package org.mule.module.raspberrypi.rover;

public class MockRover implements Rover
{

    private Direction currentDirection = Direction.STOP;
    private boolean engineRunning = false;

    @Override
    public synchronized void startEngine()
    {
        if (!engineRunning)
        {
            System.out.println("XXXXXXXXXXXX startEngine");
            engineRunning = true;
        }
    }

    @Override
    public void forward()
    {
        System.out.println("XXXXXXXXXXXX forward");
        currentDirection = Direction.FORWARD;
    }

    @Override
    public void rotateLeft()
    {
        System.out.println("XXXXXXXXXXXX left");
        currentDirection = Direction.LEFT;
    }

    @Override
    public void rotateRight()
    {
        System.out.println("XXXXXXXXXXXX right");
        currentDirection = Direction.RIGHT;
    }

    @Override
    public void backwards()
    {
        System.out.println("XXXXXXXXXXXX backwards");
        currentDirection = Direction.BACKWARDS;
    }

    @Override
    public void stop()
    {
        System.out.println("XXXXXXXXXXXX stop");
        currentDirection = Direction.STOP;
    }

    @Override
    public Direction getCurrentDirection()
    {
        return currentDirection;
    }

    @Override
    public synchronized void stopEngine()
    {
        if (engineRunning)
        {
            engineRunning = false;
            System.out.println("XXXXXXXXXXXX stopEngine");
        }
    }
}
