package J3.converter;



public class ConverterEntry {
    public static void main(String[] args) throws Exception {
        SBOLBioPAX sbol_bio_pax = new SBOLBioPAX();
        sbol_bio_pax.convertToSBOL("GeneticToggleBioPax.owl", "GeneticToggleSBOL.sbol");
    }
}
