package fr.ensimag.deca.tree;

import fr.ensimag.arm.pseudocode.LabelArm;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl24
 * @date 01/01/2023
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue tree, AbstractExpr rightOperand) {
        super(tree, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	AbstractExpr exp = this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, t1);
    	exp.verifyExpr(compiler, localEnv, currentClass);
    	this.setRightOperand(exp);
    	this.setType(t1);
    	return t1;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler, Label label) {
        //The left operand is either an identifier or a selection
        if (getLeftOperand().isIdent()) {
            boolean set = getLeftOperand().setAdrField(compiler, null);
            this.getRightOperand().codeGenAssign(compiler, getLeftOperand());
            if (set) {
                ((Identifier) getLeftOperand()).getExpDefinition().setOperand(null);
                compiler.freeReg();
            }
        } else {
            boolean set = getLeftOperand().setAdrField(compiler, null);
            this.getRightOperand().codeGenAssign(compiler, ((Selection)getLeftOperand()).getIdent());
            if (set) {
                ((Selection)getLeftOperand()).getIdent().getExpDefinition().setOperand(null);
                compiler.freeReg();
            }
        }
    }



    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean hex){
        this.getRightOperand().codeGenPrint(compiler, hex);
    }


    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        GPRegister reg = compiler.getFreeReg();
        compiler.useReg();
        //The left operand is either an identifier or a selection
        if (getLeftOperand().isIdent()) {
            boolean set = getLeftOperand().setAdrField(compiler, null);
            this.getRightOperand().codeGenAssign(compiler, getLeftOperand());
            compiler.addInstruction(new LOAD(((Identifier)getLeftOperand()).getExpDefinition().getOperand(), reg));
            if (set) {
                ((Identifier) getLeftOperand()).getExpDefinition().setOperand(null);
                compiler.freeReg();
            }
        } else {
            boolean set = getLeftOperand().setAdrField(compiler, null);
            this.getRightOperand().codeGenAssign(compiler, ((Selection)getLeftOperand()).getIdent());
            compiler.addInstruction(new LOAD(((Selection)getLeftOperand()).getIdent().getExpDefinition().getOperand(), reg));
            if (set) {
                ((Selection)getLeftOperand()).getIdent().getExpDefinition().setOperand(null);
                compiler.freeReg();
            }
        }
        return reg;
    }


    @Override
    protected void codeGenInstArm(DecacCompiler compiler, LabelArm label) {
        this.getRightOperand().codeGenAssignArm(compiler, (Identifier) getLeftOperand());
    }


    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
//        GPRegister reg = (GPRegister) th
    }



}
