import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.*;
import java.util.ArrayList;

class  StarOptionsCalculator {

    static private final int MIN_NIGHT_SEARCH = 1;
    static private final int MAX_NIGHT_SEARCH = 21;
    static private final int EARLIEST_YR_FOR_SEARCH = 2020;
    static private final int LATEST_YR_FOR_SEARCH = 2026;
    static private final String DEFAULT_PHASE_NAME = "Main Phase";

    enum ResortCode {
        HARBORSIDE_ATLANTIS, SHERATON_BROADWAY, SHERATON_DESERT, SHERATON_KAUAI, SHERATON_LAKESIDE,
        SHERATON_MOUNTAIN, SHERATON_PGA, SHERATON_STEAMBOAT, VISTANA_BEACH, VISTANA_RESORT,
        VISTANA_VILLAGES, WESTIN_CABOS, WESTIN_CANCUN, WESTIN_DESERT, WESTIN_KAANAPALI,
        WESTIN_KAANAPALI_NORTH, WESTIN_KIERLAND, WESTIN_LAGUNAMAR, WESTIN_MISSION,
        WESTIN_NANEA, WESTIN_PRINCEVILLE, WESTIN_RIVERFRONT, WESTIN_STJOHN;

        private static String getResortText(ResortCode resortCode) {
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

        private static ResortCode getResortCode(String resortCode) {
            for (ResortCode resortValues : ResortCode.values()) {
                if (resortCode.equals(ResortCode.getResortText(resortValues))){
                    return resortValues;
                }
            }
            throw new IllegalStateException("Unexpected value: " + resortCode);
        }
    }

    enum RoomType {
        ONE_BD, TWO_BD, THREE_BD, FOUR_BD;

        private static String getVillaText(RoomType villaSize) {
            switch(villaSize){
                case ONE_BD:
                    return "One Bedroom";
                case TWO_BD:
                    return "Two Bedroom";
                case THREE_BD:
                    return "Three Bedroom";
                case FOUR_BD:
                    return "Four Bedroom";
                default:
                    throw new IllegalStateException("Unexpected value: " + villaSize);
            }
        }

        private static RoomType getRoomType(String roomType) {
            for (RoomType roomValues : RoomType.values()) {
                if (roomType.equals(RoomType.getVillaText(roomValues))){
                    return roomValues;
                }
            }
            throw new IllegalStateException("Unexpected value: " + roomType);
        }
    }

    enum Season {
        PLATINUM_PLUS, PLATINUM, GOLD_PLUS, GOLD, SILVER, NO_SEASON;

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
                case NO_SEASON:
                    return "No Season";
                default:
                    throw new IllegalStateException("Unexpected value: " + villaSeason);
            }
        }
    }

    private static LocalDate setBeginDateOfYear(int chosenYear) {
        if (chosenYear >= EARLIEST_YR_FOR_SEARCH - 1 && chosenYear <= LATEST_YR_FOR_SEARCH)
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
            if (seasonChosen == Season.NO_SEASON) {
                throw new IllegalStateException("Unexpected value: " + seasonChosen.toString());
            }
            else {
                for (int i = 0; i < seasonsInVilla.length; i++) {
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
            }
            return starOptionValue;
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
            Season result = Season.NO_SEASON;
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
    }

    public static class Resort{

        private String resortName;
        private String resortLocation;
        private Phase[] resortPhases;

        private Resort(ResortCode resortKeyword) {

            String[] namesOfPhases;
            Season[][] seasonsOfPhases;
            int[][][] weeksOfPhases;

            int[][] starOptionsBuilder;
            Villa[] villaBuilder;
            Phase[] phaseBuilder = new Phase[1];

            this.resortName = ResortCode.getResortText(resortKeyword);

            switch(resortKeyword) {
                case HARBORSIDE_ATLANTIS: {
                    this.resortLocation = "Paradise Island, The Bahamas";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM_PLUS, Season.PLATINUM, Season.GOLD_PLUS}};
                    weeksOfPhases = new int[][][]{{{1, 17, 50, 52}, {18, 34, 47, 49}, {35, 46}}};
                    villaBuilder = new Villa[5];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(196900),
                            getWeekValuesFor(125000),
                            getWeekValuesFor(104100)};
                    villaBuilder[0] = new Villa(RoomType.THREE_BD, "Three-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100),
                            getWeekValuesFor(95700),
                            getWeekValuesFor(81000)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(129800),
                            getWeekValuesFor(81000),
                            getWeekValuesFor(67100)};
                    villaBuilder[2] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(51700),
                            getWeekValuesFor(44000)};
                    villaBuilder[3] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100),
                            getWeekValuesFor(44000),
                            getWeekValuesFor(37000)};
                    villaBuilder[4] = new Villa(RoomType.ONE_BD, "One-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case SHERATON_BROADWAY: {
                    this.resortLocation = "Myrtle Beach, South Carolina";
                    phaseBuilder = new Phase[2];
                    namesOfPhases = new String[]{"Plantation", "Palmetto"};
                    seasonsOfPhases = new Season[][]{
                            {Season.GOLD_PLUS, Season.GOLD, Season.SILVER},
                            {Season.PLATINUM, Season.GOLD_PLUS, Season.GOLD, Season.SILVER}
                    };
                    weeksOfPhases = new int[][][]{
                            {{9, 43, 47, 47}, {1, 1, 7, 8, 44, 46, 48, 48, 51, 52}, {2, 6, 49, 50}},
                            {{24, 35}, {15, 23, 36, 42}, {10, 14, 43, 52}, {1, 9}}
                    };
                    villaBuilder = new Villa[4];
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(56300),
                            getWeekValuesFor(46500)};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100),
                            getWeekValuesFor(44000),
                            getWeekValuesFor(37000)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(44000),
                            getWeekValuesFor(30500),
                            getWeekValuesFor(25800)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(37000),
                            getWeekValuesFor(25800),
                            getWeekValuesFor(20700)};
                    villaBuilder[3] = new Villa(RoomType.ONE_BD, "One-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);

                    villaBuilder = new Villa[5];
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(125000),
                            getWeekValuesFor(104100),
                            getWeekValuesFor(69800),
                            getWeekValuesFor(57700)};
                    villaBuilder[0] = new Villa(RoomType.THREE_BD, "Three-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[1]);
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(95700),
                            getWeekValuesFor(81000),
                            getWeekValuesFor(56300),
                            getWeekValuesFor(46500)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[1]);
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(67100),
                            getWeekValuesFor(44000),
                            getWeekValuesFor(37000)};
                    villaBuilder[2] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[1]);
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(51700),
                            getWeekValuesFor(44000),
                            getWeekValuesFor(30500),
                            getWeekValuesFor(25800)};
                    villaBuilder[3] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[1]);
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(44000),
                            getWeekValuesFor(37000),
                            getWeekValuesFor(25800),
                            getWeekValuesFor(20700)};
                    villaBuilder[4] = new Villa(RoomType.ONE_BD, "One-Bedroom", starOptionsBuilder, seasonsOfPhases[1]);
                    phaseBuilder[1] = new Phase(namesOfPhases[1], seasonsOfPhases[1], weeksOfPhases[1], villaBuilder);

                }
                break;
                case SHERATON_DESERT:
                case WESTIN_KIERLAND: {
                    this.resortLocation = "Scottsdale, Arizona";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM_PLUS, Season.GOLD_PLUS, Season.GOLD}};
                    weeksOfPhases = new int[][][]{{{1, 21, 50, 52}, {22, 27, 36, 49}, {28, 35}}};
                    villaBuilder = new Villa[3];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100),
                            getWeekValuesFor(81000),
                            getWeekValuesFor(56300)};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(44000),
                            getWeekValuesFor(30500)};
                    villaBuilder[1] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100),
                            getWeekValuesFor(37000),
                            getWeekValuesFor(25800)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "One-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case SHERATON_KAUAI: {
                    this.resortLocation = "Koloa, Kaua'i, Hawai'i";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM_PLUS}};
                    weeksOfPhases = new int[][][]{{{1, 52}}};
                    villaBuilder = new Villa[4];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100)};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "One-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100)};
                    villaBuilder[3] = new Villa(RoomType.ONE_BD, "Studio", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case SHERATON_LAKESIDE: {
                    this.resortLocation = "Avon, Colorado";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM_PLUS, Season.GOLD_PLUS, Season.SILVER}};
                    weeksOfPhases = new int[][][]{{{1, 15, 48, 52}, {21, 39}, {16, 20, 40, 47}}};
                    villaBuilder = new Villa[1];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(129800),
                            getWeekValuesFor(67100),
                            getWeekValuesFor(37000)};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case SHERATON_MOUNTAIN: {
                    this.resortLocation = "Avon, Colorado";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM_PLUS, Season.GOLD_PLUS, Season.SILVER}};
                    weeksOfPhases = new int[][][]{{{1, 15, 48, 52}, {21, 39}, {16, 20, 40, 47}}};
                    villaBuilder = new Villa[3];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100),
                            getWeekValuesFor(81000),
                            getWeekValuesFor(46500)};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(44000),
                            getWeekValuesFor(25800)};
                    villaBuilder[1] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100),
                            getWeekValuesFor(37000),
                            getWeekValuesFor(20700)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "One-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case SHERATON_PGA: {
                    this.resortLocation = "Port St. Lucie, Florida";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.GOLD_PLUS, Season.GOLD, Season.SILVER}};
                    weeksOfPhases = new int[][][]{{{1, 17, 40, 47, 51, 52}, {18, 34}, {35, 39, 48, 50}}};
                    villaBuilder = new Villa[4];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(56300),
                            getWeekValuesFor(46500)};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100),
                            getWeekValuesFor(44000),
                            getWeekValuesFor(37000)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(44000),
                            getWeekValuesFor(30500),
                            getWeekValuesFor(25800)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(37000),
                            getWeekValuesFor(25800),
                            getWeekValuesFor(20700)};
                    villaBuilder[3] = new Villa(RoomType.ONE_BD, "One-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case SHERATON_STEAMBOAT: {
                    this.resortLocation = "Steamboat Springs, Colorado";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{
                            {Season.PLATINUM_PLUS, Season.PLATINUM, Season.SILVER}
                    };
                    weeksOfPhases = new int[][][]{{
                            {1, 15, 48, 52}, {21, 39}, {16, 20, 40, 47}
                    }};
                    villaBuilder = new Villa[12];
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(296200),
                            getWeekValuesFor(191400),
                            getWeekValuesFor(93000)};
                    villaBuilder[0] = new Villa(RoomType.FOUR_BD, "Four-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(215200),
                            getWeekValuesFor(139700),
                            getWeekValuesFor(67200)};
                    villaBuilder[1] = new Villa(RoomType.THREE_BD, "Three-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(257700),
                            getWeekValuesFor(196900),
                            getWeekValuesFor(57700)};
                    villaBuilder[2] = new Villa(RoomType.THREE_BD, "Three-Bedroom Lockoff (Mountain Side)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(196900),
                            getWeekValuesFor(125000),
                            getWeekValuesFor(57700)};
                    villaBuilder[3] = new Villa(RoomType.THREE_BD, "Three-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(176700),
                            getWeekValuesFor(148100),
                            getWeekValuesFor(46500)};
                    villaBuilder[4] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff (Mountain Side)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100),
                            getWeekValuesFor(95700),
                            getWeekValuesFor(46500)};
                    villaBuilder[5] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100),
                            getWeekValuesFor(95700),
                            getWeekValuesFor(46500)};
                    villaBuilder[6] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(95700),
                            getWeekValuesFor(81000),
                            getWeekValuesFor(25800)};
                    villaBuilder[7] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium (Mountain Side)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(51700),
                            getWeekValuesFor(25800)};
                    villaBuilder[8] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(67100),
                            getWeekValuesFor(20700)};
                    villaBuilder[9] = new Villa(RoomType.ONE_BD, "Studio Villa (Mountain Side)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100),
                            getWeekValuesFor(44000),
                            getWeekValuesFor(20700)};
                    villaBuilder[10] = new Villa(RoomType.ONE_BD, "Studio Villa", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(50000),
                            getWeekValuesFor(30000),
                            getWeekValuesFor(15000)};
                    villaBuilder[11] = new Villa(RoomType.ONE_BD, "Hotel Room", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case VISTANA_BEACH: {
                    this.resortLocation = "Jensen Beach, Florida";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{
                            DEFAULT_PHASE_NAME
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
                case VISTANA_RESORT: {
                    this.resortLocation = "Orlando, Florida";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM, Season.GOLD_PLUS}};
                    weeksOfPhases = new int[][][]{{{6, 17, 23, 34, 39, 47, 51, 52},{1, 5, 18, 22, 35, 38, 48, 50}}};

                    villaBuilder = new Villa[4];
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(95700),
                            getWeekValuesFor(81000)};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(67100)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(51700),
                            getWeekValuesFor(44000)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(44000),
                            getWeekValuesFor(37000)};
                    villaBuilder[3] = new Villa(RoomType.ONE_BD, "One-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case VISTANA_VILLAGES: {
                    this.resortLocation = "Orlando, Florida";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM, Season.GOLD_PLUS}};
                    weeksOfPhases = new int[][][]{{{6, 17, 23, 34, 39, 47, 51, 52},{1, 5, 18, 22, 35, 38, 48, 50}}};

                    villaBuilder = new Villa[6];
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(139700),
                            getWeekValuesFor(118000)};
                    villaBuilder[0] = new Villa(RoomType.THREE_BD, "Three-Bedroom Lockoff (1BD Premium + Two 1BD)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(125000),
                            getWeekValuesFor(104100)};
                    villaBuilder[1] = new Villa(RoomType.THREE_BD, "Three-Bedroom Lockoff (2BD + 1BD)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(95700),
                            getWeekValuesFor(81000)};
                    villaBuilder[2] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(67100)};
                    villaBuilder[3] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(51700),
                            getWeekValuesFor(44000)};
                    villaBuilder[4] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(44000),
                            getWeekValuesFor(37000)};
                    villaBuilder[5] = new Villa(RoomType.ONE_BD, "One-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case WESTIN_CABOS: {
                    this.resortLocation = "Los Cabos, Mexico";
                    phaseBuilder = new Phase[2];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME, "Baja Point"};
                    seasonsOfPhases = new Season[][]{
                            {Season.PLATINUM_PLUS, Season.PLATINUM, Season.GOLD_PLUS},
                            {Season.PLATINUM_PLUS, Season.PLATINUM, Season.GOLD_PLUS}
                    };
                    weeksOfPhases = new int[][][]{
                            {{1, 23, 44, 52}, {24, 33, 41, 43}, {34, 40}},
                            {{1, 23, 44, 52}, {24, 33, 41, 43}, {34, 40}}
                    };
                    villaBuilder = new Villa[6];
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(310900),
                            getWeekValuesFor(243800),
                            getWeekValuesFor(169700)};
                    villaBuilder[0] = new Villa(RoomType.THREE_BD, "Governor's Suite Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(243800),
                            getWeekValuesFor(192100),
                            getWeekValuesFor(132700)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Governor's Suite", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(215200),
                            getWeekValuesFor(176700),
                            getWeekValuesFor(118000)};
                    villaBuilder[2] = new Villa(RoomType.THREE_BD, "Three-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(176700),
                            getWeekValuesFor(148100),
                            getWeekValuesFor(95700)};
                    villaBuilder[3] = new Villa(RoomType.TWO_BD, "Two-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100),
                            getWeekValuesFor(125000),
                            getWeekValuesFor(81000)};
                    villaBuilder[4] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100),
                            getWeekValuesFor(51700),
                            getWeekValuesFor(37000)};
                    villaBuilder[5] = new Villa(RoomType.ONE_BD, "Studio", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);

                    villaBuilder = new Villa[4];
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(257700),
                            getWeekValuesFor(215200),
                            getWeekValuesFor(139700)};
                    villaBuilder[0] = new Villa(RoomType.THREE_BD, "Three-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[1]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(176700),
                            getWeekValuesFor(148100),
                            getWeekValuesFor(95700)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[1]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(95700),
                            getWeekValuesFor(81000),
                            getWeekValuesFor(51700)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[1]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(67100),
                            getWeekValuesFor(44000)};
                    villaBuilder[3] = new Villa(RoomType.ONE_BD, "Hotel Room", starOptionsBuilder, seasonsOfPhases[1]);
                    phaseBuilder[1] = new Phase(namesOfPhases[1], seasonsOfPhases[1], weeksOfPhases[1], villaBuilder);
                }
                break;
                case WESTIN_CANCUN: {
                    this.resortLocation = "Cancun, Mexico";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM_PLUS, Season.GOLD_PLUS}};
                    weeksOfPhases = new int[][][]{{{5, 17, 24, 35, 44, 47, 51, 52}, {1, 4, 18, 23, 36, 43, 48, 50}}};
                    villaBuilder = new Villa[3];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(215200),
                            getWeekValuesFor(118000)};
                    villaBuilder[0] = new Villa(RoomType.THREE_BD, "Three-Bedroom (Oceanside or Lagoon Side)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100),
                            getWeekValuesFor(81000)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom (Oceanside or Lagoon Side)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100),
                            getWeekValuesFor(37000)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "Studio (Oceanside or Lagoon Side)", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case WESTIN_DESERT: {
                    this.resortLocation = "Palm Desert, California";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM_PLUS, Season.GOLD_PLUS, Season.GOLD}};
                    weeksOfPhases = new int[][][]{{{1, 21, 50, 52}, {22, 27, 36, 49}, {28, 35}}};
                    villaBuilder = new Villa[3];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100),
                            getWeekValuesFor(81000),
                            getWeekValuesFor(56300)};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(44000),
                            getWeekValuesFor(30500)};
                    villaBuilder[1] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100),
                            getWeekValuesFor(37000),
                            getWeekValuesFor(25800)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "One-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case WESTIN_KAANAPALI:
                case WESTIN_KAANAPALI_NORTH: {
                    this.resortLocation = "Lahaina, Maui, Hawai'i";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM_PLUS}};
                    weeksOfPhases = new int[][][]{{{1, 52}}};
                    villaBuilder = new Villa[6];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(176700)};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff (Oceanfront)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(95700)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium (Oceanfront)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000)};
                    villaBuilder[3] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000)};
                    villaBuilder[4] = new Villa(RoomType.ONE_BD, "Studio Premium (Oceanfront)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100)};
                    villaBuilder[5] = new Villa(RoomType.ONE_BD, "Studio Premium", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case WESTIN_LAGUNAMAR: {
                    this.resortLocation = "Cancun, Mexico";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM_PLUS, Season.GOLD_PLUS}};
                    weeksOfPhases = new int[][][]{{{5, 17, 24, 35, 44, 47, 51, 52}, {1, 4, 18, 23, 36, 43, 48, 50}}};
                    villaBuilder = new Villa[3];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100),
                            getWeekValuesFor(81000)};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(44000)};
                    villaBuilder[1] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100),
                            getWeekValuesFor(37000)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "Studio Premium", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case WESTIN_MISSION: {
                    this.resortLocation = "Rancho Mirage, California";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM_PLUS, Season.GOLD_PLUS, Season.GOLD}};
                    weeksOfPhases = new int[][][]{{{1, 21, 50, 52}, {22, 27, 36, 49}, {28, 35}}};
                    villaBuilder = new Villa[3];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100),
                            getWeekValuesFor(81000),
                            getWeekValuesFor(56300)};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(44000),
                            getWeekValuesFor(30500)};
                    villaBuilder[1] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100),
                            getWeekValuesFor(37000),
                            getWeekValuesFor(25800)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "One-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case WESTIN_NANEA: {
                    this.resortLocation = "Lahaina, Maui, Hawai'i";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM_PLUS}};
                    weeksOfPhases = new int[][][]{{{1, 52}}};
                    villaBuilder = new Villa[4];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(257700)};
                    villaBuilder[0] = new Villa(RoomType.THREE_BD, "Three-Bedroom (Oceanfront)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(176700)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom Premium (Oceanfront)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100)};
                    villaBuilder[2] = new Villa(RoomType.TWO_BD, "Two-Bedroom Premium (Resort View)", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000)};
                    villaBuilder[3] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium (Resort View)", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case WESTIN_PRINCEVILLE: {
                    this.resortLocation = "Princeville, Kaua'i, Hawai'i";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM_PLUS}};
                    weeksOfPhases = new int[][][]{{{1, 52}}};
                    villaBuilder = new Villa[3];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100)};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000)};
                    villaBuilder[1] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "Studio Premium", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case WESTIN_RIVERFRONT: {
                    this.resortLocation = "Avon, Colorado";
                    phaseBuilder = new Phase[1];
                    namesOfPhases = new String[]{DEFAULT_PHASE_NAME};
                    seasonsOfPhases = new Season[][]{{Season.PLATINUM_PLUS, Season.PLATINUM, Season.SILVER}};
                    weeksOfPhases = new int[][][]{{{1, 15, 48, 52}, {21, 39}, {16, 20, 40, 47}}};
                    villaBuilder = new Villa[3];

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(148100),
                            getWeekValuesFor(95700),
                            getWeekValuesFor(46500)};
                    villaBuilder[0] = new Villa(RoomType.TWO_BD, "Two-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(51700),
                            getWeekValuesFor(25800)};
                    villaBuilder[1] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(67100),
                            getWeekValuesFor(44000),
                            getWeekValuesFor(20700)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "Studio Premium", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);
                }
                break;
                case WESTIN_STJOHN: {
                    this.resortLocation = "St. John, U.S. Virgin Islands";
                    phaseBuilder = new Phase[4];
                    namesOfPhases = new String[]{
                            "Virgin Grand",
                            "Bay Vista",
                            "Coral Vista",
                            "Sunset Bay"
                    };
                    seasonsOfPhases = new Season[][]{
                            {Season.PLATINUM_PLUS, Season.PLATINUM, Season.GOLD_PLUS},
                            {Season.PLATINUM_PLUS, Season.PLATINUM, Season.GOLD_PLUS},
                            {Season.PLATINUM_PLUS, Season.PLATINUM, Season.GOLD_PLUS},
                            {Season.PLATINUM_PLUS, Season.PLATINUM, Season.GOLD_PLUS}
                    };
                    weeksOfPhases = new int[][][]{
                            {{1, 15, 51, 52},{16, 20, 43, 50},{21, 42}},
                            {{1, 18, 51, 52},{19, 33},{34, 50}},
                            {{1, 18, 51, 52},{19, 33},{34, 50}},
                            {{1, 18, 51, 52},{19, 33},{34, 50}}
                    };

                    villaBuilder = new Villa[4];
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(257700),
                            getWeekValuesFor(196900),
                            getWeekValuesFor(125000)};
                    villaBuilder[0] = new Villa(RoomType.THREE_BD, "Three-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(176700),
                            getWeekValuesFor(148100),
                            getWeekValuesFor(95700)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(95700),
                            getWeekValuesFor(81000),
                            getWeekValuesFor(51700)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "One-Bedroom Premium", starOptionsBuilder, seasonsOfPhases[0]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(67100),
                            getWeekValuesFor(44000)};
                    villaBuilder[3] = new Villa(RoomType.ONE_BD, "Studio Premium", starOptionsBuilder, seasonsOfPhases[0]);
                    phaseBuilder[0] = new Phase(namesOfPhases[0], seasonsOfPhases[0], weeksOfPhases[0], villaBuilder);

                    villaBuilder = new Villa[3];
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(257700),
                            getWeekValuesFor(196900),
                            getWeekValuesFor(125000)};
                    villaBuilder[0] = new Villa(RoomType.THREE_BD, "Three-Bedroom", starOptionsBuilder, seasonsOfPhases[1]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(176700),
                            getWeekValuesFor(148100),
                            getWeekValuesFor(95700)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom Loft", starOptionsBuilder, seasonsOfPhases[1]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(176700),
                            getWeekValuesFor(148100),
                            getWeekValuesFor(95700)};
                    villaBuilder[2] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[1]);
                    phaseBuilder[1] = new Phase(namesOfPhases[1], seasonsOfPhases[1], weeksOfPhases[1], villaBuilder);

                    villaBuilder = new Villa[4];
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(257700),
                            getWeekValuesFor(196900),
                            getWeekValuesFor(125000)};
                    villaBuilder[0] = new Villa(RoomType.THREE_BD, "Three-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[1]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(176700),
                            getWeekValuesFor(148100),
                            getWeekValuesFor(95700)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom Loft", starOptionsBuilder, seasonsOfPhases[1]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(176700),
                            getWeekValuesFor(148100),
                            getWeekValuesFor(95700)};
                    villaBuilder[2] = new Villa(RoomType.TWO_BD, "Two-Bedroom", starOptionsBuilder, seasonsOfPhases[1]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(67100),
                            getWeekValuesFor(44000)};
                    villaBuilder[3] = new Villa(RoomType.ONE_BD, "Studio", starOptionsBuilder, seasonsOfPhases[1]);
                    phaseBuilder[2] = new Phase(namesOfPhases[2], seasonsOfPhases[2], weeksOfPhases[2], villaBuilder);

                    villaBuilder = new Villa[3];
                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(257700),
                            getWeekValuesFor(196900),
                            getWeekValuesFor(125000)};
                    villaBuilder[0] = new Villa(RoomType.THREE_BD, "Three-Bedroom Lockoff", starOptionsBuilder, seasonsOfPhases[1]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(176700),
                            getWeekValuesFor(148100),
                            getWeekValuesFor(95700)};
                    villaBuilder[1] = new Villa(RoomType.TWO_BD, "Two-Bedroom Loft", starOptionsBuilder, seasonsOfPhases[1]);

                    starOptionsBuilder = new int[][]{
                            getWeekValuesFor(81000),
                            getWeekValuesFor(67100),
                            getWeekValuesFor(44000)};
                    villaBuilder[2] = new Villa(RoomType.ONE_BD, "Studio", starOptionsBuilder, seasonsOfPhases[1]);
                    phaseBuilder[3] = new Phase(namesOfPhases[3], seasonsOfPhases[3], weeksOfPhases[3], villaBuilder);

                }
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + resortKeyword);
            }
            this.resortPhases = phaseBuilder;
        }

        private int[] getWeekValuesFor(int weeklyValue) {
            int[] arrayOfValues;
            switch (weeklyValue){
                case 310900: arrayOfValues = new int[]{31060,46665,62195}; break;
                case 296200: arrayOfValues = new int[]{29600,44450,59250}; break;
                case 257700: arrayOfValues = new int[]{25770,38655,51540}; break;
                case 243800: arrayOfValues = new int[]{24360,36590,48770}; break;
                case 215200: arrayOfValues = new int[]{21500,32300,43050}; break;
                case 196900: arrayOfValues = new int[]{19700,29525,39375}; break;
                case 192100: arrayOfValues = new int[]{19210,28815,38420}; break;
                case 191400: arrayOfValues = new int[]{19100,28750,38300}; break;
                case 176700: arrayOfValues = new int[]{17650,26525,35350}; break;
                case 169700: arrayOfValues = new int[]{16970,25455,33940}; break;
                case 148100: arrayOfValues = new int[]{14800,22225,29625}; break;
                case 139700: arrayOfValues = new int[]{13950,20975,27950}; break;
                case 132700: arrayOfValues = new int[]{13270,19905,26540}; break;
                case 129800: arrayOfValues = new int[]{13000,19450,25950}; break;
                case 125000: arrayOfValues = new int[]{12500,18750,25000}; break;
                case 118000: arrayOfValues = new int[]{11800,17700,23600}; break;
                case 104100: arrayOfValues = new int[]{10400,15625,20825}; break;
                case 95700: arrayOfValues = new int[]{9550,14375,19150}; break;
                case 93000: arrayOfValues = new int[]{9200,14000,18700}; break;
                case 81000: arrayOfValues = new int[]{8100,12150,16200}; break;
                case 69800: arrayOfValues = new int[]{6980,10470,13960}; break;
                case 67200: arrayOfValues = new int[]{6650,10100,13525}; break;
                case 67100: arrayOfValues = new int[]{6700,10075,13425}; break;
                case 57700: arrayOfValues = new int[]{5760,8660,11550}; break;
                case 56300: arrayOfValues = new int[]{5600,8475,11275}; break;
                case 51700: arrayOfValues = new int[]{5150,7775,10350}; break;
                case 50000: arrayOfValues = new int[]{5000,7500,10000}; break;
                case 46500: arrayOfValues = new int[]{4600,7000,9350}; break;
                case 44000: arrayOfValues = new int[]{4400,6600,8800}; break;
                case 37000: arrayOfValues = new int[]{3700,5550,7400}; break;
                case 30500: arrayOfValues = new int[]{3050,4575,6100}; break;
                case 30000: arrayOfValues = new int[]{3000,4500,6000}; break;
                case 25800: arrayOfValues = new int[]{2550,3900,5175}; break;
                case 20700: arrayOfValues = new int[]{2050,3100,4175}; break;
                case 15000: arrayOfValues = new int[]{1500,2250,3000}; break;
                default:
                    throw new IllegalStateException("Unexpected value: " + weeklyValue);
            }
            return arrayOfValues;
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
                        dateSearched = getChosenCheckInDate();
                        starOptionValueResultList = new int[this.chosenNumberOfNights];
                        seasonValueResultList = new Season[this.chosenNumberOfNights];
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

        private String displaySearchResultLabel() {
            String result = selectedRoomDescription;
            if (!selectedPhaseName.equals(DEFAULT_PHASE_NAME)) {
                result = result + " at " + selectedPhaseName;
            }
            return (result + " for " + getTotalStarOptionValue() + " StarOptions.");
        }
    }

    public static void main(String[] args) {

        new CalculatorWindow();
    }

    private static class CalculatorWindow extends JFrame implements ItemListener, ActionListener {

        JComboBox pickResortList, pickNightsList, pickRoomList, pickYearList, pickMonthList, pickDayList;
        JButton beginSearch = new JButton("Search");

        ResortCode pickedResort;
        int pickedDay;
        String pickedMonth;
        Year pickedYear;
        RoomType pickedRoomType;
        int pickedNights;

        ReservationQuery currentQuery;
        ArrayList<SearchResult> currentResults;

        Container windowContainer;
        JPanel searchPanel, resultsPanel;

        public CalculatorWindow() {
            super("StarOptions Calculator");
            setSize(950,600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // searchPanel Setup
            searchPanel = new JPanel();
            searchPanel.setBorder(BorderFactory.createTitledBorder("Find Your Vacation"));
            Dimension size = getPreferredSize();
            size.width = 400;
            searchPanel.setPreferredSize(size);

            searchPanel.setLayout(new GridLayout(9,1,5,5));
            addResortSelection();
            searchPanel.add(new JPanel());
            addDateSelectionPanel();
            addNightsSelectionPanel();
            addRoomSelectionPanel();
            searchPanel.add(new JPanel());
            beginSearch.addActionListener(this);
            searchPanel.add(beginSearch);
            initializeSearchPanelValues();

            // resultsPanel Setup
            resultsPanel = new JPanel();
            resultsPanel.setBorder(BorderFactory.createTitledBorder("Search Results:"));
            size = getPreferredSize();
            size.width = 535;
            resultsPanel.setPreferredSize(size);

            // JFrame Setup
            setLayout(new BorderLayout());
            windowContainer = getContentPane();
            windowContainer.add(resultsPanel, BorderLayout.EAST);
            windowContainer.add(searchPanel, BorderLayout.WEST);
            setContentPane(windowContainer);
            setResizable(false);
            setVisible(true);
        }

        private void initializeSearchPanelValues(){
            pickedResort = ResortCode.getResortCode(pickResortList.getItemAt(0).toString());
            pickedDay = Integer.parseInt(pickDayList.getItemAt(0).toString());
            pickedMonth = pickMonthList.getItemAt(0).toString();
            pickedYear = Year.of(Integer.parseInt(pickYearList.getItemAt(0).toString()));
            pickedRoomType = RoomType.getRoomType(pickRoomList.getItemAt(0).toString());
            pickedNights = Integer.parseInt(pickNightsList.getItemAt(0).toString());
        }

        private void addResortSelection(){
            ResortCode[] resortCodesList = ResortCode.values();
            pickResortList = new JComboBox();
            for (ResortCode resortCode : resortCodesList) {
                pickResortList.addItem(ResortCode.getResortText(resortCode));
            }
            searchPanel.add(new JLabel("Select your Resort:", SwingConstants.CENTER));
            pickResortList.addItemListener( this);
            searchPanel.add(pickResortList);
        }

        private void addDateSelectionPanel(){
            searchPanel.add(new JLabel("Select Check-in Date:", SwingConstants.CENTER));
            JPanel selectDatePanel = new JPanel();
            GridLayout selectDateGrid = new GridLayout(2,3,5,5);
            selectDatePanel.setLayout(selectDateGrid);
            selectDatePanel.add(new JLabel("Month", SwingConstants.CENTER));
            selectDatePanel.add(new JLabel("Day", SwingConstants.CENTER));
            selectDatePanel.add(new JLabel("Year", SwingConstants.CENTER));
            pickMonthList = new JComboBox();
            centerItemsInJComboBox(pickMonthList);
            for (int i = 0 ; i < Month.values().length; i++) {
                pickMonthList.addItem(Month.values()[i].toString());
            }
            pickMonthList.addItemListener( this);
            selectDatePanel.add(pickMonthList);
            pickDayList = new JComboBox();
            centerItemsInJComboBox(pickDayList);
            for (int i = 0; i < Month.valueOf(String.valueOf(pickMonthList.getItemAt(0))).maxLength(); i++) {
                pickDayList.addItem(i+1);
            }
            pickDayList.addItemListener( this);
            selectDatePanel.add(pickDayList);
            pickYearList = new JComboBox();
            centerItemsInJComboBox(pickYearList);
            for (int i = EARLIEST_YR_FOR_SEARCH ; i <= LATEST_YR_FOR_SEARCH; i++) {
                pickYearList.addItem(i);
            }
            pickYearList.addItemListener( this);
            selectDatePanel.add(pickYearList);
            searchPanel.add(selectDatePanel);
        }

        private void addNightsSelectionPanel(){
            JPanel nightsPanel = new JPanel();
            GridLayout nightsGrid = new GridLayout(1,2,5,5);
            nightsPanel.setLayout(nightsGrid);
            nightsPanel.add(new JLabel("Select Number of Nights: ", SwingConstants.CENTER));
            pickNightsList = new JComboBox();
            centerItemsInJComboBox(pickNightsList);
            for (int i = MIN_NIGHT_SEARCH ; i <= MAX_NIGHT_SEARCH; i++) {
                pickNightsList.addItem(i);
            }
            pickNightsList.addItemListener( this);
            nightsPanel.add(pickNightsList);
            searchPanel.add(nightsPanel);
        }

        private void addRoomSelectionPanel(){
            JPanel villaPanel = new JPanel();
            GridLayout villaGrid = new GridLayout(1,2,5,5);
            villaPanel.setLayout(villaGrid);
            villaPanel.add(new JLabel("Select Type of Villa: ", SwingConstants.CENTER));
            pickRoomList = new JComboBox();
            centerItemsInJComboBox(pickRoomList);
            RoomType[] roomTypesList = RoomType.values();
            for (RoomType roomType : roomTypesList) {
                pickRoomList.addItem(RoomType.getVillaText(roomType));
            }
            pickRoomList.addItemListener( this);
            villaPanel.add(pickRoomList);
            searchPanel.add(villaPanel);
        }

        private void centerItemsInJComboBox(JComboBox chosenComboBox) {
            DefaultListCellRenderer centerItems = new DefaultListCellRenderer();
            centerItems.setHorizontalAlignment(SwingConstants.CENTER);
            chosenComboBox.setRenderer(centerItems);
        }

        private void displayResultsOnWindow(){
            resultsPanel.removeAll();
            resultsPanel.setLayout(new GridLayout(9,1,5,5));
            resultsPanel.add(new JLabel("Total of " + currentResults.size() + " matches found."));
            if (currentResults.size() == 0) {
                resultsPanel.add(new JLabel("This resort has no " +
                        RoomType.getVillaText(pickedRoomType) + " villas, please modify your search."));
            }
            for (SearchResult currentResult : currentResults) {
                resultsPanel.add(new JLabel(currentResult.displaySearchResultLabel()));
            }
            setContentPane(windowContainer);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object action = actionEvent.getSource();
            if (action == beginSearch) {
                currentQuery = new ReservationQuery(pickedResort, LocalDate.of(pickedYear.getValue(), Month.valueOf(pickedMonth), pickedDay),pickedNights,pickedRoomType);
                currentResults = currentQuery.getSearchResultsList();
            }
            displayResultsOnWindow();
        }

        @Override
        public void itemStateChanged(ItemEvent itemEvent) {
            Object source = itemEvent.getSource();
            if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                if (source == pickResortList) {
                    pickedResort = ResortCode.getResortCode(pickResortList.getSelectedItem().toString());
                }
                if (source == pickNightsList) {
                    pickedNights = Integer.parseInt(pickNightsList.getSelectedItem().toString());
                }
                if (source == pickRoomList) {
                    pickedRoomType = RoomType.getRoomType(pickRoomList.getSelectedItem().toString());
                }
                if (source == pickYearList) {
                    pickedYear = Year.of(Integer.parseInt(pickYearList.getSelectedItem().toString()));
                    if (Month.valueOf(pickedMonth).length(pickedYear.isLeap()) != pickDayList.getItemCount()) {
                        pickDayList.removeAllItems();
                        for (int i = 1 ; i <= Month.valueOf(pickedMonth).length(pickedYear.isLeap()); i++) {
                            pickDayList.addItem(i);
                        }
                    }
                }
                if (source == pickMonthList) {
                    pickedMonth = pickMonthList.getSelectedItem().toString();
                    if (Month.valueOf(pickedMonth).length(pickedYear.isLeap()) != pickDayList.getItemCount()) {
                        pickDayList.removeAllItems();
                        for (int i = 1 ; i <= Month.valueOf(pickedMonth).length(pickedYear.isLeap()); i++) {
                            pickDayList.addItem(i);
                        }
                    }
                }
                if (source == pickDayList) {
                    pickedDay = Integer.parseInt(pickDayList.getSelectedItem().toString());
                }
            }
        }
    }
}
