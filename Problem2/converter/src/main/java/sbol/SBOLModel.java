package sbol;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.Interaction;
import org.sbolstandard.core2.Module;
import org.sbolstandard.core2.ModuleDefinition;
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
	
	/** Create the SBOL document 
	 * 
	 * @throws SBOLValidationException
	 */
    public void createDocument() throws SBOLValidationException {
       	version = "1";
        doc = new SBOLDocument();
        doc.setDefaultURIprefix("http://www.iwbdaconf.org/2016/#bdathlon");
        doc.setComplete(true);
        doc.setCreateDefaults(true);

        // initialise sequence-related fields
    	sequences = new HashMap<String, String>();
    	sequence_codes = new ArrayList<String>();
    	sequence_constraints = new HashMap<String, SequenceConstraint>();
    }
    

	
    ///////////////////////////////////////////////////////////////////////////////////////////////////
	///////////				Modules										///////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////

    /** Create an SBOL module
     * 
     * @param display_id
     * @return
     * @throws SBOLValidationException
     */
    public ModuleDefinition createModule(String display_id) throws SBOLValidationException {
    	 return doc.createModuleDefinition(display_id, version);
    }
    
    
    
    /** createComponent - creates an SBOL component 
     * 
     * @param display_id
     * @param type
     * @param role
     * @return
     * @throws SBOLValidationException
     */
    public ComponentDefinition createComponent(String display_id, URI type, URI role) throws SBOLValidationException {
    	ComponentDefinition component = doc.createComponentDefinition(display_id, version, type);
    	
    	// if the component has a role, add it to the component
    	if(role != null)
    		component.addRole(role);
    	
    	return component;
    }    
    
    /** Create a component definition in the SBOL document with a sequence and constraints 
     * 
     * @param display_id
     * @param type
     * @param role
     * @param sequence
     * @param sequence_constraint_ids
     * @return
     * @throws SBOLValidationException
     */
    public ComponentDefinition createComponent(String display_id, URI type, URI role, String sequence, String... sequence_constraint_ids) throws SBOLValidationException {
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
    	
    	return component;
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Interactions			 					///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Create an interaction 
     * 
     * @throws SBOLValidationException  **/
    public Interaction createInteraction(String display_id, URI type, ModuleDefinition target_module) throws SBOLValidationException {
    	return target_module.createInteraction(display_id, type);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Mappings			 						///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Create a mapping 
     * 
     * @param mapping_id
     * @param refinement_type
     * @param local_id
     * @param remote_id
     * @throws SBOLValidationException
     */
    public void createMapping(String mapping_id, RefinementType refinement_type, String local_id, String remote_id) throws SBOLValidationException {
    	template_module.createMapsTo(mapping_id, refinement_type, local_id, remote_id);
    }
    
    

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Sequences									///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Create the sequences for the repression model 
     * 
     * @throws SBOLValidationException
     */
    public void createSequences() throws SBOLValidationException {
    	for(int i = 0; i < sequence_codes.size(); i++){
    		sequences.put(sequence_names.get(i), sequence_codes.get(i));
    		doc.createSequence(sequence_names.get(i), version, sequences.get(sequence_names.get(i)), Sequence.IUPAC_DNA);
    	}
    }  
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Sequence Constraints 						///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Create a sequence constraint 
     * 
     * @param display_id
     * @param restriction_type
     * @param subject
     * @param object
     */
    public void createSequenceConstraint(String display_id, RestrictionType restriction_type, String subject, String object){
    	sequence_constraints.put(display_id, new SequenceConstraint(display_id, restriction_type, subject, object));
    }
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Write SBOL									///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /** Write the SBOL model 
     * 
     * @param file_name
     * @throws IOException
     * @throws SBOLConversionException
     */
    public void writeSBOL(String file_name) throws IOException, SBOLConversionException {
    	SBOLWriter.write(doc, file_name);
    }
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Write SBOL									///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /** Validate the SBOL document
     * 
     */
    public void validateSBOL(){
    	SBOLValidate.validateSBOL(doc, true, true, true);
    	if(SBOLValidate.getNumErrors() > 0)
    		for(String error : SBOLValidate.getErrors())
    			System.out.println(error);
    }
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Getters and setters							///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Gets the SBOL document
     * 
     */
    public SBOLDocument getSBOLDocument(){
    	return doc;
    }

}
