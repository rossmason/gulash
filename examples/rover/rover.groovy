import org.mule.module.raspberrypi.rover.MockRover;
import org.mule.module.raspberrypi.rover.Rover;
import static org.mule.module.raspberrypi.builder.RaspberryPi.*;


     
        mule.declare(global(MockRover.class).as("rover"))                
        .declare(
                api("rover.raml")
                        .using(properties("consolePath", "/console", "name", "roverConfig"))
                        .on(request("/motion/forward" , ActionType.PUT).then(rover().forward()))
                        .on(request("/motion/backard" , ActionType.PUT).then(rover().backard()))
                        .on(request("/motion/left" , ActionType.PUT).then(rover().turnLeft()))
                        .on(request("/motion/right" , ActionType.PUT).then(rover().turnRight()))                        
                      
        );      