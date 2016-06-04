package hao.bk.com.config;

/**
 * Created by Nhahv on 4/11/2016.
 */
public class Config {

    public static final long TIME_SLEEP = 500;
    //public static final String PUBLIC_KEY = "pub-c-470b0c62-1d29-4905-ad3d-10e41ecae909";
    public static final String SUBSCRIBE_KEY = "sub-c-047ca2dc-fbc7-11e5-861b-02ee2ddab7fe";
    public static final String SECRET_KEY = "sec-c-NmIyOTA3NTMtYTY1Yi00Nzc2LWI1MmItOGQ2MjA0OGNkZjEy";

    public static final String CHANNEL_ = "vsi_private_chat_";
    public static final String CHANNEL_NOTIFICATION = "vsi_group_chanel_notification";

    public static final String DATA = "DATA";
    public static final String PACKAGE = "PACKAGE";

    public static final String USER_NAME = "username";
    public static final String PASS_WORD = "password";

    private static final String HOST = "http://vsi.vietitech.com/";
    private static final String API =
            "api/project_api.php?publicKey=5628acfce494c53189505f337bfa6870&action=";
    private static final String PUBLIC_KEY_HOST =
            "publicKey=5628acfce494c53189505f337bfa6870&action=";

    public static final String PROJECT_ID = "project_id";
    public static final String ID = "id";


    public static final String URL =
            "http://vsi.vietitech.com/api/member/mem_api.php?publicKey=5628acfce494c53189505f337bfa6870&action=";

    public static final String URL_GET_USER =
            "http://vsi.vietitech.com/api/member/mem_api.php?publicKey=5628acfce494c53189505f337bfa6870&action=getUser";

    public static final String CONTENT_TYPE = "application/json; charset=utf-8"; // fix Error utf-8 entity
    public static final String LOGIN = "login";
    public static final String GET_USER = "getUser";
    public static final String VERIFY = "VERIFY";
    public static final int KEY_LOGIN = 1;

    public static final String URL_LOGIN =
            HOST + API + "login";
    public static final String URL_GET_PROJECT_PAGE =
            HOST + API + "getProject&username=";
    public static final String URL_GET_NEW_VIP_PAGE =
            HOST + "api/news_api.php?" + PUBLIC_KEY_HOST + "getNewsVip&username=";
    public static final String URL_GET_PROJECT_CARE_PAGE =
            HOST + API + "getProjectCare&username=";
    public static final String URL_GET_PROJECT_ITEM_PAGE =
            HOST + API + "getProjectItme&username=";
    public static final String URL_GET_NEW_VSI_PAGE =
            HOST + "api/news_api.php?" + PUBLIC_KEY_HOST + "getNewsVsi&username=";

    public static final String URL_GET_SUPPORT =
            HOST + "api/support_api.php?" + PUBLIC_KEY_HOST + "getSupport";
    public static final String URL_CARE =
            HOST + API + "careProject&";
    public static final String URL_CANCEL_CARE =
            HOST + API + "cancelCareProject&";
    public static final String URL_GET_USER_PAGE =
            HOST + API + "getUser";
    public static final String URL_GET_USER_NAME =
            HOST + API + "getUser&username=";

    public static final String URL_GET_PROJECT_BY_ID =
            HOST
            + "api/project_api.php?"
            + PUBLIC_KEY_HOST
            + "getProject&username=";
    public static final int NOTIFICATION = 222;
    public static final int NOTIFICATION_MESSAGES = 0;
    public static final int NOTIFICATION_NEW_PROJECT = 1;
    public static final int NOTIFICATION_SUPPORT = 2;

    public static final int CARE_PROJECT_METHOD = 3;
    public static final int SUPPORT_METHOD = 4;
    public static final int NEW_PROJECT_TYPE = 1;
    public static final int NEW_SUPPORT_TYPE = 5;
    public static final int NEW_LIKE_TYPE = 2;
    public static final int NEW_COMMENT_TYPE = 3;
    public static final int NEW_CARE_TYPE = 4;


    public static final String PREFERENCES = "PREFERENCES";
    public static final String IS_LOGIN = "isLogin";
    public static final int MESSAGES_ACTIVITY = 123;
    public static final int SUPPORT_ACTIVITY = 124;

    public static final int ITEM_PROJECT = 124;


    public static final int FRAGMENT_PROJECT = 100;
    public static final int CONTACT_FRAGMENT = 101;
    public static final int FRAGMENT_SUPPORT = 102;
    public static final String ARRAY_LIST = "arrayList";

    // Hao Dv
    // coporate tab
    public static final String NEW_CHANCE_TAB = "NEW_CHANCE_TAB";
    public static final String INTERESTING_TAB = "INTERESTING_TAB";
    public static final String MY_PROJECT_tAB = "MY_PROJECT_TAB";
    public static final String NAME_BUNDLE = "NAME_BUNDLE";
    public static final String getProject = "getProject";
    public static final String getProjectCare = "getProjectCare";
    public static final String getProjectItme = "getProjectItme";
    public static final String getNewsVsi = "getNewsVsi";
    public static final String getNewsVip = "getNewsVip";
    public static final String getNewsChuyenNganh = "getNewsChuyenNganh";
    public static final String getListLateMess = "getListLateMess";
    public static final String getUser = "getUser";
    public static final String getListCare = "getListCare";
    public static final String careProject = "careProject";
    public static final String cancelCareProject = "cancelCareProject";
    public static final String CHAT_PUBNUB = "CHAT_PUBNUB";
    // Chat tab
    public static final String LAST_MSG_TAB = "LAST_MSG_TAB";
    public static final String CONTACTS_TAB = "CONTACTS_TAB";

    // News tab
    public static final String DARSH_BOARD_TAB = "DARSH_BOARD_TAB";
    public static final String VSI_NEWS_TAB = "VSI_NEWS_TAB";
    public static final String MAJOR_NEWS_TAB = "MAJOR_NEWS_TAB";
    public static final String VIP_NEWS_TAB = "VIP_NEWS_TAB";
    // Delivery tab
    public static final String SUPPORT_NEED_TAB = "SUPPORT_NEED_TAB";
    public static final String FAIR_GOOD_TAB = "FAIR_GOOD_TAB";

    // network
    public static final String BASE_URL_GET = "http://vsi.vietitech.com";
    public static final String BASE_URL = "http://vsi.vietitech.com/api/member/";
    public static final String BASE_URL_REGISTER = "http://vsi.vietitech.com/api/";
    public static final String PUBLIC_KEY = "5628acfce494c53189505f337bfa6870";
    public static final String USER_NAME_PUT = "USER_NAME_PUT";
    public static final String URL_THUMNAILS_PUT = "URL_THUMNAILS_PUT";
    public static final String status_response = "status";

    //<!--PubNub -->
    public static final String startChannelName = "vsi_chat_private_chat_";
    public static final String publish_key_pub = "pub-c-470b0c62-1d29-4905-ad3d-10e41ecae909";
    public static final String subscribe_key_pub = "sub-c-047ca2dc-fbc7-11e5-861b-02ee2ddab7fe";
    public static final String secret_key_pub = "sec-c-NmIyOTA3NTMtYTY1Yi00Nzc2LWI1MmItOGQ2MjA0OGNkZjEy";

    public static String NAME_BROAD_CAST_FROM_MAIN_TO_CHAT_ACTIVITY = "hao.bk.com.vdmvsi.main.to.chat";
    public static String BROAD_CASS_MSG_MAIN_TO_CHAT_FILTER = "MSG_MAP_TO_CHAT";

    //news detail
    public static final String NEWS_TITLE = "newsTitle";
    public static final String NEWS_INTRO = "newsIntro";
    public static final String NEWS_CONTENT = "newsContent";
    public static final String NEWS_IMG = "newsImg";
}
