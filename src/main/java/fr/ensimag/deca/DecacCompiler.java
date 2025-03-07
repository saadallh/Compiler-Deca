package fr.ensimag.deca;

import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import fr.ensimag.arm.pseudocode.*;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl24
 * @date 01/01/2023
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    //Holds information about the errors labels and messages
    private HashMap<String, String> errorsMap;

    public HashMap<String, String> getErrorsMap(){
        return errorsMap;
    }


    //The current free register, we start from 2
    private int currRegNum;

    //The maximal number of registers to use
    private int regMax;

    //Variable Offset: current d in d(GB)
    private int offset ;

    //Number of temporaries reserved in stack, in the main program(
    private int tempStack;

    //Maximum temporaries stacked in a bloc
    private int blocTempMax;


    //The maximum number of registers used in a block: we reset this at the beginning of each block
    private int blocRegMax;

    public int getBlocRegMax() {
        return blocRegMax;
    }

    public void resetBlocRegMax(){
        blocRegMax = 0;
    }

    public int getBlocTempMax(){
        return blocTempMax;
    }

    public void resetTempMax(){
        blocTempMax = 0;
    }




    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    public IMAProgram getProgram() {
        return program;
    }

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        //
        if (compilerOptions != null && compilerOptions.isArm()){
            this.dataSetArm = new HashSet<OperandArm>();
            this.currRegNumArm = 2;
            this.dataMapArm = new HashMap<LabelArm, String>();
            this.errorsMapArm = new HashMap<String, String >();
            this.definedFctArm = new HashSet<String>();
        }
        else {
            this.errorsMap = new HashMap<String, String>();
            this.regMax = 15;
            //
            this.currRegNum = 2;
            this.offset = 1;
            //
            this.tempStack = 0;
            //
            this.blocRegMax = 0;
        }

    }

    public DecacCompiler(CompilerOptions compilerOptions, File source, int regMax) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        //

        this.errorsMap = new HashMap<String, String>();

        this.regMax = regMax - 1;
        //
        this.currRegNum = 2;
        this.offset = 1;

        this.tempStack = 1;

        this.blocRegMax = 2;

    }



    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    public int getOffset(){
        return this.offset;
    }
    
    public void incOffset(int r){
        this.offset+=r;
    }

    public void setCurrRegNum(int i){
        this.currRegNum = i;
    }

    public int getCurrRegNum(){
        return this.currRegNum;
    }
    /**
     * @see
     * IMAProgram#add(AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * IMAProgram#addLabel(Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * IMAProgram#addInstruction(Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     * IMAProgram#addInstruction(Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addFirst(instruction, comment);
    }

    /**
     * @see
     * IMAProgram#addInstruction(Instruction)
     */
    public void addInstructionFirst(Instruction instruction) {
        program.addFirst(instruction);
    }

    /**
     * @see
     * IMAProgram#addInstruction(Instruction,
     * java.lang.String)
     */
    public void addInstructionFirst(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }
    
    /**
     * @see 
     * IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    
    private final CompilerOptions compilerOptions;
    private final File source;

    public void setProgram(IMAProgram program) {
        this.program = program;
    }

    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private IMAProgram program = new IMAProgram();
 

    /** The global environment for types (and the symbolTable) */
    public final SymbolTable symbolTable = new SymbolTable();
    public final EnvironmentType environmentType = new EnvironmentType(this);

    public Symbol createSymbol(String name) {
        //return null;// A FAIRE: remplacer par la ligne en commentaire ci-dessous
        return symbolTable.create(name);
    }

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = sourceFile.substring(0, sourceFile.lastIndexOf('.')) + ".ass";
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);
        if (compilerOptions.getOptionp()) {
        	prog.decompile(out);
        	return false;
        }
        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());


        prog.verifyProgram(this);
        if (compilerOptions.getOptionv()) {
        	return false;
        }
        assert(prog.checkAllDecorations());
        //

//        prog.decompile(out);

        addComment("start main program");

        if (!compilerOptions.isArm()){
            prog.codeGenProgram(this);
        }
        else {
            prog.codeGenProgramArm(this);
        }

        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        if (!compilerOptions.isArm()){
            program.display(new PrintStream(fstream));
        }
        else {
            //Displaying the arm assembly
            PrintStream s = new PrintStream(fstream);
            s.println(".global _start");
            s.println(".section .text");
            s.println();
            s.println("_start:");

            programArm.display(s);

            //Displaying the functions for arm assembly
            fctProgArm.display(s);

            //Display data section
            s.println(".section .data");
            Iterator<OperandArm> it = dataSetArm.iterator();
            while (it.hasNext()) {
                OperandArm op = it.next();
                //0 permets us to have a unique adress for 0
                s.println("\t\t" + op.toString() + ": .word  0");
            }

            for (LabelArm lab :dataMapArm.keySet() ) {
                s.println("\t\t" + lab.toString() + ": .ascii\t" + "\""+ dataMapArm.get(lab) + "\"");
            }


        }
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

    public void addError(String errLab, String errMsg){
        if (!errorsMap.containsKey(errLab)){
                errorsMap.put(errLab, errMsg);
        }
    }


    /**
     * Gives a free register, needs to check if it's possible before
     * @return
     */
    public GPRegister getFreeReg(){
        if (currRegNum>=2 && currRegNum <= regMax){
            return Register.getR(currRegNum);
        }
        else {
            throw new DecacInternalError("out of registers!");
        }
    }


    public void updateBlocRegMax(){
        if(blocRegMax < (this.currRegNum)){
            blocRegMax = (this.currRegNum);
        }
    }

    public void updateBlocTempMax(){
        if(blocTempMax < (this.tempStack)){
            blocTempMax = (this.tempStack);
        }
    }


    public void useReg(){
        assert(currRegNum <= regMax);
        currRegNum += 1;
        updateBlocRegMax();
    }


    public void resetTempStack(){
        tempStack = 0;
    }


    public void freeReg(){
        assert(currRegNum >=2);
        currRegNum -= 1;
    }

    public boolean useLoad(){
        return (regMax - currRegNum > 0);
    }


    /**
     * Increment the number of temporary variables, should be called after each PUSH
     */
    public void incrTemp(int i){

        tempStack += i;
        updateBlocTempMax();
    }

    /**
     * Decrement the number of temporary variables, should be called after each POP
      */
    public void decrTemp(int i){

        tempStack -= i;

    }

    //TODO: Solution for temporary variables

    public int getTempStack(){
        return tempStack;
    }




    /**************************For Arm*************************/

    //Used for strings
    public Map<LabelArm, String> dataMapArm;

    public Set<OperandArm> dataSetArm;

    //Adds data in .data section for arm
    public void addOperandData(OperandArm op){
        dataSetArm.add(op);
    }


    private int currRegNumArm;

    //todo: -r for -arm

    public void useRegArm(){
        currRegNumArm++;
    }


    public void freeRegArm(){
        currRegNumArm--;
    }


    /**
     * @see
     * ArmProgram#add(AbstractLineArm)
     */
    public void add(AbstractLineArm line) {
        programArm.add(line);
    }


    /**
     * @see ArmProgram#addComment(java.lang.String)
     */
    public void addCommentArm(String comment) {
        programArm.addComment(comment);
    }


    /**
     * @see
     * ArmProgram#addLabel(Label)
     */
    public void addLabel(LabelArm label) {
        programArm.addLabel(label);
    }


    /**
     * @see
     * ArmProgram#addInstruction(InstructionArm)
     */
    public void addInstruction(InstructionArm instruction) {
        programArm.addInstruction(instruction);
    }


    /**
     * @see
     * ArmProgram#addInstruction(InstructionArm,
     * java.lang.String)
     */
    public void addInstruction(InstructionArm instruction, String comment) {
        programArm.addFirst(instruction, comment);
    }


    /**
     * @see
     * ArmProgram#addInstruction(InstructionArm)
     */
    public void addInstructionFirst(InstructionArm instruction) {
        programArm.addFirst(instruction);
    }


    /**
     * @see
     * ArmProgram#addInstruction(InstructionArm,
     * java.lang.String)
     */
    public void addInstructionFirst(InstructionArm instruction, String comment) {
        programArm.addInstruction(instruction, comment);
    }


    /**
     * @see
     * ArmProgram#display()
     */
    public String displayArmProgram() {
        return programArm.display();
    }

    public GPRegisterArm getFreeRegArm(){
        return RegisterArm.getR(currRegNumArm);
    }

    /**
     * The main program. Every instruction generated will eventually end up here.
     * choosing between the IMA and the ARM achitecture depending on the
     * boolean isArm
     */
    private final ArmProgram programArm = new ArmProgram();
    private ArmProgram fctProgArm = new ArmProgram();

    public ArmProgram getFctProgArm() {
        return fctProgArm;
    }

    private HashMap<String, String> errorsMapArm;

    private HashSet<String> definedFctArm;


    public HashMap<String, String> getErrorsMapArm() {
        return errorsMapArm;
    }

    public void addErrorArm(String errLab, String errMsg){
        if (!errorsMapArm.containsKey(errLab)){
            errorsMapArm.put(errLab, errMsg);
        }
    }


    public void addFunction(String fct){
        if (!(definedFctArm.contains(fct))){
            definedFctArm.add(fct);
        }
    }

    public boolean isDefined(String fct){
       if (definedFctArm != null) {
           return definedFctArm.contains(fct);
       }
       return false;
    }

}
