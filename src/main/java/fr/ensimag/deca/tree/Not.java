package fr.ensimag.deca.tree;

import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	Type t = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (t.isBoolean()) {
    		this.setType(t);
    		return t;
    	}
    	throw new ContextualError("erreur dans la condition" + this.getOperatorName() + "operands's type not permetted", this.getLocation());
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        GPRegister valueReg = (GPRegister) getOperand().codeGenLoad(compiler);
        compiler.addInstruction(new CMP(0, valueReg));
        compiler.addInstruction(new SEQ(valueReg));
        return valueReg;
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean b, Label label){
        getOperand().codeGenBranch(compiler, !b, label);
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        //boolean a = true && true;
        Label falseNot = new Label("falseNot"+ getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        Label endNot = new Label("endNot.l" + getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());

        codeGenBranch(compiler, false, falseNot);
        GPRegister reg = compiler.getFreeReg();
        //Implicit use and free of register

        //return 1 if true
        compiler.addInstruction(new LOAD(1, reg));
        compiler.addInstruction(new BRA(endNot));
        compiler.addLabel(falseNot);
        //return 0 if false
        compiler.addInstruction(new LOAD(0, reg));
        compiler.addInstruction(new BRA(endNot));
        compiler.addLabel(endNot);
        compiler.addInstruction(new STORE(reg, adr));
    }


    /************************** For Arm ******************************************/


    @Override
    protected DValArm codeGenLoadArm(DecacCompiler compiler){
        //boolean a = true && true;
        LabelArm falseNot = new LabelArm("falseNot"+ getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        LabelArm endNot = new LabelArm("endNot.l" + getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());

        codeGenBranchArm(compiler, false, falseNot);
        GPRegisterArm reg1 = compiler.getFreeRegArm();
        compiler.useRegArm();

        //return 1 if true
        compiler.addInstruction(new MOV(reg1, new ImmediateIntegerArm(1)));
        compiler.addInstruction(new ArmBal(endNot));
        compiler.addLabel(falseNot);
        //return 0 if false
        compiler.addInstruction(new MOV(reg1, new ImmediateIntegerArm(0)));
        compiler.addInstruction(new ArmBal(endNot));
        compiler.addLabel(endNot);

        return reg1;
    }

    @Override
    protected void codeGenBranchArm(DecacCompiler compiler, boolean b, LabelArm label){
        getOperand().codeGenBranchArm(compiler, !b, label);
    }

    @Override
    protected void codeGenInitArm(DecacCompiler compiler, OperandArm adr){
        //boolean a = true && true;
        LabelArm falseNot = new LabelArm("falseNot"+ getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());
        LabelArm endNot = new LabelArm("endNot.l" + getLocation().getLine() +
                ".c" + getLocation().getPositionInLine());

        codeGenBranchArm(compiler, false, falseNot);
        GPRegisterArm reg1 = compiler.getFreeRegArm();
        compiler.useRegArm();

        //return 1 if true
        compiler.addInstruction(new MOV(reg1, new ImmediateIntegerArm(1)));
        compiler.addInstruction(new ArmBal(endNot));
        compiler.addLabel(falseNot);
        //return 0 if false
        compiler.addInstruction(new MOV(reg1, new ImmediateIntegerArm(0)));
        compiler.addInstruction(new ArmBal(endNot));
        compiler.addLabel(endNot);

        GPRegisterArm reg2 = (GPRegisterArm) compiler.getFreeRegArm();//Implicit use and free
        compiler.addInstruction(new LDR(reg2, (LabelArm) adr));
        compiler.addInstruction(new STR(reg1, new RegisterOffsetArm(0, reg2)));

        compiler.freeRegArm();//free reg1
    }








}
