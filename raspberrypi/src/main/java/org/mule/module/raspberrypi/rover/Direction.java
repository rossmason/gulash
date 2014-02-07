package org.mule.module.raspberrypi.rover;

/**
 *
 */
public enum Direction
{
    FORWARD
            {
                @Override
                public void execute(Rover rover)
                {
                    rover.forward();
                }
            },
    LEFT
            {
                @Override
                public void execute(Rover rover)
                {
                    rover.rotateLeft();
                }
            },
    RIGHT
            {
                @Override
                public void execute(Rover rover)
                {
                    rover.rotateRight();
                }
            },
    BACKWARDS
            {
                @Override
                public void execute(Rover rover)
                {
                    rover.backwards();
                }
            },
    STOP
            {
                @Override
                public void execute(Rover rover)
                {
                    rover.stop();
                }
            };

    public abstract void execute(Rover rover);
}
