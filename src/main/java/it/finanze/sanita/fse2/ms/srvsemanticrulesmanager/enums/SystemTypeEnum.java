package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.enums;

public enum SystemTypeEnum {

    NONE(null),
    TS("TS");

    private final String name;

    SystemTypeEnum(String name) {
        this.name = name;
    }

    public String value() {
        return name;
    }
}
