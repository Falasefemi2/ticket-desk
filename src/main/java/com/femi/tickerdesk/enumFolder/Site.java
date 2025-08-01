package com.femi.tickerdesk.enumFolder;

import lombok.Getter;

@Getter
public enum Site {
    LAGOS_OFFICE("Lagos Office"),
    ABUJA_OFFICE("Abuja Office");

    private final String displayName;

    Site(String displayName) {
        this.displayName = displayName;
    }

}