import java.time.DayOfWeek;

class Villa {
    private final String villaDescription;
    private final StarOptionsCalculator.RoomType villaSize;
    private final int[][] starOptionValues;
    private final StarOptionsCalculator.Season[] seasonsInVilla;

    Villa(StarOptionsCalculator.RoomType amountOfBedrooms, String villaDetails, int[][] starOptionsChart, StarOptionsCalculator.Season[] villaSeasons) {
        this.villaDescription = villaDetails;
        this.villaSize = amountOfBedrooms;
        this.starOptionValues = starOptionsChart;
        this.seasonsInVilla = villaSeasons;
    }

    StarOptionsCalculator.RoomType getVillaRoomType() {
        return villaSize;
    }

    String getVillaDescription(){
        return villaDescription;
    }

    int getStarOptionValue(StarOptionsCalculator.Season seasonChosen, DayOfWeek dayChosen){
        int starOptionValue = 0;
        if (seasonChosen == StarOptionsCalculator.Season.NO_SEASON) {
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
