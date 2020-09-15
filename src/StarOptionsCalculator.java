import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

class StarOptionsCalculator {

    static private final int MIN_NIGHT_SEARCH = 1;
    static private final int MAX_NIGHT_SEARCH = 21;

    enum ResortCode {
        HARBORSIDE_ATLANTIS, SHERATON_BROADWAY, SHERATON_DESERT, SHERATON_KAUAI, SHERATON_LAKESIDE,
        SHERATON_MOUNTAIN, SHERATON_PGA, SHERATON_STEAMBOAT, VISTANA_BEACH, VISTANA_RESORT,
        VISTANA_VILLAGES, WESTIN_CABOS, WESTIN_CANCUN, WESTIN_DESERT, WESTIN_KAANAPALI,
        WESTIN_KAANAPALI_NORTH, WESTIN_KIERLAND, WESTIN_LAGUNAMAR, WESTIN_MISSION,
        WESTIN_NANEA, WESTIN_PRINCEVILLE, WESTIN_RIVERFRONT, WESTIN_STJOHN;

        private static String displayResortText(ResortCode resortCode) {
            switch(resortCode) {
                case HARBORSIDE_ATLANTIS:
                    return "HARBORSIDE RESORT AT ATLANTIS";
                case SHERATON_BROADWAY:
                    return "SHERATON BROADWAY PLANTATION";
                case SHERATON_DESERT:
                    return "SHERATON DESERT OASIS";
                case SHERATON_KAUAI:
                    return "SHERATON KAUA‘I RESORT";
                case SHERATON_LAKESIDE:
                    return "SHERATON LAKESIDE TERRACE VILLAS AT MOUNTAIN VISTA ";
                case SHERATON_MOUNTAIN:
                    return "SHERATON MOUNTAIN VISTA";
                case SHERATON_PGA:
                    return "SHERATON PGA VACATION RESORT";
                case SHERATON_STEAMBOAT:
                    return "SHERATON STEAMBOAT RESORT VILLAS";
                case VISTANA_BEACH:
                    return "VISTANA BEACH CLUB";
                case VISTANA_RESORT:
                    return "SHERATON VISTANA RESORT";
                case VISTANA_VILLAGES:
                    return "SHERATON VISTANA VILLAGES";
                case WESTIN_CABOS:
                    return "THE WESTIN LOS CABOS RESORT VILLAS & SPA";
                case WESTIN_CANCUN:
                    return "THE WESTIN RESORT & SPA, CANCÚN";
                case WESTIN_DESERT:
                    return "THE WESTIN DESERT WILLOW VILLAS, PALM DESERT";
                case WESTIN_KAANAPALI:
                    return "THE WESTIN KĀ‘ANAPALI OCEAN RESORT VILLAS";
                case WESTIN_KAANAPALI_NORTH:
                    return "THE WESTIN KĀ‘ANAPALI OCEAN RESORT VILLAS NORTH";
                case WESTIN_KIERLAND:
                    return "THE WESTIN KIERLAND VILLAS";
                case WESTIN_LAGUNAMAR:
                    return "THE WESTIN LAGUNAMAR OCEAN RESORT";
                case WESTIN_MISSION:
                    return "THE WESTIN MISSION HILLS RESORT VILLAS, PALM SPRINGS";
                case WESTIN_NANEA:
                    return "THE WESTIN NANEA OCEAN VILLAS";
                case WESTIN_PRINCEVILLE:
                    return "THE WESTIN PRINCEVILLE OCEAN RESORT VILLAS";
                case WESTIN_RIVERFRONT:
                    return "THE WESTIN RIVERFRONT MOUNTAIN VILLAS";
                case WESTIN_STJOHN:
                    return "THE WESTIN ST. JOHN RESORT VILLAS";
                default:
                    throw new IllegalStateException("Unexpected value: " + resortCode);
            }
        }
    }

    enum RoomType {
        ONE_BD, TWO_BD, THREE_BD, FOUR_BD;

        private static String displayVillaText(RoomType villaSize) {
            switch(villaSize){
                case ONE_BD:
                    return "1-Bedroom";
                case TWO_BD:
                    return "2-Bedroom";
                case THREE_BD:
                    return "3-Bedroom";
                case FOUR_BD:
                    return "4-Bedroom";
                default:
                    throw new IllegalStateException("Unexpected value: " + villaSize);
            }
        }
    }

    enum Season {
        PLATINUM_PLUS, PLATINUM, GOLD_PLUS, GOLD, SILVER;

        private static String displaySeasonText(Season villaSeason) {
            switch(villaSeason){
                case PLATINUM_PLUS:
                    return "Platinum+";
                case PLATINUM:
                    return "Platinum";
                case GOLD_PLUS:
                    return "Gold+";
                case GOLD:
                    return "Gold";
                case SILVER:
                    return "Silver";
                default:
                    throw new IllegalStateException("Unexpected value: " + villaSeason);
            }
        }
    }

    private static LocalDate setBeginDateOfYear(int chosenYear) {
        if (chosenYear > 0 && chosenYear < 3000)
            return LocalDate.of(chosenYear, 1, 1);
        else
            throw new IllegalStateException("Invalid Year: " + chosenYear);
    }

    private static LocalDate setFirstFridayOfYear(int chosenYear) {

        LocalDate beginDate = setBeginDateOfYear(chosenYear);

        while (beginDate.getDayOfWeek() != DayOfWeek.FRIDAY) {
            beginDate = beginDate.plusDays(1);
        }
        return beginDate;
    }

    private static int getWeekNumberOfDate(LocalDate chosenDate) {

        int weekCounter = 0;
        LocalDate beginDate = setFirstFridayOfYear(chosenDate.getYear());
        if (chosenDate.isBefore(beginDate)) {
            beginDate = setFirstFridayOfYear(chosenDate.getYear() - 1);
        }
        while (beginDate.isBefore(chosenDate) || beginDate.isEqual(chosenDate)) {
            weekCounter++;
            beginDate = beginDate.plusWeeks(1);
        }
        return weekCounter;
    }

    private static boolean doesYearHas53Weeks(int chosenYear) {

        int weekCounter = 0;
        LocalDate beginDate = setFirstFridayOfYear(chosenYear);
        LocalDate targetDate = setBeginDateOfYear(chosenYear + 1);

        while (beginDate.isBefore(targetDate)) {
            weekCounter++;
            beginDate = beginDate.plusWeeks(1);
        }
        return weekCounter == 53;
    }

    private static class Villa {
        private final String villaDescription;
        private final RoomType villaSize;
        private final int[][] starOptionValues;
        private final Season[] seasonsInVilla;

        private Villa(RoomType amountOfBedrooms, String villaDetails, int[][] starOptionsChart, Season[] villaSeasons) {
            this.villaDescription = villaDetails;
            this.villaSize = amountOfBedrooms;
            this.starOptionValues = starOptionsChart;
            this.seasonsInVilla = villaSeasons;
        }

        private RoomType getVillaRoomType() {
            return villaSize;
        }

        private String getVillaDescription(){
            return villaDescription;
        }

        private int getStarOptionValue(Season seasonChosen, DayOfWeek dayChosen){
            int starOptionValue = 0;
            for (int i = 0 ; i < seasonsInVilla.length ; i++) {
                if (seasonsInVilla[i] == seasonChosen) {
                    switch (dayChosen) {
                        case MONDAY:
                        case TUESDAY:
                        case WEDNESDAY:
                            starOptionValue = this.starOptionValues[i][0];
                            break;
                        case THURSDAY:
                        case SUNDAY:
                            starOptionValue = this.starOptionValues[i][1];
                            break;
                        case FRIDAY:
                        case SATURDAY:
                            starOptionValue = this.starOptionValues[i][2];
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + dayChosen);
                    }
                }
            }
            return starOptionValue;
        }

        private void displayVillaDetails(){
            System.out.println("Villa description: " + this.villaDescription);
            System.out.println("Villa type: " + RoomType.displayVillaText(this.villaSize));
            for (int i = 0 ; i < this.starOptionValues.length ; i++) {
                System.out.print(Season.displaySeasonText(seasonsInVilla[i]) + ": ");
                for (int j = 0 ; j < this.starOptionValues[i].length ; j++) {
                    System.out.print(this.starOptionValues[i][j] + ", ");
                }
                System.out.println();
            }
        }
    }

    public static class Phase {
        private final String phaseName;
        private final Season[] phaseSeasons;
        private final int[][] phaseWeeks;
        private final Villa[] phaseVillas;

        private Phase(String nameOfPhase, Season[] seasonsInPhase, int[][] weeksInPhase, Villa[] villasInPhase) {
            this.phaseName = nameOfPhase;
            this.phaseSeasons = seasonsInPhase;
            this.phaseWeeks = weeksInPhase;
            this.phaseVillas = villasInPhase;
        }

        private String getPhaseName(){
            return phaseName;
        }

        private Season getSeasonOfPhase(int weekNumber) {
            Season result = null;
            if (weekNumber == 53)
                weekNumber = 52;

            for (int i = 0 ; i < this.phaseWeeks.length ; i++) {
                for (int j = 0 ; j < this.phaseWeeks[i].length ; j = j + 2) {
                    if (weekNumber >= this.phaseWeeks[i][j] && weekNumber <= this.phaseWeeks[i][j+1]) {
                        result = this.phaseSeasons[i];
                        break;
                    }
                }
            }
            return result;
        }

        private void displayPhaseDetails(){
            System.out.println("Phase name is: " + this.phaseName);
            System.out.print("Seasons in phase are: ");
            for (int i = 0 ; i < this.phaseSeasons.length ; i++) {
                System.out.print(Season.displaySeasonText(phaseSeasons[i]) + ": ");
                for (int j = 0 ; j < this.phaseWeeks[i].length ; j++) {
                    if (j + 1 == this.phaseWeeks[i].length)
                        System.out.print(this.phaseWeeks[i][j] + ".");
                    else
                        System.out.print(this.phaseWeeks[i][j] + ",");
                }
                System.out.println();
            }
            System.out.println();

            for (Villa phaseVilla : this.phaseVillas) {
                phaseVilla.displayVillaDetails();
            }
        }
    }

    public static class Resort{

        private final String resortName;
        private final String resortLocation;
        private final Phase[] resortPhases;

        private Resort(ResortCode resortKeyword) {

            String[] namesOfPhases;
            Season[][] seasonsOfPhases;
            int[][][] weeksOfPhases;

            int[][] starOptionsBuilder;
            Villa[] villaBuilder;
            Phase[] phaseBuilder;

            this.resortName = ResortCode.displayResortText(resortKeyword);

            switch(resortKeyword) {
                case VISTANA_BEACH: {
                    this.resortLocation = "Jensen Beach, Florida";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{
                            "Main Phase"
                    };
                    seasonsOfPhases = new Season[][]{
                            {Season.PLATINUM, Season.GOLD}
                    };
                    weeksOfPhases = new int[][][]{{
                            {1, 17, 24, 35, 46, 47, 50, 52},{18, 23, 36, 45, 48, 49}
                    }};

                    villaBuilder = new Villa[1];
                    starOptionsBuilder = new int[][]{
                            {  8100,  12150,  16200},
                            {  6700,  10075,  13425}};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case WESTIN_STJOHN: {
                    this.resortLocation = "St. John, U.S. Virgin Islands";
                    phaseBuilder = new Phase[2];
                    namesOfPhases = new String[]{
                            "Virgin Grand",
                            "Bay Vista"
                    };
                    seasonsOfPhases = new Season[][]{
                            {Season.PLATINUM_PLUS, Season.PLATINUM, Season.GOLD},
                            {Season.PLATINUM_PLUS, Season.PLATINUM, Season.GOLD}
                    };
                    weeksOfPhases = new int[][][]{
                            {{1, 15, 51, 52},{16, 20, 43, 50},{21, 42}},
                            {{1, 18, 51, 52},{19, 33},{34, 50}}
                    };

                    villaBuilder = new Villa[4];
                    starOptionsBuilder = new int[][]{
                            { 25770,  38655,  51540},
                            { 19700,  29525,  39375},
                            { 12500,  18750,  25000}};
                    villaBuilder[0] = new Villa(RoomType.THREE_BD, "Three-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            { 17670,  26505,  35340},
                            { 14800,  22225,  29625},
                            {  9550,  14375,  19150}};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            {  9550,  14375,  19150},
                            {  8100,  12150,  16200},
                            {  5150,   7775,  10350}};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            {  8100,  12150,  16200},
                            {  6700,  10075,  13425},
                            {  4400,   6600,   8800}};
                    villaBuilder[3] = new Villa(RoomType.ONE_BD, "Studio Premium", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);

                    villaBuilder = new Villa[3];
                    starOptionsBuilder = new int[][]{
                            { 25770,  38655,  51540},
                            { 19700,  29525,  39375},
                            { 12500,  18750,  25000}};
                    villaBuilder[0] = new Villa(RoomType.THREE_BD, "Three-Bedroom", starOptionsBuilder, seasonsOfPhases[1]);

                    starOptionsBuilder = new int[][]{
                            { 17670,  26505,  35340},
                            { 14800,  22225,  29625},
                            {  9550,  14375,  19150}};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom Loft", starOptionsBuilder, seasonsOfPhases[1]);

                    starOptionsBuilder = new int[][]{
                            { 17670,  26505,  35340},
                            { 14800,  22225,  29625},
                            {  9550,  14375,  19150}};
                    villaBuilder[2] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[1]);
                    phaseBuilder[1] = new Phase(namesOfPhases[1], seasonsOfPhases[1], weeksOfPhases[1], villaBuilder);
                }
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + resortKeyword);
            }
            this.resortPhases = phaseBuilder;
        }

        private void displayResortDetails(){
            System.out.println("Resort: " + this.resortName + ", located in " + this.resortLocation);
            for (Phase resortPhase : this.resortPhases) {
                resortPhase.displayPhaseDetails();
            }
        }
    }

    public static class ReservationQuery {

        private final ResortCode chosenResort;
        private final LocalDate chosenCheckInDate;
        private final int chosenNumberOfNights;
        private final RoomType chosenTypeOfRoom;

        private ReservationQuery(ResortCode resortChosen, LocalDate checkInDateChosen, int numberOfNightsChosen, RoomType typeOfRoomChosen) {
            chosenResort = resortChosen;
            chosenCheckInDate = checkInDateChosen;
            chosenTypeOfRoom = typeOfRoomChosen;
            if (numberOfNightsChosen >= MIN_NIGHT_SEARCH && numberOfNightsChosen <= MAX_NIGHT_SEARCH)
                chosenNumberOfNights = numberOfNightsChosen;
            else
                throw new IllegalStateException("Unexpected value: " + numberOfNightsChosen);
        }

        private ResortCode getChosenResort() { return chosenResort; }

        private LocalDate getChosenCheckInDate() { return chosenCheckInDate; }

        private int getChosenNumberOfNights() { return chosenNumberOfNights; }

        private RoomType getChosenTypeOfRoom() { return chosenTypeOfRoom; }

        private ArrayList<SearchResult> getSearchResultsList() {

            ArrayList<SearchResult> searchResultsList = new ArrayList<>();
            Resort resortSearched = new Resort(chosenResort);
            int[] starOptionValueResultList = new int[this.chosenNumberOfNights];
            Season[] seasonValueResultList = new Season[this.chosenNumberOfNights];
            LocalDate dateSearched = getChosenCheckInDate();

            for (int i = 0 ; i < resortSearched.resortPhases.length ; i++) {
                for (int j = 0 ; j < resortSearched.resortPhases[i].phaseVillas.length ; j++) {
                    if (resortSearched.resortPhases[i].phaseVillas[j].getVillaRoomType() == getChosenTypeOfRoom()) {
                        SearchResult newResult = new SearchResult(this);
                        for (int k = 0 ; k < this.chosenNumberOfNights ; k++) {
                            seasonValueResultList[k] = resortSearched.resortPhases[i].getSeasonOfPhase(getWeekNumberOfDate(dateSearched));
                            starOptionValueResultList[k] = resortSearched.resortPhases[i].phaseVillas[j].getStarOptionValue(seasonValueResultList[k], dateSearched.getDayOfWeek());
                            dateSearched = dateSearched.plusDays(1);
                        }
                        newResult.setSelectedRoomDescription(resortSearched.resortPhases[i].phaseVillas[j]);
                        newResult.setSelectedPhase(resortSearched.resortPhases[i]);
                        newResult.setTotalSeasonValues(seasonValueResultList);
                        newResult.setTotalStarOptionValues(starOptionValueResultList);
                        searchResultsList.add(newResult);
                    }
                }
            }
            return searchResultsList;
        }
    }

    public static class SearchResult {

        ResortCode selectedResortName;
        RoomType selectedTypeOfRoom;
        String selectedRoomDescription;
        String selectedPhaseName;
        LocalDate selectedCheckInDate;
        int selectedNumberOfNights;
        int[] totalStarOptionValues;
        Season[] totalSeasonValues;

        private SearchResult(ReservationQuery query) {

            selectedResortName = query.getChosenResort();
            selectedCheckInDate = query.getChosenCheckInDate();
            selectedNumberOfNights = query.getChosenNumberOfNights();
            selectedTypeOfRoom = query.getChosenTypeOfRoom();
        }

        private void setTotalStarOptionValues(int[] starOptionValueChart) {
            this.totalStarOptionValues = starOptionValueChart;
        }

        private void setTotalSeasonValues(Season[] seasonValueChart) {
            this.totalSeasonValues = seasonValueChart;
        }

        private void setSelectedRoomDescription(Villa selectedVilla) {
            this.selectedRoomDescription = selectedVilla.getVillaDescription();
        }

        private void setSelectedPhase(Phase selectedPhase) {
            this.selectedPhaseName = selectedPhase.getPhaseName();
        }

        private int getTotalStarOptionValue() {
            int counter = 0;
            for (int totalStarOptionValue : this.totalStarOptionValues) {
                counter += totalStarOptionValue;
            }
            return counter;
        }

        private void displaySearchResult() {
            System.out.println("Result: " + ResortCode.displayResortText(selectedResortName) +
                    ", " + RoomType.displayVillaText(selectedTypeOfRoom) + ", " + selectedRoomDescription +
                    ", for " + selectedCheckInDate.toString() + ", " + selectedNumberOfNights + " nights at " +
                    selectedPhaseName);
            System.out.print("SO Values: ");
            for (int totalStarOptionValue : this.totalStarOptionValues) {
                System.out.print(totalStarOptionValue + ", ");
            }
            System.out.println("totaling: " + getTotalStarOptionValue() + " StarOptions.");
            System.out.print("SO Seasons: ");
            for (Season totalSeasonValue : this.totalSeasonValues) {
                System.out.print(Season.displaySeasonText(totalSeasonValue) + ", ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        ArrayList<SearchResult> searchResultsList;

        ReservationQuery newQuery = new ReservationQuery(ResortCode.WESTIN_STJOHN,
                LocalDate.of(2021, 1, 1),7, RoomType.THREE_BD);

        searchResultsList = newQuery.getSearchResultsList();
        for (SearchResult searchResult : searchResultsList) searchResult.displaySearchResult();
    }
}