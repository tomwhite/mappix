import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.util.function.Function;

public class ToSphericalMercatorTransform implements Function<MultiPolygon, Geometry> {
  @Override
  public Geometry apply(MultiPolygon multiPolygon) {
    try {
      CoordinateReferenceSystem sourceCRS = DefaultGeographicCRS.WGS84;
      CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:3857"); // Spherical Mercator projection coordinate system
      MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
      return JTS.transform(multiPolygon, transform);
    } catch (FactoryException | TransformException e) {
      throw new RuntimeException(e);
    }
  }
}
