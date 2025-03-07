package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl24
 * @date 01/01/2023
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type t = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (t.isInt() | t.isFloat()) {
    		this.setType(t);
    		return t;
    	}
    	throw new ContextualError("erreur dans la condition" + this.getOperatorName() + "operands's type not permetted", this.getLocation());
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        GPRegister valueReg = (GPRegister) getOperand().codeGenLoad(compiler);
        compiler.addInstruction(new OPP(valueReg, valueReg));
        compiler.useReg();
        return valueReg;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean hex){
        GPRegister valueReg = (GPRegister) codeGenLoad(compiler);
        compiler.addInstruction(new LOAD(valueReg, Register.R1));
        compiler.freeReg();
        compiler.addInstruction(new WINT());
    }

}
