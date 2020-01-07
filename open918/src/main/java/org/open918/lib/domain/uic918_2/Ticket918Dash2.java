package org.open918.lib.domain.uic918_2;

import org.open918.lib.domain.Ticket;
import org.open918.lib.domain.TicketStandard;

/**
 * Created by joelhaasnoot on 21/11/2016.
 */
public class Ticket918Dash2 implements Ticket {

    private int version;
    private int issuerCode;
    private int id;
    private int ticketTypeCode;
    private TicketType type;

    private int numberOfAdults;
    private int numberOfChildren;

    private boolean isSpecimen = false;
    private int travelClass;
    private String ticketNumber;
    private int issuedYear;
    private int issuedDay;

    // IRT
    private int subType;
    private int departureDate;
    private int departureTime;
    private String trainNumber;
    private int coachNumber;
    private String seatNumber;
    private boolean overbookingIndicator;

    // NRT
    private boolean isReturn = false;
    private int validityFirst;
    private int validityLast;
    private int stationCodeNumOrAlpha; // IRT + NRT
    private int stationCodeListType; // IRT + NRT

    private int departureStation;
    private int arrivalStation;
    private String departureStationString;
    private String arrivalStationString;

    // GRT
    private String groupLeaderName;
    private int countermarkNumber;

    // RPT
    private int daysOfTravel;
    private int country1;
    private int country2;
    private int country3;
    private int country4;
    private int country5;
    private boolean hasSecondPage;

    // General fields
    int informationMessages;
    String openText;


    @Override
    public TicketStandard getStandard() {
        return TicketStandard.TICKET918_2;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getIssuerCode() {
        return issuerCode;
    }

    public void setIssuerCode(int issuerCode) {
        this.issuerCode = issuerCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTicketTypeCode() {
        return ticketTypeCode;
    }

    public void setTicketTypeCode(int ticketTypeCode) {
        this.ticketTypeCode = ticketTypeCode;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public boolean isSpecimen() {
        return isSpecimen;
    }

    public void setSpecimen(boolean specimen) {
        isSpecimen = specimen;
    }

    public int getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(int travelClass) {
        this.travelClass = travelClass;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getIssuedYear() {
        return issuedYear;
    }

    public void setIssuedYear(int issuedYear) {
        this.issuedYear = issuedYear;
    }

    public int getIssuedDay() {
        return issuedDay;
    }

    public void setIssuedDay(int issuedDay) {
        this.issuedDay = issuedDay;
    }

    public boolean isReturn() {
        return isReturn;
    }

    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    public int getValidityFirst() {
        return validityFirst;
    }

    public void setValidityFirst(int validityFirst) {
        this.validityFirst = validityFirst;
    }

    public int getValidityLast() {
        return validityLast;
    }

    public void setValidityLast(int validityLast) {
        this.validityLast = validityLast;
    }

    public int getStationCodeNumOrAlpha() {
        return stationCodeNumOrAlpha;
    }

    public void setStationCodeNumOrAlpha(int stationCodeNumOrAlpha) {
        this.stationCodeNumOrAlpha = stationCodeNumOrAlpha;
    }

    public int getStationCodeListType() {
        return stationCodeListType;
    }

    public void setStationCodeListType(int stationCodeListType) {
        this.stationCodeListType = stationCodeListType;
    }

    public int getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(int departureStation) {
        this.departureStation = departureStation;
    }

    public int getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(int arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public String getDepartureStationString() {
        return departureStationString;
    }

    public void setDepartureStationString(String departureStationString) {
        this.departureStationString = departureStationString;
    }

    public String getArrivalStationString() {
        return arrivalStationString;
    }

    public void setArrivalStationString(String arrivalStationString) {
        this.arrivalStationString = arrivalStationString;
    }

    public int getInformationMessages() {
        return informationMessages;
    }

    public void setInformationMessages(int informationMessages) {
        this.informationMessages = informationMessages;
    }

    public String getOpenText() {
        return openText;
    }

    public void setOpenText(String openText) {
        this.openText = openText;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getSubType() {
        return subType;
    }

    public void setDepartureDate(int departureDate) {
        this.departureDate = departureDate;
    }

    public int getDepartureDate() {
        return departureDate;
    }

    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }


    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setCoachNumber(int coachNumber) {
        this.coachNumber = coachNumber;
    }

    public int getCoachNumber() {
        return coachNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setOverbookingIndicator(boolean overbookingIndicator) {
        this.overbookingIndicator = overbookingIndicator;
    }

    public boolean getOverbookingIndicator() {
        return overbookingIndicator;
    }


    public void setGroupLeaderName(String groupLeaderName) {
        this.groupLeaderName = groupLeaderName;
    }

    public void setCountermarkNumber(int countermarkNumber) {
        this.countermarkNumber = countermarkNumber;
    }

    public int getCountermarkNumber() {
        return countermarkNumber;
    }

    public void setDaysOfTravel(int daysOfTravel) {
        this.daysOfTravel = daysOfTravel;
    }

    public int getDaysOfTravel() {
        return daysOfTravel;
    }

    public void setCountry1(int country1) {
        this.country1 = country1;
    }

    public int getCountry1() {
        return country1;
    }

    public void setCountry2(int country2) {
        this.country2 = country2;
    }

    public int getCountry2() {
        return country2;
    }

    public void setCountry3(int country3) {
        this.country3 = country3;
    }

    public void setCountry4(int country4) {
        this.country4 = country4;
    }

    public void setCountry5(int country5) {
        this.country5 = country5;
    }

    public void hadSecondPage(boolean b) {
        this.hasSecondPage = b;
    }
}
