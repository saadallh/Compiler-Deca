package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

public class TestEnvironmentExp {
    DecacCompiler compiler;
    EnvironmentExp env = new EnvironmentExp(null);
    Type INT = new IntType(null);
    ExpDefinition e = new VariableDefinition(INT,null);
    Symbol name;
    boolean test = true;
    
    @BeforeEach
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
    }
    
    @Test
    public void testDecl() throws ContextualError, DoubleDefException {
    	name = compiler.createSymbol("a");
    	env.declare(name, e);
    	assert(env.get(name) != null);
    	try {
        	env.declare(name, e);
        	assertTrue(!test);
    	} catch (DoubleDefException e) {
    		assertTrue(test);
    	}
    }
}
