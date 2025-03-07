package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

/**
 * Unary expression.
 *
 * @author gl24
 * @date 01/01/2023
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    public AbstractExpr getOperand() {
        return operand;
    }
    private AbstractExpr operand;
    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }


    protected abstract String getOperatorName();
  
    @Override
    public void decompile(IndentPrintStream s) {
        if(getOperatorName()!= "/* conv float */"){
            s.print(getOperatorName());
        }
        getOperand().decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr) {
//        getOperand().setAdrField(compiler, adr);
        GPRegister valueReg = (GPRegister) codeGenLoad(compiler);
        compiler.addInstruction(new STORE(valueReg, adr));
        compiler.freeReg();
    }

}
