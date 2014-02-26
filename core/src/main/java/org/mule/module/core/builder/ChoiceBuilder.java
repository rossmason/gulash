package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.config.dsl.Builder;
import org.mule.module.core.BuilderConfigurationException;
import org.mule.routing.ChoiceRouter;
import org.mule.routing.filters.ExpressionFilter;

import java.util.ArrayList;
import java.util.List;

import groovy.lang.Closure;


public class ChoiceBuilder implements Builder<ChoiceRouter>
{

    private List<Filter> filters = new ArrayList<Filter>();
    private List<MessageProcessorChainBuilder> routes = new ArrayList<MessageProcessorChainBuilder>();
    private MessageProcessorChainBuilder otherwise;
    private MessageProcessorChainBuilder current;

    public ChoiceBuilder on(String expression)
    {
        ExpressionFilter filter = new ExpressionFilter(expression);
        return on(filter);
    }

    public ChoiceBuilder on(Filter filter)
    {
        MessageProcessorChainBuilder route = new MessageProcessorChainBuilder();
        routes.add(route);
        current = route;
        filters.add(filter);
        return this;
    }

    public ChoiceBuilder on(final Closure<Boolean> expression)
    {
        return on(new Filter()
        {
            @Override
            public boolean accept(MuleMessage message)
            {
                return expression.call(message);
            }
        });
    }

    public ChoiceBuilder otherwise()
    {
        otherwise = new MessageProcessorChainBuilder();
        current = otherwise;
        return this;
    }

    public ChoiceBuilder then(Builder<? extends MessageProcessor>... messageProcessors)
    {
        if (current == null)
        {
            throw new BuilderConfigurationException("Invoke on or otherwise before then method");
        }
        current.chain(messageProcessors);
        return this;
    }


    @Override
    public ChoiceRouter create(MuleContext muleContext)
    {
        final ChoiceRouter choiceRouter = new ChoiceRouter();
        choiceRouter.setMuleContext(muleContext);
        for (int i = 0; i < filters.size(); i++)
        {
            Filter filter = filters.get(i);
            MessageProcessorChainBuilder route = routes.get(i);
            choiceRouter.addRoute(route.create(muleContext), filter);
        }

        choiceRouter.setDefaultRoute(otherwise.create(muleContext));
        choiceRouter.setMuleContext(muleContext);
        return choiceRouter;
    }
}
