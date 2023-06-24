package locations;

import org.osbot.rs07.api.map.Area;

/**
 * Created by Transporter on 11/08/2016 at 21:37.
 */
@SuppressWarnings("unused")
public enum FishingLocations {
    BARBARIAN_VILLAGE(new Area(
            new int[][]{
                    { 3243, 3161 },
                    { 3250, 3160 },
                    { 3243, 3140 },
                    { 3235, 3143 }
            }
    ), "TROUT / SALMON"),
    BAXTORIAN_FALLS(new Area(
            new int[][]{
                    { 3243, 3161 },
                    { 3250, 3160 },
                    { 3243, 3140 },
                    { 3235, 3143 }
            }
    ), "LEAPING_FISH"),
    CATHERBY(new Area(
            new int[][]{
                    { 3243, 3161 },
                    { 3250, 3160 },
                    { 3243, 3140 },
                    { 3235, 3143 }
            }
    ), "SHRIMPS / ANCHOVIES", "TUNA / SWORDFISH", "LOBSTERS", "SHARKS"),
    DRAYNOR_VILLAGE(new Area(
            3080, 3237,
            3091, 3217
    ), "SHRIMPS / ANCHOVIES"),
    FISHING_GUILD(new Area(
            new int[][]{
                    { 3243, 3161 },
                    { 3250, 3160 },
                    { 3243, 3140 },
                    { 3235, 3143 }
            }
    ), "TUNA / SWORDFISH", "LOBSTERS", "SHARKS"),
    KARAMAJA(new Area(
            new int[][]{
                    { 3243, 3161 },
                    { 3250, 3160 },
                    { 3243, 3140 },
                    { 3235, 3143 }
            }
    ), "SHRIMPS / ANCHOVIES", "LOBSTERS", "TUNA / SWORDFISH"),
    LUMBRIDGE_SWAMP(new Area(
            new int[][]{
                    { 3243, 3161 },
                    { 3250, 3160 },
                    { 3243, 3140 },
                    { 3235, 3143 }
            }
    ), "SHRIMPS / ANCHOVIES"),
    SEERS_VILLAGE(new Area(
            new int[][]{
                    { 3243, 3161 },
                    { 3250, 3160 },
                    { 3243, 3140 },
                    { 3235, 3143 }
            }
    ), "TROUT / SALMON"),
    SHILO_VILLAGE(new Area(
            new int[][]{
                    { 3243, 3161 },
                    { 3250, 3160 },
                    { 3243, 3140 },
                    { 3235, 3143 }
            }
    ), "TROUT / SALMON", "PIKE");

    private Area area;
    private String[] fishNames;

    FishingLocations(Area area, String... fishNames) {
        this.area = area;
        this.fishNames = fishNames;
    }

    public Area getArea() {
        return area;
    }

    public String[] getFishNames() {
        return fishNames;
    }
}