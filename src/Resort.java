public class Resort{

    private StarOptionsCalculator.ResortCode resortName;
    private String resortLocation;
    private Phase[] resortPhases;

    Resort(StarOptionsCalculator.ResortCode resortKeyword) {

        initializeResortValues(resortKeyword);
    }

    private void initializeResortValues(StarOptionsCalculator.ResortCode resortKey) {
        setResortName(resortKey);
        setResortLocation(resortKey);
        setResortPhases(resortKey);
    }

    private void setResortName(StarOptionsCalculator.ResortCode resortKey) { this.resortName = resortKey; }

    private void setResortLocation(StarOptionsCalculator.ResortCode resortKey) {
        switch (resortKey) {
            case SHERATON_DESERT:
            case WESTIN_KIERLAND:
                resortLocation = "Scottsdale, Arizona";
                break;
            case WESTIN_MISSION:
                resortLocation = "Rancho Mirage, California";
                break;
            case WESTIN_DESERT:
                resortLocation = "Palm Desert, California";
                break;
            case SHERATON_MOUNTAIN:
            case SHERATON_LAKESIDE:
            case WESTIN_RIVERFRONT:
                resortLocation = "Avon, Colorado";
                break;
            case SHERATON_STEAMBOAT:
                resortLocation = "Steamboat Springs, Colorado";
                break;
            case VISTANA_RESORT:
            case VISTANA_VILLAGES:
                resortLocation = "Orlando, Florida";
                break;
            case SHERATON_PGA:
                resortLocation = "Port St. Lucie, Florida";
                break;
            case VISTANA_BEACH:
                resortLocation = "Jensen Beach, Florida";
                break;
            case WESTIN_PRINCEVILLE:
            case SHERATON_KAUAI:
                resortLocation = "Kaua'i, Hawai'i";
                break;
            case WESTIN_KAANAPALI:
            case WESTIN_KAANAPALI_NORTH:
            case WESTIN_NANEA:
                resortLocation = "Maui, Hawai'i";
                break;
            case SHERATON_BROADWAY:
                resortLocation = "Myrtle Beach, South Carolina";
                break;
            case WESTIN_LAGUNAMAR:
            case WESTIN_CANCUN:
                resortLocation = "Cancun, Mexico";
            case WESTIN_CABOS:
                resortLocation = "Los Cabos, Mexico";
                break;
            case HARBORSIDE_ATLANTIS:
                resortLocation = "Paradise Island, Bahamas";
                break;
            case WESTIN_STJOHN:
                resortLocation = "St. John, U.S. Virgin Islands";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + resortKey);
        }
    }

    private void setResortPhases(StarOptionsCalculator.ResortCode resortKey) {
        switch(resortKey) {
            case SHERATON_DESERT:
            case WESTIN_KIERLAND:
            case WESTIN_DESERT:
            case WESTIN_MISSION:
            case SHERATON_LAKESIDE:
            case SHERATON_MOUNTAIN:
            case WESTIN_RIVERFRONT:
            case SHERATON_STEAMBOAT:
            case VISTANA_RESORT:
            case VISTANA_VILLAGES:
            case SHERATON_PGA:
            case VISTANA_BEACH:
            case WESTIN_PRINCEVILLE:
            case SHERATON_KAUAI:
            case WESTIN_KAANAPALI:
            case WESTIN_KAANAPALI_NORTH:
            case WESTIN_NANEA:
            case WESTIN_LAGUNAMAR:
            case WESTIN_CANCUN:
            case HARBORSIDE_ATLANTIS:
                this.resortPhases = new Phase[]{new Phase(resortKey, StarOptionsCalculator.PhaseName.MAIN_PHASE)};
                break;
            case SHERATON_BROADWAY:
                this.resortPhases = new Phase[]{
                        new Phase(resortKey, StarOptionsCalculator.PhaseName.PLANTATION),
                        new Phase(resortKey, StarOptionsCalculator.PhaseName.PALMETTO)};
                break;
            case WESTIN_CABOS:
                this.resortPhases = new Phase[]{
                        new Phase(resortKey, StarOptionsCalculator.PhaseName.MAIN_PHASE),
                        new Phase(resortKey, StarOptionsCalculator.PhaseName.BAJA_POINT)};
                break;
            case WESTIN_STJOHN:
                this.resortPhases = new Phase[]{
                        new Phase(resortKey, StarOptionsCalculator.PhaseName.VIRGIN_GRAND),
                        new Phase(resortKey, StarOptionsCalculator.PhaseName.BAY_VISTA),
                        new Phase(resortKey, StarOptionsCalculator.PhaseName.CORAL_VISTA),
                        new Phase(resortKey, StarOptionsCalculator.PhaseName.SUNSET_BAY)};
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + resortKey);
        }
    }

    Phase[] getResortPhases(){
        return this.resortPhases;
    }
}