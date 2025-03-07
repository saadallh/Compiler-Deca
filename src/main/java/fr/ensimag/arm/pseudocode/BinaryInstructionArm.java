package fr.ensimag.arm.pseudocode;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Base class for instructions with 2 operands.
 *
 * @author aitdriss
 * @date 10/01/2023
 */

public class BinaryInstructionArm extends InstructionArm{
	
    private OperandArm operand1, operand2;

    public OperandArm getOperand1() {
        return operand1;
    }

    public OperandArm getOperand2() {
        return operand2;
    }

    @Override
	protected
    void displayOperands(PrintStream s) {
        s.print(" ");
        s.print(operand1);
        s.print(", ");
        s.print(operand2);
    }

    protected BinaryInstructionArm(OperandArm op1, OperandArm op2) {
        Validate.notNull(op1);
        Validate.notNull(op2);
        this.operand1 = op1;
        this.operand2 = op2;
    }
    
    @Override
    protected void display(PrintStream s){
        s.print("		" + getName());
        displayOperands(s);
    }
}

