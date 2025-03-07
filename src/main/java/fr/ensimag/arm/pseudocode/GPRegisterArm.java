package fr.ensimag.arm.pseudocode;


/**
 * General Purpose Register operand (R0, R1, ... R15).
 * 
 * @author aitdriss
 * @date 10/01/2023
 */

public class GPRegisterArm extends RegisterArm{
	/**
     * @return the number of the register, e.g. 12 for R12.
     */
    public int getNumber() {
        return number;
    }

    private int number;

    GPRegisterArm(String name, int number) {
        super(name);
        this.number = number;
    }
}
