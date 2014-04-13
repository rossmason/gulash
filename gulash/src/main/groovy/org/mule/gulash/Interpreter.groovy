package org.mule.gulash

import org.codehaus.groovy.runtime.MethodClosure
import org.codehaus.groovy.tools.shell.Parser
import org.codehaus.groovy.tools.shell.util.Logger

import java.lang.reflect.Method

class Interpreter
{
    static final String SCRIPT_FILENAME = 'groovysh_evaluate'

    private final Logger log = Logger.create(this.class)

    private final GroovyShell shell

    Interpreter(GroovyShell shell)
    {
        this.shell = shell
    }

    Interpreter(final ClassLoader classLoader, final Binding binding) {
        assert classLoader
        assert binding

        shell = new GroovyShell(classLoader, binding)
    }

    Binding getContext() {
        return shell.getContext()
    }

    GroovyClassLoader getClassLoader() {
        return shell.getClassLoader()
    }

    def evaluate(final List buffer) {
        assert buffer

        def source = buffer.join(Parser.NEWLINE)

        def result

        Class type
        try {
            Script script = shell.parse(source, SCRIPT_FILENAME)
            type = script.getClass()

            log.debug("Compiled script: $script")

            if (type.declaredMethods.any { it.name == 'main' }) {
                result = script.run()
            }

            // Need to use String.valueOf() here to avoid icky exceptions causes by GString coercion
            log.debug("Evaluation result: ${String.valueOf(result)} (${result?.getClass()})")

            // Keep only the methods that have been defined in the script
            type.declaredMethods.each { Method m ->
                if (!(m.name in [ 'main', 'run' ] || m.name.startsWith('super$') || m.name.startsWith('class$') || m.name.startsWith('$'))) {
                    log.debug("Saving method definition: $m")

                    context["${m.name}"] = new MethodClosure(type.newInstance(), m.name)
                }
            }
        }
        finally {
            def cache = classLoader.classCache

            // Remove the script class generated
            cache.remove(type?.name)

            // Remove the inline closures from the cache as well
            cache.remove('$_run_closure')
        }

        return result
    }
}