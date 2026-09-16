package org.apache.deltaspike.core.api.interpreter;

/**
 * Generic interface for evaluation of expressions, like the ones provided by
 * {@link org.apache.deltaspike.core.api.exclude.Exclude#onExpression()}.
 * 
 * @param <E> expression type
 * @param <R> result type
 */
public interface ExpressionInterpreter<E, R>
{
    /**
     * Evaluates the given expression and returns the result for it.
     *
     * @param expression expression to evaluate
     *
     * @return result of the evaluated expression
     */
    R evaluate(E expression);
}