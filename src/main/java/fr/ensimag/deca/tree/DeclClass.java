package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import java.util.*;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl24
 * @date 01/01/2023
 */
public class DeclClass extends AbstractDeclClass {
	
	final private AbstractIdentifier className;
	private AbstractIdentifier superClass;
	final private ListeDeclField declfields;
	final private ListDeclMethod declmethods;
	
	public DeclClass(AbstractIdentifier className,AbstractIdentifier superClass,ListeDeclField declfields, ListDeclMethod declmethods) {
		this.className = className;
		this.superClass = superClass;
		this.declfields = declfields;
		this.declmethods = declmethods;
	}


    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Symbol name = className.getName();
    	Symbol superName;
    	ClassDefinition superClas = null;
    	if (superClass != null) {
        	superName = superClass.getName();
        	superClas = (ClassDefinition) compiler.environmentType.defOfType(superName);
        	if (superClas == null) {
        		throw new ContextualError("superClass not defined", this.getLocation());
        	}
    	}
    	else {
    		superClass = new Identifier(compiler.createSymbol("Object"));
    		superClas = (ClassDefinition) compiler.environmentType.defOfType(compiler.createSymbol("Object"));
    	}
		superClass.setDefinition(superClas);
		superClass.setLocation(superClas.getLocation());
    	ClassType type = new ClassType(name, this.getLocation(), superClas);
    	//ClassDefinition def = new ClassDefinition(type, this.getLocation(), superClas);
    	ClassDefinition def = type.getDefinition();
    	compiler.environmentType.declareClass(name, def);
    	className.setDefinition(def);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Symbol supe = null;
    	//if (superClass != null) {
    	supe = superClass.getName();
    	ClassDefinition defCourante  = (ClassDefinition) compiler.environmentType.defOfType(className.getName());
    	EnvironmentExp env = defCourante.getMembers();
    	ClassDefinition def  = (ClassDefinition) compiler.environmentType.defOfType(supe);
    	if (def == null) {
    		throw new ContextualError("problems with verifyClassMembers", this.getLocation());
    	}
    	//}
    	EnvironmentExp ee = new EnvironmentExp(null);
    	EnvironmentExp env_exp_super = def.getMembers();
    	try {
			declfields.verifyListDeclField(compiler, className.getName(), supe , ee);
			declmethods.verifyListDeclMethod(compiler, className.getName(), supe, ee);
			ee.Empilement(env);
			EnvironmentType neww = new EnvironmentType(compiler);
			neww.declareClass(className.getName(), defCourante);
			compiler.environmentType.Empilement(neww);
		} catch (DecacFatalError e) {
			// TODO Auto-generated catch block
			throw new ContextualError("environment are not disjoint", this.getLocation());
		}
    	
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Symbol s = className.getName();
    	ClassDefinition def = (ClassDefinition) compiler.environmentType.defOfType(s);
    	EnvironmentExp env = def.getMembers();
    	declfields.verifyListFields(compiler, env, s);
    	declmethods.verifyListMethod(compiler, env, s);
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //throw new UnsupportedOperationException("Not yet supported");
    	className.prettyPrint(s, prefix, false);
    	if (superClass != null) {
    		superClass.prettyPrint(s, prefix, false);
    	}
    	declfields.prettyPrint(s, prefix, false);
    	declmethods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
    	className.iter(f);
    	if (superClass != null) {
        	superClass.iter(f);
    	}
    	declfields.iter(f);
    	declmethods.iter(f); 
        }

	@Override
	public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("Not yet supported");
		s.print("class ");
		this.className.decompile(s);
		s.print(" extends ");
		if(this.superClass == null){
			s.print("Object");
		}
		else{
			this.superClass.decompile(s);
		}
		s.println("{");
		s.indent();
		this.declfields.decompile(s);
		this.declmethods.decompile(s);
		s.unindent();
		s.print("}");
		
	}

	@Override
	protected void codeGenVirtualTable(DecacCompiler compiler) {
		compiler.addComment("Virtual Table of " + this.className.getName().getName() + " class");

		//Store @superClass, stackIndex of new class
		//Store inherited methods in, stakIndex + methodIndex for new class
		compiler.addInstruction(new LEA(new RegisterOffset(superClass.getClassDefinition().getStackIndex(), Register.GB), Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getOffset(), Register.GB)));
		this.className.getClassDefinition().setStackIndex(compiler.getOffset());
		compiler.incOffset(1);
		Label objectLabel = new Label("code.Object.equals");
		LabelOperand oLabelOperand = new LabelOperand(objectLabel);
		compiler.addInstruction(new LOAD(oLabelOperand, Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getOffset(), Register.GB)));
//		compiler.incOffset(declmethods.size());

		//Creating the table:
		//Structure will hold the methods to add
		TreeMap<Integer, String> methodMap = new TreeMap<Integer, String>();//Stores index method
		ClassDefinition currSuperClass = className.getClassDefinition();
		while (!currSuperClass.getType().getName().getName().equals("Object")){
			Iterator<Map.Entry<Symbol, ExpDefinition>> it = currSuperClass.getMembers().getEnvTypes().entrySet().iterator();
			while (it.hasNext()){
				Map.Entry<Symbol, ExpDefinition> couple = it.next();
				if (couple.getValue() instanceof MethodDefinition && !methodMap.containsKey(((MethodDefinition) couple.getValue()).getIndex())){
					//If prioritizes the child class, if the index is the same it doesn't add
					methodMap.put(((MethodDefinition) couple.getValue()).getIndex(), "code."
							+ currSuperClass.getType().getName().getName() + "."+ couple.getKey().getName());
				}
			}
			currSuperClass = currSuperClass.getSuperClass();
		}

//		compiler.incOffset(1);

		//Stacking the table
		for (Map.Entry<Integer, String> couple : methodMap.entrySet()){
			Label methodLabel = new Label(couple.getValue());
			LabelOperand opMethodLabel = new LabelOperand(methodLabel);
			compiler.addInstruction(new LOAD(opMethodLabel, Register.R0));
			compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(className.getClassDefinition().getStackIndex() +
					couple.getKey(), Register.GB)));
			compiler.incOffset(1);
		}
		compiler.incOffset(1);
	}

	@Override
	protected void codeGenFieldsMethods(DecacCompiler compiler){
		compiler.addComment("--------------------------------------------------");
		compiler.addComment("                 Classe " + className.getName().getName());
		compiler.addComment("--------------------------------------------------");
		//Code for fields.initializations
		compiler.addLabel(new Label("init."+ className.getName().getName()));
		//TODO : TSTO

		int oldRegNum = compiler.getCurrRegNum();
		compiler.setCurrRegNum(2);

		compiler.addComment("Saving registers");
		IMAProgram virtualProg = new IMAProgram();
		IMAProgram realProg = compiler.getProgram();

		compiler.setProgram(virtualProg);
		compiler.resetBlocRegMax();


		if (superClass.getName().getName().equals("Object")){
			for (AbstractDeclField d : declfields.getList()){
				d.codeGenDeclField(compiler);
			}
		}
		else {
			//r1 = -2(LB), initialize all new fields to 0, push r1, BSR init.Superclass, Subsp, effective init to new ones
			compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
			compiler.addInstruction(new LOAD(0, Register.R0));
			//init to 0 all fields
			for (AbstractDeclField d : declfields.getList()){
				compiler.addInstruction(new STORE(Register.R0,
						new RegisterOffset(d.getVarName().getFieldDefinition().getIndex(), Register.R1)));
			}
			//Push object
			compiler.addInstruction(new PUSH(Register.R1));
			compiler.addInstruction(new BSR(new LabelOperand(new Label("init." + superClass.getName().getName()))));
			compiler.incrTemp(2);
			compiler.addInstruction(new SUBSP(1));
			compiler.decrTemp(1);
			for (AbstractDeclField d : declfields.getList()){
				//If the declaration is not explicit, it was already done before
				if (d.getInitialization().isExplicit()){
					d.codeGenDeclField(compiler);
				}
			}

		}

		compiler.addComment("Restoring registers");
		int d = compiler.getBlocRegMax() - 2;
		for (int i = compiler.getBlocRegMax()-1; i >= 2; i--){
			compiler.addInstruction(new POP(Register.getR(i)));
			compiler.addInstructionFirst(new PUSH(Register.getR(i)));
		}
		if (d > 0){compiler.addInstructionFirst(new TSTO(d));}
		compiler.addInstruction(new RTS());
		realProg.append(virtualProg);
		compiler.setProgram(realProg);
		compiler.resetBlocRegMax();
		compiler.setCurrRegNum(oldRegNum);




		for (AbstractDeclMethod m : declmethods.getList()){
			m.codeGenDeclMethod(compiler, className.getName().getName());
		}
	}

}
