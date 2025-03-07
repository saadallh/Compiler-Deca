package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;

/**
 * String literal
 *
 * @author gl24
 * @date 01/01/2023
 */
public class StringLiteral extends AbstractStringLiteral {

    @Override
    public String getValue() {
        return value;
    }

    private String value;

    public StringLiteral(String value) {
        Validate.notNull(value);
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	this.setType(compiler.environmentType.STRING);
    	return this.getType();
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean hex) {
        compiler.addInstruction(new WSTR(new ImmediateString(value)));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("not yet implemented");
        s.print('"' + value + '"');
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    
    @Override
    String prettyPrintNode() {
        return "StringLiteral (" + value + ")";
    }



    @Override
    protected void codeGenPrintLNArm(DecacCompiler compiler, boolean hex){
        LabelArm lab = new LabelArm("string" + getLocation().getLine() +
                "c" + getLocation().getPositionInLine());
        compiler.dataMapArm.put(lab, value + "\\n");
        compiler.addInstruction(new MOV(RegisterArm.getR(0), new ImmediateIntegerArm(1)));
        compiler.addInstruction(new MOV(RegisterArm.getR(7), new ImmediateIntegerArm(4)));
        compiler.addInstruction(new LDR(RegisterArm.getR(1),lab));
        compiler.addInstruction(new MOV(RegisterArm.getR(2), new ImmediateIntegerArm(value.length() + 1) ));
        compiler.addInstruction(new SWI(new ImmediateIntegerArm(0)));
    }


    @Override
    protected void codeGenPrintArm(DecacCompiler compiler, boolean hex){
        LabelArm lab = new LabelArm("string" + getLocation().getLine() +
                "c" + getLocation().getPositionInLine());
        compiler.dataMapArm.put(lab, value);
        compiler.addInstruction(new MOV(RegisterArm.getR(0), new ImmediateIntegerArm(1)));
        compiler.addInstruction(new MOV(RegisterArm.getR(7), new ImmediateIntegerArm(4)));
        compiler.addInstruction(new LDR(RegisterArm.getR(1),lab));
        compiler.addInstruction(new MOV(RegisterArm.getR(2), new ImmediateIntegerArm(value.length()) ));
        compiler.addInstruction(new SWI(new ImmediateIntegerArm(0)));
    }

}
