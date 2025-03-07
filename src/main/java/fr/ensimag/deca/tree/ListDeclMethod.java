package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
        for (AbstractDeclMethod m : getList()) {
            m.decompile(s);
            s.println();
	}
	}
	
    void verifyListDeclMethod(DecacCompiler compiler, Symbol className,
            Symbol superClass, EnvironmentExp env) throws ContextualError, DecacFatalError {
    	int k = ((ClassDefinition )compiler.environmentType.defOfType(superClass)).getNumberOfMethods();
    	((ClassDefinition )compiler.environmentType.defOfType(className)).setNumberOfMethods(k);
    	EnvironmentExp def;
        for (AbstractDeclMethod i : getList()) {
        	def = i.verifyDeclMethod(compiler,className, superClass, k);
        	def.UnionDisjoint(env);
        }
    }
    
    void verifyListMethod(DecacCompiler compiler, EnvironmentExp env,Symbol className) throws ContextualError {
        for (AbstractDeclMethod i : getList()) {
        	i.verifyMethod(compiler, className, env);
        }
    }


}
