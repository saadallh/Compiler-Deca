package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

public class MethodBody extends AbstractMethodBody {
	
    private ListDeclVar declVariables;
    private ListInst insts; 
    

	public MethodBody(ListDeclVar declVariables, ListInst insts) {
		super();
		this.declVariables = declVariables;
		this.insts = insts;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		s.println("{");
		s.indent();
		this.declVariables.decompile(s);
		this.insts.decompile(s);
		s.unindent();
		s.print("}");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
        declVariables.iter(f);
        insts.iter(f);		
	}

	@Override
	protected void verifyBody(DecacCompiler compiler, EnvironmentExp env, EnvironmentExp envExpParam, Symbol className,
			Type returnType) throws ContextualError {
		// TODO Auto-generated method stub
        declVariables.verifyListDeclVariable(compiler, envExpParam, (ClassDefinition) compiler.environmentType.defOfType(className));
        env.Empilement(envExpParam);
        insts.verifyListInst(compiler, envExpParam, (ClassDefinition) compiler.environmentType.defOfType(className), returnType);
	}

	@Override
	protected void codeGenBodyMethod(DecacCompiler compiler, GPRegister thisReg) {
		declVariables.codeGenListDeclVariable(compiler, Register.LB);
		insts.codeGenListInst(compiler);
	}

}
