package com.gourmetcaree.shop.pc.valueobject;

public class MenuInfo {

    private final String top;

    private final String mail;

    private final String webData;

    private final String scout;

    private final String pattern;
    
    private final String shopList;
    
    private final String userInfo;

    private MenuInfo(String top, String mail, String webData, String scout, String pattern, String shopList, String userInfo) {
        this.top = top;
        this.mail = mail;
        this.webData = webData;
        this.scout = scout;
        this.pattern = pattern;
        this.shopList = shopList;
        this.userInfo = userInfo;
    }

    public String getTop() {
        return top;
    }

    public String getMail() {
        return mail;
    }

    public String getWebData() {
        return webData;
    }

    public String getScout() {
        return scout;
    }

    public String getPattern() {
        return pattern;
    }

    public String getShopList() {
        return shopList;
    }

    public String getUserInfo() {
        return userInfo;
    }
    
    public static MenuInfo topInstance() {
        return new MenuInfo("mn_index_select", 
                "", 
                "", 
                "",
                "", 
                "", 
                "");
    }

    public static MenuInfo mailInstance() {
        return new MenuInfo("",
                "mn_entry_select",
                "",
                "",
                "",
                "",
                "");
    }

    public static MenuInfo webDataInstance() {
        return new MenuInfo("",
                "",
                "mn_jobinfo_select",
                "",
                "",
                "",
                "");
    }

    public static MenuInfo scoutInstance() {
        return new MenuInfo("",
                "",
                "",
                "mn_scout_select",
                "",
                "",
                "");
    }

    public static MenuInfo patternInstance() {
        return new MenuInfo("",
                "",
                "",
                "",
                "mn_pattern_select",
                "",
                "");
    }

    public static MenuInfo shopListInstance() {
        return new MenuInfo("",
                "",
                "",
                "",
                "",
                "mn_shopList_select",
                "");
    }

    public static MenuInfo userInfoInstance() {
        return new MenuInfo("",
                "",
                "",
                "",
                "",
                "",
                "mn_cominfo_select");
    }

    public static MenuInfo emptyInstance() {
        return new MenuInfo("",
                "",
                "",
                "",
                "",
                "",
                "");
    }
}
