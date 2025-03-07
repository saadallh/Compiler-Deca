package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl24
 * @date 01/01/2023
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) {
        //throw new UnsupportedOperationException("not yet implemented");
        this.setType(compiler.environmentType.FLOAT);
        return this.getType();
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }


    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        GPRegister valueReg = (GPRegister) getOperand().codeGenLoad(compiler);
        compiler.addInstruction(new FLOAT(valueReg, valueReg));
        return valueReg;
    }


    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean hex){
        GPRegister valueReg = (GPRegister) codeGenLoad(compiler);
        compiler.addInstruction(new LOAD(valueReg, Register.R1));
        compiler.freeReg();
        if (hex){
            compiler.addInstruction(new WFLOATX());
        }
        else {
            compiler.addInstruction(new WFLOAT());
        }
    }


}
