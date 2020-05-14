package uk.co.sancode.skeleton_service.utilities;

public enum OpenIdUrlType {
    LOGIN("authorize"),
    LOGOUT("logout"),
    RESET_PASSWORD("authorize");

    private final String endPoint;

    OpenIdUrlType(final String endPoint) {
        this.endPoint = endPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
