public enum PreparedPalettes {

    BLUE(new int[][]{
            {255,0,0,0},
            {255,0,5,80},
            {255,20,20,255},
            {255,45,155,255},
            {255,125,255,255},
            {255,255,255,255}
    }),

    FIRE(new int[][]{
            {255,0,0,0},
            {255,100,65,40},
            {255,145,80,50},
            {255,210,120,50},
            {255,255,255,120},
            {255,255,255,255}
    }),

    GRAY_SCALE(new int[][]{
            {255,0,0,0},
            {255,255,255,255}
    }),

    MALEFICENT(new int[][]{
            {255,0,0,0},
            {255,30,0,50},
            {255,75,0,100},
            {255,70,155,55},
            {255,100,255,75},
            {255,255,255,255}
    }),

    RAINBOW(new int[][]{
            {255,0,0,0},
            {255,0,255,0},
            {255,0,255,255},
            {255,0,0,255},
            {255,255,0,255},
            {255,255,0,0},
            {255,255,255,0},
            {255,255,255,255}
    });

    private int[][] colorList;

    public int[][] getColorList() {
        return colorList;
    }

    PreparedPalettes(int[][] colors) {
        this.colorList = colors;
    }
}
