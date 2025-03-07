package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.lang.String;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl24
 * @date 01/01/2023
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    
    public Set<String> set = new HashSet<String>();
    
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public boolean getOptionv() {
        return optionv;
    }
    
    public boolean getOptionr() {
        return optionr;
    }

    public boolean getOptionN(){return optionN;}

    public boolean isArm(){return arm;}
    
    public boolean getOptionp() throws DecacFatalError {
    	if (optionv && optionp) {
    		throw new DecacFatalError("L'option -p et -v ne sont pas compatibles");
    	}
        return optionp;
    }
    
    public void Settings(String s, int i) throws CLIException {
    	if (s.equals("-b")) {
    		if (i > 1) {
    			throw new CLIException("L'option -b doit etre appeler sans source file et sans options");
    		}
    		printBanner = true;
    	}
    	else if (s.equals("-v")) {
    		optionv = true;
    	}
    	else if (s.equals("-p")) {
    		optionp = true;
    	}
    	else if (s.equals("-r")) {
    		optionr = true;
    	}
        else if (s.equals("-n")){
            optionN = true;
        }
        else if (s.equals("-P")){
            parallel = true;
        }
        else if (s.equals("-arm")){
            arm = true;
        }
    }
    
    public void setNb(int d) throws CLIException {
    	if (!optionr) {
    		throw new CLIException("L'option -r doit etre suivi d'un nombre");
    	}
    	try {
            assert(d<= 16 && d >= 4);
    	} catch(java.lang.AssertionError e) {
    		throw new CLIException("L'option -r doit etre suivi d'un nombre entre 4 et 16");
    	}
    	this.customNumReg = d;
    }

    public int getCustomNumReg() {
        return customNumReg;
    }
    
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean optionv = false;

    private boolean optionp = false;
    private boolean optionr = false;

    private boolean optionN = false;

    private boolean arm = false;

    private int customNumReg = 0;
    private List<File> sourceFiles = new ArrayList<File>();

    /**
     * Parsing the arguments passed for the decac command
     * -b: shows a banner of the team
     * @param args
     * @throws CLIException
     */
    public void parseArgs(String[] args) throws CLIException {
        // A FAIRE : parcourir args pour positionner les options correctement.
    	set.add("-p");
    	set.add("-v");
    	set.add("-b");
    	set.add("-r");
        set.add("-n");
        set.add("-P");
        set.add("-arm");
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep defaul
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

//        throw new UnsupportedOperationException("not yet implemented");
        logger.debug("Trying the decac with no option arguments yet");
        if(args.length == 0){
            logger.debug("arguments empty");
        }
        else {
        	int i = args.length;
        	int j = 0;
        	String s;
        	while (j < i) {
        		s = args[j];
        		try {
        			int d = Integer.parseInt(s);
        			this.setNb(d);
        		}
        		catch (NumberFormatException ex) {
            		if (set.contains(s)) {
            			Settings(s, i);
            		}
            		else if (s.contains(".deca")) {
                        File currSource = new File(s);
                        sourceFiles.add(currSource);
            		}
            		else {
            			throw new CLIException("L'option" + s + " is incorrect"); 
        			}
        		}
    			j++;
        	}
        }
        /*
        else if (args[0].equals("-b")){
            logger.info("Showing the banner for the team");
            printBanner = true;

        }
        else{
            File currSource = new File(args[0]);
            sourceFiles.add(currSource);
        }*/
    }

    protected void displayUsage() {
        System.out.println("decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] [-arm] <fichier deca>...] | [-b]");
    }
}
