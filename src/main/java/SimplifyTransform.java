import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.simplify.VWSimplifier;

import java.util.function.Function;

public class SimplifyTransform implements Function<MultiPolygon, MultiPolygon> {
  @Override
  public MultiPolygon apply(MultiPolygon multiPolygon) {
    return (MultiPolygon) VWSimplifier.simplify(multiPolygon, 0.1);
  }
}
