public class Pixels {
  private final boolean[][] pixels;
  private final double error;

  public Pixels(boolean[][] pixels, double error) {
    this.pixels = pixels;
    this.error = error;
  }

  public boolean[][] getPixels() {
    return pixels;
  }

  public double getError() {
    return error;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int j = 0; j < pixels[0].length; j++) {
      for (int i = 0; i < pixels.length; i++) {
        boolean pixel = pixels[i][j];
        sb.append(pixel ? "\u2588" : " ");
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  public String toJavaScript() {
    StringBuilder sb = new StringBuilder();
    sb.append("[\n");
    for (int i = 0; i < pixels.length; i++) {
      sb.append("\t[");
      for (int j = 0; j < pixels[0].length; j++) {
        boolean pixel = pixels[i][j];
        sb.append(pixel ? 1 : 0);
        if (j < pixels[0].length - 1) {
          sb.append(", ");
        }
      }
      sb.append("]");
      if (i < pixels.length - 1) {
        sb.append(",");
      }
      sb.append("\n");
    }
    sb.append("]\n");
    return sb.toString();
  }
}

