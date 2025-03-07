package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class InstanceOf extends AbstractExpr {
	private AbstractIdentifier type;
	private AbstractExpr expr;

	public InstanceOf(AbstractExpr expr, AbstractIdentifier type) {
		super();
		this.type = type;
		this.expr = expr;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		Type t = expr.verifyExpr(compiler, localEnv, currentClass);
		Type t2 = type.verifyType(compiler);
		if (t.isClassOrNull() && t2.isClass()) {
			this.setType(compiler.environmentType.BOOLEAN);
			return compiler.environmentType.BOOLEAN;
		}
		throw new ContextualError("can't perform an instance of", this.getLocation());
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		s.print("( ");
		this.expr.decompile(s);
		s.print(" instanceof ");
		this.type.decompile(s);
		s.print(" )");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		expr.prettyPrint(s, prefix, false);
		type.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
        expr.iter(f);
        type.iter(f);
	}

	@Override
	protected void codeGenInit(DecacCompiler compiler, DAddr adr){
		Label debutInstanceOf = new Label("DebutInstanceOf"+ getLocation().getLine() +
                "c." + getLocation().getPositionInLine());
		Label finInstanceOf = new Label("FinInstanceOf"+ getLocation().getLine() +
                "c." + getLocation().getPositionInLine());
		ClassType t = (ClassType) expr.getType();
		ClassDefinition clas = t.getDefinition();
		GPRegister r = compiler.getFreeReg();
		compiler.useReg();
		compiler.addLabel(debutInstanceOf);
		compiler.addInstruction(new LEA(new RegisterOffset(type.getClassDefinition().getStackIndex(),Register.GB), r));
		GPRegister r2 = compiler.getFreeReg();
		compiler.useReg();
		compiler.addInstruction(new LEA(new RegisterOffset(clas.getStackIndex(), Register.GB), r2));
		compiler.addInstruction(new CMP(r, r2));
		GPRegister r3 = compiler.getFreeReg();
		compiler.useReg();
		compiler.addInstruction(new SEQ(r3));
		compiler.addInstruction(new BEQ(finInstanceOf));
		while(clas.getSuperClass().getType().getName().getName() != "Object"){
			clas = clas.getSuperClass();
			compiler.addInstruction(new LEA(new RegisterOffset(clas.getStackIndex(), Register.GB), r2));
			compiler.addInstruction(new CMP(r, r2));
			compiler.addInstruction(new SEQ(r3));
			compiler.addInstruction(new BEQ(finInstanceOf));
		}
		compiler.addInstruction(new LEA(new RegisterOffset(1, Register.GB), r2));
		compiler.addInstruction(new CMP(r, r2));
		compiler.addInstruction(new SEQ(r3));
		compiler.addInstruction(new BEQ(finInstanceOf));
		compiler.addLabel(finInstanceOf);
		compiler.addInstruction(new STORE(r3, adr));
	}

	@Override
	protected void codeGenBranch(DecacCompiler compiler, boolean b, Label label){
		Label debutInstanceOf = new Label("DebutInstanceOf"+ getLocation().getLine() +
                "c." + getLocation().getPositionInLine());
		Label finInstanceOf = new Label("FinInstanceOf"+ getLocation().getLine() +
                "c." + getLocation().getPositionInLine());
		ClassType t = (ClassType) expr.getType();
		ClassDefinition clas = t.getDefinition();
		GPRegister r = compiler.getFreeReg();
		compiler.useReg();
		compiler.addLabel(debutInstanceOf);
		compiler.addInstruction(new LEA(new RegisterOffset(type.getClassDefinition().getStackIndex(),Register.GB), r));
		GPRegister r2 = compiler.getFreeReg();
		compiler.useReg();
		compiler.addInstruction(new LEA(new RegisterOffset(clas.getStackIndex(), Register.GB), r2));
		compiler.addInstruction(new CMP(r, r2));
		GPRegister r3 = compiler.getFreeReg();
		compiler.useReg();
		compiler.addInstruction(new SEQ(r3));
		compiler.addInstruction(new BEQ(finInstanceOf));
		while(clas.getSuperClass().getType().getName().getName() != "Object"){
			clas = clas.getSuperClass();
			compiler.addInstruction(new LEA(new RegisterOffset(clas.getStackIndex(), Register.GB), r2));
			compiler.addInstruction(new CMP(r, r2));
			compiler.addInstruction(new SEQ(r3));
			compiler.addInstruction(new BEQ(finInstanceOf));
		}
		compiler.addInstruction(new LEA(new RegisterOffset(1, Register.GB), r2));
		compiler.addInstruction(new CMP(r, r2));
		compiler.addInstruction(new SEQ(r3));
		compiler.addInstruction(new BEQ(finInstanceOf));
		compiler.addLabel(finInstanceOf);
		compiler.addInstruction(new CMP(0, r3));
        if (b){
            compiler.addInstruction(new BNE(label));
        }
        else {
            compiler.addInstruction(new BEQ(label));
        }
	}

	@Override
	protected DVal codeGenLoad(DecacCompiler compiler){
		Label debutInstanceOf = new Label("DebutInstanceOf" + getLocation().getLine() +
                "c." + getLocation().getPositionInLine());
		Label finInstanceOf = new Label("FinInstanceOf"+ getLocation().getLine() +
                "c." + getLocation().getPositionInLine());
		ClassType t = (ClassType) expr.getType();
		ClassDefinition clas = t.getDefinition();
		GPRegister r = compiler.getFreeReg();
		compiler.useReg();
		compiler.addLabel(debutInstanceOf);
		compiler.addInstruction(new LEA(new RegisterOffset(type.getClassDefinition().getStackIndex(),Register.GB), r));
		GPRegister r2 = compiler.getFreeReg();
		compiler.useReg();
		compiler.addInstruction(new LEA(new RegisterOffset(clas.getStackIndex(), Register.GB), r2));
		compiler.addInstruction(new CMP(r, r2));
		GPRegister r3 = compiler.getFreeReg();
		compiler.useReg();
		compiler.addInstruction(new SEQ(r3));
		compiler.addInstruction(new BEQ(finInstanceOf));
		while(clas.getSuperClass().getType().getName().getName() != "Object"){
			clas = clas.getSuperClass();
			compiler.addInstruction(new LEA(new RegisterOffset(clas.getStackIndex(), Register.GB), r2));
			compiler.addInstruction(new CMP(r, r2));
			compiler.addInstruction(new SEQ(r3));
			compiler.addInstruction(new BEQ(finInstanceOf));
		}
		compiler.addInstruction(new LEA(new RegisterOffset(1, Register.GB), r2));
		compiler.addInstruction(new CMP(r, r2));
		compiler.addInstruction(new SEQ(r3));
		compiler.addInstruction(new BEQ(finInstanceOf));
		compiler.addLabel(finInstanceOf);
		return r3;
	}

}
