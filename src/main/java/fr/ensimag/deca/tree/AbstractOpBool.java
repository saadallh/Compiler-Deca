package fr.ensimag.deca.tree;

import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (t1.sameType(t2) && t1.isBoolean()) {
    		if (this.getOperatorName().equals("&&") | this.getOperatorName().equals("||")) {
        		this.setType(compiler.environmentType.BOOLEAN);
        		return this.getType();    			
    		}
    	}
    	throw new ContextualError("types not permetted for" + this.getOperatorName(), this.getLocation());
    }


	/******************************** FOR ARM ***********************************/

	@Override
	protected void codeGenInitArm(DecacCompiler compiler, OperandArm adr){

		GPRegisterArm result = (GPRegisterArm) codeGenLoadArm(compiler);
		//Store result
		GPRegisterArm regTemp = compiler.getFreeRegArm(); //Implicit use and free of this register
		compiler.addInstruction(new LDR(regTemp, (LabelArm) adr));
		compiler.addInstruction(new STR(result, new RegisterOffsetArm(0, regTemp)));
		compiler.freeRegArm();
	}


	/**
	 * Does the operation between the two operands and returns the result in a register
	 * @param compiler
	 * @return opLeft with the value of the operation
	 */
	@Override
	protected DValArm codeGenLoadArm(DecacCompiler compiler){
		//Store left in register: If no freeing problems, we are sure there is at least one free register
		GPRegisterArm opLeft = (GPRegisterArm) getLeftOperand().codeGenLoadArm(compiler);
		//Evaluate right
		GPRegisterArm opRight = (GPRegisterArm) getRightOperand().codeGenLoadArm(compiler);
		//Do the operation
		//TODO: Optimize for immediates
		codeGenOpMnemArm(compiler, opLeft, opRight);
		compiler.freeRegArm();
		return opLeft;

	}


	/**
	 * Generate MNEM(dval1, dval2) corresponding to the operation
	 * It generates BOV after to catch the execution error
	 * @param compiler
	 * @param dval1
	 * @param dval2
	 */
	protected void codeGenOpMnemArm(DecacCompiler compiler,GPRegisterArm dval1, GPRegisterArm dval2){
		if (getOperatorName().equals("&&")){
			compiler.addInstruction(new ArmAND(dval1, dval1, dval2));
		}
		else if (getOperatorName().equals("||")){
			compiler.addInstruction(new ArmORR(dval1, dval2, dval1));
		}
		else{
			throw new DecacInternalError("Error in parsing");
		}
	}



	@Override
	protected void codeGenBranchArm(DecacCompiler compiler, boolean b, LabelArm label){
		GPRegisterArm reg = (GPRegisterArm) codeGenLoadArm(compiler);
		DValArm trueDval = new ImmediateIntegerArm(1);
		DValArm falseDval = new ImmediateIntegerArm(0);
		compiler.addInstruction(new ArmCMP(reg, falseDval));
		if (b){
			compiler.addInstruction(new ArmBne(label));
		}
		else {
			compiler.addInstruction(new ArmBeq(label));
		}
		compiler.freeRegArm();
	}


}
