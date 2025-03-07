package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.*;

/**
 * CMP : The CMP instruction compares between the two specified operands.
 *
 * @author aitdriss
 * @date 23/01/2023
 */

public class ArmCMP extends BinaryInstructionArm{
	public ArmCMP(GPRegisterArm op1, DValArm op2) {
		super(op1, op2);
	}

	@Override
	public String getName(){
		return "cmp";
	}
}

