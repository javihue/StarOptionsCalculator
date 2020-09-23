import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

class ReservationQuery {

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

    private static LocalDate setFirstFridayOfYear(int chosenYear) {

        LocalDate beginDate = setBeginDateOfYear(chosenYear);

        while (beginDate.getDayOfWeek() != DayOfWeek.FRIDAY) {
            beginDate = beginDate.plusDays(1);
        }
        return beginDate;
    }

    private static LocalDate setBeginDateOfYear(int chosenYear) {return LocalDate.of(chosenYear, 1, 1); }

    static private final int MIN_NIGHT_SEARCH = 1;
    static private final int MAX_NIGHT_SEARCH = 21;

    private final StarOptionsCalculator.ResortCode chosenResort;
    private final LocalDate chosenCheckInDate;
    private final int chosenNumberOfNights;
    private final StarOptionsCalculator.RoomType chosenTypeOfRoom;

    ReservationQuery(StarOptionsCalculator.ResortCode resortChosen, LocalDate checkInDateChosen, int numberOfNightsChosen, StarOptionsCalculator.RoomType typeOfRoomChosen) {
        chosenResort = resortChosen;
        chosenCheckInDate = checkInDateChosen;
        chosenTypeOfRoom = typeOfRoomChosen;
        if (numberOfNightsChosen >= MIN_NIGHT_SEARCH && numberOfNightsChosen <= MAX_NIGHT_SEARCH)
            chosenNumberOfNights = numberOfNightsChosen;
        else
            throw new IllegalStateException("Unexpected value: " + numberOfNightsChosen);
    }

    StarOptionsCalculator.ResortCode getChosenResort() { return chosenResort; }

    LocalDate getChosenCheckInDate() { return chosenCheckInDate; }

    int getChosenNumberOfNights() { return chosenNumberOfNights; }

    StarOptionsCalculator.RoomType getChosenTypeOfRoom() { return chosenTypeOfRoom; }

    ArrayList<SearchResult> getSearchResultsList() {

        ArrayList<SearchResult> searchResultsList = new ArrayList<>();
        Resort resortSearched = new Resort(chosenResort);
        int[] starOptionValueResultList = new int[this.chosenNumberOfNights];
        StarOptionsCalculator.Season[] seasonValueResultList = new StarOptionsCalculator.Season[this.chosenNumberOfNights];
        LocalDate dateSearched = getChosenCheckInDate();

        for (int i = 0 ; i < resortSearched.getResortPhases().length ; i++) {
            for (int j = 0 ; j < resortSearched.getResortPhases()[i].phaseVillas.length ; j++) {
                if (resortSearched.getResortPhases()[i].phaseVillas[j].getVillaRoomType() == getChosenTypeOfRoom()) {
                    SearchResult newResult = new SearchResult(this);
                    for (int k = 0 ; k < this.chosenNumberOfNights ; k++) {
                        seasonValueResultList[k] = resortSearched.getResortPhases()[i].getSeasonOfPhase(getWeekNumberOfDate(dateSearched));
                        starOptionValueResultList[k] = resortSearched.getResortPhases()[i].phaseVillas[j].getStarOptionValue(seasonValueResultList[k], dateSearched.getDayOfWeek());
                        dateSearched = dateSearched.plusDays(1);

                    }
                    newResult.setSelectedRoomDescription(resortSearched.getResortPhases()[i].phaseVillas[j]);
                    newResult.setSelectedPhase(resortSearched.getResortPhases()[i]);
                    newResult.setTotalSeasonValues(seasonValueResultList);
                    newResult.setTotalStarOptionValues(starOptionValueResultList);
                    searchResultsList.add(newResult);
                    dateSearched = getChosenCheckInDate();
                    starOptionValueResultList = new int[this.chosenNumberOfNights];
                    seasonValueResultList = new StarOptionsCalculator.Season[this.chosenNumberOfNights];
                }
            }
        }
        return searchResultsList;
    }
}