
package com.tozmart.tozisdk.entity;

import java.util.List;

public class GetMeaInfoResponse {
    private long timeUpdate;
    private NetworkResult result;
    private int needSync;
    private int idVersion;
    private List<MeaInfoEntity> measItem4Male;
    private List<MeaInfoEntity> measItem4Female;

    public long getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(long timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    public NetworkResult getResult() {
        return result;
    }

    public void setResult(NetworkResult result) {
        this.result = result;
    }

    public int getNeedSync() {
        return needSync;
    }

    public void setNeedSync(int needSync) {
        this.needSync = needSync;
    }

    public int getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(int idVersion) {
        this.idVersion = idVersion;
    }

    public List<MeaInfoEntity> getMeasItem4Male() {
        return measItem4Male;
    }

    public void setMeasItem4Male(List<MeaInfoEntity> measItem4Male) {
        this.measItem4Male = measItem4Male;
    }

    public List<MeaInfoEntity> getMeasItem4Female() {
        return measItem4Female;
    }

    public void setMeasItem4Female(List<MeaInfoEntity> measItem4Female) {
        this.measItem4Female = measItem4Female;
    }

}
