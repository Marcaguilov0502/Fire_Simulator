import java.awt.*;
import java.util.ArrayList;

public class Palette {


    //Attributes


    private int[] colors;
    private int[] baseColors;


    //Constructors


    public Palette(int baseColor) {
        colors = new int[256];
        baseColors = new int[]{baseColor};
        for (int i = 0; i < 256; i++) {
            colors[i] = baseColors[0];
        }
    }

    public Palette(int baseR, int baseG, int baseB) {
        colors = new int[256];
        baseColors = new int[]{getColor(baseR,baseG,baseB)};
        for (int i = 0; i < 256; i++) {
            colors[i] = baseColors[0];
        }
    }

    public Palette(int baseA, int baseR, int baseG, int baseB) {
        colors = new int[256];
        baseColors = new int[]{getColor(baseA,baseR,baseG,baseB)};
        for (int i = 0; i < 256; i++) {
            colors[i] = baseColors[0];
        }
    }

    public Palette(int[] colorList) {
        colors = new int[256];
        baseColors = new int[colorList.length];
        for (int i = 0; i < colorList.length; i++) {
            baseColors[i] = colorList[i];
        }
        generatePaletteGradient();
    }

    public Palette(int[][] colorList) {
        colors = new int[256];
        baseColors = new int[colorList.length];
        if (colorList[0].length == 3) {
            for (int i = 0; i < colorList.length; i++) {
                baseColors[i] = getColor(colorList[i][0], colorList[i][1], colorList[i][2]);
            }
        } else if (colorList[0].length == 4) {
            for (int i = 0; i < colorList.length; i++) {
                baseColors[i] = getColor(colorList[i][0], colorList[i][1], colorList[i][2]);
            }
        } else {
            throw new IllegalArgumentException();
        }
        generatePaletteGradient();
    }

    public Palette(PreparedPalettes palette) {
        colors = new int[256];
        int[][] colorList = palette.getColorList();
        baseColors = new int[colorList.length];
        if (colorList[0].length == 3) {
            for (int i = 0; i < colorList.length; i++) {
                baseColors[i] = getColor(colorList[i][0], colorList[i][1], colorList[i][2]);
            }
        } else if (colorList[0].length == 4) {
            for (int i = 0; i < colorList.length; i++) {
                baseColors[i] = getColor(colorList[i][0], colorList[i][1], colorList[i][2], colorList[i][3]);
            }
        } else {
            throw new IllegalArgumentException();
        }
        generatePaletteGradient();
    }

    public Palette(ArrayList<Color> colorList) {
        colors = new int[256];
        baseColors = new int[colorList.size()];
        for (int i = 0; i < colorList.size(); i++) {
            baseColors[i] = getColor(colorList.get(i));
        }
        generatePaletteGradient();
    }


    //Methods


    public void add(int color) {
        int[] newBaseColors = new int[baseColors.length + 1];
        for (int i = 0; i < baseColors.length; i++) {
            newBaseColors[i] = baseColors[i];
        }
        newBaseColors[baseColors.length] = color;
        baseColors = newBaseColors;
        generatePaletteGradient();
    }

    public void add(int r, int g, int b) {
        int color = getColor(r,g,b);
        int[] newBaseColors = new int[baseColors.length + 1];
        for (int i = 0; i < baseColors.length; i++) {
            newBaseColors[i] = baseColors[i];
        }
        newBaseColors[baseColors.length] = color;
        baseColors = newBaseColors;
        generatePaletteGradient();
    }

    public void add(int a, int r, int g, int b) {
        int color = getColor(a,r,g,b);
        int[] newBaseColors = new int[baseColors.length + 1];
        for (int i = 0; i < baseColors.length; i++) {
            newBaseColors[i] = baseColors[i];
        }
        newBaseColors[baseColors.length] = color;
        baseColors = newBaseColors;
        generatePaletteGradient();
    }

    public int[] getARGB(int color) {

        int a = ((color >> 24) & 0xFF);
        int r = ((color >> 16) & 0xFF);
        int g = ((color >> 8) & 0xFF);
        int b = ((color >> 0) & 0xFF);

        int[] argb = new int[]{a,r,g,b};

        return argb;
    }

    public ArrayList<Color> getBaseColors() {
        ArrayList<Color> colors = new ArrayList();

        for (int i : baseColors) {
            int r = getARGB(i)[1];
            int g = getARGB(i)[2];
            int b = getARGB(i)[3];
            colors.add(new Color(r,g,b));
        }

        return colors;
    }

    public int getColor(int r, int g, int b) {
        int p = (255 << 24) | (r << 16) | (g << 8) | b;
        return p;
    }

    public int getColor(int a, int r, int g, int b) {
        int p = (a << 24) | (r << 16) | (g << 8) | b;
        return p;
    }

    public int getColor(int temperature) {
        return colors[temperature];
    }

    public int getColor(Color c) {
        return getColor(c.getAlpha(),c.getRed(),c.getGreen(),c.getBlue());
    }

    public Color getColorObject(int temperature) {
        int c = colors[temperature];
        int r = getARGB(c)[1];
        int g = getARGB(c)[2];
        int b = getARGB(c)[3];
        return new Color(r, g, b);
    }

    public void generatePaletteGradient() {
        float distanceBetweenColors = 255f / (float)(baseColors.length - 1);
        int[][] baseColorsArgb = new int[baseColors.length][4];

        for (int i = 0; i < baseColors.length; i++) {
            baseColorsArgb[i] = getARGB(baseColors[i]);
        }

        for (int section = 0; section < baseColors.length-1; section++) {
            int[] gradientSection = gradientSection(distanceBetweenColors,baseColorsArgb[section],baseColorsArgb[section + 1]);
            for (int i = 0; i < distanceBetweenColors; i++) {
                colors[i+(int)(section*distanceBetweenColors)] = gradientSection[i];
            }
        }
        colors[colors.length-1] = baseColors[baseColors.length-1];
    }

    public int[] gradientSection(float sectionSize, int[] color1, int[] color2) {
        float multiplier = 0;
        float multiplierIncrement = 1f/(sectionSize);
        int[] section = new int[(int)sectionSize+1];
        for (int i = 0; i < sectionSize; i++, multiplier += multiplierIncrement) {

            int[] argb = new int[]{
                    (int)((color1[0]*(1-multiplier)) + (color2[0]*multiplier)),
                    (int)((color1[1]*(1-multiplier)) + (color2[1]*multiplier)),
                    (int)((color1[2]*(1-multiplier)) + (color2[2]*multiplier)),
                    (int)((color1[3]*(1-multiplier)) + (color2[3]*multiplier))
            };
            section[i] = getColor(argb[0],argb[1],argb[2],argb[3]);
        }
        return section;
    }

}
