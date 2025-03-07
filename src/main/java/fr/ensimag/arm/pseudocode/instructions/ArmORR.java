package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.GPRegisterArm;
import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.TernaryInstructionArm;

/**
 * ADD: OP1 = OP2 OR OP3
 * and the operand3 and puts the result into the register operand1.
 * @author aitdriss
 * @date 18/01/2023
 */

public class ArmORR extends TernaryInstructionArm {
	public ArmORR(GPRegisterArm op1, GPRegisterArm op2, OperandArm op3) {
		super(op1, op2, op3);
	}

	@Override
	public String getName(){
		return "add";
	}
}
