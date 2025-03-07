package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (t1.sameType(t2) && t1.isInt()) {
    		this.setType(t2);
    		return this.getType();
    	}
    	throw new ContextualError("erreur dans la condition" + this.getOperatorName() + "operands's type not permetted", this.getLocation());
    	
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

    /**
     * Useful for function calls in arm assembly
     * @return
     */
    protected String getFctName(){
        return "fct_rem_arm";
    }


}
