package fr.ensimag.deca.context;

import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Definition associated to identifier in expressions.
 *
 * @author gl24
 * @date 01/01/2023
 */
public abstract class ExpDefinition extends Definition {

    public void setOperand(DAddr operand) {
        this.operand = operand;
    }

    public DAddr getOperand() {
        return operand;
    }
    private DAddr operand;
    private OperandArm operandArm;

    public ExpDefinition(Type type, Location location) {
        super(type, location);
    }



    /************************For Arm*************************/

    public void setOperandArm(OperandArm operandArm) {
        this.operandArm = operandArm;
    }

    public OperandArm getOperandArm() {
        return operandArm;
    }

}
