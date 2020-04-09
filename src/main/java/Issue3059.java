import loci.formats.FormatReader;
import loci.formats.gui.AWTImageTools;
import loci.formats.in.NDPIReader;

import java.awt.image.BufferedImage;

public class Issue3059 {
    public static void main(String[] args) {
        // image from http://downloads.openmicroscopy.org/images/Hamamatsu-NDPI/manuel/test3-FITC%202%20(485).ndpi
        String filename = Issue3059.class.getResource("/test3-FITC 2 (485).ndpi").toString().replaceAll("file:/","").replaceAll("%20"," ");;
        try (FormatReader r = new NDPIReader()) {
            r.setId(filename);
            byte[] bytes = r.openBytes(0);
            int last = bytes[bytes.length-1] & 0xf;   // here the last byte (blue value) is 0, which is correct
            System.out.println(last);    // 0
            // openImage results in value 128 - which is wrong. Trace down to makeRGBImage...
            // BufferedImage bi = AWTImageTools.openImage(bytes,r,r.getSizeX(),r.getSizeY());
            // BufferedImage bi = AWTImageTools.makeImage(bytes, r.getSizeX(), r.getSizeY(), 3, true, 1, false, true, false);
            BufferedImage bi = AWTImageTools.makeRGBImage(bytes, 3, r.getSizeX(), r.getSizeY(), true);
            int lastBi = bi.getRaster().getSample(bi.getWidth()-1,bi.getHeight()-1,0);
            System.out.println(lastBi);  // 128 but should be 0
            // this problem holds for the last 8 scanline pixels (samples width*(height-1..8)*3), before it's correct
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
