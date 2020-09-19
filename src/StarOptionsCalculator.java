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
    static private final int EARLIEST_YR_FOR_SEARCH = 2000;
    static private final int LATEST_YR_FOR_SEARCH = 2031;

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

        private void displayVillaDetails(){
            System.out.println("Villa description: " + this.villaDescription);
            System.out.println("Villa type: " + RoomType.getVillaText(this.villaSize));
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

            this.resortName = ResortCode.getResortText(resortKeyword);

            switch(resortKeyword) {
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
            System.out.println("Result: " + ResortCode.getResortText(selectedResortName) +
                    ", " + RoomType.getVillaText(selectedTypeOfRoom) + ", " + selectedRoomDescription +
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
            System.out.println("Displaying a total of " + currentResults.size() + " results in Window...");
            resultsPanel.removeAll();
            resultsPanel.setLayout(new GridLayout(9,1,5,5));
            resultsPanel.add(new JLabel("Total " + currentResults.size() + " matches found."));
            if (currentResults.size() == 0) {
                resultsPanel.add(new JLabel(ResortCode.getResortText(pickedResort) + " has no " +
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
                System.out.println("Search begins for " + ResortCode.getResortText(currentQuery.getChosenResort()) + ", checking in on " +
                    currentQuery.getChosenCheckInDate().toString() + " for a total of " + currentQuery.getChosenNumberOfNights() +
                    " nights(s) on a " + RoomType.getVillaText(currentQuery.getChosenTypeOfRoom()));
            }
            for (SearchResult searchResult : currentResults) searchResult.displaySearchResult();
            displayResultsOnWindow();
        }

        @Override
        public void itemStateChanged(ItemEvent itemEvent) {
            System.out.println("Item State Change Detected on: " + itemEvent.toString());
            Object source = itemEvent.getSource();
            if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                if (source == pickResortList) {
                    pickedResort = ResortCode.getResortCode(pickResortList.getSelectedItem().toString());
                    System.out.println("Selected Resort is: " + pickResortList.getSelectedItem().toString());
                    System.out.println("Selected Resort Index is: " + pickResortList.getSelectedIndex());
                    System.out.println("Picked Resort is:" + ResortCode.getResortText(pickedResort));
                }
                if (source == pickNightsList) {
                    pickedNights = Integer.parseInt(pickNightsList.getSelectedItem().toString());
                    System.out.println("Selected Night Amount is: " + pickNightsList.getSelectedItem().toString());
                    System.out.println("Selected Night Amount Index is: " + pickNightsList.getSelectedIndex());
                    System.out.println("Picked Night Amount is:" + pickedNights);
                }
                if (source == pickRoomList) {
                    pickedRoomType = RoomType.getRoomType(pickRoomList.getSelectedItem().toString());
                    System.out.println("Selected Room is: " + (pickRoomList.getSelectedItem()).toString());
                    System.out.println("Selected Room Index is: " + pickRoomList.getSelectedIndex());
                    System.out.println("Picked Room is:" + RoomType.getVillaText(pickedRoomType));
                }
                if (source == pickYearList) {
                    pickedYear = Year.of(Integer.parseInt(pickYearList.getSelectedItem().toString()));
                    System.out.println("Selected Year is: " + (pickYearList.getSelectedItem()).toString());
                    System.out.println("Selected Year Index is: " + pickYearList.getSelectedIndex());
                    System.out.println("Picked Year is:" + pickedYear.toString());
                    if (Month.valueOf(pickedMonth).length(pickedYear.isLeap()) != pickDayList.getItemCount()) {
                        System.out.println("Amount of days are not equal.");
                        pickDayList.removeAllItems();
                        System.out.println("Adjusted Days List to " + pickDayList.getItemCount());
                        for (int i = 1 ; i <= Month.valueOf(pickedMonth).length(pickedYear.isLeap()); i++) {
                            pickDayList.addItem(i);
                        }
                        System.out.println("Days JComboBox now currently has " + pickDayList.getItemCount() + " days ");
                    }
                }
                if (source == pickMonthList) {
                    pickedMonth = pickMonthList.getSelectedItem().toString();
                    System.out.println("Selected Month is: " + (pickMonthList.getSelectedItem()).toString());
                    System.out.println("Selected Month Index is: " + pickMonthList.getSelectedIndex());
                    System.out.println("Picked Month is: " + pickedMonth);
                    System.out.println("Selected month of " + pickedMonth + " has " + Month.valueOf(pickedMonth).maxLength() + " days ");
                    System.out.println("Days JComboBox currently has " + pickDayList.getItemCount() + " days ");
                    if (Month.valueOf(pickedMonth).length(pickedYear.isLeap()) != pickDayList.getItemCount()) {
                        System.out.println("Amount of days are not equal.");
                        pickDayList.removeAllItems();
                        System.out.println("Adjusted Days List to " + pickDayList.getItemCount());
                        for (int i = 1 ; i <= Month.valueOf(pickedMonth).length(pickedYear.isLeap()); i++) {
                            pickDayList.addItem(i);
                        }
                        System.out.println("Days JComboBox now currently has " + pickDayList.getItemCount() + " days ");
                    }
                }
                if (source == pickDayList) {
                    pickedDay = Integer.parseInt(pickDayList.getSelectedItem().toString());
                    System.out.println("Selected Day is: " + (pickDayList.getSelectedItem()).toString());
                    System.out.println("Selected Day Index is: " + pickDayList.getSelectedIndex());
                    System.out.println("Picked Day is: " + pickedDay);
                }
            }
        }
    }
}
