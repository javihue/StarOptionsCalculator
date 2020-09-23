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

        static String getPhaseText(PhaseName phaseCode) {
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
            for (int i = 1 ; i <= 21; i++) {
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
