package fr.ensimag.arm.pseudocode.instructions;
import fr.ensimag.arm.pseudocode.*;

import java.io.PrintStream;
import java.util.List;

/**
 * PUSH : The push instruction Push registers onto a full descending stack.
 *
 * @author aitdriss
 * @date 18/01/2023
 */

public class PUSH extends UnaryInstructionArm{
	public PUSH(ListRegArm regList) {
		super(regList);
	}

	@Override
	protected void display(PrintStream s) {
		// TODO Auto-generated method stub
		
	}
}
