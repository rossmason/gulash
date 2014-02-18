import org.mule.module.raspberrypi.processor.RoverMotionMessageProcessor;
import org.mule.module.raspberrypi.processor.RoverStatusMessageProcessor;
import org.mule.dsl.builder.core.PrivateFlowBuilder;
import org.mule.module.raspberrypi.rover.Direction;
import org.mule.module.raspberrypi.rover.MockRover;
import org.mule.module.raspberrypi.rover.Rover;
import org.raml.model.ActionType;

     Rover rover = new MockRover();
    
            
        mule.declare(
                api("rover.raml")
                        .using(properties("consolePath", "/console", "name", "roverConfig"))
                        .on(requestFlow(Direction.FORWARD,rover))
                        .on(requestFlow(Direction.BACKWARDS,rover))
                        .on(requestFlow(Direction.LEFT,rover))
                        .on(requestFlow(Direction.RIGHT,rover))
                        .on(requestFlow(Direction.STOP,rover))
                        .on(request("/motion/status", ActionType.PUT)
                                    .then(log("#[payload]"),
                                        invoke(RoverStatusMessageProcessor.class).using(properties("rover", rover))
                                        )
                            )
        );
    

   static PrivateFlowBuilder requestFlow(Direction direction, Rover rover)
    {
        return request("/motion/" + direction.name().toLowerCase(), ActionType.PUT)
                .then(log("#[payload]") ,
                      invoke(RoverMotionMessageProcessor.class).using(properties("rover", rover, "direction", direction)));
    }