package org.mule.module.processor

import org.mule.api.processor.MessageProcessor
import org.mule.api.{MuleMessage, MuleEvent}

class ScalaMessageProcessor(closure: MuleMessage => Any) extends MessageProcessor {
    override def process(event: MuleEvent) = {
        val result: Any = closure(event.getMessage())
        result match {
            case x: Unit => //do nothing
            case _ =>  event.getMessage.setPayload(result)
        }
        event
    }
}
