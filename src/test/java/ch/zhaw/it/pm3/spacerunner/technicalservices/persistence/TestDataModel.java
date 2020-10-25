package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

public class TestDataModel {

    private int testNumber;

    public TestDataModel(int testNumber) {
        this.testNumber = testNumber;
    }

    public int getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }
}
