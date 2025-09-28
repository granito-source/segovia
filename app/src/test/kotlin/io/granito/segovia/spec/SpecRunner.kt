package io.granito.segovia.spec

import java.lang.reflect.Method
import org.concordion.integration.junit4.ConcordionRunner
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import org.springframework.test.annotation.ProfileValueUtils
import org.springframework.test.context.TestContextManager
import org.springframework.test.context.junit4.statements.RunAfterTestClassCallbacks
import org.springframework.test.context.junit4.statements.RunBeforeTestClassCallbacks
import org.springframework.util.ReflectionUtils

class SpecRunner(clazz: Class<*>): ConcordionRunner(clazz) {
    private val testContextManager = TestContextManager(clazz)

    override fun getDescription(): Description
    {
        return if (!ProfileValueUtils.isTestEnabledInThisEnvironment(
                testClass.javaClass))
            Description.createSuiteDescription(testClass.javaClass)
        else
            super.getDescription()
    }

    override fun run(notifier: RunNotifier)
    {
        if (!ProfileValueUtils.isTestEnabledInThisEnvironment(
                testClass.javaClass)) {
            notifier.fireTestIgnored(description)

            return
        }

        super.run(notifier)
    }

    override fun createTest(): Any
    {
        val test = super.createTest()

        testContextManager.prepareTestInstance(test)

        return test
    }

    override fun withBeforeClasses(statement: Statement): Statement
    {
        return RunBeforeTestClassCallbacks(
            super.withBeforeClasses(statement), testContextManager)
    }

    override fun withAfterClasses(statement: Statement): Statement
    {
        return RunAfterTestClassCallbacks(
            super.withAfterClasses(statement), testContextManager)
    }

    companion object {
        private val WITH_RULES: Method? = ReflectionUtils.findMethod(
            SpecRunner::class.java, "withRules",
            FrameworkMethod::class.java, Any::class.java,
            Statement::class.java)

        init {
            checkNotNull(WITH_RULES) { "No withRules() method" }
            ReflectionUtils.makeAccessible(WITH_RULES)
        }
    }
}
