package fr.ensimag.deca.tree;

import fr.ensimag.arm.pseudocode.LabelArm;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;

/**
 * Print statement (print, println, ...).
 *
 * @author gl24
 * @date 01/01/2023
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    	Type type = null;
    	for (AbstractExpr i : arguments.getList()) {
    		type = i.verifyExpr(compiler, localEnv, currentClass);
    		if (!type.getName().getName().equals("string") && !type.getName().getName().equals("int") && !type.getName().getName().equals("float")) {
    			throw new ContextualError("type not accepted ", this.getLocation());   
    		} 
    	}
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler, Label label){
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrint(compiler, printHex);
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("print");
        s.print(getSuffix());
        if(getPrintHex()){
            s.print("x");
        }
        s.print("(");
        getArguments().decompile(s);
        s.print(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }


    @Override
    protected void codeGenInstArm(DecacCompiler compiler, LabelArm label) {
        if (getSuffix().equals("ln")){
            for (AbstractExpr a : getArguments().getList()) {
                a.codeGenPrintLNArm(compiler, printHex);
            }
        }
    }

}
