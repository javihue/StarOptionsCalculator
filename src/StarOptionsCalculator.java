import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.*;
import java.util.ArrayList;
import java.util.Objects;

class  StarOptionsCalculator {

    static private final int MIN_NIGHT_SEARCH = 1;
    static private final int MAX_NIGHT_SEARCH = 21;
    static private final int EARLIEST_YR_FOR_SEARCH = 2020;
    static private final int LATEST_YR_FOR_SEARCH = 2026;

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
        PLATINUM_PLUS, PLATINUM, GOLD_PLUS, GOLD, SILVER, NO_SEASON
    }

    enum PhaseName {
        MAIN_PHASE, PLANTATION, PALMETTO, BAJA_POINT, VIRGIN_GRAND, BAY_VISTA, CORAL_VISTA, SUNSET_BAY;

        private static String getPhaseText(PhaseName phaseCode) {
            switch(phaseCode) {
                case MAIN_PHASE:
                    return "Main Phase";
                case PLANTATION:
                    return "Plantation";
                case PALMETTO:
                    return "Palmetto";
                case BAJA_POINT:
                    return "Baja Point";
                case VIRGIN_GRAND:
                    return "Virgin Grand";
                case BAY_VISTA:
                    return "Bay Vista";
                case CORAL_VISTA:
                    return "Coral Vista";
                case SUNSET_BAY:
                    return "Sunset Bay";
                default:
                    throw new IllegalStateException("Unexpected value: " + phaseCode);
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
        private PhaseName phaseName;
        private Season[] phaseSeasons;
        private int[][] phaseWeeks;
        private Villa[] phaseVillas;

        private Phase(ResortCode resortKey, PhaseName nameOfPhase) {
            initializePhaseValues(resortKey, nameOfPhase);
            buildVillaList(resortKey, nameOfPhase);
        }

        private String getPhaseName(){return PhaseName.getPhaseText(phaseName);}

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

        private void initializePhaseValues(ResortCode resortKey, PhaseName nameOfPhase) {
            setPhaseName(nameOfPhase);
            setPhaseSeasons(resortKey, nameOfPhase);
            setPhaseWeeks(resortKey, nameOfPhase);
            setPhaseVillasAmount(resortKey, nameOfPhase);
        }

        private void setPhaseName(PhaseName nameOfPhase) { this.phaseName = nameOfPhase; }

        private void setPhaseSeasons(ResortCode resortKey, PhaseName nameOfPhase) {
            switch (resortKey) {
                case SHERATON_DESERT:
                case WESTIN_KIERLAND:
                case WESTIN_MISSION:
                case WESTIN_DESERT:
                    this.phaseSeasons = new Season[]{Season.PLATINUM_PLUS, Season.GOLD_PLUS, Season.GOLD};
                    break;
                case SHERATON_LAKESIDE:
                case SHERATON_MOUNTAIN:
                    this.phaseSeasons = new Season[]{Season.PLATINUM_PLUS, Season.GOLD_PLUS, Season.SILVER};
                    break;
                case WESTIN_RIVERFRONT:
                case SHERATON_STEAMBOAT:
                    this.phaseSeasons = new Season[]{Season.PLATINUM_PLUS, Season.PLATINUM, Season.SILVER};
                    break;
                case VISTANA_RESORT:
                case VISTANA_VILLAGES:
                case VISTANA_BEACH:
                    this.phaseSeasons = new Season[]{Season.PLATINUM, Season.GOLD_PLUS};
                    break;
                case SHERATON_PGA:
                    this.phaseSeasons = new Season[]{Season.GOLD_PLUS, Season.GOLD, Season.SILVER};
                    break;
                case WESTIN_PRINCEVILLE:
                case SHERATON_KAUAI:
                case WESTIN_KAANAPALI:
                case WESTIN_KAANAPALI_NORTH:
                case WESTIN_NANEA:
                    this.phaseSeasons = new Season[]{Season.PLATINUM_PLUS};
                    break;
                case SHERATON_BROADWAY: {
                    if (nameOfPhase == PhaseName.PLANTATION) {
                        this.phaseSeasons = new Season[]{Season.GOLD_PLUS, Season.GOLD, Season.SILVER};
                    }
                    if (nameOfPhase == PhaseName.PALMETTO) {
                        this.phaseSeasons = new Season[]{Season.PLATINUM, Season.GOLD_PLUS, Season.GOLD, Season.SILVER};
                    }
                    break;
                }
                case WESTIN_LAGUNAMAR:
                case WESTIN_CANCUN:
                    this.phaseSeasons = new Season[]{Season.PLATINUM_PLUS, Season.GOLD_PLUS};
                    break;
                case WESTIN_CABOS:
                case HARBORSIDE_ATLANTIS:
                case WESTIN_STJOHN:
                    this.phaseSeasons = new Season[]{Season.PLATINUM_PLUS, Season.PLATINUM, Season.GOLD_PLUS};
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + resortKey);
            }
        }

        private void setPhaseWeeks(ResortCode resortKey, PhaseName nameOfPhase) {
            switch (resortKey) {
                case SHERATON_DESERT:
                case WESTIN_KIERLAND:
                case WESTIN_MISSION:
                case WESTIN_DESERT:
                    this.phaseWeeks = new int[][]{{1, 21, 50, 52}, {22, 27, 36, 49}, {28, 35}};
                    break;
                case SHERATON_LAKESIDE:
                case SHERATON_MOUNTAIN:
                case SHERATON_STEAMBOAT:
                case WESTIN_RIVERFRONT:
                    this.phaseWeeks = new int[][]{{1, 15, 48, 52}, {21, 39}, {16, 20, 40, 47}};
                    break;
                case VISTANA_RESORT:
                case VISTANA_VILLAGES:
                    this.phaseWeeks = new int[][]{{6, 17, 23, 34, 39, 47, 51, 52},{1, 5, 18, 22, 35, 38, 48, 50}};
                    break;
                case VISTANA_BEACH:
                    this.phaseWeeks = new int[][]{{1, 17, 24, 35, 46, 47, 50, 52},{18, 23, 36, 45, 48, 49}};
                    break;
                case SHERATON_PGA:
                    this.phaseWeeks = new int[][]{{1, 17, 40, 47, 51, 52}, {18, 34}, {35, 39, 48, 50}};
                    break;
                case WESTIN_KAANAPALI:
                case WESTIN_KAANAPALI_NORTH:
                case WESTIN_NANEA:
                case WESTIN_PRINCEVILLE:
                case SHERATON_KAUAI:
                    this.phaseWeeks = new int[][]{{1, 52}};
                    break;
                case SHERATON_BROADWAY: {
                    if (nameOfPhase == PhaseName.PLANTATION) {
                        this.phaseWeeks = new int[][]{{9, 43, 47, 47}, {1, 1, 7, 8, 44, 46, 48, 48, 51, 52}, {2, 6, 49, 50}};
                        break;
                    }
                    if (nameOfPhase == PhaseName.PALMETTO) {
                        this.phaseWeeks = new int[][]{{24, 35}, {15, 23, 36, 42}, {10, 14, 43, 52}, {1, 9}};
                        break;
                    }
                }
                case WESTIN_LAGUNAMAR:
                case WESTIN_CANCUN:
                    this.phaseWeeks = new int[][]{{5, 17, 24, 35, 44, 47, 51, 52}, {1, 4, 18, 23, 36, 43, 48, 50}};
                    break;
                case WESTIN_CABOS:
                    this.phaseWeeks = new int[][]{{1, 23, 44, 52}, {24, 33, 41, 43}, {34, 40}};
                    break;
                case HARBORSIDE_ATLANTIS:
                    this.phaseWeeks = new int[][]{{1, 17, 50, 52}, {18, 34, 47, 49}, {35, 46}};
                    break;
                case WESTIN_STJOHN: {
                    if (nameOfPhase == PhaseName.VIRGIN_GRAND) {
                        this.phaseWeeks = new int[][]{{1, 15, 51, 52},{16, 20, 43, 50},{21, 42}};
                    } else {
                        if (nameOfPhase == PhaseName.BAY_VISTA || nameOfPhase == PhaseName.CORAL_VISTA || nameOfPhase == PhaseName.SUNSET_BAY) {
                            this.phaseWeeks = new int[][]{{1, 18, 51, 52},{19, 33},{34, 50}};
                        }
                    }
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + resortKey + " and " + nameOfPhase);
            }
        }

        private void setPhaseVillasAmount(ResortCode resortKey, PhaseName nameOfPhase) {
            int amountOfVillas = 0;
            switch (resortKey) {
                case SHERATON_LAKESIDE:
                case VISTANA_BEACH:
                    amountOfVillas = 1;
                    break;
                case SHERATON_DESERT:
                case WESTIN_KIERLAND:
                case WESTIN_MISSION:
                case WESTIN_DESERT:
                case SHERATON_MOUNTAIN:
                case WESTIN_RIVERFRONT:
                case WESTIN_PRINCEVILLE:
                case WESTIN_LAGUNAMAR:
                case WESTIN_CANCUN:
                    amountOfVillas = 3;
                    break;
                case VISTANA_RESORT:
                case SHERATON_PGA:
                case SHERATON_KAUAI:
                case WESTIN_NANEA:
                    amountOfVillas = 4;
                    break;
                case HARBORSIDE_ATLANTIS:
                    amountOfVillas = 5;
                    break;
                case VISTANA_VILLAGES:
                case WESTIN_KAANAPALI:
                case WESTIN_KAANAPALI_NORTH:
                    amountOfVillas = 6;
                    break;
                case SHERATON_STEAMBOAT:
                    amountOfVillas = 12;
                    break;
                case SHERATON_BROADWAY: {
                    if (nameOfPhase == PhaseName.PLANTATION) {
                        amountOfVillas = 4;
                        break;
                    }
                    else {
                        if (nameOfPhase == PhaseName.PALMETTO) {
                            amountOfVillas = 5;
                            break;
                        }
                    }
                }
                case WESTIN_CABOS: {
                    if (nameOfPhase == PhaseName.MAIN_PHASE) {
                        amountOfVillas = 6;
                        break;
                    } else {
                        if (nameOfPhase == PhaseName.BAJA_POINT) {
                            amountOfVillas = 4;
                            break;
                        }
                    }
                }
                case WESTIN_STJOHN: {
                    if (nameOfPhase == PhaseName.VIRGIN_GRAND || nameOfPhase == PhaseName.CORAL_VISTA) {
                        amountOfVillas = 4;
                        break;
                    } else {
                        if (nameOfPhase == PhaseName.BAY_VISTA || nameOfPhase == PhaseName.SUNSET_BAY) {
                            amountOfVillas = 3;
                            break;
                        }
                    }
                }
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + resortKey + " and " + nameOfPhase);
            }
            this.phaseVillas = new Villa[amountOfVillas];
        }

        private void buildVillaList(ResortCode resortKey, PhaseName nameOfPhase) {
            switch (resortKey) {
                case SHERATON_DESERT:
                case WESTIN_KIERLAND:
                case WESTIN_MISSION:
                case WESTIN_DESERT:
                    this.phaseVillas = new Villa[]{
                            buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{148100,81000,56300})),
                            buildOneBedroom( "One-Bedroom Premium", buildSOChart(new int[]{81000,44000,30500})),
                            buildOneBedroom( "One-Bedroom", buildSOChart(new int[]{67100,37000,25800}))};
                    break;
                case SHERATON_LAKESIDE:
                    this.phaseVillas = new Villa[]{
                            buildTwoBedroom( "Two-Bedroom", buildSOChart(new int[]{129800,67100,37000}))};

                    break;
                case SHERATON_MOUNTAIN:
                    this.phaseVillas = new Villa[]{
                            buildTwoBedroom( "Two-Bedroom Lockoff", buildSOChart(new int[]{148100,81000,46500})),
                            buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{81000,44000,25800})),
                            buildOneBedroom("One-Bedroom", buildSOChart(new int[]{67100,37000,20700}))};
                    break;
                case WESTIN_RIVERFRONT:
                    this.phaseVillas = new Villa[]{
                            buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{148100,95700,46500})),
                            buildOneBedroom("One-Bedroom Premium",buildSOChart(new int[]{81000,51700,25800})),
                            buildOneBedroom("Studio Premium", buildSOChart(new int[]{67100,44000,20700}))};
                    break;
                case SHERATON_STEAMBOAT:
                    this.phaseVillas = new Villa[]{
                            buildFourBedroom("Four-Bedroom Lockoff", buildSOChart(new int[]{296200,191400,93000})),
                            buildThreeBedroom("Three-Bedroom Lockoff", buildSOChart(new int[]{215200,139700,67200})),
                            buildThreeBedroom("Three-Bedroom Lockoff (Mountain Side)", buildSOChart(new int[]{257700,196900,57700})),
                            buildThreeBedroom("Three-Bedroom", buildSOChart(new int[]{196900,125000,57700})),
                            buildTwoBedroom("Two-Bedroom Lockoff (Mountain Side)", buildSOChart(new int[]{176700,148100,46500})),
                            buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{148100,95700,46500})),
                            buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{148100,95700,46500})),
                            buildOneBedroom("One-Bedroom Premium (Mountain Side)", buildSOChart(new int[]{95700,81000,25800})),
                            buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{81000,51700,25800})),
                            buildOneBedroom("Studio Villa (Mountain Side)", buildSOChart(new int[]{81000,67100,20700})),
                            buildOneBedroom("Studio Villa", buildSOChart(new int[]{67100,44000,20700})),
                            buildOneBedroom("Hotel Room", buildSOChart(new int[]{50000,30000,15000}))};
                    break;
                case VISTANA_RESORT:
                    this.phaseVillas = new Villa[]{
                            buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{95700,81000})),
                            buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{81000,67100})),
                            buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{51700,44000})),
                            buildOneBedroom("One-Bedroom", buildSOChart(new int[]{44000,37000}))};
                    break;
                case VISTANA_VILLAGES:
                    this.phaseVillas = new Villa[]{
                            buildThreeBedroom("Three-Bedroom Lockoff (1BD Premium + Two 1BD)", buildSOChart(new int[]{139700,118000})),
                            buildThreeBedroom("Three-Bedroom Lockoff (2BD + 1BD)", buildSOChart(new int[]{125000,104100})),
                            buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{95700,81000})),
                            buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{81000,67100})),
                            buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{51700,44000})),
                            buildOneBedroom("One-Bedroom", buildSOChart(new int[]{44000,37000}))};
                    break;
                case SHERATON_PGA:
                    this.phaseVillas = new Villa[]{
                            buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{81000,56300,46500})),
                            buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{67100,44000,37000})),
                            buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{44000,30500,25800})),
                            buildOneBedroom("One-Bedroom", buildSOChart(new int[]{37000,25800,20700}))};
                    break;
                case VISTANA_BEACH:
                    this.phaseVillas = new Villa[]{
                            buildTwoBedroom( "Two-Bedroom", buildSOChart(new int[]{81000, 67100}))};
                    break;
                case WESTIN_PRINCEVILLE:
                    this.phaseVillas = new Villa[]{
                            buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{148100})),
                            buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{81000})),
                            buildOneBedroom("Studio Premium", buildSOChart(new int[]{67100}))};
                    break;
                case SHERATON_KAUAI:
                    this.phaseVillas = new Villa[]{
                            buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{148100})),
                            buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{148100})),
                            buildOneBedroom("One-Bedroom", buildSOChart(new int[]{81000})),
                            buildOneBedroom("Studio", buildSOChart(new int[]{67100}))};
                    break;
                case WESTIN_KAANAPALI:
                case WESTIN_KAANAPALI_NORTH:
                    this.phaseVillas = new Villa[]{
                            buildTwoBedroom("Two-Bedroom Lockoff (Oceanfront)", buildSOChart(new int[]{176700})),
                            buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{148100})),
                            buildOneBedroom("One-Bedroom Premium (Oceanfront)", buildSOChart(new int[]{95700})),
                            buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{81000})),
                            buildOneBedroom("Studio Premium (Oceanfront)", buildSOChart(new int[]{81000})),
                            buildOneBedroom("Studio Premium", buildSOChart(new int[]{67100}))};
                    break;
                case WESTIN_NANEA:
                    this.phaseVillas = new Villa[]{
                            buildThreeBedroom("Three-Bedroom (Oceanfront)", buildSOChart(new int[]{257700})),
                            buildTwoBedroom("Two-Bedroom Premium (Oceanfront)", buildSOChart(new int[]{176700})),
                            buildTwoBedroom("Two-Bedroom Premium (Resort View)", buildSOChart(new int[]{148100})),
                            buildOneBedroom("One-Bedroom Premium (Resort View)", buildSOChart(new int[]{81000}))};
                    break;
                case SHERATON_BROADWAY: {
                    if (nameOfPhase == PhaseName.PLANTATION) {
                        this.phaseVillas = new Villa[]{
                                buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{81000,56300,46500})),
                                buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{67100,44000,37000})),
                                buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{44000,30500,25800})),
                                buildOneBedroom("One-Bedroom", buildSOChart(new int[]{37000,25800,20700}))};
                    }
                    if (nameOfPhase == PhaseName.PALMETTO) {
                        this.phaseVillas = new Villa[]{
                                buildThreeBedroom("Three-Bedroom Lockoff", buildSOChart(new int[]{125000,104100,69800,57700})),
                                buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{95700,81000,56300,46500})),
                                buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{81000,67100,44000,37000})),
                                buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{51700,44000,30500,25800})),
                                buildOneBedroom("One-Bedroom", buildSOChart(new int[]{44000,37000,25800,20700}))};
                    }
                    break;
                }
                case WESTIN_LAGUNAMAR:
                    this.phaseVillas = new Villa[]{
                            buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{148100,81000})),
                            buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{81000,44000})),
                            buildOneBedroom("Studio Premium", buildSOChart(new int[]{67100,37000}))};
                    break;
                case WESTIN_CANCUN:
                    this.phaseVillas = new Villa[]{
                            buildThreeBedroom("Three-Bedroom (Oceanside or Lagoon Side)", buildSOChart(new int[]{215200,118000})),
                            buildTwoBedroom("Two-Bedroom (Oceanside or Lagoon Side)", buildSOChart(new int[]{148100,81000})),
                            buildOneBedroom("Studio (Oceanside or Lagoon Side)", buildSOChart(new int[]{67100,37000}))};
                    break;
                case WESTIN_CABOS: {
                    if (nameOfPhase == PhaseName.MAIN_PHASE) {
                        this.phaseVillas = new Villa[]{
                                buildThreeBedroom("Governor's Suite Lockoff", buildSOChart(new int[]{310900,243800,169700})),
                                buildTwoBedroom("Governor's Suite", buildSOChart(new int[]{243800,192100,132700})),
                                buildThreeBedroom("Three-Bedroom Lockoff", buildSOChart(new int[]{215200,176700,118000})),
                                buildTwoBedroom("Two-Bedroom Premium", buildSOChart(new int[]{176700,148100,95700})),
                                buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{148100,125000,81000})),
                                buildOneBedroom("Studio", buildSOChart(new int[]{67100,51700,37000}))};
                    }
                    if (nameOfPhase == PhaseName.BAJA_POINT) {
                        this.phaseVillas = new Villa[]{
                                buildThreeBedroom("Three-Bedroom Lockoff", buildSOChart(new int[]{257700,215200,139700})),
                                buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{176700,148100,95700})),
                                buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{95700,81000,51700})),
                                buildOneBedroom("Hotel Room", buildSOChart(new int[]{81000,67100,44000}))};
                    }
                    break;
                }
                case HARBORSIDE_ATLANTIS:
                    this.phaseVillas = new Villa[]{
                            buildThreeBedroom("Three-Bedroom Lockoff", buildSOChart(new int[]{196900,125000,104100})),
                            buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{148100,95700,81000})),
                            buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{129800,81000,67100})),
                            buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{81000,51700,44000})),
                            buildOneBedroom("One-Bedroom", buildSOChart(new int[]{67100,44000,37000}))};
                    break;
                case WESTIN_STJOHN: {
                    if (nameOfPhase == PhaseName.VIRGIN_GRAND) {
                        this.phaseVillas = new Villa[]{
                                buildThreeBedroom("Three-Bedroom", buildSOChart(new int[]{257700,196900,125000})),
                                buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{176700,148100,95700})),
                                buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{95700,81000,51700})),
                                buildOneBedroom("Studio Premium", buildSOChart(new int[]{81000,67100,44000}))};
                    }
                    if (nameOfPhase == PhaseName.BAY_VISTA) {
                        this.phaseVillas = new Villa[]{
                                buildThreeBedroom("Three-Bedroom", buildSOChart(new int[]{257700,196900,125000})),
                                buildTwoBedroom("Two-Bedroom Loft", buildSOChart(new int[]{176700,148100,95700})),
                                buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{176700,148100,95700}))};
                    }
                    if (nameOfPhase == PhaseName.CORAL_VISTA) {
                        this.phaseVillas = new Villa[]{
                                buildThreeBedroom("Three-Bedroom Lockoff", buildSOChart(new int[]{257700,196900,125000})),
                                buildTwoBedroom("Two-Bedroom Loft", buildSOChart(new int[]{176700,148100,95700})),
                                buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{176700,148100,95700})),
                                buildOneBedroom("Studio", buildSOChart(new int[]{81000,67100,44000}))};
                    }
                    if (nameOfPhase == PhaseName.SUNSET_BAY) {
                        this.phaseVillas = new Villa[]{
                                buildThreeBedroom("Three-Bedroom Lockoff", buildSOChart(new int[]{257700,196900,125000})),
                                buildTwoBedroom("Two-Bedroom Loft", buildSOChart(new int[]{176700,148100,95700})),
                                buildOneBedroom("Studio", buildSOChart(new int[]{81000,67100,44000}))};
                    }
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + resortKey);
            }
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

        private int[][] buildSOChart(int[] weeklyValuesList) {
            int[][] starOptionsChart = new int[weeklyValuesList.length][];
            for (int i = 0 ; i < weeklyValuesList.length; i++) {
                starOptionsChart[i] = getWeekValuesFor(weeklyValuesList[i]);
            }
            return starOptionsChart;
        }

        private Villa buildOneBedroom(String roomDescription, int[][] weeklyValuesList) {
            return new Villa(RoomType.ONE_BD, roomDescription, weeklyValuesList, this.phaseSeasons);
        }

        private Villa buildTwoBedroom(String roomDescription, int[][] weeklyValuesList) {
            return new Villa(RoomType.TWO_BD, roomDescription, weeklyValuesList, this.phaseSeasons);
        }

        private Villa buildThreeBedroom(String roomDescription, int[][] weeklyValuesList) {
            return new Villa(RoomType.THREE_BD, roomDescription, weeklyValuesList, this.phaseSeasons);
        }

        private Villa buildFourBedroom(String roomDescription, int[][] weeklyValuesList) {
            return new Villa(RoomType.FOUR_BD, roomDescription, weeklyValuesList, this.phaseSeasons);
        }
    }

    public static class Resort{

        private ResortCode resortName;
        private String resortLocation;
        private Phase[] resortPhases;

        private Resort(ResortCode resortKeyword) {

            initializeResortValues(resortKeyword);
        }

        private void initializeResortValues(ResortCode resortKey) {
            setResortName(resortKey);
            setResortLocation(resortKey);
            setResortPhases(resortKey);
        }

        private void setResortName(ResortCode resortKey) { this.resortName = resortKey; }

        private void setResortLocation(ResortCode resortKey) {
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

        private void setResortPhases(ResortCode resortKey) {
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
                    this.resortPhases = new Phase[]{new Phase(resortKey, PhaseName.MAIN_PHASE)};
                    break;
                case SHERATON_BROADWAY:
                    this.resortPhases = new Phase[]{
                            new Phase(resortKey, PhaseName.PLANTATION),
                            new Phase(resortKey, PhaseName.PALMETTO)};
                    break;
                case WESTIN_CABOS:
                    this.resortPhases = new Phase[]{
                            new Phase(resortKey, PhaseName.MAIN_PHASE),
                            new Phase(resortKey, PhaseName.BAJA_POINT)};
                    break;
                case WESTIN_STJOHN:
                    this.resortPhases = new Phase[]{
                            new Phase(resortKey, PhaseName.VIRGIN_GRAND),
                            new Phase(resortKey, PhaseName.BAY_VISTA),
                            new Phase(resortKey, PhaseName.CORAL_VISTA),
                            new Phase(resortKey, PhaseName.SUNSET_BAY)};
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + resortKey);
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
            if (!selectedPhaseName.equals(PhaseName.getPhaseText(PhaseName.MAIN_PHASE))) {
                result = result + " at " + selectedPhaseName;
            }
            return (result + " for " + getTotalStarOptionValue() + " StarOptions.");
        }
    }

    public static void main(String[] args) {

        new CalculatorWindow();
    }

    private static class CalculatorWindow extends JFrame implements ItemListener, ActionListener {

        JComboBox<String> pickResortList;
        JComboBox<java.io.Serializable> pickNightsList;
        JComboBox<java.io.Serializable> pickRoomList;
        JComboBox<java.io.Serializable> pickYearList;
        JComboBox<java.io.Serializable> pickMonthList;
        JComboBox<java.io.Serializable> pickDayList;
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
            pickedResort = ResortCode.getResortCode(pickResortList.getItemAt(0));
            pickedDay = Integer.parseInt(pickDayList.getItemAt(0).toString());
            pickedMonth = pickMonthList.getItemAt(0).toString();
            pickedYear = Year.of(Integer.parseInt(pickYearList.getItemAt(0).toString()));
            pickedRoomType = RoomType.getRoomType(pickRoomList.getItemAt(0).toString());
            pickedNights = Integer.parseInt(pickNightsList.getItemAt(0).toString());
        }

        private void addResortSelection(){
            ResortCode[] resortCodesList = ResortCode.values();
            pickResortList = new JComboBox<>();
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
            pickMonthList = new JComboBox<>();
            centerItemsInJComboBox(pickMonthList);
            for (int i = 0 ; i < Month.values().length; i++) {
                pickMonthList.addItem(Month.values()[i].toString());
            }
            pickMonthList.addItemListener( this);
            selectDatePanel.add(pickMonthList);
            pickDayList = new JComboBox<>();
            centerItemsInJComboBox(pickDayList);
            for (int i = 0; i < Month.valueOf(String.valueOf(pickMonthList.getItemAt(0))).maxLength(); i++) {
                pickDayList.addItem(i+1);
            }
            pickDayList.addItemListener( this);
            selectDatePanel.add(pickDayList);
            pickYearList = new JComboBox<>();
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
            pickNightsList = new JComboBox<>();
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
            pickRoomList = new JComboBox<>();
            centerItemsInJComboBox(pickRoomList);
            RoomType[] roomTypesList = RoomType.values();
            for (RoomType roomType : roomTypesList) {
                pickRoomList.addItem(RoomType.getVillaText(roomType));
            }
            pickRoomList.addItemListener( this);
            villaPanel.add(pickRoomList);
            searchPanel.add(villaPanel);
        }

        private void centerItemsInJComboBox(JComboBox<java.io.Serializable> chosenComboBox) {
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
                    pickedResort = ResortCode.getResortCode(Objects.requireNonNull(pickResortList.getSelectedItem()).toString());
                }
                if (source == pickNightsList) {
                    pickedNights = Integer.parseInt(Objects.requireNonNull(pickNightsList.getSelectedItem()).toString());
                }
                if (source == pickRoomList) {
                    pickedRoomType = RoomType.getRoomType(Objects.requireNonNull(pickRoomList.getSelectedItem()).toString());
                }
                if (source == pickYearList) {
                    pickedYear = Year.of(Integer.parseInt(Objects.requireNonNull(pickYearList.getSelectedItem()).toString()));
                    if (Month.valueOf(pickedMonth).length(pickedYear.isLeap()) != pickDayList.getItemCount()) {
                        pickDayList.removeAllItems();
                        for (int i = 1 ; i <= Month.valueOf(pickedMonth).length(pickedYear.isLeap()); i++) {
                            pickDayList.addItem(i);
                        }
                    }
                }
                if (source == pickMonthList) {
                    pickedMonth = Objects.requireNonNull(pickMonthList.getSelectedItem()).toString();
                    if (Month.valueOf(pickedMonth).length(pickedYear.isLeap()) != pickDayList.getItemCount()) {
                        pickDayList.removeAllItems();
                        for (int i = 1 ; i <= Month.valueOf(pickedMonth).length(pickedYear.isLeap()); i++) {
                            pickDayList.addItem(i);
                        }
                    }
                }
                if (source == pickDayList) {
                    pickedDay = Integer.parseInt(Objects.requireNonNull(pickDayList.getSelectedItem()).toString());
                }
            }
        }
    }
}
