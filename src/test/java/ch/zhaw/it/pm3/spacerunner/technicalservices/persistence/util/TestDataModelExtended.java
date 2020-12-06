package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util;

public class TestDataModelExtended extends TestDataModel {

    private String extendedString;
    private int extendedInt;

    public TestDataModelExtended(int testNumber, String extendedString, int extendedInt) {
        super(testNumber);
        this.extendedString = extendedString;
        this.extendedInt = extendedInt;
    }

    public String getExtendedString() {
        return extendedString;
    }

    public void setExtendedString(String extendedString) {
        this.extendedString = extendedString;
    }

    public int getExtendedInt() {
        return extendedInt;
    }

    public void setExtendedInt(int extendedInt) {
        this.extendedInt = extendedInt;
    }


}
