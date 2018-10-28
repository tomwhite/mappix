import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BritishIslesPipeline {
  public void run() {
    try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("land.bin")) {
      MapLoaderTransform mapLoaderTransform = new MapLoaderTransform();
      MultiPolygon landPolygons = mapLoaderTransform.apply(in);

      LandmassFilterTransform landmassFilterTransform = new LandmassFilterTransform(britishIslesPoints());
      MultiPolygon britishIsles = landmassFilterTransform.apply(landPolygons);

//      SimplifyTransform simplifyTransform = new SimplifyTransform();
//      MultiPolygon simplifiedBritishIsles = simplifyTransform.apply(britishIsles);

      ToSphericalMercatorTransform toSphericalMercatorTransform = new ToSphericalMercatorTransform();
      Geometry britishIslesSM = toSphericalMercatorTransform.apply(britishIsles);

      for (int numPixelsExtent = 1; numPixelsExtent <= 20; numPixelsExtent++) {
        PixelateTransform pixelateTransform = new PixelateTransform(numPixelsExtent);
        Pixels pixels = pixelateTransform.apply(britishIslesSM);
        System.out.println("var pixels" + numPixelsExtent + " = " + pixels.toJavaScript() + ";");
      }
    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }
  }

  static List<Point> britishIslesPoints() throws ParseException {
    WKTReader reader = new WKTReader(new GeometryFactory());
    Point p1 = (Point) reader.read("POINT (-3.1333 51.85)"); // where I live
    Point p2 = (Point) reader.read("POINT (-6.2603 53.34)"); // Dublin
    List<Point> points = new ArrayList<>();
    points.add(p1);
    points.add(p2);
    return points;
  }

  public static void main(String[] args) {
    new BritishIslesPipeline().run();
  }
}
