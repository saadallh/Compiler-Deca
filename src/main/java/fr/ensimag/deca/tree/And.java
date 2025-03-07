package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.awt.peer.ComponentPeer;
import java.util.logging.Logger;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean b, Label label){
        //TODO: Set adress for operands when they re fields

        if (!b){
            getLeftOperand().codeGenBranch(compiler, b, label);
            getRightOperand().codeGenBranch(compiler, b, label);
        }
        else {
            Label falseAnd = new Label("falseAnd.l" + getLocation().getLine() +
                    ".c" + getLocation().getPositionInLine());
            getLeftOperand().codeGenBranch(compiler, !b, falseAnd);
            getRightOperand().codeGenBranch(compiler, b, label);
            compiler.addLabel(falseAnd);
        }
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        //boolean a = true && true;
        Label falseAnd = new Label("falseAnd"+ getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        Label endAnd = new Label("endAnd.l" + getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());

        GPRegister reg = compiler.getFreeReg();
        //Implicit use and free of register

        codeGenBranch(compiler, false, falseAnd);
        //return 1 if true
        compiler.addInstruction(new LOAD(1, reg));
        compiler.addInstruction(new BRA(endAnd));
        compiler.addLabel(falseAnd);
        //return 0 if false
        compiler.addInstruction(new LOAD(0, reg));
        compiler.addInstruction(new BRA(endAnd));
        compiler.addLabel(endAnd);
        compiler.addInstruction(new STORE(reg, adr));
    }

    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        //boolean a = true && true;
        Label falseAnd = new Label("falseAnd"+ getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        Label endAnd = new Label("endAnd.l" + getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());

        GPRegister reg = compiler.getFreeReg();
        compiler.useReg();

        codeGenBranch(compiler, false, falseAnd);
        //return 1 if true
        compiler.addInstruction(new LOAD(1, reg));
        compiler.addInstruction(new BRA(endAnd));
        compiler.addLabel(falseAnd);
        //return 0 if false
        compiler.addInstruction(new LOAD(0, reg));
        compiler.addInstruction(new BRA(endAnd));
        compiler.addLabel(endAnd);
        return reg;
    }

}
