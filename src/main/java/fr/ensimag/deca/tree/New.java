package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

public class New extends AbstractExpr {
	private AbstractIdentifier ident;

	protected String heapErr = "err_heap_overflow";
	
	public New(AbstractIdentifier ident) {
		super();
		this.ident = ident;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		Type t = ident.verifyType(compiler);
		ident.setType(t);
		ClassType type = (ClassType) compiler.environmentType.defOfType(ident.getName()).getType();
		/*if (!type.sameType(t)) {
			throw new ContextualError("instanciation impo", this.getLocation());
		}*/
		Definition def = compiler.environmentType.defOfType(ident.getName());
		this.setType(type);
		ident.setDefinition(def);
		return t;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		s.print("new ");
		this.ident.decompile(s);
		s.print("()");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		ident.prettyPrint(s, prefix, true);
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		ident.iter(f);
	}


	@Override
	protected void codeGenInit(DecacCompiler compiler, DAddr adr){
		if (!compiler.getCompilerOptions().getOptionN()) {
			compiler.addError(heapErr, "Erreur : allocation impossible, tas plein");
		}
		GPRegister reg = compiler.getFreeReg();
		compiler.addInstruction(new NEW(1 + ident.getClassDefinition().getNumberOfFields(), reg));
		if (!compiler.getCompilerOptions().getOptionN()) {
			compiler.addInstruction(new BOV(new Label(heapErr)));
		}
		compiler.useReg();
		DAddr dGB = new RegisterOffset(ident.getClassDefinition().getStackIndex(), Register.GB);
		compiler.addInstruction(new LEA(dGB, Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, reg)));
		compiler.addInstruction(new PUSH(reg));
		compiler.freeReg();
		compiler.addInstruction(new BSR(new Label("init." + getType().getName().getName())));
		compiler.incrTemp(2);
		compiler.addInstruction(new POP(reg));
		compiler.addInstruction(new STORE(reg, adr));
		//Implicit use and free of register
	}


	@Override
	protected DVal codeGenLoad(DecacCompiler compiler){

		if (!compiler.getCompilerOptions().getOptionN()) {
			compiler.addError(heapErr, "Erreur : allocation impossible, tas plein");
		}
		GPRegister reg = compiler.getFreeReg();
		compiler.addInstruction(new NEW(1 + ident.getClassDefinition().getNumberOfFields(), reg));
		if (!compiler.getCompilerOptions().getOptionN()) {
			compiler.addInstruction(new BOV(new Label(heapErr)));
		}
		compiler.useReg();
		DAddr dGB = new RegisterOffset(ident.getClassDefinition().getStackIndex(), Register.GB);
		compiler.addInstruction(new LEA(dGB, Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, reg)));
		compiler.addInstruction(new PUSH(reg));
		compiler.freeReg();
		compiler.addInstruction(new BSR(new Label("init." + getType().getName().getName())));
		compiler.incrTemp(2);
		compiler.addInstruction(new POP(reg));
		compiler.useReg();
		return reg;
	}

}
