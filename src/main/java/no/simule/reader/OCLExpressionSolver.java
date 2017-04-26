package no.simule.reader;


import no.simule.exception.ValidationParseException;
import no.simule.utils.Keywords;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;
import org.eclipse.ocl.uml.ExpressionInOCL;
import org.eclipse.uml2.uml.Constraint;

/**
 * OCLExpressionSolver parse ocl constraint and solve with eclipse ocl and transform into OCL Expression with use to
 * evaluate with eclipse ocl.
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */
public class OCLExpressionSolver {


    private String constraint;


    public OCLExpressionSolver(String constraint) {
        this.constraint = constraint;
    }

    /**
     * This method davide ocl constraint into constraint types.
     *
     * @param constraintType ocl constraint type.
     * @param expression     ocl constraint expression.
     * @param helper         eclipse ocl helper.
     * @return {@link org.eclipse.ocl.expressions.OCLExpression} OCL Expression.
     */

    public OCLExpression parseConstraint(String constraintType, String expression, OCLHelper helper)
            throws ValidationParseException, ParserException, NullPointerException {

        OCLExpression oclExpression;

        switch (constraintType) {
            case Keywords.Query_Constraint:

                oclExpression = helper.createQuery(expression);

                break;
            case Keywords.INVARIANT_Constraint:

                oclExpression = inv(expression, helper);

                break;
            case Keywords.PRE_Condition_Constraint:
                oclExpression = pre(expression, helper);

                break;
            case Keywords.POST_Condition_Constraint:

                oclExpression = post(expression, helper);
                break;
            case Keywords.Operation_Body:

                oclExpression = body(expression, helper);

                break;
            case Keywords.Def_Operation:

                oclExpression = def(expression, helper);


                break;
            default:
                throw new ValidationParseException("not processed ");
        }


        return oclExpression;
    }


    /**
     * This method parse invariant constraint and transform into ocl Expression.
     *
     * @param expression ocl constraint expression.
     * @param helper     eclipse ocl helper.
     * @return {@link org.eclipse.ocl.expressions.OCLExpression} OCL Expression.
     */

    private OCLExpression inv(String expression, OCLHelper helper) throws ValidationParseException, ParserException, NullPointerException {
        int index = constraintIndex(expression, Keywords.INVARIANT_Constraint);
        if (index > 0) {
            constraint = expression.substring(index).trim();
            System.out.println(constraint);
            Constraint oclConstraint = (Constraint) helper.createInvariant(constraint);

            OCLExpression query = null;
            if (oclConstraint.getSpecification() instanceof ExpressionInOCL) {
                ExpressionInOCL expressionInOCL = (ExpressionInOCL) oclConstraint.getSpecification();
                query = expressionInOCL.getBodyExpression();
            }

            if (query == null) {
                query = helper.createQuery(constraint);
            }

            return query;

        } else {
            throw new ValidationParseException("\"inv:\" OR \"inv :\" not found");
        }
    }

    /**
     * This method parse pre condition and transform into ocl Expression.
     *
     * @param expression ocl constraint expression.
     * @param helper     eclipse ocl helper.
     * @return {@link org.eclipse.ocl.expressions.OCLExpression} OCL Expression.
     */


    private OCLExpression pre(String expression, OCLHelper helper) throws ValidationParseException, ParserException, NullPointerException {
        int index = constraintIndex(expression, Keywords.PRE_Condition_Constraint);
        if (index > 0) {
            constraint = expression.substring(index).trim();
            System.out.println(constraint);


            Constraint oclConstraint = (Constraint) helper.createPrecondition(constraint);

            OCLExpression query = null;
            if (oclConstraint.getSpecification() instanceof ExpressionInOCL) {
                ExpressionInOCL expressionInOCL = (ExpressionInOCL) oclConstraint.getSpecification();
                query = expressionInOCL.getBodyExpression();
            }

            if (query == null) {
                query = helper.createQuery(constraint);
            }

            return query;

        } else {
            throw new ValidationParseException("\"pre:\" OR \"pre :\" not found");
        }
    }

    /**
     * This method parse post condition and transform into ocl Expression.
     *
     * @param expression ocl constraint expression.
     * @param helper     eclipse ocl helper.
     * @return {@link org.eclipse.ocl.expressions.OCLExpression} OCL Expression.
     */


    private OCLExpression post(String expression, OCLHelper helper) throws ValidationParseException, ParserException, NullPointerException {
        int index = constraintIndex(expression, Keywords.POST_Condition_Constraint);
        if (index > 0) {
            constraint = expression.substring(index).trim();
            System.out.println(constraint);


            Constraint oclConstraint = (Constraint) helper.createPostcondition(constraint);

            OCLExpression query = null;
            if (oclConstraint.getSpecification() instanceof ExpressionInOCL) {
                ExpressionInOCL expressionInOCL = (ExpressionInOCL) oclConstraint.getSpecification();
                query = expressionInOCL.getBodyExpression();
            }

            if (query == null) {
                query = helper.createQuery(constraint);
            }

            return query;


        } else {
            throw new ValidationParseException("\"post:\" OR \"post :\" not found");
        }
    }

    /**
     * This method parse body expression and transform into ocl Expression.
     *
     * @param expression ocl constraint expression.
     * @param helper     eclipse ocl helper.
     * @return {@link org.eclipse.ocl.expressions.OCLExpression} OCL Expression.
     */


    private OCLExpression body(String expression, OCLHelper helper) throws ValidationParseException, ParserException, NullPointerException {
        int index = constraintIndex(expression, Keywords.Operation_Body);
        if (index > 0) {
            constraint = expression.substring(index).trim();
            System.out.println(constraint);
            Constraint oclConstraint = (Constraint) helper.createBodyCondition(constraint);
            OCLExpression query = null;
            if (oclConstraint.getSpecification() instanceof ExpressionInOCL) {
                ExpressionInOCL expressionInOCL = (ExpressionInOCL) oclConstraint.getSpecification();
                query = expressionInOCL.getBodyExpression();
            }
            if (query == null) {
                query = helper.createQuery(constraint);
            }
            return query;

        } else {
            throw new ValidationParseException("\"body:\" OR \"body :\" not found");
        }
    }


    /**
     * This method parse def expression and transform into ocl Expression.
     *
     * @param expression ocl constraint expression.
     * @param helper     eclipse ocl helper.
     * @return {@link org.eclipse.ocl.expressions.OCLExpression} OCL Expression.
     */


    private OCLExpression def(String expression, OCLHelper helper) throws ValidationParseException, ParserException, NullPointerException {
        int index = constraintIndex(expression, Keywords.Def_Operation);
        if (index > 0) {
            constraint = expression.substring(index).trim();
            helper.defineOperation(constraint);
            OCLExpression query = null;
            query = helper.createQuery(constraint);
            return query;
        } else {
            throw new ValidationParseException("\"def:\" OR \"def :\" not found");
        }
    }


    /**
     * This method check if constraint type is part of constraint then return position of constraint in expression .
     *
     * @param constraintType ocl constraint expression.
     * @param expression     ocl constraint.
     * @return int index of constraint type.
     */

    public int constraintIndex(String expression, String constraintType) {
        int index = 0;
        if (expression.contains(constraintType + ":")) {
            index = expression.indexOf(constraintType + ":") + constraintType.length() + 1;
        } else if (expression.contains(constraintType + " :")) {
            index = expression.indexOf(constraintType + " :") + constraintType.length() + 2;
        } else if (expression.contains(constraintType)) {
            index = expression.indexOf(constraintType) + constraintType.length();
            String temp = expression.substring(index).trim();
            if (temp.contains(":")) {
                index = index + temp.indexOf(":") + 1;
            } else {
                index = 0;
            }
        }
        return index;
    }

}
