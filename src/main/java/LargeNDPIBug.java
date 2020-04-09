import loci.common.services.ServiceFactory;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.meta.IMetadata;
import loci.formats.services.OMEXMLService;


public class LargeNDPIBug {

    public static void main(String[] args) {

        try {
            String filename = LargeNDPIBug.class.getResource("/doubleSizeSlide.ndpi").toString().replaceAll("file:/","");
            IFormatReader r = new ImageReader();

            ServiceFactory factory = new ServiceFactory();
            OMEXMLService service = factory.getInstance(OMEXMLService.class);
            IMetadata meta = service.createOMEXMLMetadata();
            r.setMetadataStore(meta);

            r.setId(filename);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

