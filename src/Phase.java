public class Phase {
    private StarOptionsCalculator.PhaseName phaseName;
    private StarOptionsCalculator.Season[] phaseSeasons;
    private int[][] phaseWeeks;
    Villa[] phaseVillas;

    Phase(StarOptionsCalculator.ResortCode resortKey, StarOptionsCalculator.PhaseName nameOfPhase) {
        initializePhaseValues(resortKey, nameOfPhase);
        buildVillaList(resortKey, nameOfPhase);
    }

    String getPhaseName(){return StarOptionsCalculator.PhaseName.getPhaseText(phaseName);}

    StarOptionsCalculator.Season getSeasonOfPhase(int weekNumber) {
        StarOptionsCalculator.Season result = StarOptionsCalculator.Season.NO_SEASON;
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

    private void initializePhaseValues(StarOptionsCalculator.ResortCode resortKey, StarOptionsCalculator.PhaseName nameOfPhase) {
        setPhaseName(nameOfPhase);
        setPhaseSeasons(resortKey, nameOfPhase);
        setPhaseWeeks(resortKey, nameOfPhase);
        setPhaseVillasAmount(resortKey, nameOfPhase);
    }

    private void setPhaseName(StarOptionsCalculator.PhaseName nameOfPhase) { this.phaseName = nameOfPhase; }

    private void setPhaseSeasons(StarOptionsCalculator.ResortCode resortKey, StarOptionsCalculator.PhaseName nameOfPhase) {
        switch (resortKey) {
            case SHERATON_DESERT:
            case WESTIN_KIERLAND:
            case WESTIN_MISSION:
            case WESTIN_DESERT:
                this.phaseSeasons = new StarOptionsCalculator.Season[]{StarOptionsCalculator.Season.PLATINUM_PLUS, StarOptionsCalculator.Season.GOLD_PLUS, StarOptionsCalculator.Season.GOLD};
                break;
            case SHERATON_LAKESIDE:
            case SHERATON_MOUNTAIN:
                this.phaseSeasons = new StarOptionsCalculator.Season[]{StarOptionsCalculator.Season.PLATINUM_PLUS, StarOptionsCalculator.Season.GOLD_PLUS, StarOptionsCalculator.Season.SILVER};
                break;
            case WESTIN_RIVERFRONT:
            case SHERATON_STEAMBOAT:
                this.phaseSeasons = new StarOptionsCalculator.Season[]{StarOptionsCalculator.Season.PLATINUM_PLUS, StarOptionsCalculator.Season.PLATINUM, StarOptionsCalculator.Season.SILVER};
                break;
            case VISTANA_RESORT:
            case VISTANA_VILLAGES:
            case VISTANA_BEACH:
                this.phaseSeasons = new StarOptionsCalculator.Season[]{StarOptionsCalculator.Season.PLATINUM, StarOptionsCalculator.Season.GOLD_PLUS};
                break;
            case SHERATON_PGA:
                this.phaseSeasons = new StarOptionsCalculator.Season[]{StarOptionsCalculator.Season.GOLD_PLUS, StarOptionsCalculator.Season.GOLD, StarOptionsCalculator.Season.SILVER};
                break;
            case WESTIN_PRINCEVILLE:
            case SHERATON_KAUAI:
            case WESTIN_KAANAPALI:
            case WESTIN_KAANAPALI_NORTH:
            case WESTIN_NANEA:
                this.phaseSeasons = new StarOptionsCalculator.Season[]{StarOptionsCalculator.Season.PLATINUM_PLUS};
                break;
            case SHERATON_BROADWAY: {
                if (nameOfPhase == StarOptionsCalculator.PhaseName.PLANTATION) {
                    this.phaseSeasons = new StarOptionsCalculator.Season[]{StarOptionsCalculator.Season.GOLD_PLUS, StarOptionsCalculator.Season.GOLD, StarOptionsCalculator.Season.SILVER};
                }
                if (nameOfPhase == StarOptionsCalculator.PhaseName.PALMETTO) {
                    this.phaseSeasons = new StarOptionsCalculator.Season[]{StarOptionsCalculator.Season.PLATINUM, StarOptionsCalculator.Season.GOLD_PLUS, StarOptionsCalculator.Season.GOLD, StarOptionsCalculator.Season.SILVER};
                }
                break;
            }
            case WESTIN_LAGUNAMAR:
            case WESTIN_CANCUN:
                this.phaseSeasons = new StarOptionsCalculator.Season[]{StarOptionsCalculator.Season.PLATINUM_PLUS, StarOptionsCalculator.Season.GOLD_PLUS};
                break;
            case WESTIN_CABOS:
            case HARBORSIDE_ATLANTIS:
            case WESTIN_STJOHN:
                this.phaseSeasons = new StarOptionsCalculator.Season[]{StarOptionsCalculator.Season.PLATINUM_PLUS, StarOptionsCalculator.Season.PLATINUM, StarOptionsCalculator.Season.GOLD_PLUS};
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + resortKey);
        }
    }

    private void setPhaseWeeks(StarOptionsCalculator.ResortCode resortKey, StarOptionsCalculator.PhaseName nameOfPhase) {
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
                if (nameOfPhase == StarOptionsCalculator.PhaseName.PLANTATION) {
                    this.phaseWeeks = new int[][]{{9, 43, 47, 47}, {1, 1, 7, 8, 44, 46, 48, 48, 51, 52}, {2, 6, 49, 50}};
                    break;
                }
                if (nameOfPhase == StarOptionsCalculator.PhaseName.PALMETTO) {
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
                if (nameOfPhase == StarOptionsCalculator.PhaseName.VIRGIN_GRAND) {
                    this.phaseWeeks = new int[][]{{1, 15, 51, 52},{16, 20, 43, 50},{21, 42}};
                } else {
                    if (nameOfPhase == StarOptionsCalculator.PhaseName.BAY_VISTA || nameOfPhase == StarOptionsCalculator.PhaseName.CORAL_VISTA || nameOfPhase == StarOptionsCalculator.PhaseName.SUNSET_BAY) {
                        this.phaseWeeks = new int[][]{{1, 18, 51, 52},{19, 33},{34, 50}};
                    }
                }
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + resortKey + " and " + nameOfPhase);
        }
    }

    private void setPhaseVillasAmount(StarOptionsCalculator.ResortCode resortKey, StarOptionsCalculator.PhaseName nameOfPhase) {
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
                if (nameOfPhase == StarOptionsCalculator.PhaseName.PLANTATION) {
                    amountOfVillas = 4;
                    break;
                }
                else {
                    if (nameOfPhase == StarOptionsCalculator.PhaseName.PALMETTO) {
                        amountOfVillas = 5;
                        break;
                    }
                }
            }
            case WESTIN_CABOS: {
                if (nameOfPhase == StarOptionsCalculator.PhaseName.MAIN_PHASE) {
                    amountOfVillas = 6;
                    break;
                } else {
                    if (nameOfPhase == StarOptionsCalculator.PhaseName.BAJA_POINT) {
                        amountOfVillas = 4;
                        break;
                    }
                }
            }
            case WESTIN_STJOHN: {
                if (nameOfPhase == StarOptionsCalculator.PhaseName.VIRGIN_GRAND || nameOfPhase == StarOptionsCalculator.PhaseName.CORAL_VISTA) {
                    amountOfVillas = 4;
                    break;
                } else {
                    if (nameOfPhase == StarOptionsCalculator.PhaseName.BAY_VISTA || nameOfPhase == StarOptionsCalculator.PhaseName.SUNSET_BAY) {
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

    private void buildVillaList(StarOptionsCalculator.ResortCode resortKey, StarOptionsCalculator.PhaseName nameOfPhase) {
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
                if (nameOfPhase == StarOptionsCalculator.PhaseName.PLANTATION) {
                    this.phaseVillas = new Villa[]{
                            buildTwoBedroom("Two-Bedroom Lockoff", buildSOChart(new int[]{81000,56300,46500})),
                            buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{67100,44000,37000})),
                            buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{44000,30500,25800})),
                            buildOneBedroom("One-Bedroom", buildSOChart(new int[]{37000,25800,20700}))};
                }
                if (nameOfPhase == StarOptionsCalculator.PhaseName.PALMETTO) {
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
                if (nameOfPhase == StarOptionsCalculator.PhaseName.MAIN_PHASE) {
                    this.phaseVillas = new Villa[]{
                            buildThreeBedroom("Governor's Suite Lockoff", buildSOChart(new int[]{310900,243800,169700})),
                            buildTwoBedroom("Governor's Suite", buildSOChart(new int[]{243800,192100,132700})),
                            buildThreeBedroom("Three-Bedroom Lockoff", buildSOChart(new int[]{215200,176700,118000})),
                            buildTwoBedroom("Two-Bedroom Premium", buildSOChart(new int[]{176700,148100,95700})),
                            buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{148100,125000,81000})),
                            buildOneBedroom("Studio", buildSOChart(new int[]{67100,51700,37000}))};
                }
                if (nameOfPhase == StarOptionsCalculator.PhaseName.BAJA_POINT) {
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
                if (nameOfPhase == StarOptionsCalculator.PhaseName.VIRGIN_GRAND) {
                    this.phaseVillas = new Villa[]{
                            buildThreeBedroom("Three-Bedroom", buildSOChart(new int[]{257700,196900,125000})),
                            buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{176700,148100,95700})),
                            buildOneBedroom("One-Bedroom Premium", buildSOChart(new int[]{95700,81000,51700})),
                            buildOneBedroom("Studio Premium", buildSOChart(new int[]{81000,67100,44000}))};
                }
                if (nameOfPhase == StarOptionsCalculator.PhaseName.BAY_VISTA) {
                    this.phaseVillas = new Villa[]{
                            buildThreeBedroom("Three-Bedroom", buildSOChart(new int[]{257700,196900,125000})),
                            buildTwoBedroom("Two-Bedroom Loft", buildSOChart(new int[]{176700,148100,95700})),
                            buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{176700,148100,95700}))};
                }
                if (nameOfPhase == StarOptionsCalculator.PhaseName.CORAL_VISTA) {
                    this.phaseVillas = new Villa[]{
                            buildThreeBedroom("Three-Bedroom Lockoff", buildSOChart(new int[]{257700,196900,125000})),
                            buildTwoBedroom("Two-Bedroom Loft", buildSOChart(new int[]{176700,148100,95700})),
                            buildTwoBedroom("Two-Bedroom", buildSOChart(new int[]{176700,148100,95700})),
                            buildOneBedroom("Studio", buildSOChart(new int[]{81000,67100,44000}))};
                }
                if (nameOfPhase == StarOptionsCalculator.PhaseName.SUNSET_BAY) {
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
        return new Villa(StarOptionsCalculator.RoomType.ONE_BD, roomDescription, weeklyValuesList, this.phaseSeasons);
    }

    private Villa buildTwoBedroom(String roomDescription, int[][] weeklyValuesList) {
        return new Villa(StarOptionsCalculator.RoomType.TWO_BD, roomDescription, weeklyValuesList, this.phaseSeasons);
    }

    private Villa buildThreeBedroom(String roomDescription, int[][] weeklyValuesList) {
        return new Villa(StarOptionsCalculator.RoomType.THREE_BD, roomDescription, weeklyValuesList, this.phaseSeasons);
    }

    private Villa buildFourBedroom(String roomDescription, int[][] weeklyValuesList) {
        return new Villa(StarOptionsCalculator.RoomType.FOUR_BD, roomDescription, weeklyValuesList, this.phaseSeasons);
    }
}