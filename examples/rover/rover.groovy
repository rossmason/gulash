import org.mule.module.runtime.rover.MockRover;
import org.mule.module.runtime.rover.Rover;
import static org.mule.module.RaspberryPi.*;



        mule.declare(global(MockRover.class).as("rover"))                
        .declare(
                api("rover.raml")
                        .on("/motion/forward" , ActionType.PUT).then(rover().forward(),
                        											 log("#[payload]"))
                        .on("/motion/backard" , ActionType.PUT).then(rover().backward())
                        .on("/motion/left" , ActionType.PUT).then(rover().turnLeft())
                        .on("/motion/right" , ActionType.PUT).then(rover().turnRight())                        
                      
        );      