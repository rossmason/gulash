package org.mule.module

import org.mule.api.MuleMessage
import org.mule.module.builder.ScalaBuilder


object ScalaCore {
    def call(closure: MuleMessage => Any) = new ScalaBuilder(closure)
}
