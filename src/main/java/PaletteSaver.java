import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class PaletteSaver extends File {

    //Attributes

    private final ArrayList<String> paletteNames = new ArrayList<>();
    private final ArrayList<Palette> palettes = new ArrayList<>();


    //Constructors


    public PaletteSaver(String pathname) {
        super(pathname);
        if (!pathname.endsWith(".json")) {throw new IllegalArgumentException("File must be a .json");}
        try {
            importPaletteNames();
            importPalettes();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    //Methods


    private int[] extractColors(String s) {
        ArrayList<String> arg = substringBetweenChars(s,':',',');
        int a = Integer.parseInt(arg.get(0));
        int r = Integer.parseInt(arg.get(1));
        int g = Integer.parseInt(arg.get(2));
        int b = Integer.parseInt(substringBetweenChars(s,':','}').get(0));
        return new int[]{a,r,g,b};
    }

    private ArrayList<String> substringBetweenChars(String s, char beginChar, char endChar) {
        int begin = -1;
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == endChar && begin > 0) {
                strings.add(s.substring(begin+1,i));
                begin = -1;
            } else if (s.charAt(i) == beginChar) {
                begin = i;
            }
        }
        return strings;
    }

    public void createJSON() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(getPath()));
        bw.write("{\n" +
                "  \"palettes\":[\n" +
                "    {\n" +
                "      \"name\":\"Standard\",\n" +
                "      \"baseColors\":[\n" +
                "        {\"a\":255,\"r\":0,\"g\":0,\"b\":0},\n" +
                "        {\"a\":255,\"r\":35,\"g\":25,\"b\":20},\n" +
                "        {\"a\":255,\"r\":145,\"g\":80,\"b\":50},\n" +
                "        {\"a\":255,\"r\":210,\"g\":120,\"b\":50},\n" +
                "        {\"a\":255,\"r\":255,\"g\":255,\"b\":120},\n" +
                "        {\"a\":255,\"r\":255,\"g\":255,\"b\":255}\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}");
        bw.close();
    }

    public ArrayList<String> getPaletteNames() {
        return paletteNames;
    }

    public Palette getPalette(String name) {
        try {
            return palettes.get(paletteNames.indexOf(name));
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void importPaletteNames() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(this));
        while (br.ready()) {
            ArrayList<String> strings = substringBetweenChars(br.readLine(),'"','"');
            if (strings.size() > 1 && strings.get(0).equals("name")) {
                paletteNames.add(strings.get(1));
            }
        }
        br.close();
    }

    public void importPalettes() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(this));
        ArrayList<Color> baseColors = new ArrayList<>();
        boolean first = true;

        while (br.ready()) {
            String line = br.readLine();
            ArrayList<String> strings = substringBetweenChars(line,'"','"');

            if (!strings.isEmpty() && strings.get(0).equals("baseColors")) {
                if (!first) {
                    palettes.add(new Palette(baseColors));
                    baseColors = new ArrayList<>();
                } else {
                    first = false;
                }
            } else if (strings.size() > 3 && strings.get(0).equals("a") && strings.get(1).equals("r") && strings.get(2).equals("g") && strings.get(3).equals("b")) {
                int[] argb = extractColors(line);
                baseColors.add(new Color(argb[1], argb[2], argb[3], argb[0]));
            }
        }
        br.close();
        palettes.add(new Palette(baseColors));
    }

}
