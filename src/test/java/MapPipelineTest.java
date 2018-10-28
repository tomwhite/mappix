import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapPipelineTest {
  @Test
  public void testMapLoaderTransform() throws Exception {
    try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("land.bin")) {
      MapLoaderTransform mapLoaderTransform = new MapLoaderTransform();
      MultiPolygon landPolygons = mapLoaderTransform.apply(in);
      assertNotNull(landPolygons);
      assertEquals(4008, landPolygons.getNumGeometries());

      LandmassFilterTransform landmassFilterTransform = new LandmassFilterTransform(BritishIslesPipeline.britishIslesPoints());
      MultiPolygon britishIsles = landmassFilterTransform.apply(landPolygons);
      assertNotNull(britishIsles);
      assertEquals(2, britishIsles.getNumGeometries());
      assertEquals(2255, britishIsles.getGeometryN(0).getNumPoints());
      assertEquals(3707, britishIsles.getGeometryN(1).getNumPoints());

      SimplifyTransform simplifyTransform = new SimplifyTransform();
      MultiPolygon simplifiedBritishIsles = simplifyTransform.apply(britishIsles);
      assertEquals(2, simplifiedBritishIsles.getNumGeometries());
      assertTrue(simplifiedBritishIsles.getGeometryN(0).getNumPoints() < britishIsles.getGeometryN(0).getNumPoints());
      assertTrue(simplifiedBritishIsles.getGeometryN(1).getNumPoints() < britishIsles.getGeometryN(1).getNumPoints());

      ToSphericalMercatorTransform toSphericalMercatorTransform = new ToSphericalMercatorTransform();
      Geometry britishIslesSM = toSphericalMercatorTransform.apply(britishIsles);
      assertNotNull(britishIslesSM);
      assertEquals(2, britishIslesSM.getNumGeometries());

      PixelateTransform pixelateTransform = new PixelateTransform(9);
      Pixels pixels = pixelateTransform.apply(britishIslesSM);
      System.out.println(pixels);
      System.out.println(pixels.toJavaScript());
    }
  }
}
