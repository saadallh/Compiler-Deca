package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public class ReadFloat extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	this.setType(compiler.environmentType.FLOAT);
    	return this.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readFloat()");
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
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        compiler.addError(readErrFloat, "Erreur: valeur entree ne correspond pas à un flottant codable");
        compiler.addInstruction(new RFLOAT());
        compiler.addInstruction(new BOV(new Label(readErrFloat)));
        super.codeGenInit(compiler, adr);
    }

    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        compiler.addInstruction(new RFLOAT());
        return super.codeGenLoad(compiler);
    }


    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean hex){
        compiler.addInstruction(new WFLOAT());
    }

}
