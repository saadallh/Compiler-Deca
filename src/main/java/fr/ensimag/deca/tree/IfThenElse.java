package fr.ensimag.deca.tree;

import fr.ensimag.arm.pseudocode.LabelArm;
import fr.ensimag.arm.pseudocode.instructions.ArmBal;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl24
 * @date 01/01/2023
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getThenBranch() {
        return thenBranch;
    }

    public ListInst getElseBranch() {
        return elseBranch;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        condition.verifyCondition(compiler, localEnv, currentClass);
        thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }



    @Override
    protected void codeGenInst(DecacCompiler compiler, Label endIf) {
        if (endIf == null){
            Label endIfInst = new Label("endIf.l" + getLocation().getLine() +
                    "c." + getLocation().getPositionInLine());
            //This function is useful for recursion and storing the endIf label
            codeGenInstIfRec(compiler, endIfInst);
            compiler.addLabel(endIfInst);
        }
        else {
            codeGenInstIfRec(compiler, endIf);
        }


    }

    /**
     * Generate the branching recursively inside the if statement, branches to endIf at the end
     * @param compiler
     * @param endIf
     */
    protected void codeGenInstIfRec(DecacCompiler compiler, Label endIf) {
        Label elseIf = new Label("elseIf.l" + getLocation().getLine() + "c."
                + getLocation().getPositionInLine());
        condition.codeGenBranch(compiler, false, elseIf);
        thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(endIf));
        compiler.addLabel(elseIf);
        for (AbstractInst i : elseBranch.getList()) {
            i.codeGenInst(compiler, endIf);
        }
    }


    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("not yet implemented");
        s.print("if (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getThenBranch().decompile(s);
        s.unindent();
        s.println("} else {");
        s.indent();
        getElseBranch().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }






    /******************* For Arm ******************************/
    @Override
    protected void codeGenInstArm(DecacCompiler compiler, LabelArm endIf) {
        if (endIf == null){
            LabelArm endIfInst = new LabelArm("endIf.l" + getLocation().getLine() +
                    "c." + getLocation().getPositionInLine());
            //This function is useful for recursion and storing the endIf label
            codeGenInstIfRecArm(compiler, endIfInst);
            compiler.addLabel(endIfInst);
        }
        else {
            codeGenInstIfRecArm(compiler, endIf);
        }


    }

    /**
     * Generate the branching recursively inside the if statement, branches to endIf at the end
     * @param compiler
     * @param endIf
     */
    protected void codeGenInstIfRecArm(DecacCompiler compiler, LabelArm endIf) {
        LabelArm elseIf = new LabelArm("elseIf.l" + getLocation().getLine() + "c."
                + getLocation().getPositionInLine());
        condition.codeGenBranchArm(compiler, false, elseIf);
        thenBranch.codeGenListInstArm(compiler);
        compiler.addInstruction(new ArmBal(endIf));
        compiler.addLabel(elseIf);
        for (AbstractInst i : elseBranch.getList()) {
            i.codeGenInstArm(compiler, endIf);
        }
    }



}
