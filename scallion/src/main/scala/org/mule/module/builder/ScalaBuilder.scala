package org.mule.module.builder

import org.mule.config.dsl.Builder
import org.mule.module.processor.ScalaMessageProcessor
import org.mule.api.{MuleMessage, MuleContext}

class ScalaBuilder(closure: MuleMessage => Any) extends Builder[ScalaMessageProcessor] {
    override def create(muleContext: MuleContext) = new ScalaMessageProcessor(closure)
}
