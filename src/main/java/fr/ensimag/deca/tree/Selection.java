package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class Selection extends AbstractLValue {
	private AbstractExpr exp;
	private AbstractIdentifier ident;

	public AbstractExpr getExp() {
		return exp;
	}

	public AbstractIdentifier getIdent() {
		return ident;
	}

	public Selection(AbstractExpr exp, AbstractIdentifier ident) {
		super();
		this.exp = exp;
		this.ident = ident;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		Type t = exp.verifyExpr(compiler, localEnv, currentClass);
		ClassType c = t.asClassType("expression before DOT is not a class Type", getLocation());
		ClassDefinition class2 = c.getDefinition();
		EnvironmentExp env2 = class2.getMembers();
		
		ExpDefinition t2 = env2.get(ident.getName());
    	
		if(t2 == null) {
			throw new ContextualError("field not found", this.getLocation());
		}
		if (t2.isField()) {
			FieldDefinition res = t2.asFieldDefinition(null, getLocation());
			ident.setDefinition(res);
	    	ident.setType(res.getType());
			if(res.getVisibility() == Visibility.PUBLIC) {
				this.setType(t2.getType());
				return t2.getType();
			}
			else {
				if (currentClass == null) {
					throw new ContextualError("Access to protected field", this.getLocation());
				}
				if(compiler.environmentType.subType(t, currentClass.getType())){
					if(compiler.environmentType.subType(currentClass.getType(), res.getContainingClass().getType())) {
						this.setType(t2.getType());
						return t2.getType();
					}
				}
			}

		}
		throw new ContextualError("Selection type problem", this.getLocation());
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		this.exp.decompile(s);
		s.print(".");
		this.ident.decompile(s);		
	}
    @Override
    String prettyPrintNode() {
        return "Selection";
    }

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		
        exp.prettyPrint(s, prefix, false);
        ident.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		exp.iter(f);
        ident.iter(f);
		
	}


	@Override
	protected void codeGenPrint(DecacCompiler compiler, boolean printHex){
		boolean set = setAdrField(compiler, null);
		ident.codeGenPrint(compiler, printHex);
		if (set){
			compiler.freeReg();
			ident.getExpDefinition().setOperand(null);
		}
	}


	@Override
	protected DVal codeGenLoad(DecacCompiler compiler){
		boolean set = setAdrField(compiler, null);
		GPRegister registerToReturn = (GPRegister) ident.codeGenLoad(compiler);
		if (set) {
			ident.getFieldDefinition().setOperand(null);
//			compiler.freeReg();
		}
		return registerToReturn;
	}


	@Override
	protected boolean setAdrField(DecacCompiler compiler, GPRegister refReg){
		//If the left operand is this, we call ident.setAdrField()
		//If the left operand is an Identifier (x) and not a field, we store @x in reg and do ident.setAdrField(reg)
		//If the left operand is an Identifier (x) and a field, we get reg, do x.ident(reg), and we use it to set for ident
		//If the left operand is a selection, we call for a recursion
		return exp.setAdrField(compiler, refReg, (Identifier) ident);
	}


	@Override
	protected boolean setAdrField(DecacCompiler compiler, GPRegister refReg, Identifier ident){
		//This is called recursively to propagate the adress setting on the left of the selection
		boolean set1 = this.exp.setAdrField(compiler, refReg, (Identifier) this.ident);
		//Setting the adress of the identifier using the last adress set with the recursion on the left part
		return this.ident.setAdrField(compiler, refReg, (Identifier) ident);
	}


	@Override
	protected void codeGenInit(DecacCompiler compiler, DAddr adr){
		GPRegister reg = (GPRegister) codeGenLoad(compiler);
		compiler.addInstruction(new STORE(reg, adr));
		compiler.freeReg();
	}



	@Override
	public boolean isIdent(){
		return false;
	}





}
