import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import java.util.function.Function;

public class PixelateTransform implements Function<Geometry, Pixels> {
  private final int numPixelsExtent;
  public PixelateTransform(int numPixelsExtent) {
    this.numPixelsExtent = numPixelsExtent;
  }
  @Override
  public Pixels apply(Geometry geometry) {
    Envelope envelope = enclosingEnvelope(geometry);
    double maxExtent = Math.max(envelope.getWidth(), envelope.getHeight());
    double pixelSize = maxExtent / numPixelsExtent;
    System.out.println(pixelSize);
    int w = (int) Math.ceil(envelope.getWidth() / pixelSize) + 1;
    int h = (int) Math.ceil(envelope.getHeight() / pixelSize) + 1;
    int divisions = 1; // how many offsets to try in each direction
    Pixels pixelsWithMinError = null;
    for (int i = 0; i < divisions; i++) {
      for (int j = 0; j < divisions; j++) {
        double startX = envelope.getMinX() - pixelSize * i / divisions;
        double startY = envelope.getMinY() - pixelSize * j / divisions;
        Pixels pixels = pixelate(geometry, w, h, startX, startY, pixelSize);
        if (pixelsWithMinError == null || pixels.getError() < pixelsWithMinError.getError()) {
          pixelsWithMinError = pixels;
        }
      }
    }
    return pixelsWithMinError;
  }

  private Pixels pixelate(Geometry geometry, int w, int h, double startX, double startY, double pixelSize) {
    boolean[][] pixels = new boolean[w][h];
    double error = 0.0;
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        double minX = startX + i * pixelSize;
        double minY = startY + j * pixelSize;
        Geometry pixel = pixel(geometry.getFactory(), minX, minY, pixelSize);
        double proportion = geometry.intersection(pixel).getArea() / pixel.getArea();
        pixels[i][h - j - 1] = proportion > 0.5;
        error += pixels[i][h - j - 1] ? (1.0 - proportion) : proportion;
      }
    }
    return new Pixels(pixels, error);
  }

  private static Envelope enclosingEnvelope(Geometry geometry) {
    final Envelope envelope = new Envelope();
    final Geometry enclosingGeometry = geometry.getEnvelope();
    final Coordinate[] enclosingCoordinates = enclosingGeometry.getCoordinates();
    for (Coordinate c : enclosingCoordinates) {
      envelope.expandToInclude(c);
    }
    return envelope;
  }

  private static Geometry pixel(GeometryFactory geometryFactory, double minX, double minY, double pixelSize) {
    double maxX = minX + pixelSize;
    double maxY = minY + pixelSize;
    return geometryFactory.createPolygon(new Coordinate[] {
        new Coordinate(minX, minY),
        new Coordinate(maxX, minY),
        new Coordinate(maxX, maxY),
        new Coordinate(minX, maxY),
        new Coordinate(minX, minY),
    });
  }
}
