package cy.ac.ouc.cognition.nestor.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;

import static cy.ac.ouc.cognition.nestor.lib.trace.Trace.errln;

import cy.ac.ouc.cognition.nestor.lib.base.NESTORBase;
import cy.ac.ouc.cognition.nestor.lib.pipeline.Pipeline;

public class NESTORMainWindow extends NESTORBase {


	enum PipeLineState {
		NLP_NOT_LOADED,
		NLP_LOADED,
		NEW_INSTRUCTION,
		NL_PROCESSED,
		LOGICPREDICATES_GENERATED,
		LOGICEXPRESSIONS_GENERATED
	}


	private static PipeLineState currentPipeLineState = PipeLineState.NLP_NOT_LOADED;
	
	/* Controls definition */
	private static Text txtNaturalLanguage;
	private static Text txtLogicBasedRepresentation;

	private static Button btnTranslationPolicyEdit;

	private static Button btnViewNLPData;
	private static Button btnViewDocumentJSON;
	private static Button btnProcessNL;
	
	private static Button btnViewLogicAnnotation;
	private static Button btnGenerateLogicPredicates;
	private static Button btnGenerateLogicExpressions;

	private static Button btnAddExpressionToTarget;

	private static Label lblLblMessageArea;
	private static Label lblTPStatus;

	protected Shell shlNESTOR;


    String ls = getSysParameters().getSystem_LineSeperator();
	private static Pipeline NESTORPipe;

	
	
	public static NESTORAppParameters getAppParameters() {
		
		return NESTORAppParameters.getInstance(null);
	
	}
	

	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
	    try {

	    	getAppParameters();
	    	
			NESTORPipe = new Pipeline("", false);

			NESTORMainWindow window = new NESTORMainWindow();

			window.createContents();

	    	AdjustControlState(PipeLineState.NLP_NOT_LOADED);

	    	window.open();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}



	static void AdjustControlState(PipeLineState lPipeLineState) {

		switch (lPipeLineState) {
			case NLP_NOT_LOADED:
    			btnProcessNL.setEnabled(false);
    			btnViewNLPData.setEnabled(false);
    			btnViewDocumentJSON.setEnabled(false);
    			btnGenerateLogicPredicates.setEnabled(false);
    			btnViewLogicAnnotation.setEnabled(false);
    			btnGenerateLogicExpressions.setEnabled(false);
    			btnAddExpressionToTarget.setEnabled(false);
    			break;

			case NLP_LOADED:
			case NEW_INSTRUCTION:
    			btnProcessNL.setEnabled(true);
    			btnViewNLPData.setEnabled(false);
    			btnViewDocumentJSON.setEnabled(false);
    			btnGenerateLogicPredicates.setEnabled(true);
    			btnViewLogicAnnotation.setEnabled(false);
    			btnGenerateLogicExpressions.setEnabled(true);
    			btnAddExpressionToTarget.setEnabled(false);
    			break;

			case NL_PROCESSED:
    			btnProcessNL.setEnabled(false);
    			btnViewNLPData.setEnabled(true);
    			btnViewDocumentJSON.setEnabled(true);
    			btnGenerateLogicPredicates.setEnabled(true);
    			btnViewLogicAnnotation.setEnabled(false);
    			btnGenerateLogicExpressions.setEnabled(true);
    			btnAddExpressionToTarget.setEnabled(false);
    			break;

			case LOGICPREDICATES_GENERATED:
    			btnProcessNL.setEnabled(false);
    			btnViewNLPData.setEnabled(true);
    			btnViewDocumentJSON.setEnabled(true);
    			btnGenerateLogicPredicates.setEnabled(true);
    			btnViewLogicAnnotation.setEnabled(true);
    			btnGenerateLogicExpressions.setEnabled(true);
    			btnAddExpressionToTarget.setEnabled(false);
    			break;

			case LOGICEXPRESSIONS_GENERATED:
    			btnProcessNL.setEnabled(false);
    			btnViewNLPData.setEnabled(true);
    			btnViewDocumentJSON.setEnabled(true);
    			btnGenerateLogicPredicates.setEnabled(false);
    			btnViewLogicAnnotation.setEnabled(true);
    			btnGenerateLogicExpressions.setEnabled(true);
    			btnAddExpressionToTarget.setEnabled(true);
    			break;

			default:
    			btnProcessNL.setEnabled(false);
    			btnViewNLPData.setEnabled(false);
    			btnViewDocumentJSON.setEnabled(false);
    			btnGenerateLogicPredicates.setEnabled(false);
    			btnViewLogicAnnotation.setEnabled(false);
    			btnGenerateLogicExpressions.setEnabled(false);
    			btnAddExpressionToTarget.setEnabled(false);
    			break;

		}

		currentPipeLineState = lPipeLineState;
	}


	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		shlNESTOR.open();
		shlNESTOR.layout();
		while (!shlNESTOR.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}


	/**
	 * Create contents of the window.
	 */
	protected void createContents() {

		shlNESTOR = new Shell();
		shlNESTOR.setSize(700, 800);
		shlNESTOR.setText("NESTOR - Knowledge-Based Natural languagE to Symbolic form TranslatOR");
    	shlNESTOR.setLayout(new FillLayout(SWT.VERTICAL));

    	Composite cmpStatus = new Composite(shlNESTOR, SWT.NONE);
    	cmpStatus.setLayout(new FillLayout(SWT.HORIZONTAL));
    	
    	Group grpNLP = new Group(cmpStatus, SWT.NONE);
    	grpNLP.setText("NLP");
    	grpNLP.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	Label lblNLPInfo = new Label(grpNLP, SWT.NONE);
    	lblNLPInfo.setAlignment(SWT.CENTER);
		lblNLPInfo.setText("Stanford Core NLP");
   	
    	Label lblNLPStatus = new Label(grpNLP, SWT.NONE);
    	
    	Button btnLoadNLP = new Button(grpNLP, SWT.NONE);
    	btnLoadNLP.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			try {
    				lblNLPStatus.setText("NLP Loading...");
    				System.gc();
    				NESTORPipe.setAndLoadNLProcessor(false);
    		    	AdjustControlState(PipeLineState.NLP_LOADED);
    				lblNLPStatus.setText("NLP Loaded.");
    			}
    			catch (Exception | OutOfMemoryError nlpe) {
    				System.gc();
    				lblNLPStatus.setText("NLP Failed to Load.");
    				System.out.println("NLP failed to load!");
    				System.out.println(nlpe.getMessage());
    		    	AdjustControlState(PipeLineState.NLP_NOT_LOADED);
    	    	}
    		}
    	});
    	btnLoadNLP.setText("Load NLP Library");
    	
    	Group grpTranslationPolicy = new Group(cmpStatus, SWT.NONE);
    	grpTranslationPolicy.setText("Translation Policy");
    	grpTranslationPolicy.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	Label lblTPInfo = new Label(grpTranslationPolicy, SWT.NONE);
    	lblTPInfo.setAlignment(SWT.CENTER);
    	lblTPInfo.setText(getAppParameters().getFileTranslationPolicy());
 	
    	lblTPStatus = new Label(grpTranslationPolicy, SWT.NONE);
    	lblTPStatus.setAlignment(SWT.CENTER);
    	
    	Button btnTranslationPolicyVersion = new Button(grpTranslationPolicy, SWT.NONE);
    	btnTranslationPolicyVersion.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {

    			String TranslationPolicyString = "";
    	        try {
    	        	TranslationPolicyString = Files.readString(Paths.get(getAppParameters().getFileTranslationPolicy()));
        	    	lblTPStatus.setText(NESTORPipe.getTranslationPolicyVersion(TranslationPolicyString));
    	        	
    			} 
    	        
    	        catch (FileNotFoundException nfe) {
    				errln("Translation Policy file not found: " + nfe.getMessage());
        	    	lblTPStatus.setText("File not found");
    			}
    	        
    	        catch (IOException ioe) {
    				errln("Error reading Translation Policy: " + ioe.getMessage());
        	    	lblTPStatus.setText("File error");
    			}

    		}
    	});
    	btnTranslationPolicyVersion.setText("Get Translation Policy Version");
    	
    	btnTranslationPolicyEdit = new Button(grpTranslationPolicy, SWT.NONE);
    	btnTranslationPolicyEdit.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {

    	        try {
    	        	Program.launch(getAppParameters().getFileTranslationPolicy());

    	        } catch (Exception ple) {
		        	MessageBox mt = new MessageBox(shlNESTOR);
		        	mt.setMessage(ple.toString());
		        	mt.open();          
    	        }
    		}
    	});
    	btnTranslationPolicyEdit.setText("Edit Translation Policy");
    	
    	Group grpTargetPolicy = new Group(cmpStatus, SWT.NONE);
    	grpTargetPolicy.setText("Target Policy");
    	grpTargetPolicy.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	Label lblTargetPolicyInfo = new Label(grpTargetPolicy, SWT.NONE);
    	lblTargetPolicyInfo.setAlignment(SWT.CENTER);
    	lblTargetPolicyInfo.setText(getAppParameters().getFileGeneratedExpressions());
    	
    	Label lblTargetPolicyStatus = new Label(grpTargetPolicy, SWT.NONE);
    	lblTargetPolicyStatus.setText("");
    	
    	Button btnTargetPolicyEdit = new Button(grpTargetPolicy, SWT.NONE);
    	btnTargetPolicyEdit.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    	        try {
    	        	Program.launch(getAppParameters().getFileGeneratedExpressions());

    	        } catch (Exception ple) {
		        	MessageBox mt = new MessageBox(shlNESTOR);
		        	mt.setMessage(ple.toString());
		        	mt.open();          
    	        }
    		}
    	});
    	btnTargetPolicyEdit.setText("Edit Target Policy");
    	
    	Group grpNaturalLanguage = new Group(shlNESTOR, SWT.NONE);
    	grpNaturalLanguage.setText("Natural Language");
    	grpNaturalLanguage.setLayout(new FillLayout(SWT.HORIZONTAL));
    	
    	txtNaturalLanguage = new Text(grpNaturalLanguage, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
    	txtNaturalLanguage.addModifyListener(new ModifyListener() {
    		public void modifyText(ModifyEvent arg0) {
	    		if (currentPipeLineState.ordinal() >= PipeLineState.NLP_LOADED.ordinal())
    		    	AdjustControlState(PipeLineState.NEW_INSTRUCTION);
	    		else
	    			AdjustControlState(currentPipeLineState);
    		}
    	});
    	
    	Composite cmpInstructionTextControls = new Composite(shlNESTOR, SWT.NONE);
    	cmpInstructionTextControls.setLayout(new FillLayout(SWT.HORIZONTAL));
    	
    	Group grpNLPControls = new Group(cmpInstructionTextControls, SWT.NONE);
    	grpNLPControls.setText("Natural Language Processing");
    	grpNLPControls.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	btnViewNLPData = new Button(grpNLPControls, SWT.NONE);
    	btnViewNLPData.setEnabled(false);
    	btnViewNLPData.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    	        try
    	        {
    	        	if (currentPipeLineState.ordinal() >= PipeLineState.NL_PROCESSED.ordinal()) {
    	        		Files.write(Paths.get(getAppParameters().getFileNLPData()), NESTORPipe.getNLPData().getBytes());
    	        		Program.launch(getAppParameters().getFileNLPData());
    	        	}
    	        	else {
    		        	MessageBox m = new MessageBox(shlNESTOR);
    		        	m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot show parse data! (MSG005-1)");
    		        	m.open();            	
    	        	}
    	        }
    	        catch(Exception e2) {
		        	MessageBox m = new MessageBox(shlNESTOR);
		        	m.setMessage("Error: Cannot show parse data! (MSG005-2)" + ls + e2.getMessage());
		        	m.open();            	
     	        }
    		}
    	});
    	btnViewNLPData.setText("View NL Processing Data");
    	
    	btnViewDocumentJSON = new Button(grpNLPControls, SWT.NONE);
    	btnViewDocumentJSON.setEnabled(false);
    	btnViewDocumentJSON.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    	        try
    	        {
    	        	if (currentPipeLineState.ordinal() >= PipeLineState.NL_PROCESSED.ordinal()) {
    	        		Files.write(Paths.get(getAppParameters().getFileDocumentJSON()), NESTORPipe.getPrettyDocumentJSON().getBytes());
    	        		Program.launch(getAppParameters().getFileDocumentJSON());
    	        	}
    	        	else {
    		        	MessageBox m = new MessageBox(shlNESTOR);
    		        	m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot show document JSON! (MSG005-3)");
    		        	m.open();            	
    	        	}
    	        }
    	        catch(Exception e2) {
		        	MessageBox m = new MessageBox(shlNESTOR);
		        	m.setMessage("Error: Cannot show parse data! (MSG005-2)" + ls + e2.getMessage());
		        	m.open();            	
     	        }
    		}
    	});
    	btnViewDocumentJSON.setText("View Document JSON");
    	
    	btnProcessNL = new Button(grpNLPControls, SWT.NONE);
    	btnProcessNL.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			PipeLineState prevPipeLineState = currentPipeLineState;
    	        try
    	        {
    				txtLogicBasedRepresentation.setText("");
    	        	String instructionText = txtNaturalLanguage.getText();
    	        	if (	currentPipeLineState.ordinal() >= PipeLineState.NEW_INSTRUCTION.ordinal() &&
    	        			instructionText != null && instructionText != "") {
    	        		NESTORPipe.processNL(instructionText);
    	        		AdjustControlState(PipeLineState.NL_PROCESSED);
    	        	}
    	        	else if (currentPipeLineState.ordinal() < PipeLineState.NEW_INSTRUCTION.ordinal()) {
		        		AdjustControlState(prevPipeLineState);
    		        	MessageBox m = new MessageBox(shlNESTOR);
    		        	m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot parse! (MSG006-1)");
    		        	m.open();            	   	        		
    	        	}
    	        	else {
		        		AdjustControlState(prevPipeLineState);
    		        	MessageBox m = new MessageBox(shlNESTOR);
    		        	m.setMessage("No instructions provided: Cannot parse! (MSG006-2)");
    		        	m.open();            	
    	        	}
    	        }
    	        catch(Exception e2) {
	        		AdjustControlState(prevPipeLineState);
		        	MessageBox m = new MessageBox(shlNESTOR);
		        	m.setMessage("Error: Cannot parse natural language! (MSG006-3)" + ls + e2.getMessage());
		        	m.open();            	
     	        }
    		}
    	});
    	btnProcessNL.setEnabled(false);
    	btnProcessNL.setText("Process Natural Language");
    	
    	Group grpLogicProcessing = new Group(cmpInstructionTextControls, SWT.NONE);
    	grpLogicProcessing.setText("Logic Processing");
    	grpLogicProcessing.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	btnViewLogicAnnotation = new Button(grpLogicProcessing, SWT.NONE);
    	btnViewLogicAnnotation.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
        	    try {
        	        if (currentPipeLineState.ordinal() >= PipeLineState.LOGICPREDICATES_GENERATED.ordinal()) {
        	        	Files.write(Paths.get(getAppParameters().getFileLogicAnnotation()), NESTORPipe.getLogicAnnotationText().getBytes());
        	        	Program.launch(getAppParameters().getFileLogicAnnotation());
        	        }
        	        else {
        		        MessageBox m = new MessageBox(shlNESTOR);
        		        m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot show Logic annotations! (MSG009-1)");
        		        m.open();            	
        	        }
        	    }
        	    catch(Exception e2) {
    		        MessageBox m = new MessageBox(shlNESTOR);
    		        m.setMessage("Error: Cannot show Logic annotations! (MSG009-2)" + ls + e2.getMessage());
    		        m.open();            	
         	    }
    		}
    	});
    	btnViewLogicAnnotation.setText("View Logic Annotation");
    	btnViewLogicAnnotation.setEnabled(false);
    	
    	btnGenerateLogicPredicates = new Button(grpLogicProcessing, SWT.NONE);
    	btnGenerateLogicPredicates.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			PipeLineState prevPipeLineState = currentPipeLineState;

    			try {
    				txtLogicBasedRepresentation.setText("");
    	        	String instructionText = txtNaturalLanguage.getText();

    	        	if (	currentPipeLineState.ordinal() >= PipeLineState.NEW_INSTRUCTION.ordinal() &&
    	        			instructionText != null && instructionText != "") {

       	        		if (currentPipeLineState.ordinal() < PipeLineState.NL_PROCESSED.ordinal())
       	        			NESTORPipe.processNL(instructionText);

    	        		NESTORPipe.generateLogicPredicates();
    	        		AdjustControlState(PipeLineState.LOGICPREDICATES_GENERATED);
    	        	}

     	        	else if (instructionText == null || instructionText == "") {
		        		AdjustControlState(prevPipeLineState);
    		        	MessageBox m = new MessageBox(shlNESTOR);
    		        	m.setMessage("No instructions provided: Cannot parse! (MSG010-1)");
    		        	m.open();            	
    	        	}
    	        	
    	        	else {
		        		AdjustControlState(prevPipeLineState);
    		        	MessageBox m = new MessageBox(shlNESTOR);
    		        	m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot generate logic predicates! (MSG010-2)");
    		        	m.open();            	   	        		
    	        	}
    	        }
    	        catch(Exception e2) {
	        		AdjustControlState(prevPipeLineState);
		        	MessageBox m = new MessageBox(shlNESTOR);
		        	m.setMessage("Error: Cannot parse natural language or generate logic predicates! (MSG010-3)" + ls + e2.getMessage());
		        	m.open();            	
     	        }
    		}
    	});
    	btnGenerateLogicPredicates.setText("Generate Logic Predicates");
    	btnGenerateLogicPredicates.setEnabled(false);
    	
    	btnGenerateLogicExpressions = new Button(grpLogicProcessing, SWT.NONE);
    	btnGenerateLogicExpressions.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {

    			PipeLineState prevPipeLineState = currentPipeLineState;

    	        try {
 
    	        	txtLogicBasedRepresentation.setText("");
    	        	txtLogicBasedRepresentation.redraw();
    	        	txtLogicBasedRepresentation.getParent().layout();
    	        	lblTPStatus.setText("");
    	        	String instructionText = txtNaturalLanguage.getText();
    	        	if (instructionText != null && instructionText != "") {

    	        		if (currentPipeLineState.ordinal() < PipeLineState.NL_PROCESSED.ordinal())
       	        			NESTORPipe.processNL(instructionText);
    	        		
    	        		if (currentPipeLineState.ordinal() < PipeLineState.LOGICPREDICATES_GENERATED.ordinal())
    	        			NESTORPipe.generateLogicPredicates();

    	        		
    	    			String TranslationPolicyString = "";
    	    	        try {
    	    	        	TranslationPolicyString = Files.readString(Paths.get(getAppParameters().getFileTranslationPolicy()));
    	    			}
    	    	        
    	    	        catch (FileNotFoundException nfe) {
    	    				errln("Translation Policy file not found: " + nfe.getMessage());
    	        	    	lblTPStatus.setText("File not found");
    	        	    	throw(nfe);
    	    			}
    	    	        
    	    	        catch (IOException ioe) {
    	    				errln("Error reading Translation Policy: " + ioe.getMessage());
    	        	    	lblTPStatus.setText("File error");
    	        	    	throw(ioe);
    	    			}
    	        	    
    	    	        NESTORPipe.generateLogicExpressions(TranslationPolicyString);       		
    	        		txtLogicBasedRepresentation.setText(NESTORPipe.getLogicBasedRepresentationText());
    	        		AdjustControlState(PipeLineState.LOGICEXPRESSIONS_GENERATED);

    	        	}

    	        	else {
		        		AdjustControlState(prevPipeLineState);
    		        	MessageBox m = new MessageBox(shlNESTOR);
    		        	m.setMessage("No instruction provided: Cannot parse! (MSG011-4)");
    		        	m.open();            	
    	        	}

    	        }
 
    	        catch(Exception e2) {
	        		AdjustControlState(prevPipeLineState);
		        	MessageBox m = new MessageBox(shlNESTOR);
		        	m.setMessage("Error: Cannot parse natural language or generate logic predicates or generate logic expressions! (MSG011-6)" + ls + e2.toString());
		        	m.open();            	
     	        }

    		}
    	});
    	btnGenerateLogicExpressions.setText("Translate to Logic Representation");
    	btnGenerateLogicExpressions.setEnabled(false);
    	
    	Group grpGeneratedLogicExpressions = new Group(shlNESTOR, SWT.NONE);
    	grpGeneratedLogicExpressions.setText("Generated Logic Expressions");
    	grpGeneratedLogicExpressions.setLayout(new FillLayout(SWT.HORIZONTAL));
    	
    			    txtLogicBasedRepresentation = new Text(grpGeneratedLogicExpressions, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
    	
    	Composite cmpGenerateLogicExpressionsControls = new Composite(shlNESTOR, SWT.NONE);
    	cmpGenerateLogicExpressionsControls.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	Composite cmpGRC1 = new Composite(cmpGenerateLogicExpressionsControls, SWT.NONE);
    	cmpGRC1.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	@SuppressWarnings("unused")
		Label lblGRC1Filler1 = new Label(cmpGRC1, SWT.NONE);
    	
    	Composite cmpGRC2 = new Composite(cmpGenerateLogicExpressionsControls, SWT.NONE);
    	cmpGRC2.setLayout(new FillLayout(SWT.HORIZONTAL));
    	
    	@SuppressWarnings("unused")
		Label lblGRC2Filler1 = new Label(cmpGRC2, SWT.NONE);
    	
    	@SuppressWarnings("unused")
		Label lblGRC2Filler2 = new Label(cmpGRC2, SWT.NONE);
    	
    	btnAddExpressionToTarget = new Button(cmpGRC2, SWT.CENTER);
    	btnAddExpressionToTarget.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
     	        try {
        	        if (currentPipeLineState.ordinal() >= PipeLineState.LOGICEXPRESSIONS_GENERATED.ordinal()) {
        	        	Files.write(Paths.get(getAppParameters().getFileGeneratedExpressions()), txtLogicBasedRepresentation.getText().getBytes(), StandardOpenOption.APPEND);
			        	MessageBox m = new MessageBox(shlNESTOR);
			        	m.setMessage("Expressions added to Target Policy!");
			        	m.open();
        	        }
        	        else {
			        	MessageBox m = new MessageBox(shlNESTOR);
			        	m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot generate logic predicates! (MSG012-1)");
			        	m.open();
        	        }
				} catch (IOException e1) {
    		        MessageBox m = new MessageBox(shlNESTOR);
    		        m.setMessage("Error: Cannot write expressions to Target Policy! (MSG012-2)" + ls + e1.getMessage());
    		        m.open();            	
				}
    		}
    	});
    	btnAddExpressionToTarget.setText("Add Expressions to Target Policy");
    	
    	lblLblMessageArea = new Label(cmpGenerateLogicExpressionsControls, SWT.NONE);
    	lblLblMessageArea.setText("");

	}

}
