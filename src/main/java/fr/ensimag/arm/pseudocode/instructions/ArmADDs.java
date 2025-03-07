package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.GPRegisterArm;
import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.TernaryInstructionArm;

/**
 * ADDs: The ADDs instruction adds the value of Operand2
 * and the operand3 and puts the result into the register operand1.
 * @author aitdriss
 * @date 18/01/2023
 */

public class ArmADDs extends TernaryInstructionArm {
	public ArmADDs(GPRegisterArm op1, GPRegisterArm op2, OperandArm op3) {
		super(op1, op2, op3);
	}

	@Override
	public String getName(){
		return "adds";
	}
}
