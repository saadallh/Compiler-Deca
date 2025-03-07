package fr.ensimag.deca.tree;

import fr.ensimag.arm.pseudocode.LabelArm;
import fr.ensimag.arm.pseudocode.instructions.ArmBal;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;


import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    	condition.verifyCondition(compiler, localEnv, currentClass);
    	body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label label) {
        compiler.addComment("Generating While code");
        Label startWhile = new Label("startWhile.l" + getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        Label condWhile = new Label("condWhile.l"+getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        compiler.addInstruction(new BRA(condWhile));
        compiler.addLabel(startWhile);
        body.codeGenListInst(compiler);
        compiler.addLabel(condWhile);
        condition.codeGenBranch(compiler, true, startWhile);
    }



    @Override
    protected void codeGenInstArm(DecacCompiler compiler, LabelArm label) {
        compiler.addComment("Generating While code");
        LabelArm startWhile = new LabelArm("startWhile.l" + getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        LabelArm condWhile = new LabelArm("condWhile.l"+getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        compiler.addInstruction(new ArmBal(condWhile));
        compiler.addLabel(startWhile);
        body.codeGenListInstArm(compiler);
        compiler.addLabel(condWhile);
        condition.codeGenBranchArm(compiler, true, startWhile);
    }

}
