package fr.ensimag.arm.pseudocode.instructions;
import fr.ensimag.arm.pseudocode.*;

/**
 * CMN: The CMP instruction adds the value of op2
 * to the value in the register op1. This is the same as an ADDS 
 * instruction, except that the result is discarded.
 *
 * @author aitdriss
 * @date 18/01/2023
 */

public class CMN extends BinaryInstructionArm{
	//TODO : Ajouter condition
	public CMN(GPRegisterArm op1, DValArm op2) {
		super(op1, op2);
	}
}