package fr.ensimag.arm.pseudocode.instructions;
import fr.ensimag.arm.pseudocode.*;

/**
 * MVN : The MVN instruction The MVN instruction takes 
 * the value of Operand2, performs a bitwise logical 
 * NOT operation on the value, and places the result 
 * into Rd.
 *
 * @author aitdriss
 * @date 10/01/2023
 */

public class MVN extends BinaryInstructionArm{
	public MVN(GPRegisterArm op1, DValArm op2) {
		super(op1, op2);
	}
}
