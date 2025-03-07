package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl24
 * @date 01/01/2023
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private ListDeclVar declVariables;
    private ListInst insts;



    public Main(ListDeclVar declVariables,
                ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");
        // A FAIRE: Appeler méthodes "verify*" de ListDeclVarSet et ListInst.
        // Vous avez le droit de changer le profil fourni pour ces méthodes
        // (mais ce n'est à priori pas nécessaire).
        //throw new UnsupportedOperationException("not yet implemented");
        EnvironmentExp localEnv = new EnvironmentExp(null);
        declVariables.verifyListDeclVariable(compiler, localEnv, null);
        Type returnType = new VoidType(null);
        insts.verifyListInst(compiler, localEnv, null, returnType);
        LOG.debug("verify Main: end");

    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        compiler.addComment("Beginning of main instructions:");
        compiler.addComment("Generating code for variable declaration");
        compiler.resetTempStack();
        compiler.resetBlocRegMax();
        declVariables.codeGenListDeclVariable(compiler, Register.GB);
        compiler.addComment("Generating code for instructions");
        insts.codeGenListInst(compiler);
        if (compiler.getOffset() != 0){
            compiler.addInstructionFirst(new ADDSP(compiler.getOffset()-1)); // offset - 1 because we start at 1 and increment after using
            if (!compiler.getCompilerOptions().getOptionN()) {
                compiler.addInstructionFirst(new BOV(new Label("err_stack_overflow")));
                compiler.addInstructionFirst(new TSTO(compiler.getOffset()-1 + compiler.getBlocTempMax()));
            }
        }
        compiler.resetTempMax();

    }


    @Override
    protected void codeGenMainArm(DecacCompiler compiler){
        declVariables.codeGenListDeclVariableArm(compiler);
        insts.codeGenListInstArm(compiler);
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
