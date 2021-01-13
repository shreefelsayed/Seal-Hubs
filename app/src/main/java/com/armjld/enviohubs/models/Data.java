package com.armjld.enviohubs.models;

public class Data {

    public static String removed = "false";
    // Pick up Data
    private String mPAddress;
    private String mPShop;
    private String txtPState;
    private String mPRegion;
    private String date;
    private String id;
    private String uId;
    // Drop Data
    private String txtDState;
    private String DAddress;
    private String DDate;
    private String DPhone;
    private String DName;
    private String mDRegion;
    // Money Data
    private String GMoney;
    private String owner = "";
    private String GGet;
    // Transportation
    private String isTrans;
    private String isMetro;
    private String isMotor;
    private String isCar;
    private String statue;
    private String uAccepted;
    private String srated;
    private String srateid;
    private String pDate = "";
    private String type = "Normal";
    private String packType = "";
    private String packWeight = "";
    private String lat = "";
    private String _long = "";
    private String drated;
    private String drateid;
    private String dilverTime;
    private String acceptedTime;
    private String lastedit;
    private String notes;
    private String priority = "1";
    private String trackid = "";
    private String provider = "Esh7nly";

    private String dHub = ""; // Should be assigned Automaticlly
    private String pHub = ""; // Should be assigned Automaticlly
    private String dHubName = ""; // Should be assigned Automaticlly
    private String pHubName = ""; // Should be assigned Automaticlly

    private String pSupervisor = "";
    private String dSupervisor = "";
    private String tries = "0";


    public Data() {
    }

    public Data(String txtPState, String mPRegion, String mPAddress, String mPShop, String txtDState, String mDRegion, String DAddress, String DDate, String DPhone, String DName,
                String GMoney, String GGet, String date, String id, String uId, String isTrans, String isMetro, String isMotor, String isCar, String statue, String uAccepted, String srated,
                String srateid, String drated, String drateid, String acceptedTime, String dilverTime, String notes, String type, String owner, String pDate, String packWeight, String packType, String lat, String _long, String trackid) {

        //PICK
        this.txtPState = txtPState;
        this.mPRegion = mPRegion;
        this.mPAddress = mPAddress;
        this.mPShop = mPShop;
        this.notes = notes;
        this.packType = packType;
        this.packWeight = packWeight;
        this.acceptedTime = acceptedTime;
        this.dilverTime = dilverTime;
        this.owner = owner;
        //DROP
        this.txtDState = txtDState;
        this.mDRegion = mDRegion;
        this.DAddress = DAddress;
        this.DDate = DDate;
        this.DPhone = DPhone;
        this.DName = DName;
        this.GMoney = GMoney;
        this.GGet = GGet;
        this.date = date;
        this.pDate = pDate;

        // ids
        this.id = id;
        this.uId = uId;

        //Transportation
        this.isTrans = isTrans;
        this.isMetro = isMetro;
        this.isMotor = isMotor;
        this.isCar = isCar;

        //order statue
        this.statue = statue;
        this.uAccepted = uAccepted;

        this.srated = srated;
        this.srateid = srateid;

        this.drated = drated;
        this.drateid = drateid;
        this.type = type;
        this.lat = lat;
        this._long = _long;
        this.trackid = trackid;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String get_long() {
        return _long;
    }

    public void set_long(String _long) {
        this._long = _long;
    }

    public String getTrackid() {
        return trackid;
    }

    public void setTrackid(String trackid) {
        this.trackid = trackid;
    }

    public String getRemoved() {
        return removed;
    }

    public void setRemoved(String removed) {
        this.removed = removed;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getPackWeight() {
        return packWeight;
    }

    public void setPackWeight(String packWeight) {
        this.packWeight = packWeight;
    }

    public String getTxtPState() {
        return txtPState;
    }

    public void setTxtPState(String txtPState) {
        this.txtPState = txtPState;
    }

    public String getmPAddress() {
        return mPAddress;
    }

    public void setmPAddress(String mPAddress) {
        this.mPAddress = mPAddress;
    }

    public String getmPShop() {
        return mPShop;
    }

    public void setmPShop(String mPShop) {
        this.mPShop = mPShop;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String trans() {
        String trans = "( " + isCar + " " + isMetro + " " + isTrans + " " + isMotor + " )";
        return trans;
    }

    public String reStateP() {
        String reStateP = txtPState + " - " + mPRegion;
        return reStateP;
    }

    public String reStateD() {
        String reStateD = txtDState + " - " + mDRegion;
        return reStateD;
    }

    public String getDilverTime() {
        return dilverTime;
    }

    public void setDilverTime(String dilverTime) {
        this.dilverTime = dilverTime;
    }

    public String getAcceptedTime() {
        return acceptedTime;
    }

    public void setAcceptedTime(String acceptedTime) {
        this.acceptedTime = acceptedTime;
    }

    public String getLastedit() {
        return lastedit;
    }

    public void setLastedit(String lastedit) {
        this.lastedit = lastedit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDAddress() {
        return DAddress;
    }

    public void setDAddress(String DAddress) {
        this.DAddress = DAddress;
    }

    public String getTxtDState() {
        return txtDState;
    }

    public void setTxtDState(String txtDState) {
        this.txtDState = txtDState;
    }

    public String getDDate() {
        return DDate;
    }

    public void setDDate(String DDate) {
        this.DDate = DDate;
    }

    public String getDPhone() {
        return DPhone;
    }

    public void setDPhone(String DPhone) {
        this.DPhone = DPhone;
    }

    public String getDName() {
        return DName;
    }

    public void setDName(String DName) {
        this.DName = DName;
    }

    public String getGMoney() {
        return GMoney;
    }

    public void setGMoney(String GMoney) {
        this.GMoney = GMoney;
    }

    public String getGGet() {
        return GGet;
    }

    public void setGGet(String GGet) {
        this.GGet = GGet;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getIsTrans() {
        return isTrans;
    }

    public void setIsTrans(String isTrans) {
        this.isTrans = isTrans;
    }

    public String getIsMetro() {
        return isMetro;
    }

    public void setIsMetro(String isMetro) {
        this.isMetro = isMetro;
    }

    public String getTries() {
        return tries;
    }

    public void setTries(String tries) {
        this.tries = tries;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getIsMotor() {
        return isMotor;
    }

    public void setIsMotor(String isMotor) {
        this.isMotor = isMotor;
    }

    public String getIsCar() {
        return isCar;
    }

    public void setIsCar(String isCar) {
        this.isCar = isCar;
    }

    public String getmPRegion() {
        return mPRegion;
    }

    public void setmPRegion(String mPRegion) {
        this.mPRegion = mPRegion;
    }

    public String getmDRegion() {
        return mDRegion;
    }

    public void setmDRegion(String mDRegion) {
        this.mDRegion = mDRegion;
    }

    public String getuAccepted() {
        return uAccepted;
    }

    public void setuAccepted(String uAccepted) {
        this.uAccepted = uAccepted;
    }

    public String getSrated() {
        return srated;
    }

    public void setSrated(String srated) {
        this.srated = srated;
    }

    public String getSrateid() {
        return srateid;
    }

    public void setSrateid(String srateid) {
        this.srateid = srateid;
    }

    public String getDrated() {
        return drated;
    }

    public void setDrated(String drated) {
        this.drated = drated;
    }

    public String getDrateid() {
        return drateid;
    }

    public void setDrateid(String drateid) {
        this.drateid = drateid;
    }

    public String getdHub() {
        return dHub;
    }

    public void setdHub(String dHub) {
        this.dHub = dHub;
    }

    public String getpHub() {
        return pHub;
    }

    public void setpHub(String pHub) {
        this.pHub = pHub;
    }

    public String getpSupervisor() {
        return pSupervisor;
    }

    public void setpSupervisor(String pSupervisor) {
        this.pSupervisor = pSupervisor;
    }

    public String getdSupervisor() {
        return dSupervisor;
    }

    public void setdSupervisor(String dSupervisor) {
        this.dSupervisor = dSupervisor;
    }

    public String getdHubName() {
        return dHubName;
    }

    public void setdHubName(String dHubName) {
        this.dHubName = dHubName;
    }

    public String getpHubName() {
        return pHubName;
    }

    public void setpHubName(String pHubName) {
        this.pHubName = pHubName;
    }

}
