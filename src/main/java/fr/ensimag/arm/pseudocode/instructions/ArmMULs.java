package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.GPRegisterArm;
import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.TernaryInstructionArm;

/**
 * SUB: The SUB instruction multiplies the value of Operand3
 * and the operand2 and puts the result into the register operand1.
 *
 * @author aitdriss
 * @date 18/01/2023
 */

public class ArmMULs extends TernaryInstructionArm {
	public ArmMULs(GPRegisterArm op1, GPRegisterArm op2, OperandArm op3) {
		super(op1, op2, op3);
	}

	@Override
	public String getName(){
		return "muls";
	}
}
