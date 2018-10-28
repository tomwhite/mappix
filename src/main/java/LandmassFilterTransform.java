import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LandmassFilterTransform implements Function<MultiPolygon, MultiPolygon> {

  private List<Point> containedPoints;

  public LandmassFilterTransform(List<Point> containedPoints) {
    this.containedPoints = containedPoints;
  }

  @Override
  public MultiPolygon apply(MultiPolygon multiPolygon) {
    List<Polygon> polygons = new ArrayList<>();
    for (int n = 0; n < multiPolygon.getNumGeometries(); n++) {
      Geometry geometry = multiPolygon.getGeometryN(n);
      if (containedPoints.stream().anyMatch(geometry::contains)) {
        polygons.add((Polygon) geometry);
      }
    }
    return new MultiPolygon(polygons.toArray(new Polygon[0]), multiPolygon.getFactory());
  }
}
