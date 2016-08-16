package sbol;

import java.util.ArrayList;

import org.sbolstandard.core2.AccessType;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.DirectionType;
import org.sbolstandard.core2.Interaction;
import org.sbolstandard.core2.ModuleDefinition;
import org.sbolstandard.core2.RefinementType;
import org.sbolstandard.core2.RestrictionType;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.SequenceOntology;
import org.sbolstandard.core2.SystemsBiologyOntology;

/** SBOL Repression Model - Jonny Naylor - IWBDA 2016 (Mon 15 Aug) **/
public class RepressionModel extends SBOLModel {

	// Repression model fields 
	private ModuleDefinition crispr_template;
	private ModuleDefinition crpb_circuit;
	
	/** Repression model constructor **/
	public RepressionModel() throws SBOLValidationException {
		createDocument();
		createComponents();
		createInteractions();
    	initialiseSequences();
		createSequences();
		createConstraints();
		createDefinitions();
		createCircuit();
		createMappings();
	}
	
	
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Circuit template							///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /** Create the components for the repression model**/
    public void createComponents() throws SBOLValidationException {
    	crispr_template = createModule("CRISPR_Template");
    	createComponent("cas9_generic", 		ComponentDefinition.PROTEIN, 		null);
    	createComponent("gRNA_generic", 		ComponentDefinition.RNA, 			SequenceOntology.SGRNA);
    	createComponent("cas9_gRNA_complex", 	ComponentDefinition.COMPLEX, 		null);
    	createComponent("target_gene", 			ComponentDefinition.DNA, 			SequenceOntology.PROMOTER);
    	createComponent("target", 				ComponentDefinition.PROTEIN, 		null);
    }

    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Circuit implementation						///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /** Create the circuit for the repression model **/
    public void createCircuit() throws SBOLValidationException {
    	crpb_circuit = createModule("CRPb_characterization_circuit");
    	crpb_circuit.createFunctionalComponent("EYFP", AccessType.PRIVATE, "EYFP", version, DirectionType.NONE);
    	template_module = crpb_circuit.createModule("CRISPR_Template", "CRISPR_Template", version);
    }     
    
    /** Create the mappings for the circuit **/
    public void createMappings() throws SBOLValidationException {
    	createMapping("cas9m_BFP_map", 		RefinementType.USELOCAL, 	"cas9m_BFP", 			"cas9_generic");
    	createMapping("gRNA_b_map", 		RefinementType.USELOCAL, 	"gRNA_b", 				"gRNA_generic");
    	createMapping("cas9m_BFP_gRNA_map",	RefinementType.USELOCAL, 	"cas9m_BFP_gRNA_b", 	"cas9_gRNA_complex");
    	createMapping("EYFP_map", 			RefinementType.USELOCAL, 	"EYFP", 				"target");
    	createMapping("EYFP_gene_map", 		RefinementType.USELOCAL, 	"EYFP_gene", 			"target_gene");
    }

    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Interactions								///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Create the interactions for the repression model **/
    public void createInteractions() throws SBOLValidationException {
    	Interaction cas9complex_formation = createInteraction("cas9_cimplex_formation", SystemsBiologyOntology.NON_COVALENT_BINDING, crispr_template);
    	cas9complex_formation.createParticipation("participant_cas9_generic", 		"cas9_generic", 		SystemsBiologyOntology.REACTANT);
    	cas9complex_formation.createParticipation("participant_gRNA_generic", 		"gRNA_generic", 		SystemsBiologyOntology.REACTANT);
    	cas9complex_formation.createParticipation("participant_cas9_gRNA_complex", 	"cas9_gRNA_complex", 	SystemsBiologyOntology.PRODUCT);
    	
    	Interaction eyfp_production = createInteraction("target_production", SystemsBiologyOntology.GENETIC_PRODUCTION, crispr_template);
    	eyfp_production.createParticipation("participant_target_gene", 			"target_gene", 		SystemsBiologyOntology.PROMOTER);
    	eyfp_production.createParticipation("participant_target", 				"target", 			SystemsBiologyOntology.PRODUCT);

    	Interaction target_generic_gene_inhibition = createInteraction("target_gene_inhibition", SystemsBiologyOntology.INHIBITION, crispr_template);
    	target_generic_gene_inhibition.createParticipation("participant_cas9_gRNA_complex", 	"cas9_gRNA_complex", 	SystemsBiologyOntology.INHIBITOR);
    	target_generic_gene_inhibition.createParticipation("participant_target_gene", 			"target_gene", 			SystemsBiologyOntology.PROMOTER);
    }  
    
    /** Create the definitions for the repression model **/
    public void createDefinitions() throws SBOLValidationException {
    	createComponent("pConst", 				ComponentDefinition.DNA, 		SequenceOntology.PROMOTER);
    	createComponent("cas9m_BFP_cds", 		ComponentDefinition.DNA, 		SequenceOntology.CDS);
    	createComponent("cas9m_BFP_gene", 		ComponentDefinition.DNA, 		SequenceOntology.PROMOTER, 		null, 			"cas9m_BFP_gene_constraint");
    	createComponent("cas9m_BFP", 			ComponentDefinition.PROTEIN, 	null);
    	createComponent("CRa_U6", 				ComponentDefinition.DNA,	 	SequenceOntology.PROMOTER, 		"CRa_U6_seq", 	null);
    	createComponent("gRNA_b_nc", 			ComponentDefinition.DNA, 		SequenceOntology.CDS, 			"gRNA_b_seq", 	null);
    	createComponent("gRNA_b_terminator", 	ComponentDefinition.DNA, 		SequenceOntology.TERMINATOR);
    	createComponent("gRNA_b_gene", 			ComponentDefinition.DNA, 		SequenceOntology.PROMOTER, 		null, 			"gRNA_b_gene_constraint1", "gRNA_b_gene_constraint2");
    	createComponent("gRNA_b", 				ComponentDefinition.RNA, 		SequenceOntology.SGRNA);
    	createComponent("cas9m_BFP_gRNA_b", 	ComponentDefinition.COMPLEX, 	null);
    	createComponent("mKate_cds",	 		ComponentDefinition.DNA, 		SequenceOntology.CDS,	 		"mKate_seq",	null);
    	createComponent("mKate_gene",	 		ComponentDefinition.DNA, 		SequenceOntology.PROMOTER, 		null,			"mKate_gene_constraint");
    	createComponent("mKate",	 			ComponentDefinition.PROTEIN, 	null);
    	createComponent("Gal4VP16_cds",	 		ComponentDefinition.DNA, 		SequenceOntology.CDS);
    	createComponent("Gal4VP16_gene",	 	ComponentDefinition.DNA, 		SequenceOntology.PROMOTER, 		null,			"GAL4VP16_gene_constraint");
    	createComponent("Gal4VP16",	 			ComponentDefinition.PROTEIN, 	null);
    	createComponent("CRP_b", 				ComponentDefinition.DNA, 		SequenceOntology.PROMOTER, 		"CRP_b_seq",	null);
    	createComponent("EYFP_cds", 			ComponentDefinition.DNA, 		SequenceOntology.CDS);
    	createComponent("EYFP_gene", 			ComponentDefinition.DNA, 		SequenceOntology.PROMOTER, 		null, 			"EYFP_gene_constraint");
    	createComponent("EYFP",	 				ComponentDefinition.PROTEIN, 	null);
    }   
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Sequence Constraints 						///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Create the sequence constraints for the repression model **/
    public void createConstraints(){
    	createSequenceConstraint("cas9m_BFP_gene_constraint", 	RestrictionType.PRECEDES, 	"pConst", 		"cas9m_BFP_cds");
    	createSequenceConstraint("gRNA_b_gene_constraint1", 	RestrictionType.PRECEDES, 	"CRa_U6", 		"gRNA_b_nc");
    	createSequenceConstraint("gRNA_b_gene_constraint2", 	RestrictionType.PRECEDES, 	"gRNA_b_nc", 	"gRNA_b_terminator");
    	createSequenceConstraint("mKate_gene_constraint", 		RestrictionType.PRECEDES, 	"pConst", 		"mKate_cds");
    	createSequenceConstraint("GAL4VP16_gene_constraint", 	RestrictionType.PRECEDES, 	"pConst", 		"Gal4VP16_cds");
    	createSequenceConstraint("EYFP_gene_constraint", 		RestrictionType.PRECEDES, 	"CRP_b", 		"EYFP_cds");
    }   
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////				Sequences									///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Initialise the sequences for the repression model **/
    public void initialiseSequences(){
    	
    	// sequence names
    	sequence_names = new ArrayList<String>();
    	sequence_names.add("CRa_U6_seq");
    	sequence_names.add("gRNA_b_seq");
    	sequence_names.add("mKate_seq");
    	sequence_names.add("CRP_b_seq");
    	
    	// sequence codes
    	sequence_codes.add("GGTTTACCGAGCTCTTATTGGTTTTCAAACTTCATTGACTGTGCC" +
    			"AAGGTCGGGCAGGAAGAGGGCCTATTTCCCATGATTCCTTCATAT" +
    			"TTGCATATACGATACAAGGCTGTTAGAGAGATAATTAGAATTAAT" +
    			"TTGACTGTAAACACAAAGATATTAGTACAAAATACGTGACGTAGA" +
    			"AAGTAATAATTTCTTGGGTAGTTTGCAGTTTTAAAATTATGTTTT" +
    			"AAAATGGACTATCATATGCTTACCGTAACTTGAAATATAGAACCG" +
    			"ATCCTCCCATTGGTATATATTATAGAACCGATCCTCCCATTGGCT" +
    			"TGTGGAAAGGACGAAACACCGTACCTCATCAGGAACATGTGTTTA" +
    			"AGAGCTATGCTGGAAACAGCAGAAATAGCAAGTTTAAATAAGGCT" +
    			"AGTCCGTTATCAACTTGAAAAAGTGGCACCGAGTCGGTGCTTTTT" +
    			"TTGGTGCGTTTTTATGCTTGTAGTATTGTATAATGTTTTT");
    	
    	sequence_codes.add("AAGGTCGGGCAGGAAGAGGGCCTATTTCCCATGATTCCTTCATAT" +
    			"TTGCATATACGATACAAGGCTGTTAGAGAGATAATTAGAATTAAT" +
    			"TTGACTGTAAACACAAAGATATTAGTACAAAATACGTGACGTAGA" +
    			"AAGTAATAATTTCTTGGGTAGTTTGCAGTTTTAAAATTATGTTTT" +
    			"AAAATGGACTATCATATGCTTACCGTAACTTGAAAGTATTTCGAT" +
    			"TTCTTGGCTTTATATATCTTGTGGAAAGGACGAAACACCGTACCT" +
    			"CATCAGGAACATGTGTTTAAGAGCTATGCTGGAAACAGCAGAAAT" +
    			"AGCAAGTTTAAATAAGGCTAGTCCGTTATCAACTTGAAAAAGTGG" +
    			"CACCGAGTCGGTGCTTTTTTT");
    	
    	sequence_codes.add("TCTAAGGGCGAAGAGCTGATTAAGGAGAACATGCACATGAAGCTG" +
    			"TACATGGAGGGCACCGTGAACAACCACCACTTCAAGTGCACATCC" +
    			"GAGGGCGAAGGCAAGCCCTACGAGGGCACCCAGACCATGAGAATC" +
    			"AAGGTGGTCGAGGGCGGCCCTCTCCCCTTCGCCTTCGACATCCTG" +
    			"GCTACCAGCTTCATGTACGGCAGCAAAACCTTCATCAACCACACC" +
    			"CAGGGCATCCCCGACTTCTTTAAGCAGTCCTTCCCTGAGGTAAGT" +
    			"GGTCCTACCTCATCAGGAACATGTGTTTTAGAGCTAGAAATAGCA" +
    			"AGTTAAAATAAGGCTAGTCCGTTATCAACTTGAAAAAGTGGCACC" +
    			"GAGTCGGTGCTACTAACTCTCGAGTCTTCTTTTTTTTTTTCACAG" +
    			"GGCTTCACATGGGAGAGAGTCACCACATACGAAGACGGGGGCGTG" +
    			"CTGACCGCTACCCAGGACACCAGCCTCCAGGACGGCTGCCTCATC" +
    			"TACAACGTCAAGATCAGAGGGGTGAACTTCCCATCCAACGGCCCT" +
    			"GTGATGCAGAAGAAAACACTCGGCTGGGAGGCCTCCACCGAGATG" +
    			"CTGTACCCCGCTGACGGCGGCCTGGAAGGCAGAAGCGACATGGCC" +
    			"CTGAAGCTCGTGGGCGGGGGCCACCTGATCTGCAACTTGAAGACC" +
    			"ACATACAGATCCAAGAAACCCGCTAAGAACCTCAAGATGCCCGGC" +
    			"GTCTACTATGTGGACAGAAGACTGGAAAGAATCAAGGAGGCCGAC" +
    			"AAAGAGACCTACGTCGAGCAGCACGAGGTGGCTGTGGCCAGATAC" +
    			"TGCG");
    	
    	sequence_codes.add("GCTCCGAATTTCTCGACAGATCTCATGTGATTACGCCAAGCTACG" +
    			"GGCGGAGTACTGTCCTCCGAGCGGAGTACTGTCCTCCGAGCGGAG" +
    			"TACTGTCCTCCGAGCGGAGTACTGTCCTCCGAGCGGAGTTCTGTC" +
    			"CTCCGAGCGGAGACTCTAGATACCTCATCAGGAACATGTTGGAAT" +
    			"TCTAGGCGTGTACGGTGGGAGGCCTATATAAGCAGAGCTCGTTTA" +
    			"GTGAACCGTCAGATCG");
    	
    }

}
