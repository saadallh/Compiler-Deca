package fr.ensimag.deca.tree;

import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	// il faut ajouter les autres operations de meme domaine
    	Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (this.getOperatorName().equals("==") | this.getOperatorName().equals("!=") | this.getOperatorName().equals("<=")
    			| this.getOperatorName().equals(">=") | this.getOperatorName().equals(">")
    			| this.getOperatorName().equals("<")) {
    			if (t1.sameType(t2) && (t1.isInt() | t1.isFloat())) {
    				this.setType(compiler.environmentType.BOOLEAN);
    				return this.getType();
    			}
    			if (t1.isInt() && t2.isFloat()) {
    				this.setType(compiler.environmentType.BOOLEAN);
					this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
					this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    				return this.getType();
    				}
    			if (t1.isFloat() && t2.isInt()) {
    				this.setType(compiler.environmentType.BOOLEAN);
					this.setRightOperand(new ConvFloat(this.getRightOperand()));
					this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    				return this.getType();    			}

    		}
    	if (this.getOperatorName().equals("==") | this.getOperatorName().equals("!=")){
    		if (t1.sameType(t2) && (t1.isBoolean())) {
				this.setType(compiler.environmentType.BOOLEAN);
				return this.getType();    			
    		}
    		if ((t2.isClassOrNull()) && (t1.isClassOrNull())) {
				this.setType(compiler.environmentType.BOOLEAN);
				return this.getType(); 
    		}
    	}
    	throw new ContextualError("erreur dans la condition" + this.getOperatorName() + "operands not permetted", this.getLocation());
    	}

	/**
	 * Initialize a boolean variable with the value of the operation
	 * @param compiler
	 * @param adr: memory adress of the variable
	 */
	@Override
	protected void codeGenInit(DecacCompiler compiler, DAddr adr){
		Label falseComp = new Label("falseComp.l" + getLocation().getLine() +
				".c" + getLocation().getPositionInLine());
		Label end = new Label("endComp.l" + getLocation().getLine() +
				".c" + getLocation().getPositionInLine());

		codeGenBranch(compiler, false, falseComp);

		GPRegister reg  = compiler.getFreeReg();
		//Implicit use and free

		compiler.addInstruction(new LOAD(1, reg)); // load 1, r
		compiler.addInstruction(new STORE(reg, adr)); // store r, adr
		//No need to update registerDescriptor because we load and store
		compiler.addInstruction(new BRA(end)); // bra end
		compiler.addLabel(falseComp); // neq :
		compiler.addInstruction(new LOAD(0, reg)); // load 0, r
		compiler.addInstruction(new STORE(reg, adr)); // store r, adr
		compiler.addLabel(end); // end:


	}

	/**
	 * Branch to label if expression is b(true/false)
	 * @param compiler
	 * @param b : true or false/ compare the exprBool to this
	 * @param label :
	 */
	@Override
	protected void codeGenBranch(DecacCompiler compiler, boolean b, Label label){
		//If comparable, we load the values and compare
		if (compiler.useLoad()){
			GPRegister opLeft = (GPRegister) getLeftOperand().codeGenLoad(compiler);
			GPRegister opRight = (GPRegister) getRightOperand().codeGenLoad(compiler);
			compiler.addInstruction(new CMP(opRight, opLeft));
			compiler.freeReg();
			compiler.freeReg();
		}
		else {
			GPRegister opLeft = (GPRegister) getLeftOperand().codeGenLoad(compiler);
			compiler.addInstruction(new PUSH(opLeft));
			compiler.freeReg();
			GPRegister opRight = (GPRegister) getRightOperand().codeGenLoad(compiler);
			compiler.addInstruction(new POP(Register.R0));
			compiler.addInstruction(new CMP(opRight, Register.R0));
			compiler.freeReg();
		}
		//Free the two registers because we no longer need to use them
		codeGenMnem(compiler, label, !b);

	}

	/**
	 * Load the value of the expression in right operand
	 * this is similar to codeGenBranch, except it does return a
	 * @param compiler
	 * @return
	 */
	@Override
	protected DVal codeGenLoad(DecacCompiler compiler){
		Label falseComp = new Label("falseComp." + this.toString().split("@")[1]);
		Label endComp = new Label("endComp." + this.toString().split("@")[1]);
		if (compiler.useLoad()){
			//We are sure in this case that the operands are atomic
			GPRegister opLeft = (GPRegister) getLeftOperand().codeGenLoad(compiler);
			GPRegister opRight =  (GPRegister) getRightOperand().codeGenLoad(compiler);
			compiler.addInstruction(new CMP(opRight, opLeft));
			codeGenMnem(compiler, falseComp, getOpp());
			compiler.addInstruction(new LOAD(1, (GPRegister) opLeft));
			compiler.addInstruction(new BRA(endComp));
			compiler.addLabel(falseComp);
			compiler.addInstruction(new LOAD(0, (GPRegister) opLeft));
			compiler.addLabel(endComp);
			compiler.freeReg();//Free the right register
			return opLeft;
		}
		else {
			GPRegister opLeft = (GPRegister) getLeftOperand().codeGenLoad(compiler);
			compiler.addInstruction(new PUSH(opLeft));
			compiler.freeReg();
			GPRegister opRight =  (GPRegister) getRightOperand().codeGenLoad(compiler);
			compiler.addInstruction(new POP(Register.R0));
			compiler.addInstruction(new CMP(opRight, Register.R0));
			codeGenMnem(compiler, falseComp, getOpp());
			compiler.addInstruction(new LOAD(1, (GPRegister) opRight));
			compiler.addInstruction(new BRA(endComp));
			compiler.addLabel(falseComp);
			compiler.addInstruction(new LOAD(0, (GPRegister) opRight));
			compiler.addLabel(endComp);
			return opRight;
		}
	}


	/**
	 *Generates the mnemonic corresponding to the operation
	 * if opp is true, it generates the opposite mnemonic
	 * CMP R2, R3 (if R3 > R2, GT = true)
	 *
	 * It is advised to use CMP(rightOperand, leftOperand) to avoid confusion
	 * @param compiler
	 * @param label
	 * @param opp : if true we branch in the case the comparaison is false
	 *            if not we branch in the case the operation is false
	 */
	protected  void codeGenMnem(DecacCompiler compiler, Label label, boolean opp){
		if (getOperatorName().equals("<")){
			if (opp){
				compiler.addInstruction(new BGE(label));
			}else {
				compiler.addInstruction(new BLT(label));
			}
		}
		else if (getOperatorName().equals("<=")){
			if (opp){
				compiler.addInstruction(new BGT(label));
			}else {
				compiler.addInstruction(new BLE(label));
			}
		}
		else if (getOperatorName().equals(">")){
			if (opp){
				compiler.addInstruction(new BLE(label));
			}else {
				compiler.addInstruction(new BGT(label));
			}
		}
		else if (getOperatorName().equals(">=")){
			if (opp){
				compiler.addInstruction(new BLT(label));
			}else {
				compiler.addInstruction(new BGE(label));
			}
		}
		else if (getOperatorName().equals("==")){
			if (opp){
				compiler.addInstruction(new BNE(label));
			}else {
				compiler.addInstruction(new BEQ(label));
			}
		}
		else if (getOperatorName().equals("!=")){
			if (opp){
				compiler.addInstruction(new BEQ(label));
			}else {
				compiler.addInstruction(new BNE(label));
			}
		}
		else {
			throw new DecacInternalError("Comparaison operator shouldn't be parsed");
		}
	}


	/**
	 * Whether we should generate the opposite label
	 *	useful to facotrize the code
	 * @return true for ==/!= , false for others
	 */
	protected abstract boolean getOpp();


	/**************************** FOR ARM **********************************************/


	/**
	 * Initialize a boolean variable with the value of the operation
	 * @param compiler
	 * @param adr: memory adress of the variable
	 */
	@Override
	protected void codeGenInitArm(DecacCompiler compiler, OperandArm adr){
		LabelArm falseComp = new LabelArm("falseComp.l" + getLocation().getLine() +
				".c" + getLocation().getPositionInLine());
		LabelArm end = new LabelArm("endComp.l" + getLocation().getLine() +
				".c" + getLocation().getPositionInLine());

		codeGenBranchArm(compiler, false, falseComp);

		GPRegisterArm reg1  = compiler.getFreeRegArm();
		compiler.useRegArm();
		GPRegisterArm reg2 = compiler.getFreeRegArm(); //Implicit use and free

		compiler.addInstruction(new MOV(reg1, new ImmediateIntegerArm(1)));
		compiler.addInstruction(new LDR(reg2, (LabelArm) adr));
		compiler.addInstruction(new STR(reg1, new RegisterOffsetArm(0, reg2)));
		compiler.addInstruction(new ArmBal(end));
		compiler.addLabel(falseComp);
		compiler.addInstruction(new MOV(reg1, new ImmediateIntegerArm(0)));
		compiler.addInstruction(new LDR(reg2, (LabelArm) adr));
		compiler.addInstruction(new STR(reg1, new RegisterOffsetArm(0, reg2)));
		compiler.addLabel(end);
		compiler.freeRegArm();

	}

	/**
	 * Branch to label if expression is b(true/false)
	 * @param compiler
	 * @param b : true or false/ compare the exprBool to this
	 * @param label :
	 */
	@Override
	protected void codeGenBranchArm(DecacCompiler compiler, boolean b, LabelArm label){
			GPRegisterArm opLeft = (GPRegisterArm) getLeftOperand().codeGenLoadArm(compiler);
			GPRegisterArm opRight = (GPRegisterArm) getRightOperand().codeGenLoadArm(compiler);
			compiler.addInstruction(new ArmCMP(opLeft, opRight));
			compiler.freeRegArm();
			compiler.freeRegArm();
		//Free the two registers because we no longer need to use them
		codeGenMnemArm(compiler, label, !b);

	}

	/**
	 * Load the value of the expression in right operand
	 * this is similar to codeGenBranch, except it does return a
	 * @param compiler
	 * @return
	 */
	@Override
	protected DValArm codeGenLoadArm(DecacCompiler compiler){
		LabelArm falseComp = new LabelArm("falseComp." + this.toString().split("@")[1]);
		LabelArm endComp = new LabelArm("endComp." + this.toString().split("@")[1]);
		GPRegisterArm opLeft = (GPRegisterArm) getLeftOperand().codeGenLoadArm(compiler);
		GPRegisterArm opRight =  (GPRegisterArm) getRightOperand().codeGenLoadArm(compiler);
		compiler.addInstruction(new ArmCMP(opRight, opLeft));
		codeGenMnemArm(compiler, falseComp, getOpp());
		compiler.addInstruction(new MOV(opLeft, new ImmediateIntegerArm(1)));
		compiler.addInstruction(new ArmBal(endComp));
		compiler.addLabel(falseComp);
		compiler.addInstruction(new MOV(opLeft, new ImmediateIntegerArm(0)));
		compiler.addLabel(endComp);
		compiler.freeRegArm();//Free the right register
		return opLeft;
	}


	/**
	 *Generates the mnemonic corresponding to the operation
	 * if opp is true, it generates the opposite mnemonic
	 * CMP R2, R3 (if R2 > R3, GT = true)
	 *
	 * It is advised to use CMP(leftOperand, rightOperand) to avoid confusion
	 * @param compiler
	 * @param label
	 * @param opp : if true we branch in the case the comparaison is false
	 *            if not we branch in the case the operation is false
	 */
	protected  void codeGenMnemArm(DecacCompiler compiler, LabelArm label, boolean opp){
		if (getOperatorName().equals("<")){
			if (opp){
				compiler.addInstruction(new ArmBge(label));
			}else {
				compiler.addInstruction(new ArmBlt(label));
			}
		}
		else if (getOperatorName().equals("<=")){
			if (opp){
				compiler.addInstruction(new ArmBgt(label));
			}else {
				compiler.addInstruction(new ArmBle(label));
			}
		}
		else if (getOperatorName().equals(">")){
			if (opp){
				compiler.addInstruction(new ArmBle(label));
			}else {
				compiler.addInstruction(new ArmBgt(label));
			}
		}
		else if (getOperatorName().equals(">=")){
			if (opp){
				compiler.addInstruction(new ArmBlt(label));
			}else {
				compiler.addInstruction(new ArmBge(label));
			}
		}
		else if (getOperatorName().equals("==")){
			if (opp){
				compiler.addInstruction(new ArmBne(label));
			}else {
				compiler.addInstruction(new ArmBeq(label));
			}
		}
		else if (getOperatorName().equals("!=")){
			if (opp){
				compiler.addInstruction(new ArmBeq(label));
			}else {
				compiler.addInstruction(new ArmBne(label));
			}
		}
		else {
			throw new DecacInternalError("Comparaison operator shouldn't be parsed");
		}
	}


}
