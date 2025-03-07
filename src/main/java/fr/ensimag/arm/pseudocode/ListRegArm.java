package fr.ensimag.arm.pseudocode;

import java.util.List;

public class ListRegArm extends RegisterArm{
	
	/** reglist : is a non-empty list of registers,
	 * enclosed in braces. It can contain register
	 * ranges. It must be comma separated if it 
	 * contains more than one register or 
	 * register range.*/
	List<GPRegisterArm> listReg;

	protected ListRegArm(String name) {
		
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public List<GPRegisterArm> getListReg() {
		return listReg;
	}

}
