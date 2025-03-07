package fr.ensimag.arm.pseudocode.instructions;
import fr.ensimag.arm.pseudocode.*;

/**
 * CMP: The CMP instruction subtracts the value of Operand2 
 * from the value in Rn . This is the same as a SUBS instruction,
 * except that the result is discarded. The CMN instruction adds
 * the value of Operand2 to the value in Rn . This is the same 
 * as an ADDS instruction, except that the result is discarded.
 *
 * @author aitdriss
 * @date 18/01/2023
 */

public class CMP extends BinaryInstructionArm{
	//TODO : Ajouter condition
	public CMP(GPRegisterArm op1, DValArm op2) {
		super(op1, op2);
	}
}
