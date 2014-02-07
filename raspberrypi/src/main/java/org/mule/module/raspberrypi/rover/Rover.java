package org.mule.module.raspberrypi.rover;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Rover
{


    private GpioController gpio;
    private GpioPinDigitalOutput goLeftEngine;
    private GpioPinDigitalOutput goRightEngine;
    private GpioPinDigitalOutput backLeftEngine;
    private GpioPinDigitalOutput backRightEngine;


    private State currentState;

    private boolean engineRunning = false;


    public Rover()
    {
    }

    public synchronized void startEngine()
    {
        if (!engineRunning)
        {
            System.out.println("Starting rover");

            // create gpio controller
            gpio = GpioFactory.getInstance();
            // provision gpio pin #01 as an output pin and turn on

            goLeftEngine = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "GoLeft", PinState.LOW);
            goRightEngine = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "GoRight", PinState.LOW);
            backLeftEngine = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "BackLeft", PinState.LOW);
            backRightEngine = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "BackRight", PinState.LOW);

            stop();
            engineRunning = true;
        }
    }


    public void forward()
    {
        //Left Part
        goLeftEngine.setState(PinState.HIGH);
        backLeftEngine.setState(PinState.LOW);

        //Right Part
        goRightEngine.setState(PinState.HIGH);
        backRightEngine.setState(PinState.LOW);

        setCurrentState(State.FORWARD);

    }

    public void rotateLeft()
    {
        goLeftEngine.setState(PinState.LOW);
        backLeftEngine.setState(PinState.HIGH);


        goRightEngine.setState(PinState.HIGH);
        backRightEngine.setState(PinState.LOW);
        setCurrentState(State.LEFT);
    }

    public void rotateRight()
    {
        goLeftEngine.setState(PinState.HIGH);
        backLeftEngine.setState(PinState.LOW);

        goRightEngine.setState(PinState.LOW);
        backRightEngine.setState(PinState.HIGH);
        setCurrentState(State.RIGHT);
    }

    public void backwards()
    {
        goLeftEngine.setState(PinState.LOW);
        backLeftEngine.setState(PinState.HIGH);

        goRightEngine.setState(PinState.LOW);
        backRightEngine.setState(PinState.HIGH);
        setCurrentState(State.BACKWARDS);
    }


    public void stop()
    {
        goLeftEngine.setState(PinState.LOW);
        backLeftEngine.setState(PinState.LOW);

        goRightEngine.setState(PinState.LOW);
        backRightEngine.setState(PinState.LOW);

        setCurrentState(State.STOP);

    }


    public State getCurrentState()
    {
        return currentState;
    }


    public synchronized void stopEngine()
    {
        if (engineRunning)
        {
            stop();
            gpio.shutdown();
            engineRunning = false;
        }
    }

    public void setCurrentState(State currentState)
    {
        this.currentState = currentState;
    }
}
