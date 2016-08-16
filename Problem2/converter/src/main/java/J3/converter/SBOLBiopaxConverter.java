package J3.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.biopax.paxtools.impl.level3.ComplexAssemblyImpl;
import org.biopax.paxtools.impl.level3.ConversionImpl;
import org.biopax.paxtools.impl.level3.DegradationImpl;
import org.biopax.paxtools.impl.level3.GeneticInteractionImpl;
import org.biopax.paxtools.impl.level3.MolecularInteractionImpl;
import org.biopax.paxtools.impl.level3.PathwayImpl;
import org.biopax.paxtools.impl.level3.TemplateReactionImpl;
import org.biopax.paxtools.io.jena.JenaIOHandler;
import org.biopax.paxtools.model.BioPAXElement;
import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.model.level3.Complex;
import org.biopax.paxtools.model.level3.ComplexAssembly;
import org.biopax.paxtools.model.level3.Control;
import org.biopax.paxtools.model.level3.Conversion;
import org.biopax.paxtools.model.level3.Dna;
import org.biopax.paxtools.model.level3.DnaRegion;
import org.biopax.paxtools.model.level3.Entity;
import org.biopax.paxtools.model.level3.GeneticInteraction;
import org.biopax.paxtools.model.level3.Interaction;
import org.biopax.paxtools.model.level3.MolecularInteraction;
import org.biopax.paxtools.model.level3.Pathway;
import org.biopax.paxtools.model.level3.PhysicalEntity;
import org.biopax.paxtools.model.level3.Protein;
import org.biopax.paxtools.model.level3.Rna;
import org.biopax.paxtools.model.level3.RnaRegion;
import org.biopax.paxtools.model.level3.SmallMolecule;
import org.biopax.paxtools.model.level3.Stoichiometry;
import org.biopax.paxtools.model.level3.TemplateReaction;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.ModuleDefinition;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.SequenceOntology;
import org.sbolstandard.core2.SystemsBiologyOntology;

import sbol.SBOLModel;

/** SBOLBiopaxConverter - Converts between BioPAX and SBOL models
 * 
 * @author Jonathan Naylor - BDAthlon - 16/08/16
 *
 */
public class SBOLBiopaxConverter implements SBOLConverter {

	// BioPAX model representation
	protected Model biopax_model;
	
	// SBOL model representation
	protected SBOLModel sbol_model;
	
	/** Convert a BioPAX model to SBOL 
	 * @throws SBOLValidationException
	 * @throws SBOLConversionException 
	 */
	public void convertToSBOL(InputStream in, String sbol_file) throws IOException, SBOLValidationException, SBOLConversionException {
		initialiseBioPAXModel(in);
		initialiseSBOLModel();
		processBioPAXModel();
		sbol_model.writeSBOL(sbol_file);
	}

	/** Initialise a BioPAX model from an input stream (from a .owl/.rdf)
	 * 
	 * @param InputStream - input stream (of the BioPAX .owl/.rdf model)
	 */
	public void initialiseBioPAXModel(InputStream in){
		biopax_model = new JenaIOHandler().convertFromOWL(in);
	}
	
	/** Initialise the SBOL model
	 * 
	 * @throws SBOLValidationException
	 */
	public void initialiseSBOLModel() throws SBOLValidationException {
		sbol_model = new SBOLModel();
		sbol_model.createDocument();
	}
	
	/** Process the BioPAX model to an SBOL model 
	 * @throws SBOLValidationException  
	 * 
	 */
	public void processBioPAXModel() throws SBOLValidationException {
		for(BioPAXElement element : biopax_model.getObjects())
			processBioPAXElement(element);
	}
	
	/** Parse an BioPAX element into an SBOL element 
	 * @throws SBOLValidationException  
	 * @param BioPAXElement - element to be processed
	 */
	public void processBioPAXElement(BioPAXElement element) throws SBOLValidationException {
		
		// display details about the element
		System.out.println("--------------------------------------");
		System.out.print("\t" + element.getClass().getSimpleName());
		System.out.println("\t" + element.toString());

		// display all annotations attached to the BioPAX element
		for(Map.Entry<String, Object> entry : element.getAnnotations().entrySet())
			System.out.println(entry);
		
		// if the element is a BioPAX PhysicalEntity, convert it to an SBOL ComponentDefinition
		if(element instanceof PhysicalEntity)
			processPhysicalEntity((PhysicalEntity)element);

		// if the element is a BioPAX Pathway, convert it to an SBOL ModuleDefinition
		if(element instanceof Pathway)
			processPathway((Pathway)element);

		// if the element is a BioPAX Interaction, convert it to an SBOL Interaction
		if(element instanceof Interaction)
			processInteraction((Interaction)element);
		
		// if the element is a BioPAX Interaction, convert it to an SBOL Interaction
		if(element instanceof Stoichiometry)
			processStoichiometry((Stoichiometry)element);
	}
	
	/** Process a BioPAX PhysicalEntity 
	 * 
	 * @param physical_entity
	 * @throws SBOLValidationException  
	 */
	public void processPhysicalEntity(PhysicalEntity physical_entity) throws SBOLValidationException {
		System.out.println("\tPhysical -> ComponentDefinition");
		URI type = null;
		URI role = null;
		
		if(physical_entity instanceof Complex){ //DONE?
			Complex complex = (Complex)physical_entity;
			type = ComponentDefinition.COMPLEX;
		}
		if(physical_entity instanceof Dna){
			Dna dna = (Dna)physical_entity;
			type = ComponentDefinition.DNA;
		}
		if(physical_entity instanceof Protein){ //DONE?
			Protein protein = (Protein)physical_entity;
			type = ComponentDefinition.PROTEIN;
		}
		if(physical_entity instanceof Rna){
			Rna rna = (Rna)physical_entity;
			type = ComponentDefinition.RNA;
		}
		if(physical_entity instanceof SmallMolecule){
			SmallMolecule small_molecule = (SmallMolecule)physical_entity;
			type = ComponentDefinition.SMALL_MOLECULE;
			role = SequenceOntology.CDS;
		}
		if(physical_entity instanceof DnaRegion){ //DONE?
			DnaRegion dna_region = (DnaRegion)physical_entity;
			type = ComponentDefinition.DNA;
			role = SequenceOntology.CDS;
		}
		if(physical_entity instanceof RnaRegion){ //DONE?
			RnaRegion rna_region = (RnaRegion)physical_entity;
			type = ComponentDefinition.RNA;
			role = SequenceOntology.CDS;
		}
		
		sbol_model.createComponent(physical_entity.getRDFId().split("#")[1], type, role);
	}

	/** Process a BioPAX Pathway
	 * 
	 * @param pathway
	 * @throws SBOLValidationException 
	 */
	public void processPathway(Pathway pathway) throws SBOLValidationException{
		System.out.println("\tPathway -> ModuleDefinition");
		PathwayImpl pathway_obj = (PathwayImpl)pathway;
		String module_display_id = pathway.getRDFId().split("#")[1];

		ModuleDefinition module = sbol_model.createModule(module_display_id);
		
		Collection<org.biopax.paxtools.model.level3.Process> pathway_components = pathway.getPathwayComponent();
		for(org.biopax.paxtools.model.level3.Process component : pathway_components){
			String component_display_id = component.getRDFId().split("#")[1];
			//module.createFunctionalComponent(component_display_id, AccessType.PRIVATE, component_display_id, DirectionType.NONE);
			System.out.println("\t\t" + component_display_id);
		}
	}
	
	/** Process a BioPAX Interaction
	 * 
	 * @param interaction
	 * @throws SBOLValidationException  
	 */
	public void processInteraction(Interaction interaction) throws SBOLValidationException {
		System.out.println("\tInteraction -> Interaction");
		String display_id = interaction.getRDFId().split("#")[1];
		ModuleDefinition module = sbol_model.createModule(display_id);
		
		URI type = null;
		
		if(interaction instanceof Control){
			Control control = (Control)interaction;
			type = SystemsBiologyOntology.CONTROL;
		}
		if(interaction instanceof ConversionImpl){
			Conversion conversion = (Conversion)interaction;
			type = SystemsBiologyOntology.CONVERSION;
		}
		if(interaction instanceof GeneticInteractionImpl){
			GeneticInteraction genetic_interaciton = (GeneticInteraction)interaction;
			type = SystemsBiologyOntology.GENETIC_INTERACTION;
		}
		if(interaction instanceof MolecularInteractionImpl){
			MolecularInteraction molecular_interaction = (MolecularInteraction)interaction;
			type = SystemsBiologyOntology.MOLECULAR_INTERACTION;
		}
		if(interaction instanceof DegradationImpl){
			DegradationImpl degradation = (DegradationImpl)interaction;
			type = SystemsBiologyOntology.DEGRADATION;
		}
		if(interaction instanceof TemplateReactionImpl){
			TemplateReaction template_reaction = (TemplateReaction)interaction;
			type = SystemsBiologyOntology.PRODUCTION;
		}
		if(interaction instanceof ComplexAssemblyImpl){
			ComplexAssembly complex_assembly = (ComplexAssembly)interaction;
			type = SystemsBiologyOntology.PRODUCTION;
		}
		
		// create the interaction object
    	org.sbolstandard.core2.Interaction sbol_interaction = sbol_model.createInteraction(display_id, type, module);
		
		// add the participate stoichiometries [not working :(]
		if(interaction instanceof ComplexAssemblyImpl){
			ComplexAssembly complex_assembly = (ComplexAssembly)interaction;
			for(Stoichiometry stoichiometry : complex_assembly.getParticipantStoichiometry()){
				//sbol_interaction.
		
			}
		}
		
		System.out.println("\t\tinteraction id: " + display_id);
		
		// add all of the reaction participants
		for(Entity participant : interaction.getParticipant()){
			String participant_id = participant.getRDFId().split("#")[1];
			String participant_display_id = "participant_" + participant_id;
			System.out.println("\t\t\tparticipant_id: " + participant_id);
			
			
			// trying to create the participants in the interaction - participants need to have component definitions
			/*
			if(sbol_model.getSBOLDocument().getComponentDefinition(participant_id, "1") == null)
				sbol_model.createComponent(participant_id, ComponentDefinition.SMALL_MOLECULE, null);
			 */
			//sbol_interaction.createParticipation(participant_display_id, participant_id, SystemsBiologyOntology.REACTANT);
		}
		
	}
	
	/** Process a BioPAX Pathway
	 * 
	 * @param pathway
	 */
	public void processStoichiometry(Stoichiometry stoichiometry){
		System.out.println("\tStoichiometry -> ???????????");
		PhysicalEntity physical_entity = stoichiometry.getPhysicalEntity();
		
		//sbol_model.createModule(element.toString());
	}
	
	/** Convert an SBOL model to a BioPAX model 
	 *  
	 */
	public void convertFromSBOL(InputStream in, OutputStream out) {
		
	}
}
