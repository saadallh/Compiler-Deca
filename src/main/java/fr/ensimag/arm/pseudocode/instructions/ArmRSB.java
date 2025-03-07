package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.GPRegisterArm;
import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.TernaryInstructionArm;

/**
 * RSB: substract and store opposite op1 = -(op2 - op3)
 * op3 is flexible
 * @author aitdriss
 * @date 18/01/2023
 */

public class ArmRSB extends TernaryInstructionArm {
	public ArmRSB(GPRegisterArm op1, GPRegisterArm op2, OperandArm op3) {
		super(op1, op2, op3);
	}

	@Override
	public String getName(){
		return "rsb";
	}
}
