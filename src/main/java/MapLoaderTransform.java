import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.InputStreamInStream;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

public class MapLoaderTransform implements Function<InputStream, MultiPolygon> {

  /**
   * Load a land shape file as a {@link MultiPolygon}.
   * @param in the shape file
   * @return a representation of the land areas as polygons
   */
  @Override
  public MultiPolygon apply(InputStream in) {
    try {
      return (MultiPolygon) new WKBReader().read(new InputStreamInStream(in));
    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }
  }
}
