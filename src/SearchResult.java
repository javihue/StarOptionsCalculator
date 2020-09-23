import java.time.LocalDate;

public class SearchResult {

    StarOptionsCalculator.ResortCode selectedResortName;
    StarOptionsCalculator.RoomType selectedTypeOfRoom;
    String selectedRoomDescription;
    String selectedPhaseName;
    LocalDate selectedCheckInDate;
    int selectedNumberOfNights;
    int[] totalStarOptionValues;
    StarOptionsCalculator.Season[] totalSeasonValues;

    SearchResult(ReservationQuery query) {

        selectedResortName = query.getChosenResort();
        selectedCheckInDate = query.getChosenCheckInDate();
        selectedNumberOfNights = query.getChosenNumberOfNights();
        selectedTypeOfRoom = query.getChosenTypeOfRoom();
    }

    void setTotalStarOptionValues(int[] starOptionValueChart) {
        this.totalStarOptionValues = starOptionValueChart;
    }

    void setTotalSeasonValues(StarOptionsCalculator.Season[] seasonValueChart) {
        this.totalSeasonValues = seasonValueChart;
    }

    void setSelectedRoomDescription(Villa selectedVilla) {
        this.selectedRoomDescription = selectedVilla.getVillaDescription();
    }

    void setSelectedPhase(Phase selectedPhase) {
        this.selectedPhaseName = selectedPhase.getPhaseName();
    }

    private int getTotalStarOptionValue() {
        int counter = 0;
        for (int totalStarOptionValue : this.totalStarOptionValues) {
            counter += totalStarOptionValue;
        }
        return counter;
    }

    String displaySearchResultLabel() {
        String result = selectedRoomDescription;
        if (!selectedPhaseName.equals(StarOptionsCalculator.PhaseName.getPhaseText(StarOptionsCalculator.PhaseName.MAIN_PHASE))) {
            result = result + " at " + selectedPhaseName;
        }
        return (result + " for " + getTotalStarOptionValue() + " StarOptions.");
    }
}