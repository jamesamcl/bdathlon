package SBOLtoBioPAX.converter;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.Module;
import org.sbolstandard.core2.RefinementType;
import org.sbolstandard.core2.RestrictionType;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidate;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.SBOLWriter;
import org.sbolstandard.core2.Sequence;

/** SBOL Model - Jonny Naylor - IWBDA 2016 (Tue 15 Aug) **/
public class SBOLModel {
	
	// sbol model fields
	protected String version;
	protected SBOLDocument doc;
	protected Module template_module;
	
	protected ArrayList<String> sequence_names;
	protected ArrayList<String> sequence_codes;
	protected HashMap<String, String> sequences;
	protected HashMap<String, SequenceConstraint> sequence_constraints;
	
	/** Create the SBOL document **/
    public void createDocument() throws SBOLValidationException {
       	version = "1.0";
        doc = new SBOLDocument();
        doc.setDefaultURIprefix("http://sbols.org/CRISPR_Example/");
        doc.setComplete(true);
        doc.setCreateDefaults(true);
    }
    


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Components									///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Create a component definition in the SBOL document **/
    public void createComponent(String display_id, URI type, URI role) throws SBOLValidationException {
    	ComponentDefinition component = doc.createComponentDefinition(display_id, version, type);
    	
    	// if the component has a role, add it to the component
    	if(role != null)
    		component.addRole(role);
    }    
    
    /** Create a component definition in the SBOL document with a sequence and constraints **/
    public void createComponent(String display_id, URI type, URI role, String sequence, String... sequence_constraint_ids) throws SBOLValidationException {
    	ComponentDefinition component = doc.createComponentDefinition(display_id, version, type);
    	
    	// if there is a sequence, add it to the component
    	if(sequence != null)
    		component.addSequence(sequence);
    	
    	// if there are sequence constraints, add them to the component
    	if(sequence_constraint_ids != null){
    		for(String sequence_constraint_id : sequence_constraint_ids){
        		SequenceConstraint sequence_constraint = sequence_constraints.get(sequence_constraint_id);
    			component.createSequenceConstraint(sequence_constraint_id, sequence_constraint.getRestrictionType(), sequence_constraint.getSubject(), sequence_constraint.getObject());
    		}
    	}
    }
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Mappings			 						///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Create a mapping **/
    public void createMapping(String mapping_id, RefinementType refinement_type, String local_id, String remote_id) throws SBOLValidationException {
    	template_module.createMapsTo(mapping_id, refinement_type, local_id, remote_id);
    }
    
    

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Sequences									///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Create the sequences for the repression model **/
    public void createSequences() throws SBOLValidationException {
    	for(int i = 0; i < sequence_codes.size(); i++){
    		sequences.put(sequence_names.get(i), sequence_codes.get(i));
    		doc.createSequence(sequence_names.get(i), version, sequences.get(sequence_names.get(i)), Sequence.IUPAC_DNA);
    	}
    }  
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Sequence Constraints 						///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Create a sequence constraint **/
    public void createSequenceConstraint(String display_id, RestrictionType restriction_type, String subject, String object){
    	sequence_constraints.put(display_id, new SequenceConstraint(display_id, restriction_type, subject, object));
    }
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Write SBOL									///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public void writeSBOL() throws IOException, SBOLConversionException {
    	SBOLWriter.write(doc, "/home/jonny/repression_model.rdf");
    }
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Write SBOL									///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public void validateSBOL(){
    	SBOLValidate.validateSBOL(doc, true, true, true);
    	if(SBOLValidate.getNumErrors() > 0)
    		for(String error : SBOLValidate.getErrors())
    			System.out.println(error);
    }
}
