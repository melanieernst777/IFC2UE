package at.ac.uibk.thesis.enums;

public enum SurfaceMaterial {

    BRICK("M_Brick_Clay_Old"),
    WOOD("M_Wood_Pine"),
    GRASS("M_Ground_Grass"),
    METAL("M_Metal_Steel"),
    VARNISH("M_Wood_Pine"),
    BITUMEN("M_Metal_Steel"), // looks the most like bitumen (it is also black)
    PLASTIC("M_Basic_Floor'"),
    GLASS("M_Glass"),
    CONCRETE("M_Concrete_Poured"),
    UNKNOWN("M_Basic_Floor"),

    BROWN_WOOD("M_Wood_Oak"),
    DARK_WOOD("M_Wood_Floor_Walnut_Polished");

    private final String unrealEngineMaterialName;

    SurfaceMaterial(String pathToTexture) {
        this.unrealEngineMaterialName = pathToTexture;
    }

    public String getMaterialName() {
        return unrealEngineMaterialName;
    }

}