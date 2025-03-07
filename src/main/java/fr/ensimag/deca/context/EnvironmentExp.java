package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl24
 * @date 01/01/2023
 */
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).

    public EnvironmentExp parentEnvironment;
    private final Map<Symbol, ExpDefinition> envTypes = new HashMap<Symbol, ExpDefinition>();
    
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    @SuppressWarnings("finally")
	public ExpDefinition get(Symbol key) {
        //throw new UnsupportedOperationException("not yet implemented");
    	if (envTypes.get(key) != null) {
    		return envTypes.get(key);
    	}
    	else if (this.parentEnvironment != null) {
    		return this.parentEnvironment.get(key);
    	}
    	return null;
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary 
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        //throw new UnsupportedOperationException("not yet implemented");
    	if (envTypes.get(name) != null) {
    		throw new DoubleDefException();
    	}
    	envTypes.put(name, def);
    }
    
    public ExpDefinition getCurrent(Symbol key) {
    	return envTypes.get(key);
    }

	public Map<Symbol, ExpDefinition> getEnvTypes() {
		return envTypes;
	}

	public void Empilement(EnvironmentExp env) {
    	Set<Symbol> s = envTypes.keySet();
    	Iterator<Symbol> i = s.iterator();
    	while(i.hasNext()) {
    		Symbol verif = i.next();
    		if(env.getCurrent(verif) == null) {
    			try {
					env.declare(verif, envTypes.get(verif));
				} catch (DoubleDefException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
    
    
    public void UnionDisjoint(EnvironmentExp env) throws DecacFatalError {
    	Set<Symbol> s = envTypes.keySet();
    	Iterator<Symbol> i = s.iterator();
    	while(i.hasNext()) {
    		Symbol verif = i.next();
    		if (env.getCurrent(verif) != null) {
    			throw new DecacFatalError("internal error");
    		}
    		else {
    			ExpDefinition def = envTypes.get(verif);
    			try {
					env.declare(verif, def);
				} catch (DoubleDefException e) {
					e.printStackTrace();
				}
    		}	
    	}
    }
    
    

}
