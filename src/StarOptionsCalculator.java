import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

class StarOptionsCalculator {

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
        return LocalDate.of(chosenYear, 1, 1);
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
            //System.out.println("Week of: " + beginDate.toString() + " is week #" + weekCounter);
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

        private Season getSeasonOfPhase(int weekNumber) {
            Season result = null;
            if (weekNumber == 53)
                weekNumber = 52;

            for (int i = 0 ; i < this.phaseWeeks.length ; i++) {
                for (int j = 0 ; j < this.phaseWeeks[i].length ; j = j + 2) {
                    if (weekNumber >= this.phaseWeeks[i][j] && weekNumber <= this.phaseWeeks[i][j+1]) {
                        result = this.phaseSeasons[i];
                        return result;
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
            chosenNumberOfNights = numberOfNightsChosen;
            chosenTypeOfRoom = typeOfRoomChosen;
        }

        private ResortCode getChosenResort() { return chosenResort; }

        private LocalDate getChosenCheckInDate() { return chosenCheckInDate; }

        private int getChosenNumberOfNights() { return chosenNumberOfNights; }

        private RoomType getChosenTypeOfRoom() { return chosenTypeOfRoom; }
    }

    public static class SearchResult {

        ResortCode selectedResortName;
        RoomType selectedTypeOfRoom;
        LocalDate selectedCheckInDate;
        int selectedNumberOfNights;
        int[] totalStarOptionValues;
        Season[] totalSeasonValues;

        private SearchResult(ReservationQuery query) {

            selectedResortName = query.getChosenResort();
            selectedCheckInDate = query.getChosenCheckInDate();
            selectedNumberOfNights = query.getChosenNumberOfNights();
            selectedTypeOfRoom = query.getChosenTypeOfRoom();
            totalStarOptionValues = new int[query.getChosenNumberOfNights()];
        }
    }

    public static void main(String[] args) {
        ArrayList<SearchResult> searchResultsList = new ArrayList<SearchResult>();

        ReservationQuery newQuery = new ReservationQuery(ResortCode.VISTANA_BEACH,
                LocalDate.of(2022, 1, 7),7, RoomType.TWO_BD);

        Resort resortTest = new Resort(newQuery.chosenResort);
        resortTest.displayResortDetails();

        System.out.println("Year tested is " + newQuery.chosenCheckInDate.getYear());
        if (doesYearHas53Weeks(newQuery.chosenCheckInDate.getYear()))
            System.out.println("has 53 weeks.");
        else
            System.out.println("does not have 53 weeks.");

        System.out.println("Week number for " + newQuery.chosenCheckInDate.toString() + " is...");
        System.out.println(getWeekNumberOfDate(newQuery.chosenCheckInDate));

        Season result = resortTest.resortPhases[0].getSeasonOfPhase(getWeekNumberOfDate(newQuery.getChosenCheckInDate()));
        System.out.println("Season of " + newQuery.getChosenCheckInDate().toString() + " is " + result + ".");
    }
}

        /*Resort(String resortCode) {

            Values[] valuesBuilder;
            Room[] roomBuilder;
            Phase[] phaseBuilder;
            int[] calendarBuilder;

            switch (resortCode) {
                case "SDO": {
                    this.resortName = "Sheraton Desert Oasis";
                    this.resortCity = "Scottsdale";
                    this.resortState = "Arizona";
                    this.resortCountry = "US";

                    roomBuilder = new Room[3];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    valuesBuilder[2] = new Values(GOLD, 5600, 8475, 11275);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    valuesBuilder[2] = new Values(GOLD, 3050, 4575, 6100);
                    roomBuilder[1] = new Room(BD1, "One-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 3700, 5500, 7400);
                    valuesBuilder[2] = new Values(GOLD, 2550, 3900, 5175);
                    roomBuilder[2] = new Room(BD1, "One-Bedroom Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [SDO]          P+ ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                P+ G+ ->       <- G+ G  ->             <- G  G+ ->
                            PP, GP, GP, GP, GP, GP, GP, G, G, G, G, G, G, G, G, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                                     -> G+ P+ P+ P+
                            GP, GP, GP, GP, GP, GP, GP, GP, GP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "WKV": {
                    this.resortName = "Westin Kierland Villas";
                    this.resortCity = "Scottsdale";
                    this.resortState = "Arizona";
                    this.resortCountry = "US";

                    roomBuilder = new Room[3];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    valuesBuilder[2] = new Values(GOLD, 5600, 8475, 11275);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    valuesBuilder[2] = new Values(GOLD, 3050, 4575, 6100);
                    roomBuilder[1] = new Room(BD1, "One-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 3700, 5500, 7400);
                    valuesBuilder[2] = new Values(GOLD, 2550, 3900, 5175);
                    roomBuilder[2] = new Room(BD1, "One-Bedroom Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WKV]          P+ ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                P+ G+ ->       <- G+ G  ->             <- G  G+ ->
                            PP, GP, GP, GP, GP, GP, GP, G, G, G, G, G, G, G, G, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                                     -> G+ P+ P+ P+
                            GP, GP, GP, GP, GP, GP, GP, GP, GP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "WDW": {
                    this.resortName = "Westin Desert Willow";
                    this.resortCity = "Palm Desert";
                    this.resortState = "California";
                    this.resortCountry = "US";

                    roomBuilder = new Room[3];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    valuesBuilder[2] = new Values(GOLD, 5600, 8475, 11275);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    valuesBuilder[2] = new Values(GOLD, 3050, 4575, 6100);
                    roomBuilder[1] = new Room(BD1, "One-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 3700, 5500, 7400);
                    valuesBuilder[2] = new Values(GOLD, 2550, 3900, 5175);
                    roomBuilder[2] = new Room(BD1, "One-Bedroom Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WDW]          P+ ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                P+ G+ ->       <- G+ G  ->             <- G  G+ ->
                            PP, GP, GP, GP, GP, GP, GP, G, G, G, G, G, G, G, G, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                                     -> G+ P+ P+ P+
                            GP, GP, GP, GP, GP, GP, GP, GP, GP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "WMH": {
                    this.resortName = "Westin Mission Hills";
                    this.resortCity = "Rancho Mirage";
                    this.resortState = "California";
                    this.resortCountry = "US";

                    roomBuilder = new Room[3];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    valuesBuilder[2] = new Values(GOLD, 5600, 8475, 11275);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    valuesBuilder[2] = new Values(GOLD, 3050, 4575, 6100);
                    roomBuilder[1] = new Room(BD1, "One-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 3700, 5500, 7400);
                    valuesBuilder[2] = new Values(GOLD, 2550, 3900, 5175);
                    roomBuilder[2] = new Room(BD1, "One-Bedroom Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WMH]          P+ ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                P+ G+ ->       <- G+ G  ->             <- G  G+ ->
                            PP, GP, GP, GP, GP, GP, GP, G, G, G, G, G, G, G, G, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                                     -> G+ P+ P+ P+
                            GP, GP, GP, GP, GP, GP, GP, GP, GP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "SLT": {
                    this.resortName = "Sheraton Lakeside Terrace Villas";
                    this.resortCity = "Avon";
                    this.resortState = "Colorado";
                    this.resortCountry = "US";

                    roomBuilder = new Room[1];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 13000, 19450, 25950);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 6700, 10075, 13425);
                    valuesBuilder[2] = new Values(SILVER, 3700, 5550, 7400);
                    roomBuilder[0] = new Room(BD2, "2-Bedroom", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [SLT]          P+ ->                                  <- P+  S ->    <-  S
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, S, S, S, S, S,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                G+ ->                                              <- G+  S
                            GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, S,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                               <-  S P+ P+ P+ P+ P+
                            S, S, S, S, S, S, S, PP, PP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "SMV": {
                    this.resortName = "Sheraton Mountain Vista";
                    this.resortCity = "Avon";
                    this.resortState = "Colorado";
                    this.resortCountry = "US";

                    roomBuilder = new Room[3];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    valuesBuilder[2] = new Values(SILVER, 4600, 7000, 9350);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    valuesBuilder[2] = new Values(SILVER, 2550, 3900, 5175);
                    roomBuilder[1] = new Room(BD1, "One-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 3700, 5550, 7400);
                    valuesBuilder[2] = new Values(SILVER, 2050, 3100, 4175);
                    roomBuilder[2] = new Room(BD1, "One-Bedroom Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [SMV]          P+ ->                                  <- P+  S ->    <-  S
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, S, S, S, S, S,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                G+ ->                                              <- G+  S
                            GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, S,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                               <-  S P+ P+ P+ P+ P+
                            S, S, S, S, S, S, S, PP, PP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "WRM": {
                    this.resortName = "Westin Riverfront Mountain Villas";
                    this.resortCity = "Avon";
                    this.resortState = "Colorado";
                    this.resortCountry = "US";

                    roomBuilder = new Room[3];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    valuesBuilder[1] = new Values(PLATINUM, 9550, 14375, 19150);
                    valuesBuilder[2] = new Values(SILVER, 4600, 7000, 9350);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(PLATINUM, 5150, 7775, 10350);
                    valuesBuilder[2] = new Values(SILVER, 2550, 3900, 5175);
                    roomBuilder[1] = new Room(BD1, "One-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    valuesBuilder[1] = new Values(PLATINUM, 4400, 6600, 8800);
                    valuesBuilder[2] = new Values(SILVER, 2050, 3100, 4175);
                    roomBuilder[2] = new Room(BD1, "Studio Premium Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WRM]          P+ ->                                  <- P+  S ->    <-  S
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, S, S, S, S, S,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                 P ->                                              <-  P  S
                            P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, S,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                               <-  S P+ P+ P+ P+ P+
                            S, S, S, S, S, S, S, PP, PP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "SSR": {
                    this.resortName = "Sheraton Steamboat Resort Villas";
                    this.resortCity = "Steamboat Springs";
                    this.resortState = "Colorado";
                    this.resortCountry = "US";

                    roomBuilder = new Room[12];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 29600, 44450, 59250);
                    valuesBuilder[1] = new Values(PLATINUM, 19100, 28750, 38300);
                    valuesBuilder[2] = new Values(SILVER, 9200, 14000, 18700);
                    roomBuilder[0] = new Room(BD4, "Four-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 21500, 32300, 43050);
                    valuesBuilder[1] = new Values(PLATINUM, 13950, 20975, 27950);
                    valuesBuilder[2] = new Values(SILVER, 6650, 10100, 13525);
                    roomBuilder[1] = new Room(BD3, "Three-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 25770, 38655, 51540);
                    valuesBuilder[1] = new Values(PLATINUM, 19700, 29525, 39375);
                    valuesBuilder[2] = new Values(SILVER, 5760, 8660, 11550);
                    roomBuilder[2] = new Room(BD3, "Three-Bedroom Villa (Mountain Side)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 19700, 29525, 39375);
                    valuesBuilder[1] = new Values(PLATINUM, 12500, 18750, 25000);
                    valuesBuilder[2] = new Values(SILVER, 5760, 8660, 11550);
                    roomBuilder[3] = new Room(BD3, "Three-Bedroom Villa (Valley Side)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 17650, 26525, 35350);
                    valuesBuilder[1] = new Values(PLATINUM, 14800, 22225, 29625);
                    valuesBuilder[2] = new Values(SILVER, 4600, 7000, 9350);
                    roomBuilder[4] = new Room(BD2, "Two-Bedroom Lockoff Villa (Mountain Side)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    valuesBuilder[1] = new Values(PLATINUM, 9550, 14375, 19150);
                    valuesBuilder[2] = new Values(SILVER, 4600, 7000, 9350);
                    roomBuilder[5] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    valuesBuilder[1] = new Values(PLATINUM, 9550, 14375, 19150);
                    valuesBuilder[2] = new Values(SILVER, 4600, 7000, 9350);
                    roomBuilder[6] = new Room(BD2, "Two-Bedroom Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 9550, 14375, 19150);
                    valuesBuilder[1] = new Values(PLATINUM, 8100, 12150, 16200);
                    valuesBuilder[2] = new Values(SILVER, 2550, 3900, 5175);
                    roomBuilder[7] = new Room(BD1, "One-Bedroom Premium Villa (Mountain Side)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(PLATINUM, 5170, 7755, 10340);
                    valuesBuilder[2] = new Values(SILVER, 2550, 3900, 5175);
                    roomBuilder[8] = new Room(BD1, "One-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(PLATINUM, 6700, 10075, 13425);
                    valuesBuilder[2] = new Values(SILVER, 2050, 3100, 4175);
                    roomBuilder[9] = new Room(BD1, "Studio Villa (Mountain Side)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    valuesBuilder[1] = new Values(PLATINUM, 4400, 6600, 8800);
                    valuesBuilder[2] = new Values(SILVER, 2050, 3100, 4175);
                    roomBuilder[10] = new Room(BD1, "Studio Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 5000, 7500, 10000);
                    valuesBuilder[1] = new Values(PLATINUM, 3000, 4500, 6000);
                    valuesBuilder[2] = new Values(SILVER, 1500, 2250, 3000);
                    roomBuilder[11] = new Room(BD1, "Hotel Room", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [SSR]          P+ ->                                  <- P+  S ->    <-  S
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, S, S, S, S, S,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                 P ->                                              <-  P  S
                            P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, S,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                               <-  S P+ P+ P+ P+ P+
                            S, S, S, S, S, S, S, PP, PP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "SVR": {
                    this.resortName = "Sheraton Vistana Resort";
                    this.resortCity = "Orlando";
                    this.resortState = "Florida";
                    this.resortCountry = "US";

                    roomBuilder = new Room[4];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM, 9550, 14375, 19150);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    roomBuilder[0] = new Room(BD2, "2-Bedroom Lockoff", valuesBuilder);

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 6700, 10075, 13425);
                    roomBuilder[1] = new Room(BD2, "2-Bedroom", valuesBuilder);

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM, 5150, 7775, 10350);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    roomBuilder[2] = new Room(BD1, "1-Bedroom Premium", valuesBuilder);

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM, 4400, 6600, 8800);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 3700, 5550, 7400);
                    roomBuilder[3] = new Room(BD1, "1-Bedroom", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [SVR]          G+ ->    <- G+  P ->                         <-  P G+ ->
                            GP, GP, GP, GP, GP, P, P, P, P, P, P, P, P, P, P, P, P, GP, GP, GP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                <- G+  P ->                         <-  P G+ -> <- G+  P ->
                            GP, GP, P, P, P, P, P, P, P, P, P, P, P, P, GP, GP, GP, GP, P, P,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                               <-  P G+ G+ G+  P  P
                            P, P, P, P, P, P, P, GP, GP, GP, P, P, P
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "SVV": {
                    this.resortName = "Sheraton Vistana Villages";
                    this.resortCity = "Orlando";
                    this.resortState = "Florida";
                    this.resortCountry = "US";

                    roomBuilder = new Room[6];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM, 13950, 20975, 27950);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 11800, 17700, 23600);
                    roomBuilder[0] = new Room(BD3, "3-Bedroom Lockoff (1BD Premium + Two 1BD)", valuesBuilder);

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM, 12500, 18750, 25000);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 10400, 15625, 20825);
                    roomBuilder[1] = new Room(BD3, "3-Bedroom Lockoff (2BD + 1BD)", valuesBuilder);

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM, 9550, 14375, 19150);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    roomBuilder[2] = new Room(BD2, "2-Bedroom Lockoff", valuesBuilder);

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 6700, 10075, 13425);
                    roomBuilder[3] = new Room(BD2, "2-Bedroom", valuesBuilder);

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM, 5150, 7775, 10350);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    roomBuilder[4] = new Room(BD1, "1-Bedroom Premium", valuesBuilder);

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM, 4400, 6600, 8800);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 3700, 5550, 7400);
                    roomBuilder[5] = new Room(BD1, "1-Bedroom", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [SVV]          G+ ->    <- G+  P ->                         <-  P G+ ->
                            GP, GP, GP, GP, GP, P, P, P, P, P, P, P, P, P, P, P, P, GP, GP, GP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                <- G+  P ->                         <-  P G+ -> <- G+  P ->
                            GP, GP, P, P, P, P, P, P, P, P, P, P, P, P, GP, GP, GP, GP, P, P,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                               <-  P G+ G+ G+  P  P
                            P, P, P, P, P, P, P, GP, GP, GP, P, P, P
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "SPV": {
                    this.resortName = "Sheraton PGA Vacation Resort";
                    this.resortCity = "Port St. Lucie";
                    this.resortState = "Florida";
                    this.resortCountry = "US";

                    roomBuilder = new Room[4];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(GOLD, 5600, 8475, 11275);
                    valuesBuilder[2] = new Values(SILVER, 4600, 7000, 9350);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(GOLD_PLUS, 6700, 10075, 13425);
                    valuesBuilder[1] = new Values(GOLD, 4400, 6600, 8800);
                    valuesBuilder[2] = new Values(SILVER, 3700, 5550, 7400);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    valuesBuilder[1] = new Values(GOLD, 3050, 4575, 6100);
                    valuesBuilder[2] = new Values(SILVER, 2550, 3900, 5175);
                    roomBuilder[2] = new Room(BD1, "One-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(GOLD_PLUS, 3700, 5550, 7400);
                    valuesBuilder[1] = new Values(GOLD, 2550, 3900, 5175);
                    valuesBuilder[2] = new Values(SILVER, 2050, 3100, 4175);
                    roomBuilder[3] = new Room(BD1, "One-Bedroom Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [SPV]          G+ ->                                        <- G+  G ->
                            GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, G, G, G,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                                                    <-  G  S  S  S  S  S G+
                            G, G, G, G, G, G, G, G, G, G, G, G, G, G, S, S, S, S, S, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                               <- G+  S  S  S G+ G+
                            GP, GP, GP, GP, GP, GP, GP, S, S, S, GP, GP, GP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "VBC": {
                    this.resortName = "Vistana Beach Club";
                    this.resortCity = "Jensen Beach";
                    this.resortState = "Florida";
                    this.resortCountry = "US";

                    roomBuilder = new Room[1];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 6700, 10075, 13425);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [VBC]           P ->                                        <-  P G+ ->
                            P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, GP, GP, GP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                   <- G+  P ->                         <-  P G+ ->
                            GP, GP, GP, P, P, P, P, P, P, P, P, P, P, P, P, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                         <- G+  P  P G+ G+  P  P  P
                            GP, GP, GP, GP, GP, P, P, GP, GP, P, P, P, P
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "WPO": {
                    this.resortName = "Westin Princeville Ocean Resort Villas";
                    this.resortCity = "Princeville, Kaua'i";
                    this.resortState = "Hawai'i";
                    this.resortCountry = "US";

                    roomBuilder = new Room[3];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    roomBuilder[1] = new Room(BD1, "One-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    roomBuilder[2] = new Room(BD1, "Studio Premium Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WPO]          P+ ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                                              <- P+
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "SKR": {
                    this.resortName = "Sheraton Kauai Resort";
                    this.resortCity = "Koloa, Kaua'i";
                    this.resortState = "Hawai'i";
                    this.resortCountry = "US";

                    roomBuilder = new Room[4];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Villa", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    roomBuilder[2] = new Room(BD1, "One-Bedroom Villa", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    roomBuilder[3] = new Room(BD1, "Studio Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [SKR]          P+ ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                                              <- P+
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "WKO": {
                    this.resortName = "Westin Ka'anapali Ocean Resort Villas";
                    this.resortCity = "Lahaina, Maui";
                    this.resortState = "Hawai'i";
                    this.resortCountry = "US";

                    roomBuilder = new Room[6];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 17650, 26525, 35350);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Lockoff Villa (Oceanfront)", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 9550, 14375, 19150);
                    roomBuilder[2] = new Room(BD1, "One-Bedroom Premium Villa (Oceanfront)", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    roomBuilder[3] = new Room(BD1, "One-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    roomBuilder[4] = new Room(BD1, "Studio Premium Villa (Oceanfront)", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    roomBuilder[5] = new Room(BD1, "Studio Premium Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WKO]          P+ ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                                              <- P+
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "WKN": {
                    this.resortName = "Westin Ka'anapali Ocean Resort Villas North";
                    this.resortCity = "Lahaina, Maui";
                    this.resortState = "Hawai'i";
                    this.resortCountry = "US";

                    roomBuilder = new Room[6];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 17650, 26525, 35350);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Lockoff Villa(Oceanfront)", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 9550, 14375, 19150);
                    roomBuilder[2] = new Room(BD1, "One-Bedroom Premium Villa (Oceanfront)", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    roomBuilder[3] = new Room(BD1, "One-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    roomBuilder[4] = new Room(BD1, "Studio Premium Villa (Oceanfront)", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    roomBuilder[5] = new Room(BD1, "Studio Premium Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WKN]          P+ ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                                              <- P+
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "WNO": {
                    this.resortName = "Westin Nanea Ocean Resort Villas";
                    this.resortCity = "Lahaina, Maui";
                    this.resortState = "Hawai'i";
                    this.resortCountry = "US";

                    roomBuilder = new Room[4];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 25770, 38655, 51540);
                    roomBuilder[0] = new Room(BD3, "Three-Bedroom Villa (Oceanfront)", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 17650, 26525, 35350);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Villa (Oceanfront)", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    roomBuilder[2] = new Room(BD2, "Two-Bedroom Villa (Resort View)", valuesBuilder);

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    roomBuilder[3] = new Room(BD1, "One-Bedroom Premium Villa (Resort View)", valuesBuilder);


                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WNO]          P+ ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                                              <- P+
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "SBP": {
                    this.resortName = "Sheraton Broadway Plantation";
                    this.resortCity = "Myrtle Beach";
                    this.resortState = "South Carolina";
                    this.resortCountry = "US";

                    roomBuilder = new Room[4];
                    phaseBuilder = new Phase[2];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(GOLD, 5600, 8475, 11275);
                    valuesBuilder[2] = new Values(SILVER, 4600, 7000, 9350);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Lockoff Villa (Plantation Phase)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(GOLD_PLUS, 6700, 10075, 13425);
                    valuesBuilder[1] = new Values(GOLD, 4400, 6600, 8800);
                    valuesBuilder[2] = new Values(SILVER, 3700, 5500, 7400);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Villa (Plantation Phase)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    valuesBuilder[1] = new Values(GOLD, 3050, 4575, 6100);
                    valuesBuilder[2] = new Values(SILVER, 2550, 3900, 5175);
                    roomBuilder[2] = new Room(BD1, "One-Bedroom Premium Villa (Plantation Phase)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(GOLD_PLUS, 3700, 5550, 7400);
                    valuesBuilder[1] = new Values(GOLD, 2550, 3900, 5175);
                    valuesBuilder[2] = new Values(SILVER, 2050, 3100, 4175);
                    roomBuilder[3] = new Room(BD1, "One-Bedroom Villa (Plantation Phase)", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [SBP-P]         G  S ->    <-  S  G  G G+ ->
                            G, S, S, S, S, S, G, G, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //
                            GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                   <- G+  G  G  G G+  G  S  S  G  G
                            GP, GP, GP, G, G, G, GP, G, S, S, G, G, G
                    };
                    phaseBuilder[0] = new Phase("Plantation Phase", calendarBuilder, roomBuilder);

                    roomBuilder = new Room[5];

                    valuesBuilder = new Values[4];
                    valuesBuilder[0] = new Values(PLATINUM, 12500, 18750, 25000);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 10400, 15625, 20825);
                    valuesBuilder[2] = new Values(GOLD, 6980, 10470, 13960);
                    valuesBuilder[3] = new Values(SILVER, 5760, 8660, 11550);
                    roomBuilder[0] = new Room(BD3, "Three-Bedroom Lockoff Villa (Palmetto Phase)", valuesBuilder);

                    valuesBuilder = new Values[4];
                    valuesBuilder[0] = new Values(PLATINUM, 9550, 14375, 19150);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    valuesBuilder[2] = new Values(GOLD, 5600, 8475, 11275);
                    valuesBuilder[3] = new Values(SILVER, 4600, 7000, 9350);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Lockoff Villa (Palmetto Phase)", valuesBuilder);

                    valuesBuilder = new Values[4];
                    valuesBuilder[0] = new Values(PLATINUM, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 6700, 10075, 13425);
                    valuesBuilder[2] = new Values(GOLD, 4400, 6600, 8800);
                    valuesBuilder[3] = new Values(SILVER, 3700, 5550, 7400);
                    roomBuilder[2] = new Room(BD2, "Two-Bedroom Villa (Palmetto Phase)", valuesBuilder);

                    valuesBuilder = new Values[4];
                    valuesBuilder[0] = new Values(PLATINUM, 5150, 7775, 10350);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    valuesBuilder[2] = new Values(GOLD, 3050, 4575, 6100);
                    valuesBuilder[3] = new Values(SILVER, 2550, 3900, 5175);
                    roomBuilder[3] = new Room(BD1, "One-Bedroom Premium Villa (Palmetto Phase)", valuesBuilder);

                    valuesBuilder = new Values[4];
                    valuesBuilder[0] = new Values(PLATINUM, 4400, 6600, 8800);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 3700, 5550, 7400);
                    valuesBuilder[2] = new Values(GOLD, 2550, 3900, 5175);
                    valuesBuilder[3] = new Values(SILVER, 2050, 3100, 4175);
                    roomBuilder[4] = new Room(BD1, "One-Bedroom Villa (Palmetto Phase)", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [SBP-P2]        S ->                <-  S  G ->    <-  G G+ ->
                            S, S, S, S, S, S, S, S, S, G, G, G, G, G, GP, GP, GP, GP, GP, GP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                   <- G+  P ->                         <-  P G+ ->
                            GP, GP, GP, P, P, P, P, P, P, P, P, P, P, P, P, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                <- G+  G ->                   <-  G
                            GP, GP, G, G, G, G, G, G, G, G, G, G, G
                    };
                    phaseBuilder[1] = new Phase("Palmetto Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "WLO": {
                    this.resortName = "Westin Lagunamar Ocean Resort";
                    this.resortCity = "Cancun";
                    this.resortState = "Quintana Roo";
                    this.resortCountry = "Mexico";

                    roomBuilder = new Room[3];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    roomBuilder[0] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    roomBuilder[1] = new Room(BD1, "One-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 3700, 5550, 7400);
                    roomBuilder[2] = new Room(BD1, "Studio Premium Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WLO]          G+ -> <- G+ P+ ->                            <- P+ G+ ->
                            GP, GP, GP, GP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, GP, GP, GP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                   <- G+ P+ ->                         <- P+ G+ ->
                            GP, GP, GP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                   <- G+ P+ -> <- P+ G+ G+ G+ P+ P+
                            GP, GP, GP, PP, PP, PP, PP, GP, GP, GP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "WRC": {
                    this.resortName = "Westin Resort & Spa Cancun";
                    this.resortCity = "Cancun";
                    this.resortState = "Quintana Roo";
                    this.resortCountry = "Mexico";

                    roomBuilder = new Room[3];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 21500, 32300, 43050);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 11800, 17700, 23600);
                    roomBuilder[0] = new Room(BD3, "Three-Bedroom Lockoff Villa (Oceanside or Lagoon Side)", valuesBuilder);

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Villa (Oceanside or Lagoon Side)", valuesBuilder);

                    valuesBuilder = new Values[2];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    valuesBuilder[1] = new Values(GOLD_PLUS, 3700, 5550, 7400);
                    roomBuilder[2] = new Room(BD1, "Studio Villa (Oceanside or Lagoon Side)", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WRC]          G+ -> <- G+ P+ ->                            <- P+ G+ ->
                            GP, GP, GP, GP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, GP, GP, GP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                   <- G+ P+ ->                         <- P+ G+ ->
                            GP, GP, GP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                   <- G+ P+ -> <- P+ G+ G+ G+ P+ P+
                            GP, GP, GP, PP, PP, PP, PP, GP, GP, GP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "WLC": {
                    this.resortName = "Westin Los Cabos Resort";
                    this.resortCity = "Los Cabos";
                    this.resortState = "Baja California Sur";
                    this.resortCountry = "Mexico";

                    roomBuilder = new Room[6];
                    phaseBuilder = new Phase[2];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 31060, 46665, 62195);
                    valuesBuilder[1] = new Values(PLATINUM, 24360, 36590, 48770);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 16970, 25455, 33940);
                    roomBuilder[0] = new Room(BD2, "Governor's Suite Lockoff", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 24360, 36590, 48770);
                    valuesBuilder[1] = new Values(PLATINUM, 19210, 28815, 38420);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 13270, 19905, 26540);
                    roomBuilder[1] = new Room(BD2, "Governor's Suite", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 21500, 32300, 43050);
                    valuesBuilder[1] = new Values(PLATINUM, 17650, 26525, 35350);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 11800, 17700, 23600);
                    roomBuilder[2] = new Room(BD3, "Three-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 17670, 26505, 35340);
                    valuesBuilder[1] = new Values(PLATINUM, 14800, 22225, 29625);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 9550, 14375, 19150);
                    roomBuilder[3] = new Room(BD2, "Two-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    valuesBuilder[1] = new Values(PLATINUM, 12500, 18750, 25000);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    roomBuilder[4] = new Room(BD2, "Two-Bedroom Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    valuesBuilder[1] = new Values(PLATINUM, 5150, 7775, 10350);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 3700, 5550, 7400);
                    roomBuilder[5] = new Room(BD1, "Studio Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WLC-M]        P+ ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                   <- P+  P ->                   <-  P G+ ->          <- G+
                            PP, PP, PP, P, P, P, P, P, P, P, P, P, P, GP, GP, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                 P  P  P P+ ->                <- P+
                            P, P, P, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);

                    roomBuilder = new Room[4];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 25750, 38675, 51550);
                    valuesBuilder[1] = new Values(PLATINUM, 21500, 32300, 43050);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 13950, 20975, 27950);
                    roomBuilder[0] = new Room(BD3, "Three-Bedroom Lockoff Villa (Baja Point)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 17650, 26525, 35350);
                    valuesBuilder[1] = new Values(PLATINUM, 14800, 22225, 29625);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 9550, 14375, 19150);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Lockoff Villa (Baja Point)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 9550, 14375, 19150);
                    valuesBuilder[1] = new Values(PLATINUM, 8100, 12150, 16200);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 5150, 7775, 10350);
                    roomBuilder[2] = new Room(BD1, "One-Bedroom Premium Villa (Baja Point)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(PLATINUM, 6700, 10075, 13425);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    roomBuilder[3] = new Room(BD1, "Hotel Room (Baja Point)", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WLC-BP]       P+ ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                   <- P+  P ->                   <-  P G+ ->          <- G+
                            PP, PP, PP, P, P, P, P, P, P, P, P, P, P, GP, GP, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                 P  P  P P+ ->                <- P+
                            P, P, P, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP
                    };
                    phaseBuilder[1] = new Phase("Baja Point Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "HRA": {
                    this.resortName = "Harborside Resort At Atlantis";
                    this.resortCity = "Paradise Island";
                    this.resortState = "New Providence";
                    this.resortCountry = "The Bahamas";

                    roomBuilder = new Room[5];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 19700, 29525, 39375);
                    valuesBuilder[1] = new Values(PLATINUM, 12500, 18750, 25000);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 10400, 15625, 20825);
                    roomBuilder[0] = new Room(BD3, "Three-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 14800, 22225, 29625);
                    valuesBuilder[1] = new Values(PLATINUM, 9550, 14375, 19150);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 8100, 12150, 16200);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Lockoff Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 13000, 19450, 25950);
                    valuesBuilder[1] = new Values(PLATINUM, 8100, 12150, 16200);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 6700, 10075, 13425);
                    roomBuilder[2] = new Room(BD2, "Two-Bedroom Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(PLATINUM, 5150, 7775, 10350);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    roomBuilder[3] = new Room(BD1, "One-Bedroom Premium Villa", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 6700, 10075, 13425);
                    valuesBuilder[1] = new Values(PLATINUM, 4400, 6600, 8800);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 3700, 5550, 7400);
                    roomBuilder[4] = new Room(BD1, "One-Bedroom Villa", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [HRA]          P+ ->                                        <- P+  P ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, P, P, P,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                                                    <-  P G+ ->
                            P, P, P, P, P, P, P, P, P, P, P, P, P, P, GP, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                            <- G+  P  P  P P+ P+ P+
                            GP, GP, GP, GP, GP, GP, P, P, P, PP, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Main Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
                case "WSJ": {
                    this.resortName = "Westin St. John Resort Villas";
                    this.resortCity = "St. John";
                    this.resortState = "U.S. Virgin Islands";
                    this.resortCountry = "US";

                    roomBuilder = new Room[4];
                    phaseBuilder = new Phase[4];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 25770, 38655, 51540);
                    valuesBuilder[1] = new Values(PLATINUM, 19700, 29525, 39375);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 12500, 18750, 25000);
                    roomBuilder[0] = new Room(BD3, "Three-Bedroom Pool Villa (Virgin Grand)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 17670, 26505, 35340);
                    valuesBuilder[1] = new Values(PLATINUM, 14800, 22225, 29625);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 9550, 14375, 19150);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Townhouse (Virgin Grand)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 9550, 14375, 19150);
                    valuesBuilder[1] = new Values(PLATINUM, 8100, 12150, 16200);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 5150, 7775, 10350);
                    roomBuilder[2] = new Room(BD1, "One-Bedroom Townhouse (Virgin Grand)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(PLATINUM, 6700, 10075, 13425);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    roomBuilder[3] = new Room(BD1, "Studio Terrace (Virgin Grand)", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WSJ-VG]       P+ ->                                  <- P+  P ->    <-  P
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, P, P, P, P, P,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                G+ ->
                            GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                <- G+  P ->             <-  P P+ P+
                            GP, GP, P, P, P, P, P, P, P, P, PP, PP, PP
                    };
                    phaseBuilder[0] = new Phase("Virgin Grand", calendarBuilder, roomBuilder);

                    roomBuilder = new Room[3];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 25770, 38655, 51540);
                    valuesBuilder[1] = new Values(PLATINUM, 19700, 29525, 39375);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 12500, 18750, 25000);
                    roomBuilder[0] = new Room(BD3, "Three-Bedroom Villa (Bay Vista)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 17670, 26505, 35340);
                    valuesBuilder[1] = new Values(PLATINUM, 14800, 22225, 29625);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 9550, 14375, 19150);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Loft (Bay Vista)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 17670, 26505, 35340);
                    valuesBuilder[1] = new Values(PLATINUM, 14800, 22225, 29625);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 9550, 14375, 19150);
                    roomBuilder[2] = new Room(BD2, "Two-Bedroom Villa (Bay Vista)", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WSJ-BV]       P+ ->                                           <- P+  P ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, P, P,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                                                 <-  P G+ ->
                            P, P, P, P, P, P, P, P, P, P, P, P, P, GP, GP, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                                        <- G+ P+ P+
                            GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, PP, PP, PP
                    };
                    phaseBuilder[1] = new Phase("Bay Vista", calendarBuilder, roomBuilder);

                    roomBuilder = new Room[4];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 25770, 38655, 51540);
                    valuesBuilder[1] = new Values(PLATINUM, 19700, 29525, 39375);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 12500, 18750, 25000);
                    roomBuilder[0] = new Room(BD3, "Three-Bedroom Lockoff Villa (Coral Vista)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 17670, 26505, 35340);
                    valuesBuilder[1] = new Values(PLATINUM, 14800, 22225, 29625);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 9550, 14375, 19150);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Loft (Coral Vista)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 17670, 26505, 35340);
                    valuesBuilder[1] = new Values(PLATINUM, 14800, 22225, 29625);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 9550, 14375, 19150);
                    roomBuilder[2] = new Room(BD2, "Two-Bedroom Villa (Coral Vista)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(PLATINUM, 6700, 10075, 13425);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    roomBuilder[3] = new Room(BD1, "Studio (Coral Vista)", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WSJ-CV]       P+ ->                                           <- P+  P ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, P, P,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                                                 <-  P G+ ->
                            P, P, P, P, P, P, P, P, P, P, P, P, P, GP, GP, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                                        <- G+ P+ P+
                            GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, PP, PP, PP
                    };
                    phaseBuilder[2] = new Phase("Coral Vista", calendarBuilder, roomBuilder);


                    roomBuilder = new Room[3];

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 25770, 38655, 51540);
                    valuesBuilder[1] = new Values(PLATINUM, 19700, 29525, 39375);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 12500, 18750, 25000);
                    roomBuilder[0] = new Room(BD3, "Three-Bedroom Lockoff Villa (Sunset Bay)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 17670, 26505, 35340);
                    valuesBuilder[1] = new Values(PLATINUM, 14800, 22225, 29625);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 9550, 14375, 19150);
                    roomBuilder[1] = new Room(BD2, "Two-Bedroom Premium Villa (Sunset Bay)", valuesBuilder);

                    valuesBuilder = new Values[3];
                    valuesBuilder[0] = new Values(PLATINUM_PLUS, 8100, 12150, 16200);
                    valuesBuilder[1] = new Values(PLATINUM, 6700, 10075, 13425);
                    valuesBuilder[2] = new Values(GOLD_PLUS, 4400, 6600, 8800);
                    roomBuilder[2] = new Room(BD1, "Studio Villa (Sunset Bay)", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [WSJ-SB]       P+ ->                                           <- P+  P ->
                            PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, PP, P, P,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                                                 <-  P G+ ->
                            P, P, P, P, P, P, P, P, P, P, P, P, P, GP, GP, GP, GP, GP, GP, GP,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                                        <- G+ P+ P+
                            GP, GP, GP, GP, GP, GP, GP, GP, GP, GP, PP, PP, PP
                    };
                    phaseBuilder[3] = new Phase("Sunset Bay", calendarBuilder, roomBuilder);

                    this.resortPhases = phaseBuilder;
                    break;
                }
                default: {
                    this.resortName = "Ghost Resort";
                    this.resortCity = "Ghost Town";
                    this.resortState = "Middleof";
                    this.resortCountry = "Nowhere";

                    roomBuilder = new Room[1];
                    phaseBuilder = new Phase[1];

                    valuesBuilder = new Values[1];
                    valuesBuilder[0] = new Values(SILVER, 1, 1, 1);
                    roomBuilder[0] = new Room(BD1, "Ghost Room", valuesBuilder);

                    calendarBuilder = new int[]{
                            // Week Calendar: 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20
                            // [SLT]          P+ ->                                  <- P+  S ->    <-  S
                            S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S,
                            //                21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                            //                G+ ->                                              <- G+  S
                            S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S, S,
                            //                41 42 43 44 45 46 47 48 49 50 51 52
                            //                               <-  S P+ P+ P+ P+ P+
                            S, S, S, S, S, S, S, S, S, S, S, S, S
                    };
                    phaseBuilder[0] = new Phase("Ghost Phase", calendarBuilder, roomBuilder);
                    this.resortPhases = phaseBuilder;
                    break;
                }
            }
        }*/


        /*
        String getResortName() {
            return resortName;
        }

        String getResortLocation() {
            return resortLocation;
        }

        int getAmountOfPhasesInResort() { return resortPhases.length; }

        /*void displayResortInfo() {
            System.out.println("Resort Information >> Name: " + this.getResortName() + " located in " +
                    this.getResortCity() + ", " + this.getResortState() + ", " + this.getResortCountry());
            System.out.println("Resort has a total of " + this.getPhaseLength() + " phase(s).");
            for (int p = 0; p < this.getPhaseLength(); p++) {
                System.out.println("Name of phase #" + (p + 1) + " is: " + this.resortPhases[p].getPhaseName()
                        + " and it has a total of " + this.resortPhases[p].getRoomLength() + " types of room(s)");
                System.out.println("Phase: " + this.resortPhases[p].getPhaseName() + " has " +
                        this.resortPhases[p].calendar.length + " total weeks.");
                System.out.println("Calendar of weeks and seasons for " + this.resortPhases[p].getPhaseName() + ":");
                for (int c = 0; c < this.resortPhases[p].calendar.length; c++) {
                    System.out.print("Week #" + (c + 1) + " is a "
                            + this.resortPhases[p].getCalendarSeasonText(c + 1) + " season. \n");
                }
                for (int r = 0; r < this.resortPhases[p].getRoomLength(); r++) {
                    System.out.println("\n" + (r + 1) + ".) " + this.resortPhases[p].room[r].getRoomTypeText() +
                            ": " + this.resortPhases[p].room[r].getRoomDesc());
                    for (int v = 0; v < this.resortPhases[p].room[r].getValuesLength(); v++) {
                        System.out.println("Season is: " +
                                this.resortPhases[p].room[r].starOptions[v].getSeasonText() + " and StarOptions " +
                                "values are: Monday: " +
                                this.resortPhases[p].room[r].starOptions[v].getValueOf(DayOfWeek.MONDAY) + ", Tuesday: " +
                                this.resortPhases[p].room[r].starOptions[v].getValueOf(DayOfWeek.TUESDAY) + ", Wednesday: " +
                                this.resortPhases[p].room[r].starOptions[v].getValueOf(DayOfWeek.WEDNESDAY) + ", Thursday: " +
                                this.resortPhases[p].room[r].starOptions[v].getValueOf(DayOfWeek.THURSDAY) + ", Friday: " +
                                this.resortPhases[p].room[r].starOptions[v].getValueOf(DayOfWeek.FRIDAY) + ", Saturday: " +
                                this.resortPhases[p].room[r].starOptions[v].getValueOf(DayOfWeek.SATURDAY) + ", Sunday: " +
                                this.resortPhases[p].room[r].starOptions[v].getValueOf(DayOfWeek.SUNDAY));
                    }
                }
            }
        }*/
    /*}





    protected class searchQuery {
        String chosenResort;
        LocalDate chosenCheckInDate;
        int chosenNumberOfNights;
        RoomType chosenTypeOfRoom;

        searchQuery(String resort, LocalDate checkInDate, int numberOfNights, RoomType typeOfRoom) {
            chosenResort = resort;
            chosenCheckInDate = checkInDate;
            chosenNumberOfNights = numberOfNights;
            chosenTypeOfRoom = typeOfRoom;
        }

        private String getChosenResort() { return chosenResort; }

        private LocalDate getChosenCheckInDate() { return chosenCheckInDate; }

        private int getChosenNumberOfNights() { return chosenNumberOfNights; }

        private RoomType getChosenTypeOfRoom() { return chosenTypeOfRoom; }
    }


    protected class searchResult {
        String selectedResortName;
        RoomType selectedTypeOfRoom;
        LocalDate selectedCheckInDate;
        int selectedNumberOfNights;
        int[] totalStarOptionValues;
        Season[] totalSeasonValues;

        searchResult(searchQuery query) {
            selectedResortName = query.getChosenResort();
            selectedCheckInDate = query.getChosenCheckInDate();
            selectedNumberOfNights = query.getChosenNumberOfNights();
            totalStarOptionValues = new int[query.getChosenNumberOfNights()];
            }

            int totalStarOptionsValue() {
                int total = 0;
                for (int totalStarOptionValue : totalStarOptionValues) total += totalStarOptionValue;
                return total;
            }

            /*void displayResult() {
                System.out.println(this.resortName + " at " + this.location + " from " + this.checkInDate.toString() + " to " +
                        this.checkInDate.plusDays(this.numberOfNights) + " for a total of " + this.numberOfNights +
                        " nights on a " + typeOfRoom + " would cost " + this.totalStarOptions() + " StarOptions.");
            }*/
       /*  }

        // End of Classes ---------------------------------------------------------------------------------------------




        public static void main(String[] args) {
            ArrayList<searchResult> searchResultsList = new ArrayList<searchResult>();
            Calculator newCalc = new Calculator();
        }

        static int getWeekNumber(LocalDate targetDate) {
            int weekCounter = 0;
            LocalDate beginDate;

            beginDate = LocalDate.of(targetDate.getYear(), Month.JANUARY, 1);

            while (beginDate.getDayOfWeek() != DayOfWeek.FRIDAY) {
                beginDate = beginDate.plusDays(1);
            }

            if (targetDate.isBefore(beginDate)) {
                beginDate = LocalDate.of((targetDate.getYear() - 1), Month.JANUARY, 1);
            }

            while (beginDate.isBefore(targetDate) || beginDate.isEqual(targetDate)) {
                beginDate = beginDate.plusWeeks(1);
                weekCounter++;
            }
            if (weekCounter == 53) weekCounter--;
            return weekCounter;
        }

        static void setStarOptionValues(searchQuery query, Room room, int[] calendar, searchResult result) {

            if (query.typeOfRoom == room.roomType) {

                result.typeOfRoom = room.roomDesc;
                LocalDate checkInDate = query.checkInDate;
                int weekNumber = getWeekNumber(query.checkInDate);

                for (int i = 0; i < query.numberOfNights ; i++) {
                    for (int j = 0; j < room.starOptions.length; j++) {
                        if (calendar[weekNumber - 1] == room.starOptions[j].getSeason()) {
                            result.starOptionValues[i] = room.starOptions[j].getValueOf(checkInDate.getDayOfWeek());
                        }
                    }

                    checkInDate = checkInDate.plusDays(1);

                    if (checkInDate.getDayOfWeek() == DayOfWeek.FRIDAY) {
                        weekNumber++;
                        if (weekNumber > 53) {
                            weekNumber = 1;
                        }
                    }
                }
            }
        }

        static void extractResults(searchQuery query, ArrayList<searchResult> searchResults, Resort selectResort) {
            for (int p = 0; p < selectResort.getPhaseLength(); p++) {
                for (int r = 0; r < selectResort.resortPhases[p].getRoomLength(); r++) {
                    if (query.typeOfRoom == selectResort.resortPhases[p].room[r].roomType) {
                        searchResult newResult = new searchResult(query);
                        searchResult.location = selectResort.getResortCity() + ", " + selectResort.getResortState() + ", " + selectResort.getResortCountry();
                        searchResult.resortName = selectResort.getResortName();
                        setStarOptionValues(query, selectResort.resortPhases[p].room[r], selectResort.resortPhases[p].calendar, newResult);
                        searchResults.add(newResult);
                    }
                }
            }
        }

        static void displayAllSearchResults(ArrayList<searchResult> searchResultArrayList) {
            for (int num = 0 ; num < searchResultArrayList.size() ; num++) {
                searchResultArrayList.get(num).displayResult();
            }
        }

        // END OF MAIN FUNCTIONS/METHODS ------------------------------------------------------------------------------
        // START OF CALCULATOR GUI ------------------------------------------------------------------------------------

        public static class Calculator extends JFrame implements ActionListener, ItemListener {
            ArrayList<searchResult> searchResultsList = new ArrayList<searchResult>();

            JComboBox selectResort = new JComboBox();
            LocalDate selectDate = LocalDate.of(2020, Month.JANUARY, 21);
            JComboBox selectNumberOfNights = new JComboBox();
            JComboBox selectTypeOfRoom = new JComboBox();
            JButton beginSearch = new JButton("Search");

            String resortCode;
            int numberOfNights;
            int typeOfRoom;

            JLabel[] resultRoomDescription;
            JLabel[] resultAmount;
            JButton[] resultDetails;


            JPanel mainSearchPanel = new JPanel();
            JPanel querySearchPanel = new JPanel();
            JPanel queryResultPanel = new JPanel();

            String[] resortNames = new String[23];
            String[] resortCodes = new String [23];
            String defaultResortSelection = "Select Resort:";

            public Calculator() {
                super("StarOptions Calculator");
                setSize(900,600);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Load Components for querySearchPanel inside mainSearchPanel
                JLabel queryPrompt = new JLabel("Find Your Vacation:" , SwingConstants.LEFT);

                resortNames[0] = "Sheraton Desert Oasis";
                resortCodes[0] = "SDO";
                resortNames[1] = "Westin Kierland Villas";
                resortCodes[1] = "WKV";
                resortNames[2] = "Westin Desert Willow";
                resortCodes[2] = "WDW";
                resortNames[3] = "Westin Mission Hills";
                resortCodes[3] = "WMH";
                resortNames[4] = "Sheraton Lakeside Terrace";
                resortCodes[4] = "SLT";
                resortNames[5] = "Sheraton Mountain Vista";
                resortCodes[5] = "SMV";
                resortNames[6] = "Westin Riverfront";
                resortCodes[6] = "WRM";
                resortNames[7] = "Sheraton SteamBoat Resort";
                resortCodes[7] = "SSR";
                resortNames[8] = "Sheraton Vistana Resort";
                resortCodes[8] = "SVR";
                resortNames[9] = "Sheraton Vistana Villages";
                resortCodes[9] = "SVV";
                resortNames[10] = "Sheraton PGA";
                resortCodes[10] = "SPV";
                resortNames[11] = "Vistana Beach Club";
                resortCodes[11] = "VBC";
                resortNames[12] = "Westin Princeville";
                resortCodes[12] = "WPO";
                resortNames[13] = "Sheraton Kauai";
                resortCodes[13] = "SKR";
                resortNames[14] = "Westin Ka'anapali";
                resortCodes[14] = "WKO";
                resortNames[15] = "Westin Ka'anapali North";
                resortCodes[15] = "WKN";
                resortNames[16] = "Westin Nanea";
                resortCodes[16] = "WNV";
                resortNames[17] = "Sheraton Broadway Plantation";
                resortCodes[17] = "SBP";
                resortNames[18] = "Westin Lagunamar";
                resortCodes[18] = "WLO";
                resortNames[19] = "Westin Resort & Spa Cancun";
                resortCodes[19] = "WRC";
                resortNames[20] = "Westin Los Cabos";
                resortCodes[20] = "WLC";
                resortNames[21] = "Harborside At Atlantis";
                resortCodes[21] = "HRA";
                resortNames[22] = "Westin St. John";
                resortCodes[22] = "WSJ";

                selectResort.addItem(defaultResortSelection);

                for (int r = 0 ; r < resortNames.length ; r++) {
                    selectResort.addItem(resortNames[r]);
                }

                for (int d = 1 ; d < 22 ; d++) {
                    selectNumberOfNights.addItem("" + d);
                }

                selectTypeOfRoom.addItem("One Bedroom");
                selectTypeOfRoom.addItem("Two Bedroom");
                selectTypeOfRoom.addItem("Three Bedroom");
                selectTypeOfRoom.addItem("Four Bedroom");

                selectResort.addItemListener(this);
                selectNumberOfNights.addItemListener(this);
                selectTypeOfRoom.addItemListener(this);
                beginSearch.addActionListener(this);

                String tempString = selectDate.toString();
                JLabel tempDate = new JLabel(tempString);

                querySearchPanel.add(queryPrompt);
                querySearchPanel.add(selectResort);
                querySearchPanel.add(tempDate);
                querySearchPanel.add(selectNumberOfNights);
                querySearchPanel.add(selectTypeOfRoom);
                querySearchPanel.add(beginSearch);

                mainSearchPanel.add(querySearchPanel);
                mainSearchPanel.add(queryResultPanel);
                setContentPane(mainSearchPanel);
                setVisible(true);
            }

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Object action = actionEvent.getSource();
                if (action == beginSearch && selectResort.getSelectedItem().toString() != defaultResortSelection) {
                    //get query and begin search function
                    searchResultsList.clear();
                    queryResultPanel.removeAll();

                    searchQuery currentQuery = new searchQuery(resortCode, selectDate, numberOfNights, typeOfRoom);
                    Resort resortObject = new Resort(resortCode);
                    extractResults(currentQuery, searchResultsList, resortObject);
                    displayAllSearchResults(searchResultsList);
                    System.out.println("SearchResultsList size is" + searchResultsList.size());

                    GridLayout queryResultGrid = new GridLayout(searchResultsList.size(),1);
                    queryResultPanel.setLayout(queryResultGrid);

                    resultRoomDescription = new JLabel[searchResultsList.size()];
                    resultAmount = new JLabel[searchResultsList.size()];
                    resultDetails = new JButton[searchResultsList.size()];

                    for (int i = 0 ; i < searchResultsList.size() ; i++) {
                        GridLayout individualQueryResultGrid = new GridLayout(1,3);
                        JPanel individualQueryResult = new JPanel();
                        individualQueryResult.setLayout(individualQueryResultGrid);
                        resultRoomDescription[i] = new JLabel();
                        resultRoomDescription[i].setText(searchResultsList.get(i).typeOfRoom);
                        resultAmount[i] = new JLabel();
                        resultAmount[i].setText(Integer.toString(searchResultsList.get(i).totalStarOptions()));
                        resultAmount[i].setHorizontalAlignment(SwingConstants.CENTER);
                        resultDetails[i] = new JButton("Details...");
                        resultDetails[i].addActionListener(this);
                        individualQueryResult.add(resultRoomDescription[i]);
                        individualQueryResult.add(resultAmount[i]);
                        individualQueryResult.add(resultDetails[i]);
                        queryResultPanel.add(individualQueryResult);
                    }
                    mainSearchPanel.add(querySearchPanel);
                    mainSearchPanel.add(queryResultPanel);
                    setContentPane(mainSearchPanel);
                }
                if (searchResultsList.size() > 0) {
                    for (int d = 0; d < resultDetails.length; d++) {
                        if (action == resultDetails[d]) {
                            System.out.println("Clicked on button #" + d + "which has:");
                            searchResultsList.get(d).displayResult();
                        }
                    }
                }
            }

            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                Object source = itemEvent.getSource();
                if (source == selectResort && selectResort.getSelectedItem().toString() != defaultResortSelection) {
                    System.out.println("Selected Resort is: " + selectResort.getSelectedItem().toString());
                    System.out.println("Selected Index is: " + selectResort.getSelectedIndex());
                    for (int c = 0 ; c < resortNames.length ; c++) {
                        if (selectResort.getSelectedItem().toString() == resortNames[c]) {
                            resortCode = resortCodes[c];
                        }
                    }
                }
                if (source == selectNumberOfNights) {
                    numberOfNights = Integer.parseInt((String)selectNumberOfNights.getSelectedItem());
                }
                if (source == selectTypeOfRoom) {
                    switch ((String)selectTypeOfRoom.getSelectedItem()) {
                        case "One Bedroom": {
                            typeOfRoom = BD1;
                            break;
                        }
                        case "Two Bedroom": {
                            typeOfRoom = BD2;
                            break;
                        }
                        case "Three Bedroom": {
                            typeOfRoom = BD3;
                            break;
                        }
                        case "Four Bedroom": {
                            typeOfRoom = BD4;
                            break;
                        }
                        default:
                    }
                    System.out.println(typeOfRoom);
                }

            }
        }
    }

    */