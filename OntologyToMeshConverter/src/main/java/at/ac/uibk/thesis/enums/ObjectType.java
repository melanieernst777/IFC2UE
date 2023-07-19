package at.ac.uibk.thesis.enums;

public enum ObjectType {

    DOOR("IfcDoor"),
    WINDOW("IfcWindow"),
    WALL("IfcWall"),
    ROOF("IfcRoof"),
    SLAB("IfcSlab"),
    STAIR_FLIGHT("IfcStairFlight"),
    RAILING("IfcRailing"),
    OPENING_ELEMENT("IfcOpeningElement"),
    SPACE("IfcSpace"),
    FLOW_TERMINAL("IfcFlowTerminal"),
    FURNISHING_ELEMENT("IfcFurnishingElement"),
    BUILDING_ELEMENT_PROXY("IfcBuildingElementProxy"),
    UNDEFINED("undefined");


    private final String identifier;

    ObjectType(String identifier) {
        this.identifier = identifier;
    }

    public static ObjectType getObjectType(String representationName) {
        for (ObjectType value : ObjectType.values()) {
            if (representationName.contains(value.identifier)) {
                return value;
            }
        }
        return ObjectType.UNDEFINED;
    }
}
